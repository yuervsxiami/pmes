package com.cnuip.pmes2.controller.api.request;

import java.util.List;
import java.util.Map;

public class QutationBean {
	private String returncode;
	private String message;
	private List<Map<String,String>> scyyzzlList;//审查员引证专利文献
	private List<Map<String,String>> scyyzfzlList;//审查员非引证专利文献
	private List<Map<String,String>> sqryzzlList;//申请人引证专利文献
	private List<Map<String,String>> scybyzzlList;//审查员被引证专利文献
	private List<Map<String,String>> sqrbyzzlList;//申请人被引证专利文献
	
	public String getReturncode() {
		return returncode;
	}

	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Map<String, String>> getScyyzzlList() {
		return scyyzzlList;
	}

	public void setScyyzzlList(List<Map<String, String>> scyyzzlList) {
		this.scyyzzlList = scyyzzlList;
	}

	public List<Map<String, String>> getScyyzfzlList() {
		return scyyzfzlList;
	}

	public void setScyyzfzlList(List<Map<String, String>> scyyzfzlList) {
		this.scyyzfzlList = scyyzfzlList;
	}

	public List<Map<String, String>> getSqryzzlList() {
		return sqryzzlList;
	}

	public void setSqryzzlList(List<Map<String, String>> sqryzzlList) {
		this.sqryzzlList = sqryzzlList;
	}

	public List<Map<String, String>> getScybyzzlList() {
		return scybyzzlList;
	}

	public void setScybyzzlList(List<Map<String, String>> scybyzzlList) {
		this.scybyzzlList = scybyzzlList;
	}

	public List<Map<String, String>> getSqrbyzzlList() {
		return sqrbyzzlList;
	}

	public void setSqrbyzzlList(List<Map<String, String>> sqrbyzzlList) {
		this.sqrbyzzlList = sqrbyzzlList;
	}

	@Override
	public String toString() {
		return "QutationBean [returncode=" + returncode + ", message=" + message + ", scyyzzlList=" + scyyzzlList
				+ ", scyyzfzlList=" + scyyzfzlList + ", sqryzzlList=" + sqryzzlList + ", scybyzzlList=" + scybyzzlList
				+ ", sqrbyzzlList=" + sqrbyzzlList + "]";
	}
	
}
