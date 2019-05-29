package com.cnuip.pmes2.domain.tkResultBean;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/29.
 * Time: 9:50
 */
@Setter
@Getter
public class TKPatentListChangeResult {
    private boolean success;
    private String message;
    private Object error;
    private PatentListChange result;
}
