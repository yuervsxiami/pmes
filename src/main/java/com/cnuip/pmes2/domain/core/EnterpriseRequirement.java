package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * EnterpriseRequirement
 *
 * @author: xiongwei
 * Date: 2018/2/5 下午8:44
 */
@Data
public class EnterpriseRequirement extends BaseModel {

    private Long id;
    private Long enterpriseId;
    private Enterprise enterprise;
    private String requirement;
    private String keywords;
    private Integer state; // 0-流程未处理，1-流程已处理

    private Long userId;
    private User user;

    private Date createTime;
    private Date updateTime;

    private Integer hasIndexing;
    private Integer hasPatentMatching;
    private Integer hasProfessorMatching;

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

    //实例类型
    @JsonIgnore
    private Integer instanceType;
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
