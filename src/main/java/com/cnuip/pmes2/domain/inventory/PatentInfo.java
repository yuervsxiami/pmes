package com.cnuip.pmes2.domain.inventory;

import lombok.Data;

import java.util.Date;

/**
 * PatentInfo
 *
 * @author: xiongwei
 * Date: 2017/12/23 上午11:56
 */
@Data
public class PatentInfo {

    private Long patentId; // 主键
    private Long idPatent; // 主键
    private String sectionName; // 专利所属数据库名称，如fmzl
    private String sysId; // 专利唯一ID
    private String ti; // 名称
    private String an; // 申请号
    private String ad; // 申请日
    private String pnm; // 公告号
    private String onm; // 公开号
    private String pd; // 公告日
    private String od; // 公开日
    private String pa; // 申请（专利权）人
    private String ab; // 摘要/简要说明
    private String lastLegalStatus; // 最新法律状态（有效、无效、在审、有效期届满）
    private String pin; // 发明（设计）人
    private String whereAbouts; // 专利去向（1.原始仓库2.标签标引库3.专利超市）
    private String isChecked; //
    private Date creatTime;
    private Date updateTime;
    private PatentSub3Info extra;
    
}
