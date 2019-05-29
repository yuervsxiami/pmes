package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
* Create By Crixalis:
* 2018年1月4日 上午10:10:34
*/
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Data
public class Meta extends BaseModel {
	
	private Long id;
	private String key;
	private String name;
	private Integer state;
	private Integer type;
	private Integer valueType;
	private List<MetaValue> values;
	private Long userId;
	private User user;
	private Date createTime;
	private Date updateTime;

}
