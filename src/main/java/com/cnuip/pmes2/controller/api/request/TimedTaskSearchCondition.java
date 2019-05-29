package com.cnuip.pmes2.controller.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by wangzhibin on 2018/3/13.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimedTaskSearchCondition{
    //类型
    private Integer type;
    private String types;
    //状态
    private Integer state;
    //申请日
    private Date fromCreateTime;
    private Date toCreateTime;
}
