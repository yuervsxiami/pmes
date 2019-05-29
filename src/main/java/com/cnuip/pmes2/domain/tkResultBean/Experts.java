package com.cnuip.pmes2.domain.tkResultBean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/28.
 * Time: 16:22
 */
@Getter
@Setter
public class Experts {
    private Integer status;
    private List<String> experts_scorelist;
    private List<String> experts_typelist;
    private Integer experts_num;
    private List<String> experts_namelist;
}
