package com.cnuip.pmes2.repository.core;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cnuip.pmes2.controller.api.request.MetaSearchCondition;
import com.cnuip.pmes2.domain.core.Meta;
import com.cnuip.pmes2.domain.core.MetaValue;

/**
* Create By Crixalis:
* 2018年1月4日 上午10:28:28
*/
@Repository
public interface MetaMapper {

	Meta selectByKey(String key);
	
	Meta selectByPrimaryKey(Long id);
	
	List<Meta> selectAll();
	
	List<Meta> selectByType(@Param("type")Integer type, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
	
	List<MetaValue> selectMetaValue(String key);
	
	MetaValue selectValueByPrimaryKey(Long id);
	
	int addMeta(Meta meta);
	
	int addMetaValue(MetaValue value);
	
	int addMetaValues(List<MetaValue> values);
	
	int updateMeta(Meta meta);
	
	int updateMetaValue(MetaValue value);
	
	int changeMetaState(@Param("key")String key, @Param("state")Integer state);
	
	int deleteMetaValue(Long id);
	
	int checkKeyExist(String key);
	
	List<Meta> search(@Param("condition")MetaSearchCondition condition, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
	
}
