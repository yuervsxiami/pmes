package com.cnuip.pmes2.service;

import com.cnuip.pmes2.controller.api.request.PatentTaskOrderLabelSearchCondition;
import com.cnuip.pmes2.domain.core.InstanceLabel;
import com.cnuip.pmes2.domain.core.LabelChangeHistory;
import com.cnuip.pmes2.domain.core.TaskOrderLabel;
import com.cnuip.pmes2.exception.TaskOrderLabelException;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by wangzhibin on 2018/1/26.
 */
public interface TaskOrderLabelService {

    PageInfo<TaskOrderLabel> selectByTaskOrderId(Long taskOrderId, int pageNum, int pageSize);

    List<TaskOrderLabel> findByTaskOrderId(Long taskOrderId);

    TaskOrderLabel insert(TaskOrderLabel label) throws TaskOrderLabelException;

    /**
     * 直接变更标签值,同时将人工变更的历史记录进行记录
     * @param label
     * @param userId
     * @param patentId
     * @return
     * @throws TaskOrderLabelException
     */
    InstanceLabel update(InstanceLabel label, Long userId, Long patentId) throws TaskOrderLabelException;

    /**
     * 根据标签id和专利id查找标签变更记录
     * @param labelId
     * @param patentId
     * @return
     */
    List<LabelChangeHistory> findLabelChangeHistory(Long labelId, Long patentId);

    void delete(Long id) throws TaskOrderLabelException;

    PageInfo<TaskOrderLabel> patentSearch(PatentTaskOrderLabelSearchCondition condition, int pageNum, int pageSize);

}
