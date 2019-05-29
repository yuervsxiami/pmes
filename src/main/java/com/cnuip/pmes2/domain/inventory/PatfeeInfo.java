package com.cnuip.pmes2.domain.inventory;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
* Create By Crixalis:
* 2018年3月3日 上午10:51:53
*/
@Data
public class PatfeeInfo implements Serializable {

	private String idFee;
	private String applyNum;
	private String sysId;
	private String fee;
	private String feeType;
	private String receiption;
	private String registerCode;
	private String hkInfo;
	private String state;
	private String hkDate;
	private String receiptionDate;
	private Date creatTime;
	private Date signTime;
	
}
