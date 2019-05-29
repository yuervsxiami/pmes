package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
* Create By Crixalis:
* 2018年3月6日 上午9:53:43
*/
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimedTask extends BaseModel {
	
	public TimedTask(Integer type, Integer totalAmount, Date beginTime) {
		super();
		this.type = type;
		this.totalAmount = totalAmount;
		this.beginTime = beginTime;
	}
	private Long id;
	private Integer type;
	private Integer totalAmount;
	private Integer finishAmount = 0;
	private Date createTime;
	private Date updateTime;
	private Date finishTime;
	private Date beginTime;

}
