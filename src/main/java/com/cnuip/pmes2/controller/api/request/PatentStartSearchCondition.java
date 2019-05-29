package com.cnuip.pmes2.controller.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Created by wangzhibin on 2018/1/23.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatentStartSearchCondition {
    //条件模式
    private Integer mode;
    //法律状态
    private List<String> lastLegalStatus;
    //专利类型
    private List<Long> types;
    //专利名称
    private String ti;
    //申请日
    private Date fromAd;
    private Date toAd;
    //申请号
    private String an;
    //申请号列表
    private List<String> ans;
    //公开日
    private Date fromOd;
    private Date toOd;
    //公开号
    private String onm;
    //公告日
    private Date fromPd;
    private Date toPd;
    //公告号
    private String pnm;
    //申请人
    private String pa;
    //发明人
    private String pin;
    // 操作人；或在查询定单时指定分派的人
    private String username;
    // 是否已批量处理
    private Integer hasBatchIndexed;
    // 专利流程是否处理
    private Integer indexState;
    //模糊查询：M，精确查询：J
    private Integer searchType;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //记录最后修改时间
    private Date lastUpdateTime;

    private Date lastCreateTime;

}
