package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.controller.api.request.ProcessTaskSearchCondition;
import com.cnuip.pmes2.domain.core.ProcessTask;
import com.cnuip.pmes2.domain.core.ProcessTaskLabel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wangzhibin on 2018/1/12.
 */
@Repository
public interface ProcessTaskMapper {

	ProcessTask selectSimpleByPrimaryKey(Long id);
	
    ProcessTask selectByPrimaryKey(Long id);

    @Select("SELECT * FROM p_process_task WHERE id = #{id}")
    @ResultMap("baseMap")
    ProcessTask findById(Long id);

    List<ProcessTask> selectAll(@Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

    List<ProcessTask> selectByProcessId(Long processId);

    List<ProcessTask> selectByType(@Param("type")Integer type, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

    List<ProcessTaskLabel> selectProcessTaskLabels(@Param("id")Long id,@Param("labelsetId")Long labelsetId);

    List<ProcessTaskLabel> selectProcessTaskLabelsByLabelsetId(Long labelsetId);

    int copyProcessTaskLabels(@Param("processTaskLabels")List<ProcessTaskLabel> processTaskLabels, @Param("labelSetId") Long labelsetId);

    int addProcessTask(ProcessTask processTask);

    int addTaskLabel(ProcessTaskLabel taskLabel);

    int addTaskLabels(@Param("list")List<ProcessTaskLabel> taskLabels,@Param("labelsetId")long labelsetId);

    int updateProcessTask(ProcessTask processTask);

    int deleteTaskLabel(@Param("taskId")long taskId,@Param("labelId")long labelId,@Param("labelsetId")long labelsetId);
    int deleteTaskLabels(@Param("taskId")long taskId,@Param("labelsetId")long labelsetId);
    Boolean existsLabel(@Param("taskId")long taskId,@Param("labelId")long labelId,@Param("labelsetId")long labelsetId);

    int changeState(@Param("id")long id, @Param("state")Integer state);
    int changeTime(@Param("id")long id, @Param("alertTime")Long alertTime, @Param("dueTime")Long dueTime);
    int changeRole(@Param("id")long id, @Param("roleId")Long roleId);


    List<ProcessTask> search(@Param("condition")ProcessTaskSearchCondition condition,
                         @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

    Long selectRoleIdByTaskKey(String key);
    
    Long selectIdByTaskKey(String key);
    
}
