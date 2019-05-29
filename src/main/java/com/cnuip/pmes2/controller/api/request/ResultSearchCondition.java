package com.cnuip.pmes2.controller.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2019/5/27.
 * Time: 10:47
 */
@Setter
@Getter
public class ResultSearchCondition {
    private String provinceName;
    private String college;
    private String name;
    private String mode;
    private String maturity;
    private String phone;
    private String source;
    private String ipcCodes;
    private String nicCodes;
    private String ntccCodes;
    private Integer pageNum;
    private Integer pageSize;
}
