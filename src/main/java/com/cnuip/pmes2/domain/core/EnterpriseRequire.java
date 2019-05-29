package com.cnuip.pmes2.domain.core;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/10/12.
 * Time: 14:44
 */

@Data
public class EnterpriseRequire implements Serializable {
    private int id;
    private String companyName;
    //企业信息id
    private Long enterpriseId;
    private String title;
    private String requirement;
    //分类(必须到第三级) sys_classify.id
    private Long classifyId;
    //企业需求关键词
    private String keywords;
    private int state;
    private int pushState;
    private String source;
    //原始id
    private Long originId;
    private String requirementType;
    private String enterpriseType;
    private int userId;
    private Date originCreateTime;
    private Date createTime;
    private Date updateTime;
    private String keywordsWeight;
    private String code;
}
