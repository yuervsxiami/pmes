package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.repository.core.AbstractMapper;
import com.cnuip.pmes2.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangzhibin on 2018/3/6.
 */
public class AbstractServiceImpl<T> extends BaseServiceImpl implements AbstractService<T> {

    @Autowired
    private AbstractMapper<T> abstractMapper;

    @Override
    public T insertOne(T entity) {
        abstractMapper.insertOne(entity);
        return entity;
    }

    @Override
    public void deleteByField(String field, Object value) {
        abstractMapper.deleteByField(field, value);
    }

    @Override
    public T updateOne(T entity) {
        abstractMapper.updateOne(entity);
        return entity;
    }

    @Override
    public T updateValueByField(String pkField, Object pkValue, String field, Object value) {
        abstractMapper.updateValueByField(pkField, pkValue, field, value);
        return abstractMapper.selectOneByField(pkField, pkValue);
    }

    @Override
    public boolean existsByField(String field, Object value) {
        return abstractMapper.existsByField(field, value);
    }

    @Override
    public List<T> getAll() {
        return abstractMapper.getAll();
    }

    @Override
    public T selectOneByField(String field, Object value) {
        return abstractMapper.selectOneByField(field, value);
    }

    @Override
    public List<T> selectManyByField(String field, Object value) {
        return abstractMapper.selectManyByField(field, value);
    }
}
