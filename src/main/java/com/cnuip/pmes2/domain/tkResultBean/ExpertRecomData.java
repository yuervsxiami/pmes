package com.cnuip.pmes2.domain.tkResultBean;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/28.
 * Time: 16:18
 */
@Setter
@Getter
public class ExpertRecomData {
    private Integer status;
    private KeyWords keywords;
    private Experts experts;
    private Companies companies;
}
