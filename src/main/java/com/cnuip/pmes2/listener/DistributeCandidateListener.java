package com.cnuip.pmes2.listener;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnuip.pmes2.controller.api.request.TaskOrderDealParam;
import com.cnuip.pmes2.domain.core.TaskOrder;
import com.cnuip.pmes2.domain.core.User;
import com.cnuip.pmes2.exception.TaskOrderException;
import com.cnuip.pmes2.repository.core.ProcessTaskMapper;
import com.cnuip.pmes2.repository.core.TaskOrderMapper;
import com.cnuip.pmes2.service.TaskOrderService;

@Component
public class DistributeCandidateListener implements TaskListener {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private ProcessTaskMapper processTaskMapper;
	
	@Autowired
	private TaskOrderMapper taskOrderMapper;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private TaskOrderService taskOrderService;

	@Override
	public void notify(DelegateTask task) {
		Long roleId = processTaskMapper.selectRoleIdByTaskKey(task.getTaskDefinitionKey());
		if(roleId != null) {
			taskService.addCandidateGroup(task.getId(), "role" + roleId);
		}
		//创建act的task时同时创建系统订单
		Long processTaskId = processTaskMapper.selectIdByTaskKey(task.getTaskDefinitionKey());
		Long processOrderId = (Long) task.getVariable("processOrderId");
		TaskOrder order = new TaskOrder(task.getId(), processOrderId, processTaskId);
		taskOrderMapper.insert(order);	
		//派单部分
		String nextUser = (String) task.getVariable("nextUser");
		if(nextUser!= null) {
			task.setAssignee("user" + nextUser);
			taskOrderMapper.changeUserByActTaskId(task.getId(), Long.parseLong(nextUser));
			task.setVariable("nextUser", null);
		}
	}

}
