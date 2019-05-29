package com.cnuip.pmes2.service;

import com.cnuip.pmes2.controller.api.request.PatentStartSearchCondition;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.exception.PatentStartException;
import com.github.pagehelper.PageInfo;

import java.util.List;


/**
 * Created by wangzhibin on 2018/1/25.
 */
public interface PatentStartService {

    Patent selectByPrimaryKey(Long id);
    Patent selectSimpleByPrimaryKey(Long id);
    Patent selectByAn(String an);
    Patent insert(Patent patent) throws PatentStartException;
    Patent update(Patent patent) throws PatentStartException;

    PageInfo<Patent> search(PatentStartSearchCondition condition, int pageNum, int pageSize);
    PageInfo<Patent> searchWithAns(List<String> ans, int pageNum, int pageSize);

}
