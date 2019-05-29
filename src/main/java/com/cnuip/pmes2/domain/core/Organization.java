package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Create By Crixalis: 2017年12月27日 下午1:43:25
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Data
public class Organization extends BaseModel {

	private Long id;
	private String name;
	private String remark;
	private Long parentId;
	private Date createTime;
	private Date updateTime;
	private List<Organization> sonOrganizations;

}
