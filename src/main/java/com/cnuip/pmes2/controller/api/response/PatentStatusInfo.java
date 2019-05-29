package com.cnuip.pmes2.controller.api.response;

import com.cnuip.pmes2.domain.inventory.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @auhor Crixalis
 * @date 2018/5/7 11:04
 */
@Data
public class PatentStatusInfo implements Serializable{
	//申请号
	private String an;
	//法律状态信息
	private List<LegalStatusInfo> legalStatusInfos;
	//实施许可信息
	private List<PatprsexploitationInfo> patprsexploitationInfos;
	//质押保全信息
	private List<PatprspreservationInfo> patprspreservationInfos;
	//转让信息
	private List<PatprstransferInfo> patprstransferInfos;
	//评分信息
	private List<ScoreInfo> scoreInfos;
	//费用信息
	private List<PatfeeInfo> patfeeInfos;

}
