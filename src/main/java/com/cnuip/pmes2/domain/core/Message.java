package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import lombok.Data;

import java.util.Date;

/**
* Create By Crixalis:
* 2018年3月9日 上午9:42:26
*/
@Data
public class Message extends BaseModel {

	private Long id;
	private Long userId;
	private Integer type;
	private Long relatedId;
	private String pic;
	private String url;
	private String content;
	private String senderName;
	private Boolean hasRead;
	private Integer hasDeal;
    private Date createTime;
    private Date updateTime;
	
}
