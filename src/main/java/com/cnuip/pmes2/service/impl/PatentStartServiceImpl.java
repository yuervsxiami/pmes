package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.controller.api.request.PatentStartSearchCondition;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.exception.PatentStartException;
import com.cnuip.pmes2.repository.core.PatentMapper;
import com.cnuip.pmes2.service.PatentStartService;
import com.cnuip.pmes2.util.ResponseEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wangzhibin on 2018/1/25.
 */
@Service
@Transactional(readOnly=true)
public class PatentStartServiceImpl implements PatentStartService {
    @Autowired
    private PatentMapper patentMapper;

    @Override
    public Patent selectByPrimaryKey(Long id) {
        return patentMapper.selectByPrimaryKey(id);
    }

    @Override
    public Patent selectSimpleByPrimaryKey(Long id) {
        return patentMapper.selectSimpleByPrimaryKey(id);
    }

    @Override
    public Patent selectByAn(String an) {
        return patentMapper.selectByAn(an);
    }

    @Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
    @Override
    public Patent insert(Patent patent) throws PatentStartException{
        try{
            patentMapper.insert(patent);
            return patentMapper.selectByPrimaryKey(patent.getId());
        }
        catch (Exception e){
            throw new PatentStartException(e, ResponseEnum.PATENT_ADD_ERROR);
        }
    }

    @Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
    @Override
    public Patent update(Patent patent) throws PatentStartException{
        try{
            patentMapper.update(patent);
            return patentMapper.selectByPrimaryKey(patent.getId());
        }
        catch (Exception e){
            throw new PatentStartException(e, ResponseEnum.PATENT_UPDATE_ERROR);
        }
    }

    @Override
    public PageInfo<Patent> search(PatentStartSearchCondition condition, int pageNum, int pageSize) {
        Page<Patent> page = (Page<Patent>) patentMapper.search(condition, pageNum, pageSize);
        return page.toPageInfo();
    }

    @Override
    public PageInfo<Patent> searchWithAns(List<String> ans, int pageNum, int pageSize) {
        Page<Patent> page = (Page<Patent>) patentMapper.searchWithAns(ans, pageNum, pageSize);
        return page.toPageInfo();
    }
}
