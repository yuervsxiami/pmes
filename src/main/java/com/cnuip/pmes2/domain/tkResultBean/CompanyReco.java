package com.cnuip.pmes2.domain.tkResultBean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/28.
 * Time: 16:39
 */
@Setter
@Getter
public class CompanyReco {
    private List<String> recommendList;
    private List<String> assigneesList;
}
