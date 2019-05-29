package com.cnuip.pmes2.controller.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Created by wangzhibin on 2018/1/11.
 * 标签体系类型、标签体系名称、版本、操作人、操作时间区间
 */
@Data
@AllArgsConstructor
public class LabelsetSearchCondition {

    private Integer type;
    private String name;
    private String version;
    private String username;
    private Date fromTime;
    private Date toTime;

}
