package com.cnuip.pmes2.service;

import com.cnuip.pmes2.controller.api.request.ProcessSearchCondition;
import com.cnuip.pmes2.domain.core.ProcessOrder;
import com.cnuip.pmes2.domain.core.TaskOrderLabel;
import com.cnuip.pmes2.exception.ProcessException;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import com.cnuip.pmes2.domain.core.Process;

import java.util.List;

/**
 * Created by wangzhibin on 2018/1/12.
 */
public interface ProcessService {

    Process selectSimpleByPrimaryKey(Long id);

    Process selectByPrimaryKey(Long id);

    List<TaskOrderLabel> selectAllLabel(Long processOrderId);

    PageInfo<Process> selectAll(int pageNum, int pageSize);

    List<Process> selectAllByInstanceType(Integer type);

    PageInfo<Process> selectByType(Integer type, int pageNum, int pageSize);

    Process addProcess(Process process,long userId) throws ProcessException;

    Process updateProcess(Process process,long userId) throws ProcessException;

    void changeState(long id, Integer state) throws ProcessException;
    void changeLabelset(long id, Long labelsetId) throws ProcessException ;
    void changeTime(long id, Long alertTime, Long dueTime) throws ProcessException;

    PageInfo<Process> search(ProcessSearchCondition condition, int pageNum, int pageSize);

    Long getLastProcessCnfIdByType(Integer type);

}
