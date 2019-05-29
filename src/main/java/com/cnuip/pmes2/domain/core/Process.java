package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by wangzhibin on 2018/1/12.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Process extends BaseModel {
    private Long id;
    private String name;
    private Long labelsetId; //标签体系模版 id
    private Labelset labelset;
    private Integer type; //流程类型
    private Integer instanceType; //实例类型
    private Integer state; //流程状态
    private Long alertTime; //预警时间
    private Long dueTime; //超时时间
    private Long userId; //操作人id'
    private User user;
    private Date createTime;
    private Date updateTime;
    private List<ProcessTask> tasks;

}
