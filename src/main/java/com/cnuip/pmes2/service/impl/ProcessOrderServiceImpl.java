package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.constant.MessageType;
import com.cnuip.pmes2.constant.Workflows;
import com.cnuip.pmes2.constant.Workflows.ProcessType;
import com.cnuip.pmes2.controller.api.request.ProcessOrderSearchCondition;
import com.cnuip.pmes2.domain.core.EnterpriseRequirement;
import com.cnuip.pmes2.domain.core.Match;
import com.cnuip.pmes2.domain.core.Message;
import com.cnuip.pmes2.domain.core.ProcessOrder;
import com.cnuip.pmes2.domain.core.TaskOrder;
import com.cnuip.pmes2.domain.core.User;
import com.cnuip.pmes2.domain.core.*;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.exception.ProcessOrderException;
import com.cnuip.pmes2.repository.core.*;
import com.cnuip.pmes2.service.BaseService;
import com.cnuip.pmes2.service.BatchQuickService;
import com.cnuip.pmes2.service.ProcessOrderService;
import com.cnuip.pmes2.util.LabelDealUtil;
import com.cnuip.pmes2.util.ResponseEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.cnuip.pmes2.domain.core.Process;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhibin on 2018/1/26.
 */
@Service
@Transactional(readOnly=true)
public class ProcessOrderServiceImpl extends BaseServiceImpl implements ProcessOrderService {
	
    @Autowired
    private ProcessOrderMapper processOrderMapper;
    
    @Autowired
    private TaskOrderMapper taskOrderMapper;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private EnterpriseRequirementMapper enterpriseRequireMapper;
	
	@Autowired
	private MatchMapper matchMapper;
	
	@Autowired
	private MessageMapper messageMapper;

	@Autowired
	private BatchQuickService batchQuickService;

	@Autowired
	private PatentMapper patentMapper;

	@Autowired
	private PatentIndexMapper patentIndexMapper;

	@Autowired
	private EnterpriseMapper enterpriseMapper;

	@Autowired
	private EnterpriseRequirementMapper enterpriseRequirementMapper;

	@Autowired
	private LabelsetMapper labelsetMapper;

	@Autowired
	private ProcessMapper processMapper;

	private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ProcessOrder selectSimpleByPrimaryKey(Long id) {
        return processOrderMapper.selectSimpleByPrimaryKey(id);
    }

    @Override
    public PageInfo<ProcessOrder> selectSimpleByInstanceId(Long instanceId, int pageNum, int pageSize) {
        Page<ProcessOrder> page = (Page<ProcessOrder>) processOrderMapper.selectSimpleByInstanceId(instanceId, pageNum, pageSize);
        return page.toPageInfo();
    }

    @Override
    public PageInfo<ProcessOrder> selectSimpleByInstanceType(Integer instanceType, int pageNum, int pageSize) {
        Page<ProcessOrder> page = (Page<ProcessOrder>) processOrderMapper.selectSimpleByInstanceType(instanceType, pageNum, pageSize);
        return page.toPageInfo();
    }

    @Override
    public PageInfo<ProcessOrder> selectSimpleByProcessType(Integer processType, int pageNum, int pageSize) {
        Page<ProcessOrder> page = (Page<ProcessOrder>) processOrderMapper.selectSimpleByProcessType(processType, pageNum, pageSize);
        return page.toPageInfo();
    }

