package com.cnuip.pmes2.domain.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @auhor Crixalis
 * @date 2018/4/13 14:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstanceLabel {

	private Long id;
	private Long instanceId;
	private Integer instanceType;
	private Long labelId;
	private String strValue;
	private String textValue;
	private Long userId;
	private Date createTime;
	private Date updateTime;
	private Label label;

	public InstanceLabel(Long instanceId, Long labelId, String strValue, String textValue) {
		this.instanceId = instanceId;
		this.labelId = labelId;
		this.strValue = strValue;
		this.textValue = textValue;
		this.instanceType = 1;
	}

	public InstanceLabel(Long instanceId, Long labelId, String strValue, String textValue, Integer instanceType) {
		this.instanceId = instanceId;
		this.labelId = labelId;
		this.strValue = strValue;
		this.textValue = textValue;
		this.instanceType = instanceType;
	}
}
