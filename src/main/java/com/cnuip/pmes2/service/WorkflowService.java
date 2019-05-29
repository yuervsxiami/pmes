package com.cnuip.pmes2.service;

import com.cnuip.pmes2.constant.Workflows;
import com.cnuip.pmes2.controller.api.request.PatentStartSearchCondition;
import com.cnuip.pmes2.domain.core.Patent;
import com.github.pagehelper.PageInfo;

/**
 * WorkflowService
 *
 * @author: xiongwei
 * Date: 2018/1/28 下午2:49
 */
public interface WorkflowService {

    /**
     * 根据指定条件查询专利定单
     * @param flow
     * @param condition
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<Patent> findPatentByWorkflow(Workflows.PatentBasicIndex flow, PatentStartSearchCondition condition,
                                          int pageNum, int pageSize);

    /**
     * 启动专利基础标引流程
     * @param patent
     * @return
     */
    Patent startPatentBasicIndex(Patent patent, long userId);
}
