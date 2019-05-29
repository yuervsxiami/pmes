package com.cnuip.pmes2.service;

import com.cnuip.pmes2.controller.api.request.ProcessOrderSearchCondition;
import com.cnuip.pmes2.domain.core.ProcessOrder;
import com.cnuip.pmes2.domain.core.User;
import com.cnuip.pmes2.exception.ProcessOrderException;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by wangzhibin on 2018/1/26.
 */
public interface ProcessOrderService extends BaseService {
	
	/**
	 * 开启流程,根据流程类型确定开启流程图
	 * @param order
	 * @return
	 * @throws ProcessOrderException
	 */
	ProcessOrder startProcess(ProcessOrder order, User user) throws ProcessOrderException;

    ProcessOrder selectSimpleByPrimaryKey(Long id);
    PageInfo<ProcessOrder> selectSimpleByInstanceId(Long instanceId, int pageNum, int pageSize);
    PageInfo<ProcessOrder> selectSimpleByInstanceType(Integer instanceType, int pageNum, int pageSize);
    PageInfo<ProcessOrder> selectSimpleByProcessType(Integer processType, int pageNum, int pageSize);

    ProcessOrder selectByPrimaryKey(Long id);
    PageInfo<ProcessOrder> selectByInstanceId(Long instanceId, int pageNum, int pageSize);
    PageInfo<ProcessOrder> selectByInstanceType(Integer instanceType, int pageNum, int pageSize);
    PageInfo<ProcessOrder> selectByProcessType(Integer processType, int pageNum, int pageSize);

    ProcessOrder insert(ProcessOrder order) throws ProcessOrderException;
    void delete(Long id) throws ProcessOrderException;

    void changeState(Long id, Integer state) throws ProcessOrderException;

    /**
     * 根据id查询定单详情
     * @param id
     * @return
     */
    ProcessOrder findByOrderId(Long id);

    PageInfo<ProcessOrder> searchPatent(ProcessOrderSearchCondition condition);
    PageInfo<ProcessOrder> searchRequirement(ProcessOrderSearchCondition condition);

    List<ProcessOrder> getAllProcessOrders(Integer instanceType, Long instanceId, Long processId);
    
	/**
	 * 把预警的processOrder进行预警并发送站内消息
	 */
	void alertProcessOrder();

	/**
	 * 把超时的processOrder进行超时标记并发送站内消息
	 */
	void dueProcessOrder();
}
