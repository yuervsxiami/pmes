package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.constant.MessageType;
import com.cnuip.pmes2.constant.Workflows;
import com.cnuip.pmes2.controller.api.request.PatentTaskOrderSearchCondition;
import com.cnuip.pmes2.controller.api.request.TaskOrderDealParam;
import com.cnuip.pmes2.controller.api.response.HumanIndexAuditResponse;
import com.cnuip.pmes2.controller.api.response.HumanIndexResponse;
import com.cnuip.pmes2.domain.core.*;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.exception.TaskOrderException;
import com.cnuip.pmes2.repository.core.*;
import com.cnuip.pmes2.repository.inventory.PatentInfoMapper;
import com.cnuip.pmes2.service.*;
import com.cnuip.pmes2.util.LabelDealUtil;
import com.cnuip.pmes2.util.ResponseEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by wangzhibin on 2018/1/25.
 */
@Service
@Transactional(readOnly=true)
public class TaskOrderServiceImpl extends BaseServiceImpl implements TaskOrderService {

    @Autowired
    private TaskOrderMapper taskOrderMapper;
    
    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessTaskMapper processTaskMapper;
    
    @Autowired
    private LabelMapper labelMapper;

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
	private PatentMapper patentMapper;
	
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private EnterpriseRequireMatchService enterpriseRequireMatchService;
	
	@Autowired
	private OriginalPatentMapper originalPatentMapper;
	
	@Autowired
	private MatchMapper matchMapper;
	
	@Autowired
	private MessageMapper messageMapper;

	@Autowired
	private BatchQuickService batchQuickService;

	@Autowired
	private TaskSkipMapper taskSkipMapper;

	@Autowired
	private PatentEvaluateService patentEvaluateService;

