package com.cnuip.pmes2.controller.api.request;

import lombok.Data;

/**
* Create By Crixalis:
* 2018年1月16日 下午4:32:49
*/
@Data
public class ProcessInstanceSearchCondition {

	private String id;
	private String processInstanceId;
	private Long instanceId;
	private Long userId;
	
}
