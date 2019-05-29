package com.cnuip.pmes2.controller.api.request;

import com.cnuip.pmes2.domain.core.NationalEconomy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * Created by wangzhibin on 2018/3/3.
 */
@Data
public class ProcessOrderSearchCondition{
    //实例类型
    private Integer instanceType;
    //定单号
    private String actInstanceId;
    //工单号
    private String actTaskId;
    //定单状态
    private Integer state;
    //定单日期
    private Date fromUpdateTime;
    private Date toUpdateTime;
    //流程模版
    private Long processId;

    //申请号
    private String an;
    //专利名称
    private String ti;
    //申请人
    private String pa;
    //发明人
    private String pin;
    //法律状态
    private String lastLegalStatus;

    //企业名称
    private String name;
    //企业需求
    private String requirement;
    //国民经济领域
    private String nationalEconomyField;

    private Integer pageSize;
    private Integer pageNum;
}
