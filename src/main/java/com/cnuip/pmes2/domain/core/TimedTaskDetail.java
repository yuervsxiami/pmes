package com.cnuip.pmes2.domain.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @auhor Crixalis
 * @date 2018/5/31 10:10
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimedTaskDetail {

	private Long id;
	private Long timedTaskId;
	private Long patentId;
	private String an;
	private String ti;
	private String lastLegalStatus;
	private Integer type;
	private String pa;
	private String pin;
	private Date ad;
	private Integer state;
	private String log;
	private Date createTime;
	private Date updateTime;

	public TimedTaskDetail(Patent patent) {
		this.patentId = patent.getId();
		this.state = 0;
		this.an = patent.getAn();
		this.ti = patent.getTi();
		this.lastLegalStatus = patent.getLastLegalStatus();
		this.type = patent.getType();
		this.pa = patent.getPa();
		this.pin = patent.getPin();
		this.ad = patent.getAd();
	}

}