	@Autowired
	private InstanceLabelMapper instanceLabelMapper;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
    @Override
    public TaskOrder selectByPrimaryKey(Long id) {
        return taskOrderMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TaskOrder> selectByProcessOrderId(Long processOrderId) {
        return taskOrderMapper.selectByProcessOrderId(processOrderId);
    }

    @Override
    public PageInfo<TaskOrder> patentSearch(PatentTaskOrderSearchCondition condition, int pageNum, int pageSize) {
        Page<TaskOrder> page = (Page<TaskOrder>) taskOrderMapper.patentSearch(condition, pageNum, pageSize);
        return page.toPageInfo();
    }

    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
    @Override
    public TaskOrder insert(TaskOrder taskOrder) throws TaskOrderException {
        try {
            taskOrderMapper.insert(taskOrder);
            return taskOrderMapper.selectByPrimaryKey(taskOrder.getId());
        } catch (Exception e) {
            throw new TaskOrderException(e, ResponseEnum.TASK_ORDER_ADD_ERROR);
        }
    }

    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
    @Override
    public void delete(Long id) throws TaskOrderException {
        try {
            taskOrderMapper.delete(id);
        } catch (Exception e) {
            throw new TaskOrderException(e, ResponseEnum.TASK_ORDER_DELETE_ERROR);
        }
    }

    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
    @Override
    public void changeUser(Long id, Long userId) throws TaskOrderException {
        try {
            taskOrderMapper.changeUser(id, userId);
        } catch (Exception e) {
            throw new TaskOrderException(e, ResponseEnum.TASK_ORDER_CHANGE_USER_ERROR);
        }
    }

    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
    @Override
    public void changeState(Long id, Integer state) throws TaskOrderException {
        try {
            taskOrderMapper.changeState(id, state);
        } catch (Exception e) {
            throw new TaskOrderException(e, ResponseEnum.TASK_ORDER_CHANGE_STATE_ERROR);
        }
    }

	@Override
	public PageInfo<TaskOrder> getMyPatentTask(User user, PatentTaskOrderSearchCondition condition, int pageNum,
			int pageSize) throws TaskOrderException {
		switch (condition.getProcessType()) {
		case 2:
		case 3:
		case 4:
		case 5:
			Page<TaskOrder> page = (Page<TaskOrder>) taskOrderMapper.getMyPatentTaskOrder(condition, pageNum, pageSize);
			return page.toPageInfo();
		}
		throw new TaskOrderException(ResponseEnum.TASK_ORDER_MISSMATING_PROCESS_TYPE);
	}

	@Override
	public PageInfo<TaskOrder> getMyEnterpriseTask(User user, Enterprise condition, int pageNum,
			int pageSize) throws TaskOrderException {
		if (condition.getProcessType().equals(6)) {
			Page<TaskOrder> page = (Page<TaskOrder>) taskOrderMapper.getMyEnterpriseTaskOrder(condition, pageNum, pageSize);
			List<TaskOrder> items = page.getResult();
			for (TaskOrder ord : items) {
				Enterprise ep = ord.getEnterprise();
				ep = this.fillEnterprise(ep, new String[]{"nationalEconomy", "province", "city", "district"});
			}
			return page.toPageInfo();
		}
		throw new TaskOrderException(ResponseEnum.TASK_ORDER_MISSMATING_PROCESS_TYPE);
	}
	
	@Override
	public PageInfo<TaskOrder> getMyEnterpriseRequirementTask(User user, EnterpriseRequirement condition, int pageNum,
			int pageSize) throws TaskOrderException {
		if (condition.getProcessType().equals(7)) {
			Page<TaskOrder> page = (Page<TaskOrder>) taskOrderMapper.getMyEnterpriseRequirementTaskOrder(condition, pageNum, pageSize);
			List<TaskOrder> items = page.getResult();
			for (TaskOrder ord : items) {
				EnterpriseRequirement epr = ord.getEnterpriseRequirement();
				if (epr!=null) {
					Enterprise ep = epr.getEnterprise();
					ep = this.fillEnterprise(ep, new String[]{"nationalEconomy", "province", "city", "district"});
				}
			}
			return page.toPageInfo();
		}
		throw new TaskOrderException(ResponseEnum.TASK_ORDER_MISSMATING_PROCESS_TYPE);
	}
	
	@Override
	public PageInfo<TaskOrder> getMyMatchTask(User user, Match condition, int pageNum,
			int pageSize) throws TaskOrderException {
		if (condition.getProcessType().equals(8)) {
			Page<TaskOrder> page = (Page<TaskOrder>) taskOrderMapper.getMyMatchTaskOrder(condition, pageNum, pageSize);
			List<TaskOrder> items = page.getResult();
			for (TaskOrder ord : items) {
				Match match = ord.getMatch();
				if (match != null) {
					EnterpriseRequirement epr = match.getEnterpriseRequirement();
					if (epr != null) {
						Enterprise ep = epr.getEnterprise();
						ep = this.fillEnterprise(ep, new String[]{"nationalEconomy", "province", "city", "district"});
					}
				}
			}
			return page.toPageInfo();
		}
		throw new TaskOrderException(ResponseEnum.TASK_ORDER_MISSMATING_PROCESS_TYPE);
	}
	
    @Override
    public PageInfo<TaskOrder> getMyTask(User user, int pageNum, int pageSize) {
    	TaskQuery taskQuery = taskService.createTaskQuery()
    			.taskCandidateOrAssigned("user" + user.getId());
    	List<Task> taskList = taskQuery.listPage(pageSize * (pageNum-1), pageSize);
    	Long total = taskQuery.count();
    	List<TaskOrder> taskOrders = new ArrayList<>();
    	for (Task task : taskList) {
    		TaskOrder taskOrder = taskOrderMapper.selectByActTaskId(task.getId());
    		if(taskOrder != null) {
    			if(taskOrder.getProcessOrder().getInstanceType().equals(1)) {
    				taskOrder.setPatent(patentMapper.selectByPrimaryKey(taskOrder.getProcessOrder().getInstanceId()));
    			}
    			taskOrders.add(taskOrder);
    		}
		}
    	PageInfo<TaskOrder> pageInfo = new PageInfo<>();
    	pageInfo.setTotal(total);
    	pageInfo.setList(taskOrders);
    	return pageInfo;
    }

    /**
     * 获取环节候选人
     */
    @Override
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
    public PageInfo<User> getCandidateUser(Long taskOrderId, User user, int pageSize, int pageNum) throws TaskOrderException {
    	TaskOrder taskOrder = claimTaskOrder(taskOrderId, user);
    	taskOrderMapper.changeUser(taskOrderId, user.getId());
    	ProcessOrder processOrder = taskOrder.getProcessOrder();
    	Long nextTaskId = Workflows.findNextTaskId(processOrder.getProcessType(), taskOrder.getProcessTaskId());
    	ProcessTask nextTask = processTaskMapper.selectSimpleByPrimaryKey(nextTaskId);
    	Long roleId = nextTask.getRoleId();
    	Page<User> page = (Page<User>) userMapper.selectUserByRole(roleId, pageNum, pageSize);
    	return page.toPageInfo();
    }

    /**
     * 获取转派候选人
     */
    @Override
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
    public PageInfo<User> getRedeployCandidateUser(Long taskOrderId, User user, int pageSize, int pageNum) throws TaskOrderException {
    	TaskOrder taskOrder = claimTaskOrder(taskOrderId, user);
    	taskOrderMapper.changeUser(taskOrderId, user.getId());
    	ProcessTask task = processTaskMapper.selectSimpleByPrimaryKey(taskOrder.getProcessTaskId());
    	Long roleId = task.getRoleId();
    	Page<User> page = (Page<User>) userMapper.selectUserByRoleNoUser(roleId, user.getId(), pageNum, pageSize);
    	return page.toPageInfo();
    }
    
	/**
	 * 做工单转派
	 * @param param
	 * @param user
	 * @throws TaskOrderException
	 */
    @Override
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
	public void dealRedeployOrder(TaskOrderDealParam param, User user) throws TaskOrderException {
    	Long taskOrderId = param.getTaskOrderId();
    	TaskOrder taskOrder = taskOrderMapper.selectSimpleByPrimaryKey(taskOrderId);
    	ProcessTask processTask = taskOrder.getProcessTask();
    	String actTaskId = taskOrder.getActTaskId();
		Task task = checkTaskOwnership(user, actTaskId);
		taskService.setAssignee(task.getId(), "user" + param.getAssignUserId());
		taskOrderMapper.redeploy(taskOrderId, param.getAssignUserId());
		String content = "用户"+ user.getName() +"将工单号为" + taskOrder.getActTaskId() + "的工单转派给您,流程类型为" + Workflows.findProcessNameById(processTask.getProcessId().intValue()) +",环节为" + processTask.getName() + ",请及时处理。";
		taskOrder.setProcessId(processTask.getProcessId());
		taskOrder.setUserId(param.getAssignUserId());
		Message message = makeMessage(taskOrder, content);
		message.setType(MessageType.TaskRedeploy.getType());
		messageMapper.add(message);
	}

    @Override
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
    public List<TaskOrderLabel> getLabelInTask(Long taskOrderId, User user) throws TaskOrderException {
    	TaskOrder taskOrder = claimTaskOrder(taskOrderId, user);
    	List<TaskOrderLabel> taskOrderLabels = new ArrayList<>();
    	List<Label> labels = labelMapper.selectInProcessTask(taskOrder.getProcessTaskId(),taskOrder.getProcessOrder().getLabelsetId());
		for (Label label : labels) {
	    	taskOrderLabels.add(makeTaskOrderLabel(taskOrderId, label, null, null));
		}
    	return taskOrderLabels;
    }

	private TaskOrder claimTaskOrder(Long taskOrderId, User user) throws TaskOrderException {
		TaskOrder taskOrder = taskOrderMapper.selectSimpleByPrimaryKey(taskOrderId);
    	String actTaskId = taskOrder.getActTaskId();
		Task task = checkTaskOwnership(user, actTaskId);		
		taskService.claim(task.getId(), "user" + user.getId());
		return taskOrder;
	}

    @Override
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
    public HumanIndexResponse getSemiLabel(Long taskOrderId, User user) throws TaskOrderException {
    	TaskOrder taskOrder = claimTaskOrder(taskOrderId, user);
    	HumanIndexResponse res = new HumanIndexResponse();
    	List<Label> labels = labelMapper.selectInProcessTask(taskOrder.getProcessTaskId(),taskOrder.getProcessOrder().getLabelsetId());
    	String reason = (String) taskService.getVariable(taskOrder.getActTaskId(), "semiReason");
    	res.setReason(reason);
    	List<TaskOrderLabel> taskOrderLabels = findTaskOrderLabels(taskOrderId, taskOrder, labels);
    	res.setLabels(taskOrderLabels);
    	return res;
    }

	private List<TaskOrderLabel> findTaskOrderLabels(Long taskOrderId, TaskOrder taskOrder, List<Label> labels) {
		List<TaskOrderLabel> taskOrderLabels = new ArrayList<>();
    	String holding = (String) taskService.getVariable(taskOrder.getActTaskId(), "holding");
		Map<String,String> values = (Map<String,String>) taskService.getVariable(taskOrder.getActTaskId(), "values");
		for (Label label : labels) {
			String value = null;
			if(values != null) {
				value = values.get(label.getKey());
			}
			taskOrderLabels.add(makeTaskOrderLabel(taskOrderId, label, value, null));
		}
//    	if(holding != null) {
//    		findbackChargebackedLabelValue(taskOrderId, taskOrder, labels, taskOrderLabels);
//    	} else {
//    		TaskOrder chargebackOrder = taskOrderMapper.selectRecentChargebackOrder(taskOrder.getProcessOrderId(), 8);
//    		findbackChargebackedLabelValue(taskOrderId, chargebackOrder, labels, taskOrderLabels);
//    	}
		return taskOrderLabels;
	}
    
//	private void findbackChargebackedLabelValue(Long taskOrderId, TaskOrder chargebackOrder, List<Label> labels,
//			List<TaskOrderLabel> taskOrderLabels) {
//		List<Long> labelIds = new ArrayList<>();
//    	for (Label label : labels) {
//	    	if(chargebackOrder != null) {
//	    		labelIds.add(label.getId());
//	    	}
//    	}
//    	Map<Long,String> labelValueMap = new HashMap<>();
//    	if(chargebackOrder != null && labelIds.size()>0) {
//    		List<LabelValue> labelValues = taskOrderLabelMapper.selectLabelValues(chargebackOrder.getId(), labelIds);
//    		for (LabelValue labelValue : labelValues) {
//    			labelValueMap.put(labelValue.getLabelId(), labelValue.getValue());
//    		}
//    	}
//		for (Label label : labels) {
//	    	taskOrderLabels.add(makeTaskOrderLabel(taskOrderId, label, labelValueMap.get(label.getId()), null));
//		}
//	}
    
    @Override
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
    public HumanIndexResponse getHumanLabel(Long taskOrderId, User user) throws TaskOrderException {
    	TaskOrder taskOrder = claimTaskOrder(taskOrderId, user);
    	HumanIndexResponse res = new HumanIndexResponse();
    	List<Label> labels = labelMapper.selectInProcessTask(taskOrder.getProcessTaskId(),taskOrder.getProcessOrder().getLabelsetId());
    	String reason = (String) taskService.getVariable(taskOrder.getActTaskId(), "reason");
    	res.setReason(reason);
    	List<TaskOrderLabel> taskOrderLabels = findTaskOrderLabels(taskOrderId, taskOrder, labels);
    	res.setLabels(taskOrderLabels);
    	return res;
    }
    
    @Override
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
    public List<TaskOrderLabel> doValueIndex(Long taskOrderId, User user)  throws TaskOrderException {
    	TaskOrder taskOrder = claimTaskOrder(taskOrderId, user);
		Labelset labelset = labelsetMapper.selectByProcessTaskId(taskOrder.getProcessTaskId());
		ProcessOrder processOrder = taskOrder.getProcessOrder();
		if(!processOrder.getInstanceType().equals(1)) {
			throw new TaskOrderException(ResponseEnum.TASK_ORDER_VALUEINDEX_ONLYPATNET);
		}
		Patent patent = patentMapper.selectSimpleByPrimaryKey(processOrder.getInstanceId());
		//注册标签处理工具
		LabelDealUtil util = new LabelDealUtil(patentMapper, originalPatentMapper, taskOrderLabelMapper, cNIPRService, patent.getAn(), false , taskOrder.getId());
		//将该环节流程曾经获取的标签读取
		Map<String,String> values = (Map<String,String>) taskService.getVariable(taskOrder.getActTaskId(), "values");
		util.setValues(values);
//		List<Long> aliveTaskOrderIds = taskOrderMapper.selectAliveIdsByProcessOrderId(processOrder.getId());
//		List<TaskOrderLabel> preTaskOrderLabels = taskOrderLabelMapper.getRecentLabelByTaskOrderId(aliveTaskOrderIds);
//		util.setValues(preTaskOrderLabels);
		calFourthLabels(util);
		calThirdLabels(util);
		//获取价值体系
		List<LabelsetLabel> labelsetLabels = labelsetMapper.selectValueSystem(labelset.getId());
		//进行价值体系计算
		for (LabelsetLabel labelsetLabel : labelsetLabels) {
			util.makeSystemValue(labelsetLabel);
		}
		values = util.getValues();
    	List<TaskOrderLabel> taskOrderLabels = new ArrayList<>();
    	//获取环节下挂的标签
    	List<Label> labels = labelMapper.selectInProcessTask(taskOrder.getProcessTaskId(),taskOrder.getProcessOrder().getLabelsetId());
    	for (Label label : labels) {
			String value = values.get(label.getKey());
			if(value != null) {
				taskOrderLabels.add(makeTaskOrderLabel(taskOrderId, label, value, null));
			}
		}
    	return taskOrderLabels;
    }

	private void calThirdLabels(LabelDealUtil util) {
		List<Label> labels = labelMapper.selectByTypeAndIndexType(1, Arrays.asList(2,4), Arrays.asList(10));
		for (Label label : labels) {
			util.makeValue(label);
		}
	}

	/**
	 * 计算第四级别和少量第五级计算的标签
	 * 这些标签计算依赖自动获取的标签
	 * @param util
	 */
	private void calFourthLabels(LabelDealUtil util) {
		List<Label> labels = labelMapper.selectByTypeAndIndexType(1, Arrays.asList(2,4), Arrays.asList(5,7));
		for (Label label : labels) {
			util.makeValue(label);
		}
	}
    
    @Override
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
    public List<TaskOrderLabel> doAutoIndex(Long taskOrderId, User user) throws TaskOrderException {
    	TaskOrder taskOrder = claimTaskOrder(taskOrderId, user);
    	List<TaskOrderLabel> taskOrderLabels = new ArrayList<>();
    	List<Label> labels = labelMapper.selectInProcessTask(taskOrder.getProcessTaskId(),taskOrder.getProcessOrder().getLabelsetId());
    	ProcessOrder processOrder = processOrderMapper.selectSimpleByPrimaryKey(taskOrder.getProcessOrderId());
    	if(processOrder.getInstanceType().equals(1)) {
			Map<String,String> values = (Map<String,String>) taskService.getVariable(taskOrder.getActTaskId(), "values");
    		Patent patent = patentMapper.selectSimpleByPrimaryKey(processOrder.getInstanceId());
    		if(values == null) {
				try {
					values = batchQuickService.calOnePatent(patent).getValues();
				} catch (BussinessLogicException e) {
					e.printStackTrace();
				}
			}
			for (Label label : labels) {
				String value = values.get(label.getKey());
				taskOrderLabels.add(makeTaskOrderLabel(taskOrderId, label, value, null));
			}
    	}
    	if(processOrder.getInstanceType().equals(2) || processOrder.getInstanceType().equals(3)) {
    		LabelDealUtil util = new LabelDealUtil(patentMapper, originalPatentMapper, taskOrderLabelMapper, cNIPRService, null, false, taskOrderId);
    		for (Label label : labels) {
    			String value = util.makeValue(label);
				taskOrderLabels.add(makeTaskOrderLabel(taskOrderId, label, value, null));
    		}
    	}
    	return taskOrderLabels;
    }

	/**
	 * 自动处理标引
	 */
	@Override
	@Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
    public void autoDoIndex() {
		List<Long> needSkipTaskIds = taskSkipMapper.findNeedSkipByTaskType(Arrays.asList(2L,3L,5L,6L,7L,8L));
		List<TaskOrder> taskOrders = taskOrderMapper.selectNeedSkipByProcessTaskId(needSkipTaskIds);
		logger.info("自动处理标引,共" + taskOrders.size() + "条定单" );
		for(TaskOrder taskOrder: taskOrders) {
			try {
				//视为被单子所有人自动把单子处理了
				User user = new User();
				user.setId(taskOrder.getUserId());
				String actTaskId = taskOrder.getActTaskId();
				TaskSkipInfo taskSkipInfo = getTaskSkipInfoById(taskOrder.getProcessTaskId());
				if(taskSkipInfo==null) {
//					logger.error("自动处理标引定单号为" + taskOrder.getId() + "的定单失败, taskSkipInfo=" + taskSkipInfo);
					continue;
				}
				Task task = taskService.createTaskQuery().taskCandidateOrAssigned("user" + user.getId()).taskId(actTaskId).singleResult();
				taskService.complete(task.getId());
				taskOrderMapper.deal(taskOrder.getId(), user.getId());
				logger.info("自动处理标引定单号为" + taskOrder.getId() + "的定单完成" );
			} catch (Exception e) {
				//自动处理标引失败,关闭当前标引环节的自动派单
				logger.error("自动处理标引定单号为" + taskOrder.getId() + "的定单失败", e);
				taskSkipMapper.close(taskOrder.getProcessTaskId());
				logger.error("自动处理标引定单号为" + taskOrder.getId() + "的定单失败" );
			}
		}
	}

	/**
	 * 自动处理半自动审核
	 */
	@Override
	@Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
	public void autoDoSemiAudit() {
		List<Long> needSkipTaskIds = taskSkipMapper.findNeedSkipByTaskType(Arrays.asList(10L));
		List<TaskOrder> taskOrders = taskOrderMapper.selectNeedSkipByProcessTaskId(needSkipTaskIds);
		logger.info("自动处理半自动审核,共" + taskOrders.size() + "条定单" );
		for(TaskOrder taskOrder: taskOrders) {
			try {
				//视为被单子所有人自动把单子处理了
				User user = new User();
				user.setId(taskOrder.getUserId());
				String actTaskId = taskOrder.getActTaskId();
				TaskSkipInfo taskSkipInfo = getTaskSkipInfoById(taskOrder.getProcessTaskId());
				if(taskSkipInfo==null) {
//					logger.info("自动处理半自动审核定单号为" + taskOrder.getId() + "的定单失败，taskSkipInfo=" + taskSkipInfo);
					continue;
				}
				Task task = taskService.createTaskQuery().taskCandidateOrAssigned("user" + user.getId()).taskId(actTaskId).singleResult();
				Map<String, Object> variables= new HashMap<>();
				variables.put("passSemi", true);
				taskService.complete(task.getId(),variables);
				taskOrderMapper.deal(taskOrder.getId(), user.getId());
				logger.info("自动处理半自动审核定单号为" + taskOrder.getId() + "的定单完成" );
			} catch (Exception e) {
				//自动处理标引失败,关闭当前标引环节的自动派单
				taskSkipMapper.close(taskOrder.getProcessTaskId());
				logger.error("自动处理半自动审核定单号为" + taskOrder.getId() + "的定单失败", e);
			}
		}
	}

	/**
	 * 自动处理人工审核
	 */
	@Override
	@Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
	public void autoDoAudit() {
		List<Long> needSkipTaskIds = taskSkipMapper.findNeedSkipByTaskType(Arrays.asList(4L));
		List<TaskOrder> taskOrders = taskOrderMapper.selectNeedSkipByProcessTaskId(needSkipTaskIds);
		logger.info("自动处理人工审核,共" + taskOrders.size() + "条定单" );
		for(TaskOrder taskOrder: taskOrders) {
			try {
				//视为被单子所有人自动把单子处理了
				User user = new User();
				user.setId(taskOrder.getUserId());
				String actTaskId = taskOrder.getActTaskId();
				TaskSkipInfo taskSkipInfo = getTaskSkipInfoById(taskOrder.getProcessTaskId());
				if(taskSkipInfo==null) {
//					logger.error("自动处理人工审核定单号为" + taskOrder.getId() + "的定单失败, taskSkipInfo=" + taskSkipInfo);
					continue;
				}
				Task task = taskService.createTaskQuery().taskCandidateOrAssigned("user" + user.getId()).taskId(actTaskId).singleResult();
				Map<String, Object> variables= new HashMap<>();
				variables.put("pass", true);
				taskService.complete(task.getId(),variables);
				taskOrderMapper.deal(taskOrder.getId(), user.getId());
				logger.info("自动处理人工审核定单号为" + taskOrder.getId() + "的定单完成" );
			} catch (Exception e) {
				//自动处理标引失败,关闭当前标引环节的自动派单
				taskSkipMapper.close(taskOrder.getProcessTaskId());
				logger.error("自动处理人工审核定单号为" + taskOrder.getId() + "的定单失败", e);
			}
		}
	}


	/**
	 * 自动处理派单任务,需要自动派单的派单流程,roleId必须和管理员admin的roleId相同
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
    public void autoDoAssignOrder() {
		List<Long> needSkipTaskIds = taskSkipMapper.findNeedSkipByTaskType(Arrays.asList(1L));
		List<TaskOrder> taskOrders = taskOrderMapper.selectNeedSkipByProcessTaskId(needSkipTaskIds);
		logger.info("自动派单,共" + taskOrders.size() + "条定单" );
		//视为管理员在自动派单
		User user = new User();
		user.setId(1L);
		for(TaskOrder taskOrder: taskOrders) {
			try {
				String actTaskId = taskOrder.getActTaskId();
				TaskSkipInfo taskSkipInfo = getTaskSkipInfoById(taskOrder.getProcessTaskId());
				if(taskSkipInfo==null || taskSkipInfo.getUserIdList()==null || taskSkipInfo.getUserIdList().size()==0) {
					taskSkipMapper.close(taskOrder.getProcessTaskId());
					continue;
				}
				Task task = taskService.createTaskQuery().taskCandidateOrAssigned("user" + user.getId()).taskId(actTaskId).singleResult();
				Map<String, Object> variables= new HashMap<>();
				variables.put("nextUser", "" + taskSkipInfo.getUserIdList().get(getRandomIndex(taskSkipInfo.getSize())));
				variables.put("assignUser", "1");
				taskService.complete(task.getId(), variables);
				taskOrderMapper.deal(taskOrder.getId(), user.getId());
				logger.info("自动派单定单号为" + taskOrder.getId() + "的定单完成" );
			} catch (Exception e) {
				//派单失败,关闭当前派单环节的自动派单
				taskSkipMapper.close(taskOrder.getProcessTaskId());
				logger.error("自动派单定单号为" + taskOrder.getId() + "的定单失败", e);
			}
		}
	};

    private int getRandomIndex(int mod) {
    	int num = (int) (Math.random()*100);
		return num%mod;
	}

    public TaskSkipInfo getTaskSkipInfoById(Long id) {
    	TaskSkipInfo taskSkipInfo = taskSkipMapper.findByPrimaryKey(id);
    	if(taskSkipInfo==null) {
    		return null;
		}
		if(!Strings.isNullOrEmpty(taskSkipInfo.getUserIds())) {
    		String[] userIds = taskSkipInfo.getUserIds().split(";");
    		if(userIds.length>0) {
    			List<Long> userIdList = new ArrayList<>(userIds.length);
				for(String userId: userIds) {
					userIdList.add(Long.valueOf(userId));
				}
				taskSkipInfo.setUserIdList(userIdList);
				taskSkipInfo.setSize(userIdList.size());
			}
		}
		return taskSkipInfo;
	}
    
    @Override
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
    public void dealTaskOrder(TaskOrderDealParam param, User user) throws TaskOrderException {
    	Long taskOrderId = param.getTaskOrderId();
    	TaskOrder taskOrder = taskOrderMapper.selectSimpleByPrimaryKey(taskOrderId);
    	ProcessTask processTask = taskOrder.getProcessTask();
    	String actTaskId = taskOrder.getActTaskId();
    	switch (processTask.getTaskType()) {
		case 1://派单
			dealAssignOrder(param, user, actTaskId);
			break;
		case 2://自动标引
		case 3://人工标引
		case 5://价值标引
		case 6://价格标引
		case 7://深度标引
		case 8://半自动标引
			doSaveIndex(param, user, actTaskId);
			break;
//			dealHumanIndex(param, user, actTaskId, taskOrderId);
//			break;
		case 4://人工标引审核
			dealHumanIndexAudit(param, user, actTaskId);
			break;
		case 10://半自动标引审核
			dealSemiAutoIndexAudit(param, user, actTaskId);
			break;
		case 9://匹配结果筛选
			dealArtificialSelection(param, user, actTaskId);
			break;
		}
    	taskOrderMapper.deal(taskOrderId, user.getId());
    }
    
    @Override
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
    public void holdTaskOrder(TaskOrderDealParam param, User user) throws TaskOrderException {
    	Long taskOrderId = param.getTaskOrderId();
    	TaskOrder taskOrder = taskOrderMapper.selectSimpleByPrimaryKey(taskOrderId);
    	ProcessTask processTask = taskOrder.getProcessTask();
    	String actTaskId = taskOrder.getActTaskId();
    	switch (processTask.getTaskType()) {
		case 3://人工标引
		case 8://半自动标引
			Task task = checkTaskOwnership(user, actTaskId);
			Map<String,String> values = (Map<String,String>) taskService.getVariable(task.getId(), "values");
			if(values == null) {
				values = new HashMap<>();
			}
			for(TaskOrderLabel tol: param.getCaledLabels()) {
				values.put(tol.getLabel().getKey(), tol.getLabel().getValue());
			}
			taskService.setVariable(task.getId(),"values", values);
//			saveTaskOrderLabel(param, user);
			break;
		}
    	taskService.setVariable(actTaskId, "holding", "true");
    }

    /**
     * 筛选匹配结果
     * @param param
     * @param user
     * @param actTaskId
     * @throws TaskOrderException 
     */
    private void dealArtificialSelection(TaskOrderDealParam param, User user, String actTaskId) throws TaskOrderException {
		Task task = checkTaskOwnership(user, actTaskId);
		Map<String, Object> variables= new HashMap<>();
		variables.put("pass", true);
		String matchResult = param.getMatchResult();
		if(matchResult == null) {
			throw new TaskOrderException(ResponseEnum.MATCH_NOTGET_RESULT);
		}
		Long processOrderId = (Long) taskService.getVariable(task.getId(), "processOrderId");
		ProcessOrder processOrder = processOrderMapper.selectSimpleByPrimaryKey(processOrderId);
		if(processOrder.getInstanceType().equals(5)) {
			Match match = new Match(processOrder.getInstanceId(), matchResult, 1);
			matchMapper.update(match);
			taskService.complete(task.getId(), variables);
			return;
		}
		throw new TaskOrderException(ResponseEnum.TASK_ORDER_MISSMATING_PROCESS_TYPE);
	}

	/**
     * 人工标引审核,如果审核未通过则把上上步的task标记为被退单
     * @param param
     * @param user
     * @param actTaskId
     * @throws TaskOrderException
     */
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
	void dealHumanIndexAudit(TaskOrderDealParam param, User user, String actTaskId) throws TaskOrderException {
		Task task = checkTaskOwnership(user, actTaskId);
		Boolean pass = param.getPass();
		Map<String, Object> variables= new HashMap<>();
		variables.put("pass", pass);
		if(pass == false) {
			String reason = param.getReason();
			variables.put("reason", reason);
			String chanrgebackActTackId = (String) taskService.getVariable(task.getId(), "prepreTaskId");
			taskOrderMapper.changeStateByActTaskId(chanrgebackActTackId, 2);
			TaskOrder taskOrder = taskOrderMapper.selectByActTaskId(chanrgebackActTackId);
			variables.put("nextUser", taskOrder.getUserId() + "");
			variables.put("auditUser", user.getId() + "");
			variables.put("needSkip", "false");
		}
		Long processOrderId = (Long) taskService.getVariable(task.getId(), "processOrderId");
		ProcessOrder processOrder = processOrderMapper.selectSimpleByPrimaryKey(processOrderId);
//		if(processOrder.getInstanceType().equals(1)) {
//			Patent patent = patentMapper.selectSimpleByPrimaryKey(processOrder.getInstanceId());
//			String value = taskOrderLabelMapper.selectValueByKeyAndAn("patentValue", patent.getAn());
//			patentMapper.updatePatentValue(patent.getAn(), value);
//		}
		taskService.complete(task.getId(), variables);
	}
    
    /**
     * 半自动标引审核,如果审核未通过则把上上步的task标记为被退单
     * @param param
     * @param user
     * @param actTaskId
     * @throws TaskOrderException
     */
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
    void dealSemiAutoIndexAudit(TaskOrderDealParam param, User user, String actTaskId) throws TaskOrderException {
    	Task task = checkTaskOwnership(user, actTaskId);
    	Boolean pass = param.getPass();
    	Map<String, Object> variables= new HashMap<>();
    	variables.put("passSemi", pass);
    	if(pass == false) {
    		String reason = param.getReason();
    		variables.put("semiReason", reason);
    		String chanrgebackActTackId = (String) taskService.getVariable(task.getId(), "prepreTaskId");
    		taskOrderMapper.changeStateByActTaskId(chanrgebackActTackId, 2);
    		TaskOrder taskOrder = taskOrderMapper.selectByActTaskId(chanrgebackActTackId);
    		variables.put("nextUser", taskOrder.getUserId() + "");
    	}
    	Long processOrderId = (Long) taskService.getVariable(task.getId(), "processOrderId");
    	ProcessOrder processOrder = processOrderMapper.selectSimpleByPrimaryKey(processOrderId);
    	taskService.complete(task.getId(), variables);
    }

    /**
     * 把前台确认过标引的值进行进入储存
     * @param param
     * @param user
     * @param actTaskId
     * @throws TaskOrderException
     */
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
	void doSaveIndex(TaskOrderDealParam param, User user, String actTaskId) throws TaskOrderException {
		Task task = checkTaskOwnership(user, actTaskId);
		Map<String,String> values = (Map<String,String>) taskService.getVariable(task.getId(), "values");
		if(values == null) {
			values = new HashMap<>();
		}
		for(TaskOrderLabel tol: param.getCaledLabels()) {
			values.put(tol.getLabel().getKey(), tol.getLabel().getValue());
		}
		taskService.setVariable(task.getId(),"values", values);
		saveTaskOrderLabel(param, user);
		taskService.complete(task.getId());
	}

	private void saveTaskOrderLabel(TaskOrderDealParam param, User user) {
		List<TaskOrderLabel> caledLabels = param.getCaledLabels();
		taskOrderLabelMapper.deleteByTaskOrderId(param.getTaskOrderId());
		for (TaskOrderLabel taskOrderLabel : caledLabels) {
			taskOrderLabel.setUserId(user.getId());
			Label label = taskOrderLabel.getLabel();
			if(label==null) {
				continue;
			}
			String value = label.getValue();
			if(Strings.isNullOrEmpty(value)) {
				continue;
			}
			if (label.getValueType().equals(2)) {
				taskOrderLabel.setTextValue(value);
			} else {
				taskOrderLabel.setStrValue(value);
			}
			taskOrderLabelMapper.insert(taskOrderLabel);
		}
	}

    /**
     * 做人工标引,记录人工填的值
     * @param param
     * @param user
     * @param actTaskId
     * @param taskOrderId
     * @throws TaskOrderException
     */
//    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
//	private void dealHumanIndex(TaskOrderDealParam param, User user, String actTaskId, Long taskOrderId) throws TaskOrderException {
//		Task task = checkTaskOwnership(user, actTaskId);
//		List<Label> indexedLabels = param.getAssignedLabels();
//		for (Label assignedLabel : indexedLabels) {
//			String value = assignedLabel.getValue();
//			TaskOrderLabel taskOrderLabel = makeTaskOrderLabel(taskOrderId, assignedLabel, value, user.getId());
//			if(taskOrderLabel != null) {
//				taskOrderLabelMapper.insert(taskOrderLabel);
//			}
//		}
//		taskService.complete(task.getId());
//	}

	/**
	 * 做派单管理,把单子派给下一个人
	 * @param param
	 * @param user
	 * @param actTaskId
	 * @throws TaskOrderException
	 */
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
	void dealAssignOrder(TaskOrderDealParam param, User user, String actTaskId) throws TaskOrderException {
		Task task = checkTaskOwnership(user, actTaskId);
		Map<String, Object> variables= new HashMap<>();
		variables.put("nextUser", "" + param.getAssignUserId());
		variables.put("assignUser", "" + user.getId());
		taskService.complete(task.getId(), variables);
	}

	/**
	 * 检查actTask的所有权
	 * @param user
	 * @param actTaskId
	 * @return
	 * @throws TaskOrderException
	 */
	private Task checkTaskOwnership(User user, String actTaskId) throws TaskOrderException {
		Task task = taskService.createTaskQuery().taskCandidateOrAssigned("user" + user.getId()).taskId(actTaskId).singleResult();
		if(task == null) {
			throw new TaskOrderException(ResponseEnum.TASK_ORDER_ISNOT_YOURS);
		}
		return task;
	}

	/**
	 * 将label,taskId和实际取值转化为taskOrderLabel
	 * @param taskOrderId
	 * @param assignedLabel
	 * @param value
	 * @return
	 */
	private TaskOrderLabel makeTaskOrderLabel(Long taskOrderId, Label assignedLabel, String value, Long userId) {
		TaskOrderLabel taskOrderLabel = new TaskOrderLabel();
		taskOrderLabel.setTaskOrderId(taskOrderId);
		taskOrderLabel.setLabelId(assignedLabel.getId());
		taskOrderLabel.setUserId(userId);
		if(value == null) {
			value = assignedLabel.getDefaultValue();
		}
		assignedLabel.setValue(value);
		taskOrderLabel.setLabel(assignedLabel);
//		if (assignedLabel.getValueType().equals(2)) {
//			taskOrderLabel.setTextValue(value);
//		} else {
//			taskOrderLabel.setStrValue(value);
//		}
		return taskOrderLabel;
	}

	@Override
	@Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
	public HumanIndexAuditResponse getSemiAutoResult(Long taskOrderId, User user) throws TaskOrderException {
		TaskOrder taskOrder = claimTaskOrder(taskOrderId, user);
		HumanIndexAuditResponse res = new HumanIndexAuditResponse();
		Long processOrderId = taskOrder.getProcessOrderId();
		TaskOrder semiAutoOrder = taskOrderMapper.selectRecentOrderByType(processOrderId, 8);
		semiAutoOrder.setLabels(taskOrderLabelMapper.selectByTaskOrderId(semiAutoOrder.getId(), 0, 0));
    	res.setReason((String) taskService.getVariable(taskOrder.getActTaskId(), "semiReason"));
		res.setSemiAutoOrder(semiAutoOrder);
		return res;
	}
	
	@Override
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
	public HumanIndexAuditResponse getHumanResult(Long taskOrderId, User user) throws TaskOrderException {
		TaskOrder taskOrder = claimTaskOrder(taskOrderId, user);
		HumanIndexAuditResponse res = new HumanIndexAuditResponse();
		Long processOrderId = taskOrder.getProcessOrderId();
		TaskOrder manualAuditOrder = taskOrderMapper.selectRecentOrderByType(processOrderId, 3);
		manualAuditOrder.setLabels(taskOrderLabelMapper.selectByTaskOrderId(manualAuditOrder.getId(), 0, 0));
		res.setManualAuditOrder(manualAuditOrder);
    	res.setReason((String) taskService.getVariable(taskOrder.getActTaskId(), "reason"));
//		if(taskOrder.getProcessOrder().getInstanceType().equals(1)) {
//			TaskOrder semiAutoOrder = taskOrderMapper.selectRecentOrderByType(processOrderId, 8);
//			semiAutoOrder.setLabels(taskOrderLabelMapper.selectByTaskOrderId(semiAutoOrder.getId(), 0, 0));
//			res.setSemiAutoOrder(semiAutoOrder);
//		}
		return res;
	}

	@Override
	public List<TaskOrderLabel> findAllLabels(Long patentId, Integer processType) {
		return this.taskOrderLabelMapper.findLatestProcessLabels(patentId, processType);
	}

	@Override
	public List<InstanceLabel> findAllInstanceLabels(Long patentId, Integer processType) {
		return instanceLabelMapper.findLatestProcessLabels(patentId,processType);
	}

	@Override
	public List<TaskOrderLabel> findAllLabels(Long patentId) {
		return this.taskOrderLabelMapper.findAllPatentLabels(patentId);
	}

	@Override
	public ValueIndexPatent findValueIndexPatentById(Long id) {
		return this.patentMapper.findValueIndexPatentById(id);
	}

	@Override
	public HumanAssessmentPatent findPatentByOrderId(Long id) {
		ProcessOrder processOrder = this.processOrderMapper.selectSimpleByPrimaryKey(id);
		processOrder.setTaskOrders(this.taskOrderMapper.findOrdersByProcessOrderId(id));
		Patent patent = this.patentMapper.selectSimpleByPrimaryKey(processOrder.getInstanceId());
		patent.setValueIndexOrder(processOrder);

		HumanAssessmentPatent humanAssessmentPatent = new HumanAssessmentPatent();
		BeanUtils.copyProperties(patent, humanAssessmentPatent);

		// 生成价值解读文字
		Map<String, String> labels = new HashMap<>();
		for (TaskOrder taskOrder: patent.getValueIndexOrder().getTaskOrders()) {
			for (TaskOrderLabel label: taskOrder.getLabels()) {
				String strVal = label.getStrValue();
				labels.put(label.getLabel().getKey(), Strings.isNullOrEmpty(strVal) ? label.getTextValue() : strVal);
			}
		}
		if (labels.size() > 0) {
			try {
				humanAssessmentPatent.setExpPatentValues(this.patentEvaluateService.expPatentValues(labels));
			} catch (Exception e) {
				logger.error("解读价值评估出错", e);
			}
		}

		return humanAssessmentPatent;
	}

	@Override
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
	public void alertTaskOrder() {
		List<TaskOrder> orders = taskOrderMapper.getAlertTaskOrder();
		if(orders == null || orders.size() == 0) {
			return;
		}
		List<Long> orderIds = new ArrayList<Long>(orders.size());
		for (TaskOrder taskOrder : orders) {
			String content = "工单号为" + taskOrder.getActTaskId() + "的工单已经过了预警时间,流程类型为" + Workflows.findProcessNameById(taskOrder.getProcessId().intValue()) +",环节为" + taskOrder.getTaskName() + ",请及时处理。";
			try {
				Message message = makeMessage(taskOrder, content);
				message.setType(MessageType.ProcessAlert.getType());
				messageMapper.add(message);
				orderIds.add(taskOrder.getId());
			} catch (Exception e) {
				logger.error("发送工单预警错误,工单号" + taskOrder.getActTaskId(),e);
			}
		}
		taskOrderMapper.updateHasAlert(orderIds);
	}
	
	private String getMessageRoute(int processType) {
		switch (processType) {
		case 2:
		case 3:
		case 4:
		case 5:
			return "patent";
		case 6:
			return "enterprise";
		case 7:
			return "enterpriseRequirement";
		case 8:
			return "match.patent";
		}
		return "patent";
	}

	private Message makeMessage(TaskOrder taskOrder, String content) {
		Message message = new Message();
		message.setUserId(taskOrder.getUserId());
		message.setContent(content);
		String url = "main.console." + getMessageRoute(taskOrder.getProcessId().intValue()) + ".process.order({processType:'"+ Workflows.findProcessTypeById(taskOrder.getProcessId().intValue())+"',taskType:'" + Workflows.findTaskKeyById(taskOrder.getProcessTaskId().intValue()) + "',actTaskId:'"+ taskOrder.getActTaskId() +"'})";
		message.setUrl(url);
		return message;
	}
	
	@Override
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
	public void dueTaskOrder() {
		List<TaskOrder> orders = taskOrderMapper.getDueTaskOrder();
		if(orders == null || orders.size() == 0) {
			return;
		}
		List<Long> orderIds = new ArrayList<Long>(orders.size());
		for (TaskOrder taskOrder : orders) {
			String content = "工单号为" + taskOrder.getActTaskId() + "的工单已经过了超时时间,流程类型为" + Workflows.findProcessNameById(taskOrder.getProcessId().intValue()) +",环节为" + taskOrder.getTaskName() + ",请及时处理。";
			try {
				Message message = makeMessage(taskOrder, content);
				message.setType(MessageType.ProcessDue.getType());
				messageMapper.add(message);
				orderIds.add(taskOrder.getId());
			} catch (Exception e) {
				logger.error("发送工单超时错误,工单号" + taskOrder.getActTaskId(),e);
			}
		}
		taskOrderMapper.updateHasDue(orderIds);
	}

	@Override
	public PageInfo<TaskOrder> findDueOrders(User user, int pageNum, int pageSize){
		Page<TaskOrder> page = (Page<TaskOrder>) taskOrderMapper.findDueOrders(user,pageNum,pageSize);
		return  page.toPageInfo();
	}

	@Override
	public PageInfo<TaskOrder> findAlertOrders(User user, int pageNum, int pageSize){
		Page<TaskOrder> page = (Page<TaskOrder>) taskOrderMapper.findAlertOrders(user,pageNum,pageSize);
		return  page.toPageInfo();
	}

	@Override
	public PageInfo<TaskOrder> findUnfinishedOrders(User user, int pageNum, int pageSize){
		Page<TaskOrder> page = (Page<TaskOrder>) taskOrderMapper.findUnfinishedOrders(user,pageNum,pageSize);
		return  page.toPageInfo();
	}

	@Override
	public PageInfo<TaskOrder> findFinishedOrders(User user, int pageNum, int pageSize){
		Page<TaskOrder> page = (Page<TaskOrder>) taskOrderMapper.findFinishedOrders(user,pageNum,pageSize);
		return  page.toPageInfo();
	}

	@Override
	public PageInfo<TaskOrder> findBackOrders(User user, int pageNum, int pageSize){
		Page<TaskOrder> page = (Page<TaskOrder>) taskOrderMapper.findBackOrders(user,pageNum,pageSize);
		return  page.toPageInfo();
	}
}