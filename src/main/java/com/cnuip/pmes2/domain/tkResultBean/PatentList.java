package com.cnuip.pmes2.domain.tkResultBean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/29.
 * Time: 9:23
 */
@Setter
@Getter
public class PatentList {
    private Integer total;
    private List<Map<String,String>> data;
}
