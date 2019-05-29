package com.cnuip.pmes2.constant;

/**
 * Workflows
 *
 * @author: xiongwei
 * Date: 2018/1/28 下午2:54
 */
public enum TimedTaskType {

	BatchUpdatePatent("批量更新专利", "all",1),
	BatchUpdateSub1("批量更新st_patentinfo_sub1", "st_patentinfo_sub1", 2),
	BatchUpdateSub3("批量更新st_patentinfo_sub3", "st_patentinfo_sub3", 3),
	BatchUpdateLegalStatus("批量更新st_legalstatusinfo", "st_legalstatusinfo", 4),
	BatchUpdateFee("批量更新st_patfeeinfo", "st_patfeeinfo", 5),
	BatchUpdatePrsexploitation("批量更新st_patprsexploitationinfo", "st_patprsexploitationinfo", 6),
	BatchUpdatePrspreservation("批量更新st_patprspreservationinfo", "st_patprspreservationinfo", 7),
	BatchUpdatePrstransfer("批量更新st_patprstransferinfo", "st_patprstransferinfo", 8),
	BatchUpdateScore("批量更新st_scoreinfo", "st_scoreinfo", 9),
    ;
	
	private TimedTaskType(String name, String tableName, Integer type) {
		this.name = name;
		this.tableName = tableName;
		this.type = type;
	}
	private String name;
	private String tableName;
	private Integer type;

	public String getName() {
		return name;
	}

	public String getTableName() {
		return tableName;
	}

	public Integer getType() {
		return type;
	}
    
}
