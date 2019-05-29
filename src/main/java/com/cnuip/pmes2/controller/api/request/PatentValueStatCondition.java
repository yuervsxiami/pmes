package com.cnuip.pmes2.controller.api.request;

import com.cnuip.pmes2.constant.Patents;
import lombok.Data;

import java.util.Date;

/**
 * PatentValueStatCondition
 * 专利价值分布查询接口
 *
 * @author: xiongwei
 * Date: 2018/3/28 下午5:43
 */
@Data
public class PatentValueStatCondition {

    private Patents.Types patentType; // 专利类型
    private String pin; // 发明人
    private String pa; // 专利权人
    private String sic; // 主分类号

    // 申请日
    private Date fromADate; // 开始时间
    private Date toADate; // 结束时间

    // 公开公告日
    private Date fromODate; // 开始时间
    private Date toODate; // 结束时间

}
