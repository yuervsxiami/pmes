package com.cnuip.pmes2.repository.core;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by wangzhibin on 2018/3/6.
 */
public interface AbstractMapper<T> {
    void insertOne(T t);
    void deleteByField(@Param("field") String field, @Param("value") Object value);
    void updateOne(T t);
    void updateValueByField(@Param("pkField") String pkField, @Param("pkValue") Object pkValue,
                               @Param("field") String field, @Param("value") Object value);

    boolean existsByField(@Param("field") String field, @Param("value") Object value);

    List<T> getAll();
    T selectOneByField(@Param("field") String field, @Param("value") Object value);
    List<T> selectManyByField(@Param("field") String field, @Param("value") Object value);
}
