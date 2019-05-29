package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.controller.api.request.ProcessSearchCondition;
import com.cnuip.pmes2.domain.core.Labelset;
import com.cnuip.pmes2.domain.core.Process;
import com.cnuip.pmes2.domain.core.ProcessOrder;
import com.cnuip.pmes2.domain.core.TaskOrderLabel;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.exception.ProcessException;
import com.cnuip.pmes2.repository.core.LabelsetMapper;
import com.cnuip.pmes2.repository.core.ProcessMapper;
import com.cnuip.pmes2.repository.core.ProcessOrderMapper;
import com.cnuip.pmes2.repository.core.TaskOrderLabelMapper;
import com.cnuip.pmes2.service.ProcessService;
import com.cnuip.pmes2.util.ResponseEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wangzhibin on 2018/1/12.
 */
@Service
@Transactional(readOnly=true)
public class ProcessSeviceImpl implements ProcessService {
    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private TaskOrderLabelMapper taskOrderLabelMapper;

    @Autowired
    private ProcessOrderMapper processOrderMapper;

    @Autowired
    private LabelsetMapper labelsetMapper;

    @Override
    public Process selectSimpleByPrimaryKey(Long id){
        return processMapper.selectSimpleByPrimaryKey(id);
    }

    @Override
    public Process selectByPrimaryKey(Long id){
        Process process = processMapper.selectByPrimaryKey(id);
        if(process != null && process.getLabelset()!=null) {
            Labelset labelset = process.getLabelset();
            labelset.setLabelsetLabels(labelsetMapper.selectLabelsetLabels(labelset.getId()));
        }
        return process;
    }

    @Override
    public List<TaskOrderLabel> selectAllLabel(Long processOrderId) {
        return taskOrderLabelMapper.selectByProcessOrderId(processOrderId);
    }

    @Override
    public PageInfo<Process> selectAll(int pageNum, int pageSize){
        Page<Process> page = (Page<Process>) processMapper.selectAll(pageNum,pageSize);
        return page.toPageInfo();
    }

    @Override public Long getLastProcessCnfIdByType(Integer type){
        return  processMapper.getLastProcessCnfIdByType(type);
    }

    @Override
    public List<Process> selectAllByInstanceType(Integer type) {
        return processMapper.selectAllByInstanceType(type);
    }

    @Override
    public PageInfo<Process> selectByType(Integer type, int pageNum, int pageSize) {
        Page<Process> page = (Page<Process>) processMapper.selectByType(type, pageNum, pageSize);
        return page.toPageInfo();
    }

    @Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
    @Override
    public Process addProcess(Process process, long userId) throws ProcessException {
        try {
            process.setUserId(userId);
            if (process.getState() == null || (!process.getState().equals(1) && !process.getState().equals(0))) {
                process.setState(1);
            }
            processMapper.addProcess(process);
            return processMapper.selectByPrimaryKey(process.getId());
        } catch (Exception e) {
            throw new ProcessException(e, ResponseEnum.PROCESS_ADD_ERROR);
        }
    }

    @Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
    @Override
    public Process updateProcess(Process process, long userId) throws ProcessException {
        try {
            process.setUserId(userId);

            processMapper.updateProcess(process);
            return processMapper.selectByPrimaryKey(process.getId());
        } catch (Exception e) {
            throw new ProcessException(e, ResponseEnum.PROCESS_UPDATE_ERROR);
        }
    }

    @Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
    @Override
    public void changeState(long id, Integer state) throws ProcessException {
        Process process = processMapper.selectByPrimaryKey(id);
        if(process == null) {
            throw new ProcessException(ResponseEnum.PROCESS_NOT_EXIST);
        }
        if(state == 0) {
            int num = processOrderMapper.getAliveProcessOrderNum(process.getType());
            if(num > 0) {
                throw new ProcessException(ResponseEnum.PROCESS_HAS_AVLICE_ORDER);
            }
        }
        processMapper.changeState(id,state);
    }

    @Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
    @Override
    public void changeLabelset(long id, Long labelsetId) throws ProcessException {
        Process process = processMapper.selectByPrimaryKey(id);
        if(process == null) {
            throw new ProcessException(ResponseEnum.PROCESS_NOT_EXIST);
        }
        if(!process.getState().equals(0)) {
            throw new ProcessException(ResponseEnum.PROCESS_CANT_SET_LABELSET);
        }
        try {
            processMapper.changeLabelset(id, labelsetId);
        } catch (Exception e) {
            throw new ProcessException(e, ResponseEnum.PROCESS_SET_LABELSET_ERROR);
        }
    }

    @Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
    @Override
    public void changeTime(long id, Long alertTime, Long dueTime) throws ProcessException {
        try {
            if (alertTime != null && alertTime == 0) {
                alertTime = null;
            }
            if (dueTime != null && dueTime == 0) {
                dueTime = null;
            }
            processMapper.changeTime(id, alertTime, dueTime);

        } catch (Exception e) {
            throw new ProcessException(e,ResponseEnum.PROCESS_SET_TIME_ERROR);
        }
    }

    @Override
    public PageInfo<Process> search(ProcessSearchCondition condition, int pageNum, int pageSize) {
        Page<Process> page = (Page<Process>) processMapper.search(condition, pageNum, pageSize);
        return page.toPageInfo();
    }

}
