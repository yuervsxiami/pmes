package com.cnuip.pmes2.controller.api.request;

import java.util.List;

import com.cnuip.pmes2.domain.core.Label;
import com.cnuip.pmes2.domain.core.TaskOrderLabel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskOrderDealParam {

	private Long taskOrderId;
	//派单用的userId
	private Long assignUserId;
//	//被标引过的标签
//	private List<Label> assignedLabels;
	//被计算过的标签
	private List<TaskOrderLabel> caledLabels;
	//人工审核是否通过
	private Boolean pass;
	//退单原因
	private String reason;
	//搜索结果
	private String matchResult;

	public TaskOrderDealParam(Long taskOrderId, Long assignUserId) {
		this.taskOrderId = taskOrderId;
		this.assignUserId = assignUserId;
	}
}
