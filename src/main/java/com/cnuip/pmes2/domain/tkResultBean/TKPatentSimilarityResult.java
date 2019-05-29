package com.cnuip.pmes2.domain.tkResultBean;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/28.
 * Time: 14:43
 */
@Setter
@Getter
public class TKPatentSimilarityResult {
    private boolean success;
    private String message;
    private String result;
    private Object error;
}
