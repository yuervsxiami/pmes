package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Create By Crixalis: 2018年1月4日 上午10:17:53
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Data
@NoArgsConstructor
public class MetaValue extends BaseModel {

	private Long id;
	private String metaKey;
	private String name;
	private String value;
	private String displayType;
	private Long userId;
	private User user;
	private Date createTime;
	private Date updateTime;

	public MetaValue(String metaKey, String name) {
		super();
		this.metaKey = metaKey;
		this.name = name;
	}

}
