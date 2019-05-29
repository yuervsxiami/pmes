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
public class ProcessTask extends BaseModel {
    private Long id;
    private String name; //环节名称
    private Long processId; //流程模版 id
    private Process process;
    private Integer taskType; //环节类型
    private Integer state;
    private Long roleId; //环节可用的角色
    private Role role;
    private Long alertTime; //预警时间
    private Long dueTime; //超时时间
    private Long userId; //操作人id'
    private User user;
    private Date createTime;
    private Date updateTime;
    private List<ProcessTaskLabel> labels;

}