    @Override
    public ProcessOrder selectByPrimaryKey(Long id) {
        return processOrderMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<ProcessOrder> selectByInstanceId(Long instanceId, int pageNum, int pageSize) {
        Page<ProcessOrder> page = (Page<ProcessOrder>) processOrderMapper.selectByInstanceId(instanceId, pageNum, pageSize);
        return page.toPageInfo();
    }

    @Override
    public PageInfo<ProcessOrder> selectByInstanceType(Integer instanceType, int pageNum, int pageSize) {
        Page<ProcessOrder> page = (Page<ProcessOrder>) processOrderMapper.selectByInstanceType(instanceType, pageNum, pageSize);
        return page.toPageInfo();
    }

    @Override
    public PageInfo<ProcessOrder> selectByProcessType(Integer processType, int pageNum, int pageSize) {
        Page<ProcessOrder> page = (Page<ProcessOrder>) processOrderMapper.selectByProcessType(processType, pageNum, pageSize);
        return page.toPageInfo();
    }

    @Override
    @Transactional(rollbackFor = {BussinessLogicException.class,Exception.class})
    public ProcessOrder insert(ProcessOrder order) throws ProcessOrderException{
        try{
            processOrderMapper.insert(order);
            return processOrderMapper.selectSimpleByPrimaryKey(order.getId());
        }
        catch (Exception e){
            throw new ProcessOrderException(e,ResponseEnum.PROCESS_ORDER_ADD_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = {BussinessLogicException.class,Exception.class})
    public void delete(Long id) throws ProcessOrderException {
        try {
            processOrderMapper.delete(id);
        } catch (Exception e) {
            throw new ProcessOrderException(e, ResponseEnum.PROCESS_ORDER_DELETE_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = {BussinessLogicException.class,Exception.class})
    public void changeState(Long id, Integer state) throws ProcessOrderException{
        try{
            processOrderMapper.changeState(id,state);
        }
        catch (Exception e){
            throw new ProcessOrderException(e, ResponseEnum.PROCESS_ORDER_CHANGE_STATE_ERROR);
        }
    }

	@Override
    @Transactional(rollbackFor = {BussinessLogicException.class,Exception.class})
	public ProcessOrder startProcess(ProcessOrder order, User user) throws ProcessOrderException {
		ProcessOrder aliveOrder = processOrderMapper.getUnfinishedIndexOrder(order.getInstanceId(), order.getProcessType());
		if(aliveOrder != null) {
			throw new ProcessOrderException(ResponseEnum.PROCESS_ORDER_ISEXIST);
		}
		Process process = processMapper.selectAliveByType(order.getProcessType());
		if(process == null) {
			throw new ProcessOrderException(ResponseEnum.PROCESS_NOT_ALIVE);
		}
		order.setLabelsetId(process.getLabelsetId());
		if(order.getProcessType() < 8) {
			Labelset labelset = labelsetMapper.selectAliveByType(order.getProcessType());
			if(labelset==null) {
				throw new ProcessOrderException(ResponseEnum.PROCESS_MISS_LABELSET);
			}
		}
		//如果是匹配信息,instanceId为企业需求的id,先插入匹配信息对象,然后吧instanceId变为企业信息id
		if (order.getInstanceType().equals(5)) {
			EnterpriseRequirement require = enterpriseRequireMapper.findById(order.getInstanceId());
			Match match = new Match(require.getRequirement(), 1, require.getId(), 0, user.getId());
			matchMapper.save(match);
            initMatchPatent(order);
			order.setInstanceId(match.getId());
		}
		ProcessInstance instance = startActProcess(order);
		order.setActInstanceId(instance.getProcessInstanceId());
		order.setState(0);
		if(user != null) {
			order.setUserId(user.getId());
		}
		ProcessOrder returnOrder = insert(order);
		runtimeService.setVariable(instance.getId(), "processOrderId", returnOrder.getId());
		String actTaskId = processOrderMapper.getFirstActTaskId(instance.getProcessInstanceId());
		taskOrderMapper.updateProcessOrderIdByActTaskId(returnOrder.getId(), actTaskId);
		if(returnOrder.getInstanceType().equals(1)) {
            initPatentIndex(order, instance, returnOrder);
        }
        if(returnOrder.getInstanceType().equals(2)) {
            initEnterpriseIndex(returnOrder);
        }
        if(returnOrder.getInstanceType().equals(3)) {
            initEnterpriseRequirementIndex(returnOrder);
        }
		return returnOrder;
	}

    private void initMatchPatent(ProcessOrder order) {
        System.out.println(order);
        enterpriseRequireMapper.changeIndexState(order.getInstanceId(), null, 1, null);
    }

    private void initEnterpriseRequirementIndex(ProcessOrder returnOrder) {
        enterpriseRequireMapper.changeIndexState(returnOrder.getInstanceId(),1, null,null);
    }

    private void initEnterpriseIndex(ProcessOrder returnOrder) {
        enterpriseMapper.changeIndexState(returnOrder.getInstanceId(), 1);
    }

    private void initPatentIndex(ProcessOrder order, ProcessInstance instance, ProcessOrder returnOrder) throws ProcessOrderException {
        Patent patent = patentMapper.selectByPrimaryKey(returnOrder.getInstanceId());
        try {
            LabelDealUtil util = batchQuickService.calOnePatent(patent);
            runtimeService.setVariable(instance.getId(), "values", util.getValues());
        } catch (BussinessLogicException e) {
            logger.error("批量快速失败" + patent.getAn(),e);
            throw new ProcessOrderException(ResponseEnum.PROCESS_ORDER_FAIL_FLASHPHONE);
        }
        if(patent.getIndex() == null) {
            patentIndexMapper.insert(patent.getAn());
        }
        PatentIndex index = new PatentIndex();
        index.setAn(patent.getAn());
        switch (order.getProcessType()) {
            case 2:
                index.setHasBaseIndexing(1);
                break;
            case 3:
                index.setHasValueIndexing(1);
                break;
            case 4:
                index.setHasPriceIndexing(1);
                break;
            case 5:
                index.setHasDeepIndexing(1);
                break;
        }
        patentIndexMapper.update(index);
    }

    private String getKeyByProcessType (Integer type) {
		ProcessType[] processTypes = Workflows.ProcessType.values();
		for (ProcessType processType : processTypes) {
			if(processType.getId().equals(type)) {
				return processType.getProcessDefinitionKey();
			}
		}
		return "";
	}
	
	private ProcessInstance startActProcess(ProcessOrder order) {
		String key = getKeyByProcessType(order.getProcessType());
		ProcessInstance instance = runtimeService.startProcessInstanceByKey(key);
		return instance;
	}

    @Override
    public ProcessOrder findByOrderId(Long id) {
        return this.processOrderMapper.findByOrderId(id);
    }

    @Override
    public PageInfo<ProcessOrder> searchPatent(ProcessOrderSearchCondition condition){
        Page<ProcessOrder> page = (Page<ProcessOrder>) processOrderMapper.searchPatent(condition, condition.getPageNum(),condition.getPageSize());
        return page.toPageInfo();
    }

    @Override
    public PageInfo<ProcessOrder> searchRequirement(ProcessOrderSearchCondition condition){
        Page<ProcessOrder> page = (Page<ProcessOrder>) processOrderMapper.searchRequirement(condition, condition.getPageNum(),condition.getPageSize());
        List<ProcessOrder> items = page.getResult();
        if (items!=null){
            for(ProcessOrder po:items){
                EnterpriseRequirement epr = po.getEnterpriseRequirement();
                if (epr!=null){
                    Enterprise ep = epr.getEnterprise();
                    ep = this.fillEnterprise(ep, new String[]{"nationalEconomy", "province", "city", "district"});
                }
            }
        }
        return page.toPageInfo();
    }

    @Override
    public List<ProcessOrder> getAllProcessOrders(Integer instanceType, Long instanceId, Long processId) {
        return processOrderMapper.getAllProcessOrders(instanceType, instanceId, processId);
    }

	@Override
    @Transactional(rollbackFor = {BussinessLogicException.class,Exception.class})
	public void alertProcessOrder() {
		List<ProcessOrder> orders = processOrderMapper.getAlertProcessOrder();
		if(orders == null || orders.size() == 0) {
			return;
		}
		List<Long> orderIds = new ArrayList<Long>(orders.size());
		for (ProcessOrder processOrder : orders) {
			String content = "定单号为" + processOrder.getActInstanceId() + "的定单已经过了预警时间,流程类型为" + Workflows.findProcessNameById(processOrder.getProcessCnfId().intValue()) +"。";
			try {
				Message message = makeMessage(processOrder, content);
				message.setType(MessageType.ProcessAlert.getType());
				messageMapper.add(message);
				orderIds.add(processOrder.getId());
			} catch (Exception e) {
				logger.error("发送定单预警错误,工单号" + processOrder.getActInstanceId(),e);
			}
		}
		processOrderMapper.updateHasAlert(orderIds);
	}

	private Message makeMessage(ProcessOrder processOrder , String content) {
		Message message = new Message();
		message.setUserId(processOrder.getUserId());
		message.setContent(content);
		return message;
	}

	@Override
    @Transactional(rollbackFor = {BussinessLogicException.class,Exception.class})
	public void dueProcessOrder() {
		List<ProcessOrder> orders = processOrderMapper.getDueProcessOrder();
		if(orders == null || orders.size() == 0) {
			return;
		}
		List<Long> orderIds = new ArrayList<Long>(orders.size());
		for (ProcessOrder processOrder : orders) {
			String content = "定单号为" + processOrder.getActInstanceId() + "的定单已经过了超时时间,流程类型为" + Workflows.findProcessNameById(processOrder.getProcessCnfId().intValue()) +"。";
			try {
				Message message = makeMessage(processOrder, content);
				message.setType(MessageType.ProcessDue.getType());
				messageMapper.add(message);
				orderIds.add(processOrder.getId());
			} catch (Exception e) {
				logger.error("发送定单超时错误,工单号" + processOrder.getActInstanceId(),e);
			}
		}
		processOrderMapper.updateHasDue(orderIds);
		
	}
}
