package com.cnuip.pmes2.controller.api.response;

import lombok.Data;

/**
* 流程下各类型工单使用的时间概况
* Create By Crixalis:
* 2018年3月15日 下午2:25:34
*/
@Data
public class TaskUseTime {
	
	private Integer taskType;
	private Long max;
	private Long min;
	private Long avg;

}
