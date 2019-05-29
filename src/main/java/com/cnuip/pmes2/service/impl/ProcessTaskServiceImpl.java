package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.controller.api.request.ProcessTaskSearchCondition;
import com.cnuip.pmes2.domain.core.ProcessTask;
import com.cnuip.pmes2.domain.core.Process;
import com.cnuip.pmes2.domain.core.ProcessTaskLabel;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.exception.ProcessTaskException;
import com.cnuip.pmes2.repository.core.ProcessMapper;
import com.cnuip.pmes2.repository.core.ProcessTaskMapper;
import com.cnuip.pmes2.service.ProcessTaskService;
import com.cnuip.pmes2.util.ResponseEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wangzhibin on 2018/1/13.
 */
@Service
@Transactional(readOnly=true)
public class ProcessTaskServiceImpl implements ProcessTaskService {

    @Autowired
    private ProcessTaskMapper processTaskMapper;

    @Autowired
    private ProcessMapper processMapper;

    @Override
    public ProcessTask selectByPrimaryKey(Long id) {
        return processTaskMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<ProcessTask> selectAll(int pageNum, int pageSize) {
        Page<ProcessTask> page = (Page<ProcessTask>) processTaskMapper.selectAll(pageNum, pageSize);
        return page.toPageInfo();
    }

    @Override
    public PageInfo<ProcessTask> selectByType(Integer type, int pageNum, int pageSize) {
        Page<ProcessTask> page = (Page<ProcessTask>) processTaskMapper.selectByType(type, pageNum, pageSize);
        return page.toPageInfo();
    }

    @Override
    public PageInfo<ProcessTask> search(ProcessTaskSearchCondition condition, int pageNum, int pageSize) {
        Page<ProcessTask> page = (Page<ProcessTask>) processTaskMapper.search(condition, pageNum, pageSize);
        return page.toPageInfo();
    }

    @Override
    public List<ProcessTaskLabel> selectProcessTaskLabels(Long id) {
        ProcessTask processTask = processTaskMapper.selectSimpleByPrimaryKey(id);
        Process process = processMapper.selectSimpleByPrimaryKey(processTask.getProcessId());
        return processTaskMapper.selectProcessTaskLabels(id,process.getLabelsetId());
    }

    @Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
    @Override
    public ProcessTask addProcessTask(ProcessTask task, long userId) throws ProcessTaskException {
        try {
            task.setUserId(userId);
            if (task.getState() == null || (!task.getState().equals(1) && !task.getState().equals(0))) {
                task.setState(0);
            }
            processTaskMapper.addProcessTask(task);
            return processTaskMapper.selectByPrimaryKey(task.getId());
        } catch (Exception e) {
            throw new ProcessTaskException(e, ResponseEnum.TASK_ADD_ERROR);
        }
    }

    @Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
    @Override
    public ProcessTask updateProcessTask(ProcessTask task, long userId) throws ProcessTaskException {
        try {
            task.setUserId(userId);
            processTaskMapper.updateProcessTask(task);
            Process process = processMapper.selectByPrimaryKey(task.getProcessId());
            processTaskMapper.deleteTaskLabels(task.getId(),process.getLabelsetId());
            if (task.getLabels() != null && !task.getLabels().isEmpty()) {
                processTaskMapper.addTaskLabels(task.getLabels(),process.getLabelsetId());
            }
            return processTaskMapper.selectByPrimaryKey(task.getId());
        } catch (Exception e) {
            throw new ProcessTaskException(e, ResponseEnum.TASK_UPDATE_ERROR);
        }
    }

    @Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
    @Override
    public void changeState(long id, Integer state){
        processTaskMapper.changeState(id,state);
    }

    @Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
    @Override
    public void changeTime(long id, Long alertTime, Long dueTime) throws ProcessTaskException{

        try {
            if (alertTime != null && alertTime == 0) {
                alertTime = null;
            }
            if (dueTime != null && dueTime == 0) {
                dueTime = null;
            }
            processTaskMapper.changeTime(id, alertTime, dueTime);

        } catch (Exception e) {
            throw new ProcessTaskException(e,ResponseEnum.TASK_SET_TIME_ERROR);
        }
    }

    @Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
    @Override
    public void changeRole(long id, Long roleId) throws ProcessTaskException {
        try {
            processTaskMapper.changeRole(id, roleId);
        } catch (Exception e) {
            throw new ProcessTaskException(e, ResponseEnum.TASK_SET_ROLE_ERROR);
        }
    }

//    public List<ProcessTaskLabel>
}
