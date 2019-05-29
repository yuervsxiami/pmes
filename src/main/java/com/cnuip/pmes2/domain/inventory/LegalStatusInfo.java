package com.cnuip.pmes2.domain.inventory;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
* Create By Crixalis:
* 2018年3月3日 上午10:41:05
*/
@Data
public class LegalStatusInfo implements Serializable{
	
	private Long idLegal;
	private String uuid;
	private String stran;
	private String sysId;
	private String strLegalStatus;
	private String strstatusInfo;
	private String strLegalStatusDay;
	private String strAccredit;
	private Date creatTime;
	private Date signTime;

}
