package com.cnuip.pmes2.domain.core;

import lombok.Data;

import java.util.Date;

/**
 * @auhor Crixalis
 * @date 2018/3/26 15:51
 */
@Data
public class IndexEr {

	private Long id;
	private String an;
	private String reason;
	private Date createTime;
	private Date updateTime;
	private Integer isDeal;
	private Patent patent;

}
