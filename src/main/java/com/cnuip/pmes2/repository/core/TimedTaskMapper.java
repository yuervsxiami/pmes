package com.cnuip.pmes2.repository.core;

import java.util.Date;
import java.util.List;

import com.cnuip.pmes2.controller.api.request.TimedTaskDetailCondition;
import com.cnuip.pmes2.controller.api.request.TimedTaskSearchCondition;
import com.cnuip.pmes2.domain.core.TimedTaskDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cnuip.pmes2.domain.core.TimedTask;

/**
* Create By Crixalis:
* 2018年3月6日 上午10:05:17
*/
@Repository
public interface TimedTaskMapper {
	
	void insert(TimedTask timeTask);
	
	void updateFinishAmount(@Param("id") Long id, @Param("amount") Integer amount);
	
	void finish(Long id);

	TimedTask selectByPrimaryKey(Long id);
	
	TimedTask selectUnfinishTask(Integer type);

	List<TimedTask> search(@Param("condition") TimedTaskSearchCondition condition,@Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
	
	Date selectLastUpdateTime(Integer type);

	void insertDetail(TimedTaskDetail taskDetail);

	List<TimedTaskDetail> searchDetail(@Param("condition") TimedTaskDetailCondition condition,
									   @Param("pageSize") int pageSize, @Param("pageNum")int pageNum);

	void finishTaskDetail(Long id);

	void recordDetailLog(@Param("id")Long id, @Param("log")String log);

}
