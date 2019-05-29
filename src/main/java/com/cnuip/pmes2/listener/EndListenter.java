package com.cnuip.pmes2.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class EndListenter implements TaskListener {

	@Override
	public void notify(DelegateTask task) {
		String prepreTaskId = (String) task.getVariable("preTaskId");
		task.setVariable("prepreTaskId", prepreTaskId);
		task.setVariable("preTaskId", task.getId());
		task.setVariable("holding", null);
	}


}
