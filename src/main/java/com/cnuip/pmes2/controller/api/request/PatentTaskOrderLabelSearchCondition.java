package com.cnuip.pmes2.controller.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by wangzhibin on 2018/1/26.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatentTaskOrderLabelSearchCondition {
    //实例类型
    private Integer instanceType;
    //操作人
    private Long userId;
    //工单状态
    private Integer state;
    //申请号
    private String an;
    //专利名称
    private String ti;
    //标签名称
    private String labelName;
    //标签来源
    private String labelSource;
    //标签体系
    private Long labelsetId;
    //操作时间
    private Date fromUpdateTime;
    private Date toUpdateTime;
}
