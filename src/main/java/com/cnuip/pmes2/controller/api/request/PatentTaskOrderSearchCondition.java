package com.cnuip.pmes2.controller.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by wangzhibin on 2018/1/25.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatentTaskOrderSearchCondition extends TaskOrderSearchCondition{
    //实例类型
    private Integer instanceType;
    //申请号
    private String an;
    //专利名称
    private String ti;
    //申请日
    private Date fromAd;
    private Date toAd;
    //申请人
    private String pa;
    //发明人
    private String pin;
    //法律状态
    private String lastLegalStatus;

}
