package com.cnuip.pmes2.domain.tkResultBean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/28.
 * Time: 16:24
 */
@Setter
@Getter
public class Companies {
    private Integer status;
    private List<String> companies_scorelist;
    private List<String> companies_typelist;
    private List<String> companies_namelist;
    private Integer companies_num;
}
