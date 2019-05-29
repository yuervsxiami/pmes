package com.cnuip.pmes2.controller.api.request;

import java.util.List;

public class SimilarityBean {
	private int returncode;
	private String message;
	private Long unfilterTotalCount;
	private List<PatentInfo> patentInfoList;
	private List<SectionInfo> sectionInfos; 
	private String relevance;
	private String wordTips;
	private String trsLastWhere;
	private int recordCount;
	private String patentWords;
	public int getReturncode() {
		return returncode;
	}
	public void setReturncode(int returncode) {
		this.returncode = returncode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getUnfilterTotalCount() {
		return unfilterTotalCount;
	}
	public void setUnfilterTotalCount(Long unfilterTotalCount) {
		this.unfilterTotalCount = unfilterTotalCount;
	}
	public List<PatentInfo> getPatentInfoList() {
		return patentInfoList;
	}
	public void setPatentInfoList(List<PatentInfo> patentInfoList) {
		this.patentInfoList = patentInfoList;
	}
	public List<SectionInfo> getSectionInfos() {
		return sectionInfos;
	}
	public void setSectionInfos(List<SectionInfo> sectionInfos) {
		this.sectionInfos = sectionInfos;
	}
	public String getRelevance() {
		return relevance;
	}
	public void setRelevance(String relevance) {
		this.relevance = relevance;
	}
	public String getWordTips() {
		return wordTips;
	}
	public void setWordTips(String wordTips) {
		this.wordTips = wordTips;
	}
	public String getTrsLastWhere() {
		return trsLastWhere;
	}
	public void setTrsLastWhere(String trsLastWhere) {
		this.trsLastWhere = trsLastWhere;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public String getPatentWords() {
		return patentWords;
	}
	public void setPatentWords(String patentWords) {
		this.patentWords = patentWords;
	}
	@Override
	public String toString() {
		return "SimilarityBean [returncode=" + returncode + ", message=" + message + ", unfilterTotalCount="
				+ unfilterTotalCount + ", patentInfoList=" + patentInfoList + ", sectionInfos=" + sectionInfos
				+ ", relevance=" + relevance + ", wordTips=" + wordTips + ", trsLastWhere=" + trsLastWhere
				+ ", recordCount=" + recordCount + ", patentWords=" + patentWords + "]";
	}
	
}
