package com.cnuip.pmes2.controller.api.response;

import java.util.List;

import com.cnuip.pmes2.domain.core.Label;
import com.cnuip.pmes2.domain.core.TaskOrderLabel;

import lombok.Data;

/**
* Create By Crixalis:
* 2018年1月29日 下午4:33:36
*/
@Data
public class HumanIndexResponse {

	private List<TaskOrderLabel> labels;
	//退单原因
	private String reason;
	
}
