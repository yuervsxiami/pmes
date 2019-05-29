package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProcessOrder extends BaseModel {
	private Long id;
	private Long labelsetId;
	private String actInstanceId; // 流程邀请实例id
	private Long instanceId; // 实例id
	private Integer instanceType; // 实例类型
	private Integer processType; // 流程类型
	private Long processCnfId; // 流程模版配置id
	private Process process;
	private Integer state; // 状态
	private Long userId;
	private User user;
	private Boolean hasAlert;
	private Boolean hasDue;
	private Date createTime;
	private Date updateTime;
	private List<TaskOrder> taskOrders; // 工单集合
	private Patent patent; // 专利
	private EnterpriseRequirement enterpriseRequirement; // 企业需求
	private String processName;
}
