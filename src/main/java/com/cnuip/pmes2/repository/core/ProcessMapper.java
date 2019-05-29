package com.cnuip.pmes2.repository.core;
import com.cnuip.pmes2.controller.api.request.ProcessSearchCondition;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.cnuip.pmes2.domain.core.Process;

import java.util.List;

/**
 * Created by wangzhibin on 2018/1/12.
 */
@Repository
public interface ProcessMapper extends AbstractMapper<Process> {

    Process selectSimpleByPrimaryKey(Long id);

    Process selectProcessById(Long id);

    Process selectByPrimaryKey(Long id);

    Long getLastProcessCnfIdByType(Integer type);

    List<Process> selectAll(@Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

    List<Process> selectAllByInstanceType(@Param("type")Integer type);

    List<Process> selectByType(@Param("type")Integer type, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

    Process selectAliveByType(Integer type);

    int addProcess(Process process);

    int updateProcess(Process process);

    int changeState(@Param("id")long id, @Param("state")Integer state);

    List<Process> search(@Param("condition")ProcessSearchCondition condition,
                          @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

    int changeLabelset(@Param("id")long id, @Param("labelsetId")Long labelsetId);
    int changeTime(@Param("id")long id, @Param("alertTime")Long alertTime, @Param("dueTime")Long dueTime);
}
