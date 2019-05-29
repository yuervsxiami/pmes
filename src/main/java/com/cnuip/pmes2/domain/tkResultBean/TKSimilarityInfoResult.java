package com.cnuip.pmes2.domain.tkResultBean;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/28.
 * Time: 14:52
 */
@Setter
@Getter
public class TKSimilarityInfoResult {
    private boolean success;
    private String message;
    private Object error;
    private SimilarityResult result;
}
