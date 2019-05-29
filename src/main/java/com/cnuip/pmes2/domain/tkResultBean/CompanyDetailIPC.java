package com.cnuip.pmes2.domain.tkResultBean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/28.
 * Time: 17:09
 */
@Setter
@Getter
public class CompanyDetailIPC {
    private List<DetailIPC> data;
    private Integer total;
}
