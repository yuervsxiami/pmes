package com.cnuip.pmes2.repository.core;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cnuip.pmes2.controller.api.request.LabelSearchCondition;
import com.cnuip.pmes2.domain.core.Label;

/**
* Create By Crixalis:
* 2018年1月11日 上午9:17:57
*/
@Repository
public interface LabelMapper {

	Label selectSimpleByPrimaryKey(Long id);
	
	Label selectByPrimaryKey(Long id);
	
	Label selectSimpleByKey(String key);
	
	Label selectByKey(String key);
	
	List<Label> selectByType(@Param("type")Integer type, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
	
	int add(Label label);
	
	int update(Label label);
	
	int changeState(@Param("id")Long id, @Param("state")Integer state);
	
	int checkKeyExist(String key);
	
	List<Label> search(@Param("condition")LabelSearchCondition condition, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
	
	List<Label> selectByTypeAndIndexType(@Param("type")Integer type, @Param("indexTypes")List<Integer> indexTypes, @Param("mapTypes")List<Integer> mapTypes);

	List<Long> selectIdByTypeAndIndexType(@Param("type")Integer type, @Param("indexTypes")List<Integer> indexTypes, @Param("mapTypes")List<Integer> mapTypes);

	List<Label> selectInProcessTask(@Param("processTaskId")Long processTaskId,@Param("labelsetId")Long labelsetId);

	int checkHasUsed(Long labelId);
	
}
