package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.constant.Patents;
import com.cnuip.pmes2.constant.TimedTaskType;
import com.cnuip.pmes2.constant.Workflows;
import com.cnuip.pmes2.domain.core.*;
import com.cnuip.pmes2.domain.inventory.PatentInfo;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.repository.core.*;
import com.cnuip.pmes2.repository.inventory.PatentInfoMapper;
import com.cnuip.pmes2.service.BatchQuickService;
import com.cnuip.pmes2.service.CNIPRService;
import com.cnuip.pmes2.util.LabelDealUtil;
import com.cnuip.pmes2.util.ResponseEnum;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

@Service
public class BatchQuickServiceImpl implements BatchQuickService {

	@Autowired
	private ProcessOrderMapper processOrderMapper;
	
	@Autowired
	private LabelsetMapper labelsetMapper;
	
	@Autowired
	private PatentInfoMapper patentInfoMapper;
	
	@Autowired
	private CNIPRService cNIPRService;
	
	@Autowired
	private TaskOrderLabelMapper taskOrderLabelMapper;
	
	@Autowired
	private TaskOrderMapper taskOrderMapper;

	@Autowired
	private PatentMapper patentMapper;
	
	@Autowired
	private LabelMapper labelMapper; 
	
	@Autowired
	private TimedTaskMapper timedTaskMapper;
	
	@Autowired
	private OriginalPatentMapper originalPatentMapper;

	@Autowired
	private InstanceLabelMapper instanceLabelMapper;
	
	private Logger logger = LoggerFactory.getLogger(BatchQuickServiceImpl.class);
	
	@Override
	public void batchUpdate(Integer max) {
		logger.info("*********快速评估专利开始*********");
//		Date lastUpdateTime = patentMapper.selectLastUpdateTime();
		int type = TimedTaskType.BatchUpdatePatent.getType();
		Date lastUpdateTime = timedTaskMapper.selectLastUpdateTime(type);
		if(lastUpdateTime == null) {
			lastUpdateTime = new Date(1471968000000L);//2016年8月24日,系统创建时间
		}
		int total = patentInfoMapper.selectTotalAfterTimeNum(lastUpdateTime);
		if(max != null && max > 0) {
			total = Math.min(max, total);
		}
		logger.info("lastUpdateTime=" + lastUpdateTime + " 待处理专利数=" + total);
		int pageSize = 10000;
		for(int i = 0; i < Math.ceil(1.0 * total / pageSize); i++) {
			List<PatentInfo> list = patentInfoMapper.findAfterTime(lastUpdateTime, i + 1, pageSize);
			for (PatentInfo patentInfo : list) {
				logger.info("处理专利：" + patentInfo.getTi() + " 申请号=" + patentInfo.getAn());
				Patent existPatent = patentMapper.selectByAn(patentInfo.getAn());
				if(existPatent!= null && patentInfo.getPatentId() <=  Long.valueOf(existPatent.getIdPatent())) {
					logger.info("处理专利：" + patentInfo.getTi() + " 申请号=" + patentInfo.getAn() + " 已处理");
					continue;
				}
				try {
					Patent patent = dealOnePatentInfo(patentInfo);
					startOneBatchProcess(patent);
					logger.info("处理专利：" + patentInfo.getTi() + " 申请号=" + patentInfo.getAn() + " 处理完成");
				} catch (BussinessLogicException e) {
					logger.error("快速评估专利失败：" + patentInfo, e);
				}
			}
		}
		logger.info("*********快速评估专利结束*********");
	}


	@Transactional(rollbackFor=BussinessLogicException.class)
	@Override
	public Patent dealOnePatentInfo (PatentInfo patentInfo) throws BussinessLogicException {
		Patent patent = new Patent(patentInfo);
		dealOnePatent(patent);
		return patentMapper.selectByAn(patent.getAn());
	}


