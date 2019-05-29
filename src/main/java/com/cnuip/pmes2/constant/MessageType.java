package com.cnuip.pmes2.constant;
/**
* Create By Crixalis:
* 2018年3月9日 上午9:57:17
*/
public enum MessageType {
	
	ProcessAlert("ProcessAlert", 1),//流程预警
	ProcessDue("ProcessDue", 2),//流程超时
	ProcessChargeback("ProcessChargeback", 3),//流程退单
	TaskRedeploy("TaskRedeploy", 4),//流程转派
	;
	
	private MessageType(String name, Integer type) {
		this.name = name;
		this.type = type;
	}
	private String name;
	private Integer type;
	public String getName() {
		return name;
	}
	public Integer getType() {
		return type;
	}
	
}
