package com.cnuip.pmes2.domain.inventory;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
* Create By Crixalis:
* 2018年3月3日 上午11:13:23
*/
@Data
public class PatprstransferInfo implements Serializable {

	private Long idTrans;
	private String an;
	private String sysId;
	private String applicantInfo;
	private String strStatusInfo;
	private String strLegalStatus;
	private String strLegalStatusDay;
	private String eventCode;
	private String ti;
	private String ab;
	private String cl;
	private String ipc;
	private String type;
	private String beforeTransAp;
	private String afterTransAp;
	private String currentAp;
	private String beforeTransAddr;
	private String afterTransAddr;
	private String currentAddr;
	private String areaCode;
	private String effectiveDate;
	private Date creatTime;
	private Date signTime;

}
