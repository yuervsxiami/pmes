package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class TaskOrder extends BaseModel {

	private Long id;
	private String actTaskId; // 流程引擎环节id
	private Long processOrderId; // 流程定单 id
	private Long processTaskId; // 环节配置id
	private ProcessTask processTask;
	private Integer state; // 状态
	private Long userId;
	private Boolean hasAlert;
	private Boolean hasDue;
	private Date createTime;
	private Date updateTime;
	private User user;
	private String taskName;
	private Long processId;
	private List<TaskOrderLabel> labels; // 标签集合

	Patent patent;
	ProcessOrder processOrder;
	
	Enterprise enterprise;
	
	EnterpriseRequirement enterpriseRequirement;
	
	Match match;
	
	public TaskOrder(String actTaskId, Long processOrderId, Long processTaskId) {
		super();
		this.actTaskId = actTaskId;
		this.processOrderId = processOrderId;
		this.processTaskId = processTaskId;
		this.state = 0;
	}
	
	
}
