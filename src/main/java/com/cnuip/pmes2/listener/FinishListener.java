package com.cnuip.pmes2.listener;

import com.cnuip.pmes2.domain.core.*;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.repository.core.*;

import com.cnuip.pmes2.service.CNIPRService;
import com.cnuip.pmes2.util.LabelDealUtil;
import com.cnuip.pmes2.util.ResponseEnum;
import com.google.common.base.Strings;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FinishListener implements TaskListener {

	private static final long serialVersionUID = 1L;

	private final Logger logger = LoggerFactory.getLogger(FinishListener.class);
	
	@Autowired
	private ProcessOrderMapper processOrderMapper;
	@Autowired
	private PatentMapper patentMapper;
	@Autowired
	private TaskOrderLabelMapper taskOrderLabelMapper;
	@Autowired
	private LabelMapper labelMapper;
	@Autowired
	private EnterpriseMapper enterpriseMapper;
	@Autowired
	private EnterpriseRequirementMapper enterpriseRequirementMapper;
	@Autowired
	private MatchMapper matchMapper;
	@Autowired
	private PatentIndexMapper patentIndexMapper;
	@Autowired
	private InstanceLabelMapper instanceLabelMapper;
	@Autowired
	private OriginalPatentMapper originalPatentMapper;
	@Autowired
	private CNIPRService cNIPRService;
	@Autowired
	private LabelsetMapper labelsetMapper;
	
	private static Map<String, Label> labelMap = new HashMap<>();
	
	@Override
	public void notify(DelegateTask task) {
		logger.info("FinishListener(" + task.getName() + ") notify:" + task.getVariables());
		Long processOrderId = (Long) task.getVariable("processOrderId");
		Boolean pass = (Boolean) task.getVariable("pass");
		if(pass) {
			processOrderMapper.changeState(processOrderId, 1);
			ProcessOrder processOrder = this.processOrderMapper.selectSimpleByPrimaryKey(processOrderId);
			//将订单下标签全部进行归档
			archiveLabels(processOrderId, processOrder);
			// 判断此专利的所有流程是否已处理完
			if(processOrder.getInstanceType().equals(1)) {
				updatePatentInfo(processOrderId, processOrder);
//				List<Integer> completeProcessTypes = this.processOrderMapper.findProcessTypesByInstanceId(processOrder.getInstanceId());
				this.patentMapper.changeIndexState(processOrder.getInstanceId(), 1);
				Patent patent = patentMapper.selectByPrimaryKey(processOrder.getInstanceId());
				//修改专利标引状态
				changeIndexState(processOrder, patent);
				//重新归档专利标签
				reArchivePatentLabels(processOrder, patent);
			}
			// 修改企业信息实例状态
			if(processOrder.getInstanceType().equals(2)) {
				enterpriseMapper.changeIndexState(processOrder.getInstanceId(), 2);
				enterpriseMapper.changeState(processOrder.getInstanceId(), 1);
			}
			// 修改企业需求实例状态
			if(processOrder.getInstanceType().equals(3)) {
				enterpriseRequirementMapper.changeIndexState(processOrder.getInstanceId(), 2, null,null);
				enterpriseRequirementMapper.changeState(processOrder.getInstanceId(), 1);
			}
			// 修改匹配实例状态
			if(processOrder.getInstanceType().equals(5)) {
				enterpriseRequirementMapper.changeIndexState(processOrder.getInstanceId(), null, 2,null);
				matchMapper.changeState(processOrder.getInstanceId(), 1);
			}
		}
	}

	private void reArchivePatentLabels(ProcessOrder processOrder, Patent patent) {
		LabelDealUtil util = new LabelDealUtil(patentMapper, originalPatentMapper, taskOrderLabelMapper, cNIPRService, patent.getAn(), false , null);
		util.setBatch(false);
		List<TaskOrderLabel> taskOrderLabels = new ArrayList<>();
		//获取所有自动标签
		taskOrderLabels.addAll(getAllAutoAcquisition(util));
		//把所有手工标签填入
		fixManulLabel(processOrder, util);
		//陆续计算不同层标签
		taskOrderLabels.addAll(calFourthLabels(util));
		taskOrderLabels.addAll(calThirdLabels(util));
		taskOrderLabels.addAll(calDeriveLabels(util));
		//把计算完的标签更新
		List<InstanceLabel> caledInstanceLabels = changeTaskOrderLabelToInstanceLabel(processOrder,taskOrderLabels);
		for(InstanceLabel instanceLabel:caledInstanceLabels) {
			if(Strings.isNullOrEmpty(instanceLabel.getStrValue()) && Strings.isNullOrEmpty(instanceLabel.getTextValue())) {
				continue;
			}
			instanceLabelMapper.updateLabels(instanceLabel);
		}
		patentMapper.updateHasBatchIndexed(patent.getId());
	}

	private void fixManulLabel(ProcessOrder processOrder, LabelDealUtil util) {
		List<Long> labelIds = labelMapper.selectIdByTypeAndIndexType(1, Arrays.asList(3,4,5), null);
		List<InstanceLabel> instanceLabels = instanceLabelMapper.findByInstanceAndLabelIds(labelIds, processOrder.getInstanceId(), processOrder.getInstanceType());
		for(InstanceLabel instanceLabel:instanceLabels) {
			if(Strings.isNullOrEmpty(instanceLabel.getStrValue()) && Strings.isNullOrEmpty(instanceLabel.getTextValue())) {
				continue;
			}
			util.getValues().put(instanceLabel.getLabel().getKey(),instanceLabel.getStrValue()!=null?instanceLabel.getStrValue():instanceLabel.getTextValue());
		}
	}

	private void changeIndexState(ProcessOrder processOrder, Patent patent) {
		PatentIndex index = new PatentIndex();
		index.setAn(patent.getAn());
		switch (processOrder.getProcessType()) {
			case 2:
				index.setHasBaseIndex(1);
				index.setHasBaseIndexing(2);
				break;
			case 3:
				index.setHasValueIndex(1);
				index.setHasValueIndexing(2);
				break;
			case 4:
				index.setHasPriceIndex(1);
				index.setHasPriceIndexing(2);
				break;
			case 5:
				index.setHasDeepIndex(1);
				index.setHasDeepIndexing(2);
				break;
		}
		patentIndexMapper.update(index);
	}

	private void archiveLabels(Long processOrderId, ProcessOrder processOrder) {
		List<TaskOrderLabel> taskOrderLabels = taskOrderLabelMapper.selectByProcessOrderId(processOrderId);
		//将taskOrderLabel转化为instanceLabel
		List<InstanceLabel> instanceLabels = changeTaskOrderLabelToInstanceLabel(processOrder, taskOrderLabels);
		if(instanceLabels != null && instanceLabels.size() > 0) {
			instanceLabelMapper.replaceLabels(instanceLabels);
		}
	}

	private List<InstanceLabel> changeTaskOrderLabelToInstanceLabel(ProcessOrder processOrder, List<TaskOrderLabel> taskOrderLabels) {
		List<InstanceLabel> instanceLabels = new ArrayList<>(taskOrderLabels.size());
		for(TaskOrderLabel taskOrderLabel:taskOrderLabels) {
			InstanceLabel instanceLabel = new InstanceLabel();
			instanceLabel.setInstanceId(processOrder.getInstanceId());
			instanceLabel.setInstanceType(processOrder.getInstanceType());
			instanceLabel.setLabelId(taskOrderLabel.getLabelId());
			instanceLabel.setStrValue(taskOrderLabel.getStrValue());
			instanceLabel.setTextValue(taskOrderLabel.getTextValue());
			instanceLabel.setUserId(instanceLabel.getUserId());
			instanceLabels.add(instanceLabel);
		}
		return instanceLabels;
	}

	private void updatePatentInfo(Long processOrderId, ProcessOrder processOrder) {
		Label keyWord1Label = getLabel("keyWords1");
		Label keyWord3Label = getLabel("keyWords3");
		Label patentValueLabel = getLabel("patentValue");
		Label independentItemLabel = getLabel("independentItem");
		List<TaskOrderLabel> taskOrderLabels = taskOrderLabelMapper.getLabelByProcessOrder(processOrderId, Arrays.asList(keyWord1Label.getId(), keyWord3Label.getId(), patentValueLabel.getId(), independentItemLabel.getId()));
		String keyWord1 = null;
		String keyWord3 = null;
		String patentValue = null;
//		String independentItem = null;
		String keyword = null;
		for (TaskOrderLabel taskOrderLabel : taskOrderLabels) {
			Long labelId = taskOrderLabel.getLabelId();
			String value = taskOrderLabel.getStrValue() != null ? taskOrderLabel.getStrValue() :taskOrderLabel.getTextValue();
			if(labelId.equals(keyWord1Label.getId())) {
				keyWord1 = value;
			}
			if(labelId.equals(keyWord3Label.getId())) {
				keyWord3 = value;
			}
			if(labelId.equals(patentValueLabel.getId())) {
				patentValue = value;
			}
//			if(labelId.equals(independentItemLabel.getId())) {
//				independentItem = value;
//			}
		}
		if(keyWord1 != null || keyWord3 != null) {
			keyword = (keyWord1 != null ? keyWord1 : "") + (keyWord3 != null ? keyWord3 : "");
		}
		if(keyword != null || patentValue != null /*|| independentItem != null*/) {
			patentMapper.updatePatentInfo(processOrder.getInstanceId(), patentValue, keyword, null);
		}
	}

	private Label getLabel(String key) {
		Label label =  labelMap.get(key);
		if(label == null) {
			label = labelMapper.selectSimpleByKey(key);
		}
		return label;
	}

	/**
	 * 计算第三级别和少量第五级计算的标签
	 * 这些标签计算依赖第四级以后和自动获取的标签
	 * @param util
	 */
	private List<TaskOrderLabel> calThirdLabels(LabelDealUtil util) {
		List<Label> labels = labelMapper.selectByTypeAndIndexType(1, Arrays.asList(2,4), Arrays.asList(10));
		return makeTaskOrderLabels(util, labels);
	}

	/**
	 * 计算第四级别和少量第五级计算的标签
	 * 这些标签计算依赖自动获取的标签
	 * @param util
	 */
	private List<TaskOrderLabel> calFourthLabels(LabelDealUtil util) {
		List<Label> labels = labelMapper.selectByTypeAndIndexType(1, Arrays.asList(2,4), Arrays.asList(5,7));
		return makeTaskOrderLabels(util, labels);
	}

	/**
	 * 计算由第三层标签根据权重计算
	 * @param util
	 */
	private List<TaskOrderLabel> calDeriveLabels(LabelDealUtil util) {
		Labelset labelset = labelsetMapper.selectAliveByType(3);
		List<LabelsetLabel> labelsetLabels = labelsetMapper.selectValueSystem(labelset.getId());
		Long taskOrderId = util.getTaskOrderId();
		List<TaskOrderLabel> taskOrderLabels = new ArrayList<TaskOrderLabel>(labelsetLabels.size());
		for (LabelsetLabel labelsetLabel : labelsetLabels) {
			util.makeSystemValue(labelsetLabel);
		}
		Map<String, String> values = util.getValues();
		for (LabelsetLabel labelsetLabel : labelsetLabels) {
			addTaskOrderLabelElement(taskOrderId, taskOrderLabels, values, labelsetLabel);
		}
		return taskOrderLabels;
	}

	private void addTaskOrderLabelElement(Long taskOrderId, List<TaskOrderLabel> taskOrderLabels,
										  Map<String, String> values, LabelsetLabel labelsetLabel) {
		TaskOrderLabel taskOrderLabel = new TaskOrderLabel();
		taskOrderLabel.setLabelId(labelsetLabel.getLabelId());
		taskOrderLabel.setTaskOrderId(taskOrderId);
		String value = values.get(labelsetLabel.getLabel().getKey());
		if(value == null) {
			return;
		}
		if(labelsetLabel.getLabel().getValueType().equals(2)) {
			taskOrderLabel.setTextValue(value);
		} else {
			taskOrderLabel.setStrValue(value);
		}
		taskOrderLabels.add(taskOrderLabel);
		if(labelsetLabel.getChildren()!=null && labelsetLabel.getChildren().size()>0) {
			for (LabelsetLabel child : labelsetLabel.getChildren()) {
				if(child.getType().equals(1)) {
					addTaskOrderLabelElement(taskOrderId, taskOrderLabels, values, child);
				}
			}
		}
	}

	/**
	 * 获取所有可以自动获取的标签
	 * 包含自动获取,自动获取可修改和自动计算
	 * @param util
	 */
	private List<TaskOrderLabel> getAllAutoAcquisition(LabelDealUtil util) {
		List<Label> labels = labelMapper.selectByTypeAndIndexType(1, Arrays.asList(1,3,5), null);
		return makeTaskOrderLabels(util, labels);
	}

	private List<TaskOrderLabel> makeTaskOrderLabels(LabelDealUtil util, List<Label> labels) {
		List<TaskOrderLabel> taskOrderLabels = new ArrayList<TaskOrderLabel>(labels.size());
		Long taskOrderId = util.getTaskOrderId();
		for (Label label : labels) {
			TaskOrderLabel taskOrderLabel = new TaskOrderLabel();
			taskOrderLabel.setLabelId(label.getId());
			taskOrderLabel.setTaskOrderId(taskOrderId);
			String value = util.makeValue(label);
			if(value == null) {
				continue;
			}
			if(label.getValueType().equals(2)) {
				taskOrderLabel.setTextValue(value);
			} else {
				taskOrderLabel.setStrValue(value);
			}
			taskOrderLabels.add(taskOrderLabel);
		}
		return taskOrderLabels;
	}
}
