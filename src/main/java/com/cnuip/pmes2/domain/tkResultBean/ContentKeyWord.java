package com.cnuip.pmes2.domain.tkResultBean;

import lombok.Getter;
import lombok.Setter;

import java.lang.ref.PhantomReference;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/28.
 * Time: 14:24
 */
@Setter
@Getter
public class ContentKeyWord {
    private Integer status;
    private List<String> keywords;
}
