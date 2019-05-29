package com.cnuip.pmes2.controller.api.request;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestParam;

import com.cnuip.pmes2.domain.core.Label;

import lombok.Data;

/**
 * Create By Crixalis: 2018年1月11日 上午9:53:24
 */
@Data
public class LabelSearchCondition extends Label {

	private Date fromTime;
	private Date toTime;
	private String username;

	public LabelSearchCondition(Integer type, String name, Integer state, Integer indexType, Long source,
			String username, Date fromTime, Date toTime) {
		this.type = type;
		this.name = name;
		this.state = state;
		this.indexType = indexType;
		this.source = source;
		this.username = username;
		this.fromTime = fromTime;
		this.toTime = toTime;
	};

}
