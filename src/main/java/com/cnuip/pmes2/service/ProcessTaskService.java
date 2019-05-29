package com.cnuip.pmes2.service;

import com.cnuip.pmes2.controller.api.request.ProcessTaskSearchCondition;
import com.cnuip.pmes2.domain.core.ProcessTask;
import com.cnuip.pmes2.domain.core.ProcessTaskLabel;
import com.cnuip.pmes2.exception.ProcessTaskException;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by wangzhibin on 2018/1/13.
 */
public interface ProcessTaskService {

    ProcessTask selectByPrimaryKey(Long id);

    PageInfo<ProcessTask> selectAll(int pageNum, int pageSize);

    PageInfo<ProcessTask> selectByType(Integer type, int pageNum, int pageSize);

    ProcessTask addProcessTask(ProcessTask task, long userId) throws ProcessTaskException;

    ProcessTask updateProcessTask(ProcessTask task, long userId) throws ProcessTaskException;

    PageInfo<ProcessTask> search(ProcessTaskSearchCondition condition, int pageNum, int pageSize);

    List<ProcessTaskLabel> selectProcessTaskLabels(Long id);

    void changeState(long id, Integer state);
    void changeTime(long id, Long alertTime, Long dueTime) throws ProcessTaskException;
    void changeRole(long id, Long userId) throws ProcessTaskException;
}

