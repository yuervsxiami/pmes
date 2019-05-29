package com.cnuip.pmes2.domain.inventory;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
* Create By Crixalis:
* 2018年3月3日 上午11:16:06
*/
@Data
public class ScoreInfo implements Serializable {

	private String idScore;
	private String an;
	private String score;
	private String title;
	private String texts;
	private String num;
	private Date creatTime;
	private Date signTime;
	
}
