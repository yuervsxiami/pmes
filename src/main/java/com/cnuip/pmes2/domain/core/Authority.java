package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Create By Crixalis: 2017年12月27日 下午4:50:02
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Data
@NoArgsConstructor
public class Authority extends BaseModel {
	private Long id;
	private String name;
	private String title;
	private Long parentId;
	private Long sortOrder;
	private String url;
	private String pic;
	private Boolean isMenu;
	private Date createTime;
	private Date updateTime;
	private List<Authority> sonAuthorities;
	
	public Authority(Long id) {
		super();
		this.id = id;
	}

}
