package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
* Create By Crixalis:
* 2018年2月8日 上午9:18:19
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match extends BaseModel {
	
	public Match(Long id, String result, Integer state) {
		super();
		this.id = id;
		this.result = result;
		this.state = state;
	}

	public Match(String keyword, Integer type, Long requireId, Integer state, Long userId) {
		super();
		this.keyword = keyword;
		this.type = type;
		this.requireId = requireId;
		this.state = state;
		this.userId = userId;
	}
	
	private Long id;
	private String keyword;
	private Integer type;
	private Long requireId;
	private String result;
	private Integer state;
	private Long userId;
	private User user;
	private Date createTime;
	private Date updateTime;

	private EnterpriseRequirement enterpriseRequirement;
	
    // 操作的开始时间
    @JsonIgnore
    private Date optDateFrom;
    // 操作的结束时间
    @JsonIgnore
    private Date optDateTo;
    // 分页参数
    @JsonIgnore
    private Integer pageNum;
    @JsonIgnore
    private Integer pageSize;
    
    //流程类型
    @JsonIgnore
    private Integer processType;
    //定单号
    @JsonIgnore
    private String actInstanceId;
    //环节类型
    @JsonIgnore
    private Integer taskType;
    //工单号
    @JsonIgnore
    private String actTaskId;
    //工单状态
    @JsonIgnore
    private Integer taskState;

	// 关联的最新的定单
	private ProcessOrder processOrder;

}
