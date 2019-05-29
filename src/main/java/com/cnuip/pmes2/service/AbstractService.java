package com.cnuip.pmes2.service;

import java.util.List;

/**
 * Created by wangzhibin on 2018/3/6.
 */
public interface AbstractService<T> extends BaseService {
    T insertOne(T t);
    void deleteByField(String field, Object value);
    T updateOne(T t);
    T updateValueByField(String pkField, Object pkValue, String field, Object value);

    boolean existsByField(String field, Object value);

    List<T> getAll();
    T selectOneByField(String field, Object value);
    List<T> selectManyByField(String field, Object value);
}
