package com.cnuip.pmes2.domain.inventory;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
* Create By Crixalis:
* 2018年3月3日 上午11:03:31
*/
@Data
public class PatprspreservationInfo implements Serializable {

	private Long idPre;
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
	private String chuZhiRen;
	private String zhiQuanRen;
	private String DangQianZqr;
	private String heTongZt;
	private String heTongDjh;
	private String shengXiaoRi;
	private String bianGenRi;
	private String jieChuRi;
	private Date creatTime;
	private Date signTime;
	
}