	private void dealOnePatent(Patent patent) throws BussinessLogicException {
		try {
			if (patentMapper.checkAnIsExist(patent.getAn()) > 0) {
				patentMapper.update(patent);
			} else {
				patentMapper.insert(patent);
			}
		} catch (Exception e) {
			throw new BussinessLogicException(e);
		}
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void startOneBatchProcess(Patent patent) throws BussinessLogicException {
		ProcessOrder processOrder;
		TaskOrder taskOrder;
		try {
			processOrder = startProcess(patent);
			taskOrder = startTask(processOrder);
		} catch (Exception e) {
			logger.error("启动批量流程出错", e);
			throw new BussinessLogicException(e, ResponseEnum.PROCESS_ORDER_ADD_ERROR);
		}
		//注册标签处理工具
//		LabelDealUtil util = new LabelDealUtil(patentMapper, patentInfoMapper, taskOrderLabelMapper, cNIPRService, patent.getAn(), true , taskOrder.getId());
		LabelDealUtil util = calOnePatent(patent, taskOrder, true, true);
		try {
//			logger.info("完成订单工单开始");
			taskOrderMapper.changeState(taskOrder.getId(), 1);
			processOrderMapper.changeState(processOrder.getId(), 1);
//			logger.info("完成订单工单结束,把结果更新入主表开始");
			Map<String, String> values = util.getValues();
			String keyWord1 = values.get("keyWords1"); 
			String keyWord3 = values.get("keyWords3"); 
			String patentValue = values.get("patentValue");
			String keyword = null;
			if(keyWord1 != null || keyWord3 != null) {
				keyword = (keyWord1 != null ? keyWord1 : "") + (keyWord3 != null ? keyWord3 : "");
			}
			patentMapper.updatePatentInfo(patent.getId(), patentValue, keyword, null);
			patentMapper.updateHasBatchIndexed(patent.getId());
//			logger.info("把结果更新入主表结束");
		} catch(Exception e) {
			logger.error("更新批量流程出错", e);
			throw new BussinessLogicException(e, ResponseEnum.PROCESS_ORDER_CHANGE_STATE_ERROR);
		}
	}

	@Override
	public LabelDealUtil calOnePatent(Patent patent) throws BussinessLogicException {
		return calOnePatent(patent, new TaskOrder(),false, false);
	}

	private LabelDealUtil calOnePatent(Patent patent, TaskOrder taskOrder, boolean isBatch, boolean needInsert) throws BussinessLogicException {
		LabelDealUtil util = new LabelDealUtil(patentMapper, originalPatentMapper, taskOrderLabelMapper, cNIPRService, patent.getAn(), false , taskOrder.getId());
		util.setBatch(isBatch);
		List<TaskOrderLabel> taskOrderLabels = new ArrayList<>();
//		logger.info("开始");
		try {
			taskOrderLabels.addAll(getAllAutoAcquisition(util));
			List<Long> labelIds = labelMapper.selectIdByTypeAndIndexType(1, Arrays.asList(3,4,5), null);
			List<InstanceLabel> instanceLabels = instanceLabelMapper.findByInstanceAndLabelIds(labelIds, patent.getId(), 1);
			for(InstanceLabel instanceLabel:instanceLabels) {
				if(Strings.isNullOrEmpty(instanceLabel.getStrValue()) && Strings.isNullOrEmpty(instanceLabel.getTextValue())) {
					continue;
				}
				util.getValues().put(instanceLabel.getLabel().getKey(),instanceLabel.getStrValue()!=null?instanceLabel.getStrValue():instanceLabel.getTextValue());
				for(TaskOrderLabel taskOrderLabel: taskOrderLabels) {
					if(taskOrderLabel.getLabelId().equals(instanceLabel.getLabelId())) {
						taskOrderLabel.setStrValue(instanceLabel.getStrValue());
						taskOrderLabel.setTextValue(instanceLabel.getTextValue());
					}
				}
			}
		} catch (Exception e) {
			logger.error("批量获取出错", e);
			throw new BussinessLogicException(e, ResponseEnum.PROCESS_ORDER_BATCH_GET_ERROR);
		}
//		logger.info("获取所有需要自动获取的标签结束，计算四级标签开始");
		try {
			if(patent.getType()!=3) {
				taskOrderLabels.addAll(calFourthLabels(util));
//			logger.info("计算四级标签结束，三级标签开始");
				taskOrderLabels.addAll(calThirdLabels(util));
//			logger.info("计算三级标签结束，权重标签开始");
				taskOrderLabels.addAll(calDeriveLabels(util));
			}
//			logger.info("计算权重标签结束");
		} catch (Exception e) {
			logger.error("批量计算出错", e);
			throw new BussinessLogicException(e, ResponseEnum.PROCESS_ORDER_BATCH_INSERT_ERROR);
		}
		try {
//			logger.info("插入开始");
			List<TaskOrderLabel> ts = new ArrayList<>(taskOrderLabels.size());
			if(needInsert) {
				for(TaskOrderLabel taskOrderLabel: taskOrderLabels) {
					if(Strings.isNullOrEmpty(taskOrderLabel.getStrValue()) && Strings.isNullOrEmpty(taskOrderLabel.getTextValue())) {
						continue;
					}
					ts.add(taskOrderLabel);
					instanceLabelMapper.updateLabels(new InstanceLabel(patent.getId(),taskOrderLabel.getLabelId(), taskOrderLabel.getStrValue(),taskOrderLabel.getTextValue()));
				}
 				taskOrderLabelMapper.batchInsert(ts);
			}
//			logger.info("插入结束");
		} catch (Exception e) {
			logger.error("批量插入出错", e);
			throw new BussinessLogicException(e, ResponseEnum.PROCESS_ORDER_BATCH_CAL_ERROR);
		}
		return util;
	}

	/**
	 * 计算由第三层标签根据权重计算
	 * @param util
	 */
	private List<TaskOrderLabel> calDeriveLabels(LabelDealUtil util) {
		Labelset labelset = labelsetMapper.selectAliveByType(1);
		List<LabelsetLabel> labelsetLabels = labelsetMapper.selectValueSystem(labelset.getId());
//		for (LabelsetLabel labelsetLabel : labelsetLabels) {
//			util.makeSystemValue(labelsetLabel);
//		}
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
	 * 计算第三级别和少量第五级计算的标签
	 * 这些标签计算依赖第四级以后和自动获取的标签
	 * @param util
	 */
	private List<TaskOrderLabel> calThirdLabels(LabelDealUtil util) {
		List<Label> labels = labelMapper.selectByTypeAndIndexType(1, Arrays.asList(2,4), Arrays.asList(10)); 
//		for (Label label : labels) {
//			util.makeValue(label);
//		}
		return makeTaskOrderLabels(util, labels);
	}

	/**
	 * 计算第四级别和少量第五级计算的标签
	 * 这些标签计算依赖自动获取的标签
	 * @param util
	 */
	private List<TaskOrderLabel> calFourthLabels(LabelDealUtil util) {
		List<Label> labels = labelMapper.selectByTypeAndIndexType(1, Arrays.asList(2,4), Arrays.asList(5,7)); 
//		for (Label label : labels) {
//			util.makeValue(label);
//		}
		return makeTaskOrderLabels(util, labels);
	}

	/**
	 * 获取所有可以自动获取的标签
	 * 包含自动获取,自动获取可修改和自动计算
	 * @param util
	 */
	private List<TaskOrderLabel> getAllAutoAcquisition(LabelDealUtil util) {
		List<Label> labels = labelMapper.selectByTypeAndIndexType(1, Arrays.asList(1,3,5), null); 
//		for (Label label : labels) {
//			util.makeValue(label);
//		}
		return makeTaskOrderLabels(util, labels);
	}

	private TaskOrder startTask(ProcessOrder processOrder) {
		TaskOrder taskOrder = new TaskOrder();
		taskOrder.setProcessOrderId(processOrder.getId());
		taskOrder.setState(0);
		taskOrderMapper.insert(taskOrder);
		return taskOrder;
	}

	private ProcessOrder startProcess(Patent patent) {
		ProcessOrder processOrder = new ProcessOrder();
		processOrder.setInstanceId(patent.getId());
		processOrder.setInstanceType(Patents.ProcessType.Patent.getValue());
		processOrder.setProcessType(Workflows.ProcessType.BatchUpdate.getId());
		processOrder.setState(0);
		processOrderMapper.insert(processOrder);
		return processOrder;
	}

	@Override
	public List<TaskOrderLabel> getRecentLabelValueByTaskId(List<Long> taskIds) {
		List<TaskOrderLabel> labels = taskOrderLabelMapper.getRecentLabelByTaskOrderId(taskIds);
		return labels;
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

	@Override
	public void batchUpdatePatent() {
		logger.info("*********快速同步专利开始*********");
//		Date lastUpdateTime = patentMapper.selectLastUpdateTime();
		int type = TimedTaskType.BatchUpdatePatent.getType();
		TimedTask timedTask = timedTaskMapper.selectUnfinishTask(type);
		if(timedTask == null) {
			Date lastUpdateTime = timedTaskMapper.selectLastUpdateTime(type);
				if(lastUpdateTime == null) {
					lastUpdateTime = new Date(1471968000000L);//2016年8月24日,系统创建时间
				}
				int total = originalPatentMapper.selectTotalAfterTimeNum(lastUpdateTime);
				if(total == 0) {
					logger.info("待快速同步专利数=0,终止快速同步");
					return;
			}
//		int total = 100;
			timedTask = new TimedTask(type, total,lastUpdateTime);
			timedTask.setTotalAmount(total);
			timedTaskMapper.insert(timedTask);
		}
		logger.info("定时任务id:" + timedTask.getId() + ",最后更新时间" + timedTask.getBeginTime() + "，待同步专利数=" + (timedTask.getTotalAmount() - timedTask.getFinishAmount()));
		int pageSize = 10000;
		for(int i = timedTask.getFinishAmount() / pageSize; i < Math.ceil(1.0 * timedTask.getTotalAmount() / pageSize); i++) {
			List<String> list = originalPatentMapper.findAnAfterTime(timedTask.getBeginTime(), i + 1, pageSize);
			for (String an : list) {
				PatentInfo info = originalPatentMapper.findRecentInfo(an);
				Date lastOupdateTime = patentMapper.selectOupdateTimeByAn(an);
				if(lastOupdateTime != null && lastOupdateTime.compareTo(info.getUpdateTime()!=null?info.getUpdateTime():info.getCreatTime())>=0) {
					continue;
				}
				try {
					Patent patent = new Patent(info,0);
					if (lastOupdateTime != null) {
						patentMapper.update(patent);
					} else {
						patentMapper.insert(patent);
					}
				} catch (Exception e) {
					originalPatentMapper.insertDealError(an);
					logger.error("同步专利出错,专利an号为:" + an, e);
//					logger.error("同步专利出错, 专利信息为:" , info);
				}
			}
			int amount = Math.min(pageSize, timedTask.getTotalAmount() - i * pageSize);
			if(amount < 0) {
				amount = 0;
			}
			timedTaskMapper.updateFinishAmount(timedTask.getId(), amount);
			logger.info("快速同步专利完成" + amount + "条");
		}
		timedTaskMapper.finish(timedTask.getId());
		logger.info("*********快速同步专利结束*********");
	}

	@Override
	public void batchUpdatePatentWithEndNum(int endNum) {
		logger.info("*********快速同步尾号为 "+ endNum +"的专利开始*********");
//		Date lastUpdateTime = patentMapper.selectLastUpdateTime();
		int type = 100 + endNum;
		TimedTask timedTask = timedTaskMapper.selectUnfinishTask(type);
		if(timedTask == null) {
			Date lastUpdateTime = timedTaskMapper.selectLastUpdateTime(type);
			if(lastUpdateTime == null) {
				lastUpdateTime = new Date(1471968000000L);//2016年8月24日,系统创建时间
			}
			int total = originalPatentMapper.selectTotalAfterTimeNumWithEndNum(lastUpdateTime, endNum);
			if(total == 0) {
				logger.info("待快速同步专利数=0,终止快速同步");
				return;
			}
//		int total = 100;
			timedTask = new TimedTask(type, total,lastUpdateTime);
			timedTask.setTotalAmount(total);
			timedTaskMapper.insert(timedTask);
		}
		logger.info("定时任务id:" + timedTask.getId() + ",最后更新时间" + timedTask.getBeginTime() + "，待同步专利数=" + (timedTask.getTotalAmount() - timedTask.getFinishAmount()));
		int pageSize = 10000;
		for(int i = timedTask.getFinishAmount() / pageSize; i < Math.ceil(1.0 * timedTask.getTotalAmount() / pageSize); i++) {
			List<String> list = originalPatentMapper.findAnAfterTimeWithEndNum(timedTask.getBeginTime(), endNum , i * pageSize, pageSize);
			for (String an : list) {
				PatentInfo info = originalPatentMapper.findRecentInfo(an);
				Date lastOupdateTime = patentMapper.selectOupdateTimeByAn(an);
				if(lastOupdateTime != null && lastOupdateTime.compareTo(info.getUpdateTime()!=null?info.getUpdateTime():info.getCreatTime())>=0) {
					continue;
				}
				try {
					Patent patent = new Patent(info,0);
					if (lastOupdateTime != null) {
						patentMapper.update(patent);
					} else {
						try {
							patentMapper.insert(patent);
						} catch (DuplicateKeyException e) {
							continue;
						}
					}
				} catch (Exception e) {
					originalPatentMapper.insertDealError(an);
					logger.error("同步专利出错,专利an号为:" + an, e);
//					logger.error("同步专利出错, 专利信息为:" , info);
				}
			}
			int amount = Math.min(pageSize, timedTask.getTotalAmount() - i * pageSize);
			if(amount < 0) {
				amount = 0;
			}
			timedTaskMapper.updateFinishAmount(timedTask.getId(), amount);
			logger.info("快速同步尾号为 "+ endNum +"的专利完成" + amount + "条");
		}
		timedTaskMapper.finish(timedTask.getId());
		logger.info("快速同步尾号为 "+ endNum +"的专利结束*********");
	}

	@Override
	public void batchUpdatePatentWithAnEndNum(int endNum) {
		logger.info("*********快速an同步尾号为 "+ endNum +"的专利开始*********");
		int type = 110 + endNum;
		TimedTask timedTask = timedTaskMapper.selectUnfinishTask(type);
		if(timedTask == null) {
			Date lastUpdateTime = timedTaskMapper.selectLastUpdateTime(type);
			if(lastUpdateTime == null) {
				lastUpdateTime = new Date(1471968000000L);//2016年8月24日,系统创建时间
			}
			int total = originalPatentMapper.selectTotalAfterTimeNumWithEndNum(lastUpdateTime, endNum);
			if(total == 0) {
				logger.info("待快速同步专利数=0,终止快速同步");
				return;
			}
			timedTask = new TimedTask(type, total,lastUpdateTime);
			timedTask.setTotalAmount(total);
			timedTaskMapper.insert(timedTask);
		}
		logger.info("定时任务id:" + timedTask.getId() + ",最后更新时间" + timedTask.getBeginTime() + "，待同步专利数=" + (timedTask.getTotalAmount() - timedTask.getFinishAmount()));
		int pageSize = 10000;
		for(int i = timedTask.getFinishAmount() / pageSize; i < Math.ceil(1.0 * timedTask.getTotalAmount() / pageSize); i++) {
			List<String> list = originalPatentMapper.findAnAfterTimeWithEndNum(timedTask.getBeginTime(), endNum , i * pageSize, pageSize);
			for (String an : list) {
				PatentInfo info = originalPatentMapper.findRecentInfo(an);
				try {
					Patent patent = new Patent(info,0);
					patentMapper.update(patent);
				} catch (Exception e) {
					originalPatentMapper.insertDealError(an);
					logger.error("同步专利出错,专利an号为:" + an, e);
				}
			}
			int amount = Math.min(pageSize, timedTask.getTotalAmount() - i * pageSize);
			if(amount < 0) {
				amount = 0;
			}
			timedTaskMapper.updateFinishAmount(timedTask.getId(), amount);
			logger.info("快速同步an尾号为 "+ endNum +"的专利完成" + amount + "条");
		}
		timedTaskMapper.finish(timedTask.getId());
		logger.info("快速同步an尾号为 "+ endNum +"的专利结束*********");
	}

	@Override
	public void batchUpdateSub3PatentWithAnEndNum(int endNum) {
		logger.info("*********快速an同步尾号为 "+ endNum +"的sub3专利开始*********");
		int type = 130 + endNum;
		TimedTask timedTask = timedTaskMapper.selectUnfinishTask(type);
		if(timedTask == null) {
			Date lastUpdateTime = timedTaskMapper.selectLastUpdateTime(type);
			if(lastUpdateTime == null) {
				lastUpdateTime = new Date(1483200000000L);//2017年1月1日,系统创建时间
			}
			int total = originalPatentMapper.selectSub3TotalAfterTimeNumWithEndNum(lastUpdateTime, endNum);
			if(total == 0) {
				logger.info("待快速同步sub3专利数=0,终止快速同步");
				return;
			}
			timedTask = new TimedTask(type, total,lastUpdateTime);
			timedTask.setTotalAmount(total);
			timedTaskMapper.insert(timedTask);
		}
		logger.info("定时任务id:" + timedTask.getId() + ",最后更新时间" + timedTask.getBeginTime() + "，待同步sub3专利数=" + (timedTask.getTotalAmount() - timedTask.getFinishAmount()));
		int pageSize = 10000;
		for(int i = timedTask.getFinishAmount() / pageSize; i < Math.ceil(1.0 * timedTask.getTotalAmount() / pageSize); i++) {
			List<String> list = originalPatentMapper.findSub3AnAfterTimeWithEndNum(timedTask.getBeginTime(), endNum , i * pageSize, pageSize);
			for (String an : list) {
				PatentInfo info = originalPatentMapper.findRecentInfo(an);
				try {
					Patent patent = new Patent(info,0);
					patentMapper.update(patent);
				} catch (Exception e) {
					originalPatentMapper.insertDealError(an);
					logger.error("同步sub3专利出错,专利an号为:" + an, e);
				}
			}
			int amount = Math.min(pageSize, timedTask.getTotalAmount() - i * pageSize);
			if(amount < 0) {
				amount = 0;
			}
			timedTaskMapper.updateFinishAmount(timedTask.getId(), amount);
			logger.info("快速同步an尾号为 "+ endNum +"的sub3专利完成" + amount + "条");
		}
		timedTaskMapper.finish(timedTask.getId());
		logger.info("快速同步an尾号为 "+ endNum +"的sub3专利结束*********");
	}

	@Override
	public void batchUpdatePatentByType(int pType) {
		String patentType = null;
		switch (pType) {
		case 1:
			patentType = "发明";
			break;
		case 2:
			patentType = "外观";
			break;
		case 3:
			patentType = "使用新型";
			break;
		}
		logger.info("*********快速同步" + patentType + "专利开始*********");
//		Date lastUpdateTime = patentMapper.selectLastUpdateTime();
		int type = 20 + pType;
		TimedTask timedTask = timedTaskMapper.selectUnfinishTask(type);
		if(timedTask == null) {
			Date lastUpdateTime = timedTaskMapper.selectLastUpdateTime(type);
			if(lastUpdateTime == null) {
				lastUpdateTime = new Date(1471968000000L);//2016年8月24日,系统创建时间
			}
			int total = originalPatentMapper.selectTotalAfterTimeNumByType(lastUpdateTime, pType);
			if(total == 0) {
				logger.info("待快速同步专利数=0,终止快速同步");
				return;
			}
//		int total = 100;
			timedTask = new TimedTask(type, total,lastUpdateTime);
			timedTask.setTotalAmount(total);
			timedTaskMapper.insert(timedTask);
		}
		logger.info("定时任务id:" + timedTask.getId() + ",最后更新时间" + timedTask.getBeginTime() + "，待同步专利数=" + (timedTask.getTotalAmount() - timedTask.getFinishAmount()));
		int pageSize = 10000;
		for(int i = timedTask.getFinishAmount() / pageSize; i < Math.ceil(1.0 * timedTask.getTotalAmount() / pageSize); i++) {
			List<String> list = originalPatentMapper.findAnAfterTimeByType(timedTask.getBeginTime(), pType , i * pageSize, pageSize);
			for (String an : list) {
				PatentInfo info = originalPatentMapper.findRecentInfo(an);
				Date lastOupdateTime = patentMapper.selectOupdateTimeByAn(an);
				if(lastOupdateTime != null && lastOupdateTime.compareTo(info.getUpdateTime()!=null?info.getUpdateTime():info.getCreatTime())>=0) {
					continue;
				}
				try {
					Patent patent = new Patent(info,0);
					if (lastOupdateTime != null) {
						patentMapper.update(patent);
					} else {
						patentMapper.insert(patent);
					}
				} catch (Exception e) {
					originalPatentMapper.insertDealError(an);
					logger.error("同步专利出错,专利an号为:" + an, e);
//					logger.error("同步专利出错, 专利信息为:" , info);
				}
			}
			int amount = Math.min(pageSize, timedTask.getTotalAmount() - i * pageSize);
			if(amount < 0) {
				amount = 0;
			}
			timedTaskMapper.updateFinishAmount(timedTask.getId(), amount);
			logger.info("快速同步" + patentType + "专利完成" + amount + "条");
		}
		timedTaskMapper.finish(timedTask.getId());
		logger.info("快速同步" + patentType + "专利结束*********");
	}

	@Override
	public void dealError() {
		List<String> list = originalPatentMapper.findDealError();
		for (String an : list) {
			PatentInfo info = originalPatentMapper.findRecentInfo(an);
			try {
				Patent patent = new Patent(info,0);
				Patent existPatent = patentMapper.selectByAn(an);
				originalPatentMapper.deleteDealError(an);
				if (existPatent == null) {
					patentMapper.update(patent);
				} else {
					patentMapper.insert(patent);
				}
			} catch (Exception e) {
				originalPatentMapper.insertDealError(an);
				logger.error("同步专利出错,专利an号为:" + an, e);
//				logger.error("同步专利出错, 专利信息为:" , info);
			}
		}
	}

	@Override
	public void batchQuickIndex(int endNum) {
		logger.info("*********批量快速计算专利任务" + endNum +  "开始*********");
//		Date lastUpdateTime = patentMapper.selectLastUpdateTime();
		int type = 10 + endNum;
		TimedTask timedTask = timedTaskMapper.selectUnfinishTask(type);
		if(timedTask == null) {
			Date lastUpdateTime = timedTaskMapper.selectLastUpdateTime(type);
			if(lastUpdateTime == null) {
				lastUpdateTime = new Date(1471968000000L);//2016年8月24日,系统创建时间
			}
			int total = patentMapper.selectTotalAfterTimeNum(lastUpdateTime, endNum);
			if(total == 0) {
				logger.info("*********批量快速计算专利任务" + endNum +  "的专利数=0,终止快速评估");
				return;
			}
//		int total = 100;
			timedTask = new TimedTask(type, total,lastUpdateTime);
			timedTask.setTotalAmount(total);
			timedTaskMapper.insert(timedTask);
		}
		logger.info("定时任务id:" + timedTask.getId() + ",最后更新时间" + timedTask.getBeginTime() + " 待批量快速计算专利任务" + endNum +  "的专利数=" + (timedTask.getTotalAmount() - timedTask.getFinishAmount()));
		int pageSize = 1000;
		int firstBatchCount = 0;
		for(int i = timedTask.getFinishAmount() / pageSize; i < Math.ceil(1.0 * timedTask.getTotalAmount() / pageSize); i++) {
			List<Patent> list = patentMapper.selectPatentUpdateAfterTime(timedTask.getBeginTime(), endNum, i * pageSize - firstBatchCount, pageSize);
			for (Patent patent : list) {
				try {
					logger.info("快速评估专利：" + patent.getTi() + " 申请号=" + patent.getAn());
					startOneBatchProcess(patent);
					logger.info("快速评估专利：" + patent.getTi() + " 申请号=" + patent.getAn() + " 处理完成");
					if(patent.getHasBatchIndexed().equals(0)) {
						firstBatchCount = firstBatchCount + 1;
					}
				} catch (BussinessLogicException e) {
					logger.error("批量快速计算专利任务" + endNum +  "的专利失败：" + patent, e);
				}
			}
			int amount = Math.min(pageSize, timedTask.getTotalAmount() - i * pageSize);
			if(amount < 0) {
				amount = 0;
			}
			timedTaskMapper.updateFinishAmount(timedTask.getId(), amount);
			logger.info("批量快速计算专利任务" + endNum +  "完成" + amount + "条");
		}
		timedTaskMapper.finish(timedTask.getId());
		logger.info("*********批量快速计算专利任务" + endNum +  "结束*********");
	}
	
	@Override
	public void batchQuickIndexByType(int pType) {
		String patentType = null;
		switch (pType) {
		case 1:
			patentType = "发明";
			break;
		case 2:
			patentType = "外观";
			break;
		case 3:
			patentType = "使用新型";
			break;
		}
		logger.info("*********快速评估" + patentType +  "专利开始*********");
//		Date lastUpdateTime = patentMapper.selectLastUpdateTime();
		int type = 23 + pType;
		Date lastUpdateTime = timedTaskMapper.selectLastUpdateTime(type);
		if(lastUpdateTime == null) {
			lastUpdateTime = new Date(1471968000000L);//2016年8月24日,系统创建时间
		}
		int total = patentMapper.selectTotalAfterTimeByTypeNum(lastUpdateTime, pType);
		if(total == 0) {
			logger.info("待快速评估" + patentType +  "专利数=0,终止快速评估");
			return;
		}
		TimedTask timedTask = new TimedTask(type, total,lastUpdateTime);
		timedTaskMapper.insert(timedTask);
		logger.info("定时任务id:" + timedTask.getId() + ",最后更新时间" + lastUpdateTime + " 待快速评估" + patentType +  "专利数=" + total);
		int pageSize = 10000;
		for(int i = 0; i < Math.ceil(1.0 * total / pageSize); i++) {
			List<Patent> list = patentMapper.selectPatentUpdateAfterTimeByType(lastUpdateTime, pType, i * pageSize, pageSize);
			for (Patent patent : list) {
				try {
					logger.info("快速评估专利：" + patent.getTi() + " 申请号=" + patent.getAn());
					startOneBatchProcess(patent);
					logger.info("快速评估专利：" + patent.getTi() + " 申请号=" + patent.getAn() + " 处理完成");
				} catch (BussinessLogicException e) {
					logger.error("快速评估" + patentType +  "专利失败：" + patent, e);
				}
			}
			int amount = Math.min(pageSize, total - i * pageSize);
			timedTaskMapper.updateFinishAmount(timedTask.getId(), amount);
		}
		timedTaskMapper.finish(timedTask.getId());
		logger.info("*********快速评估" + patentType +  "专利结束*********");
	}

	@Override
	public void batchInsert() {
		int amount = 2000;
		while(true) {
			logger.info("*********快速插入专利开始*********");
			List<String> list = originalPatentMapper.findUndeal(amount);
			if(list != null && list.size() == 0) {
				break;
			}
			List<Patent> patents = new ArrayList<>(amount);
			for (String an : list) {
				PatentInfo info = originalPatentMapper.findRecentInfo(an);
				try {
					Patent patent = new Patent(info, 0);
					patents.add(patent);
				} catch (Exception e) {
					originalPatentMapper.insertDealError(an);
					logger.error("插入专利出错,专利an号为:" + an, e);
				}
			}
			if (patents.size() > 0) {
				try {
					patentMapper.batchInsert(patents);
				} catch (Exception e) {
					logger.error("插入专利出错", e);
					for (int i = 0; i < patents.size(); i++) {
						Patent patent = patents.get(i);
						try {
							patentMapper.insert(patent);
						} catch (Exception ex) {
							logger.error("插入专利出错,专利an号为:" + patent.getAn(), ex);
						}

					}
				}
			}
			logger.info("快速插入专利完成" + patents.size() + "条完成");
		}
	}

	@Override
	public void batchQuickWithEndNum(Integer endNum) {
		while (true) {
			logger.info("*********快速计算尾号为" + endNum + "专利开始*********");
			List<Patent> patents = patentMapper.findUnindexedPatentByEndNum(endNum);
			if(patents != null && patents.size() == 0) {
				break;
			}
			TimedTask timedTask = new TimedTask(200 + endNum, patents.size(),new Date());
			timedTaskMapper.insert(timedTask);
			logger.info("定时任务id:" + timedTask.getId() + ",待快速计算专利数=" + timedTask.getTotalAmount());
			for(Patent patent: patents) {
				TimedTaskDetail timedTaskDetail = new TimedTaskDetail(patent);
				timedTaskDetail.setTimedTaskId(timedTask.getId());
				timedTaskMapper.insertDetail(timedTaskDetail);
				try {
					logger.info("快速计算专利：" + patent.getTi() + " 申请号=" + patent.getAn());
					startOneBatchProcess(patent);
					logger.info("快速计算专利：" + patent.getTi() + " 申请号=" + patent.getAn() + " 处理完成");
					timedTaskMapper.finishTaskDetail(timedTaskDetail.getId());
				} catch (BussinessLogicException e) {
					logger.error("批量快速计算专利任务" + endNum +  "的专利失败：" + patent, e);
					patentMapper.addIndexError(patent.getAn(), e.getMessage());
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw, true));
					timedTaskMapper.recordDetailLog(timedTaskDetail.getId(),sw.toString());
				}
				timedTaskMapper.updateFinishAmount(timedTask.getId(), 1);
			}
			timedTaskMapper.finish(timedTask.getId());
			logger.info("*********快速计算尾号为" + endNum + "专利结束*********");
		}
	}

}
