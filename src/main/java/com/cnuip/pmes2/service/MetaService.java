package com.cnuip.pmes2.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cnuip.pmes2.controller.api.request.MetaSearchCondition;
import com.cnuip.pmes2.domain.core.Meta;
import com.cnuip.pmes2.domain.core.MetaValue;
import com.cnuip.pmes2.exception.MetaException;
import com.github.pagehelper.PageInfo;

/**
* Create By Crixalis:
* 2018年1月4日 下午2:31:37
*/
public interface MetaService {

	Meta selectByKey(String key);
	
	Meta selectByPrimaryKey(Long id);

	List<Meta> selectAll();
	
	PageInfo<Meta> selectByType(Integer type, int pageNum, int pageSize);
	
	PageInfo<Meta> search(MetaSearchCondition condition, int pageNum, int pageSize);
	
	List<MetaValue> selectMetaValues(String key);
	
	MetaValue selectMetaValue(Long id);
	
	Meta addMeta(Meta meta, Long userId) throws MetaException;
	
	MetaValue addMetaValue(MetaValue value) throws MetaException;
	
	Meta updateMeta(Meta meta, Long userId) throws MetaException;
	
	MetaValue updateMetaValue(MetaValue value);
	
	void changeMetaState(@Param("key")String key, @Param("state")Integer state);

	void deleteMetaValue(Long id);

}