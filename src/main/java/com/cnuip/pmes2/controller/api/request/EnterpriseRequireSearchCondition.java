package com.cnuip.pmes2.controller.api.request;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/10/12.
 * Time: 15:43
 */
@Setter
@Getter
public class EnterpriseRequireSearchCondition {
    private String title;
    private String companyName;
    private Integer pushState;
    private String source;
    private Date startTime;
    private Date endTime;
    private Integer pageSize;
    private Integer pageNum;
}
