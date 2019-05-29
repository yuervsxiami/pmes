package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Create By Crixalis: 2017年12月27日 下午1:43:25
 */
@Data
public class Role extends BaseModel {

	private Long id;
	private String name;
	private String remark;
	private Date createTime;
	private Date updateTime;
	private List<Authority> authorities;

}
