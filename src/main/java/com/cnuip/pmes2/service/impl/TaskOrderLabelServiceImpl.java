package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.controller.api.request.PatentTaskOrderLabelSearchCondition;
import com.cnuip.pmes2.domain.core.InstanceLabel;
import com.cnuip.pmes2.domain.core.LabelChangeHistory;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.core.TaskOrderLabel;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.exception.TaskOrderLabelException;
import com.cnuip.pmes2.repository.core.InstanceLabelMapper;
import com.cnuip.pmes2.repository.core.LabelChangeHistoryMapper;
import com.cnuip.pmes2.repository.core.TaskOrderLabelMapper;
import com.cnuip.pmes2.service.BatchQuickService;
import com.cnuip.pmes2.service.PatentService;
import com.cnuip.pmes2.service.TaskOrderLabelService;
import com.cnuip.pmes2.util.ResponseEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by wangzhibin on 2018/1/26.
 */
@Service
@Transactional(readOnly=true)
public class TaskOrderLabelServiceImpl implements TaskOrderLabelService {

    @Autowired
    private TaskOrderLabelMapper taskOrderLabelMapper;

    @Autowired
    private LabelChangeHistoryMapper labelChangeHistoryMapper;

    @Autowired
    private BatchQuickService batchQuickService;

    @Autowired
    private InstanceLabelMapper instanceLabelMapper;

    @Autowired
    private PatentService patentService;

    @Override
    public PageInfo<TaskOrderLabel> selectByTaskOrderId(Long taskOrderId, int pageNum, int pageSize) {
        Page<TaskOrderLabel> page = (Page<TaskOrderLabel>) taskOrderLabelMapper.selectByTaskOrderId(taskOrderId, pageNum, pageSize);
        return page.toPageInfo();
    }

    @Override
    public List<TaskOrderLabel> findByTaskOrderId(Long taskOrderId) {
        return this.taskOrderLabelMapper.findByTaskOrderId(taskOrderId);
    }

    @Override
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
    public TaskOrderLabel insert(TaskOrderLabel label) throws TaskOrderLabelException {
        try {
            taskOrderLabelMapper.insert(label);
            return taskOrderLabelMapper.selectByPrimaryKey(label.getId());
        } catch (Exception e) {
            throw new TaskOrderLabelException(e, ResponseEnum.TASK_ORDER_LABEL_ADD_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
    public InstanceLabel update(InstanceLabel label, Long userId, Long patentId) throws TaskOrderLabelException {
        try {
            InstanceLabel beforeLabel = instanceLabelMapper.selectByPrimaryKey(label.getId());
            LabelChangeHistory labelChangeHistory = new LabelChangeHistory(patentId,label.getLabelId(),beforeLabel.getStrValue(),
                    label.getStrValue(),beforeLabel.getTextValue(),label.getTextValue(),userId);
            instanceLabelMapper.updateLabels(label);
            labelChangeHistoryMapper.insert(labelChangeHistory);
            if(label.getLabelId().equals(264L)) {
                Patent patent = patentService.selectSimpleByPrimaryKey(patentId);
                batchQuickService.startOneBatchProcess(patent);
            }
            return instanceLabelMapper.selectByPrimaryKey(label.getId());
        } catch (Exception e) {
            throw new TaskOrderLabelException(e, ResponseEnum.TASK_ORDER_LABEL_UPDATE_ERROR);
        }
    }

    @Override
    public List<LabelChangeHistory> findLabelChangeHistory(Long labelId, Long patentId) {
        return labelChangeHistoryMapper.select(patentId,labelId);
    }

    @Override
    @Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
    public void delete(Long id) throws TaskOrderLabelException {
        try {
            taskOrderLabelMapper.delete(id);
        } catch (Exception e) {
            throw new TaskOrderLabelException(e, ResponseEnum.TASK_ORDER_LABEL_DELETE_ERROR);
        }
    }

    @Override
    public PageInfo<TaskOrderLabel> patentSearch(PatentTaskOrderLabelSearchCondition condition, int pageNum, int pageSize) {
        Page<TaskOrderLabel> page = (Page<TaskOrderLabel>) taskOrderLabelMapper.patentSearch(condition, pageNum, pageSize);
        return page.toPageInfo();
    }
}


