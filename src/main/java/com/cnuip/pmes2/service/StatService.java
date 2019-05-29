package com.cnuip.pmes2.service;

import java.util.List;
import java.util.Map;

import com.cnuip.pmes2.controller.api.response.TaskRemindNum;
import com.cnuip.pmes2.controller.api.response.TaskUseTime;
import com.cnuip.pmes2.controller.api.response.UserRemindNum;

/**
 * StatService
 *
 * @author: xiongwei
 * Date: 2018/3/11 下午8:49
 */
public interface StatService {

    /**
     * 统计专利定单总体概况
     * @return
     */
    Map<String, Long> countPatentProcessOrders();

	Map<String, Long> countProcessOrdersByProcessType(Long processType);
	
	/**
	 * 统计指定订单下各种工单的处理时间
	 * @param processId
	 * @return
	 */
	List<TaskUseTime> countUseTimeByProcess(Long processId);

	/**
	 * 统计指定订单下各种工单的预警数量
	 * @param processId
	 * @return
	 */
	List<TaskRemindNum> countTaskAlertNumByProcess(Long processId);

	/**
	 * 统计指定订单下各种工单的预警数量
	 * @param processId
	 * @return
	 */
	List<TaskRemindNum> countTaskDueNumByProcess(Long processId);
	
	/**
	 * 统计指定类型定单预警工单最多的人
	 * @param processId
	 * @return
	 */
	List<UserRemindNum> countMaxUserAlert(Long processId);
	
	/**
	 * 统计指定类型定单超时工单最多的人
	 * @param processId
	 * @return
	 */
	List<UserRemindNum> countMaxUserDue(Long processId);

    /**
     * 按流程统计超时定单
     * @return
     */
    Map<String, Long> countDetailForDueProcessOrders();

    /**
     * 按流程统计预警定单
     * @return
     */
    Map<String, Long> countDetailForAlertProcessOrders();

    /**
     * 按流程统计进行中定单
     * @return
     */
    Map<String, Long> countDetailForProcessingOrders();

    /**
     * 按流程统计已完成定单
     * @return
     */
    Map<String, Long> countDetailForDoneOrders();

}
