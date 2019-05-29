package com.cnuip.pmes2.controller.api.request;


public class SectionInfo {
	private String sectionName;
	private Long recordNum;
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public Long getRecordNum() {
		return recordNum;
	}
	public void setRecordNum(Long recordNum) {
		this.recordNum = recordNum;
	} 
}
