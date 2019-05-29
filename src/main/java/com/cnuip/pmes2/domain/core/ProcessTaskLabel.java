package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * Created by wangzhibin on 2018/1/12.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ProcessTaskLabel extends BaseModel {
    private Long taskId; //环节 id;
    private Long labelId; //标签 id;
    private Label label;
    private Date createTime;
    private Date updateTime;
}
