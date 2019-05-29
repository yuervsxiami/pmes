package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.domain.core.TaskSkipInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @auhor Crixalis
 * @date 2018/5/25 11:23
 */
@Repository
public interface TaskSkipMapper {

	List<Long> findNeedSkipByTaskType(@Param("taskTypes")List<Long> taskTypes);

	TaskSkipInfo findByPrimaryKey(Long id);

	void close(Long id);

}
