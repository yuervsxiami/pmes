package com.cnuip.pmes2.task;

import com.cnuip.pmes2.service.TaskOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
* Create By Crixalis:
* 2018年5月25日 下午2:44:40
*/
//@Component
public class AutoDealTask {

	private Logger logger = LoggerFactory.getLogger(FastEvaluateTask.class);

	@Autowired
	private TaskOrderService taskOrderService;

	/**
	 * 根据p_task_skip_info,自动派单
	 */
	@Scheduled(fixedDelay = 5000)
	public void autoDoAssignOrder() {
		logger.info("*************自动派单开始*************");
		taskOrderService.autoDoAssignOrder();
		logger.info("*************自动派单结束*************");
	}

	/**
	 * 根据p_task_skip_info,自动处理标引,会不记录任何标签,跳过环节
	 */
	@Scheduled(fixedDelay = 5000)
	public void autoDoIndex() {
		logger.info("*************自动处理标引开始*************");
		taskOrderService.autoDoIndex();
		logger.info("*************自动处理标引结束*************");
	}

	/**
	 * 根据p_task_skip_info,自动处理审核,直接通过
	 */
//	@Scheduled(fixedDelay = 5000)
	public void autoDoAudit() {
		logger.info("*************自动处理审核开始*************");
		taskOrderService.autoDoAudit();
		logger.info("*************自动处理审核结束*************");
	}


	/**
	 * 根据p_task_skip_info,自动处理半自动审核,直接通过
	 */
	@Scheduled(fixedDelay = 5000)
	public void autoDoSemiAudit() {
		logger.info("*************自动处理半自动审核开始*************");
		taskOrderService.autoDoSemiAudit();
		logger.info("*************自动处理半自动审核结束*************");
	}


}
