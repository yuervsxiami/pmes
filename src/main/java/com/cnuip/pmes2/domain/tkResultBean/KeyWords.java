package com.cnuip.pmes2.domain.tkResultBean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/28.
 * Time: 16:19
 */
@Setter
@Getter
public class KeyWords {
    private Integer status;
    private List<String> keywords;
    private Integer keywords_num;
    private List<String> scores;

}
