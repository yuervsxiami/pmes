package com.cnuip.pmes2.domain.core;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2019/5/21.
 * Time: 10:32
 */
@Setter
@Getter
public class Result implements Serializable {
    private Integer id;
    @Excel(name = "*成果名称",orderNum = "0")
    private String name;
    @Excel(name = "成熟度",orderNum = "4")
    private String maturity;
    @Excel(name = "合作方式",orderNum = "5")
    private String mode;
    @Excel(name="省份",orderNum = "2")
    private String provinceName;
    @Excel(name = "大学名称",orderNum = "0")
    private String college;
    @Excel(name = "学院",orderNum = "1")
    private String facultyDepartment;
    @Excel(name = "联系人",orderNum = "6")
    private String linkman;
    @Excel(name = "联系电话",orderNum = "7")
    private String phone;
    @Excel(name = "邮箱",orderNum = "8")
    private String email;
    @Excel(name = "成果所有人",orderNum = "9")
    private String holder;
    @Excel(name = "成果所有人电话",orderNum = "10")
    private String holderPhone;
    @Excel(name = "来源",orderNum = "11")
    private String source;
    private Long editorId;
    private String editorName;
    private Date createdTime;
    private Date updatedTime;
    private String ipcIds;
    private String nicIds;
    private String ntccIds;
    @Excel(name="*成果介绍",orderNum = "2")
    private String introduction;
    private String innovationPoint;
    private String technicalIndicator;
    @Excel(name="*应用领域",orderNum = "1")
    private String applicationDomain;
    private String marketOutlook;
    private String teamIntroduction;
    private String content;
}
