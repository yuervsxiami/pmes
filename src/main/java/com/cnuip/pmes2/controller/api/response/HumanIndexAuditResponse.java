package com.cnuip.pmes2.controller.api.response;
/**
* Create By Crixalis:
* 2018年1月30日 上午10:24:05
*/

import java.util.List;

import com.cnuip.pmes2.domain.core.TaskOrder;

import lombok.Data;

@Data
public class HumanIndexAuditResponse {
	
	private TaskOrder semiAutoOrder;
	
	private TaskOrder manualAuditOrder;
	
	private String reason;

}
