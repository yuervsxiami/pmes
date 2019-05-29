package com.cnuip.pmes2.controller.api.request;
/**
 * 元数据搜索条件,提供元数据类型,元数据名称,元数据状态,操作人,最后一次操作时间范围作为搜索条件
 * 
* Create By Crixalis:
* 2018年1月5日 上午10:43:56
*/

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MetaSearchCondition {

	private Integer type;
	private String name;
	private Integer state;
	private String username;
	private Date fromTime;
	private Date toTime;

}
