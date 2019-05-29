package com.cnuip.pmes2.domain.tkResultBean;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/28.
 * Time: 10:40
 */
@Setter
@Getter
public class DataDetail {
    private String input_keyword;
    private List similar_keywords;
}
