package com.cnuip.pmes2.domain.tkResultBean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/29.
 * Time: 9:55
 */
@Setter
@Getter
public class PatentListChange {
    private List<Map<String,String>> minusData;
    private List<Map<String,String>> addData;
}
