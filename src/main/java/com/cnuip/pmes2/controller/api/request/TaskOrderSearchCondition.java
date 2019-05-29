package com.cnuip.pmes2.controller.api.request;

import java.util.Date;

import lombok.Data;

/**
* Create By Crixalis:
* 2018年2月2日 上午9:51:54
*/
@Data
public class TaskOrderSearchCondition {
	
	public Integer processType;
	
	public Integer taskType;
    //操作人
    private Long userId;
    //定单号
    private String actInstanceId;
    //工单号
    private String actTaskId;
    //工单状态
    private Integer state;
    //操作时间
    private Date fromUpdateTime;
    private Date toUpdateTime;

    private Integer pageNum;
    private Integer pageSize;

}
