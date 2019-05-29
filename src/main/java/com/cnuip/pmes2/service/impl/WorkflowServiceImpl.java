package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.constant.Patents;
import com.cnuip.pmes2.constant.Workflows;
import com.cnuip.pmes2.controller.api.request.PatentStartSearchCondition;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.core.ProcessOrder;
import com.cnuip.pmes2.service.ProcessOrderService;
import com.cnuip.pmes2.service.WorkflowService;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * WorkflowServiceImpl
 *
 * @author: xiongwei
 * Date: 2018/1/28 下午3:30
 */
@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProcessOrderService processOrderService;

    @Override
    public PageInfo<Patent> findPatentByWorkflow(Workflows.PatentBasicIndex flow, PatentStartSearchCondition condition,
                                             int pageNum, int pageSize) {
        TaskQuery taskQuery = this.taskService.createTaskQuery();
        taskQuery.processDefinitionKey("patentBasicIndex");
        taskQuery.taskDefinitionKey(flow.getTaskKey());
        if (!Strings.isNullOrEmpty(condition.getUsername())) {
            taskQuery.taskAssignee(condition.getUsername()); // 被指派的用户
        }
        PageInfo<Patent> result = new PageInfo<>();
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotal(taskQuery.count());
        List<Task> tasks = taskQuery.listPage(pageNum, pageSize);
//        result.setList();
        return null;
    }

    @Override
    public Patent startPatentBasicIndex(Patent patent, long userId) {
        ProcessOrder processOrder = new ProcessOrder();
        processOrder.setInstanceId(patent.getId());
        processOrder.setInstanceType(Patents.ProcessOrderType.Patent.getValue()); // 专利
//        processOrder.setProcessType(1);
        processOrder.setProcessCnfId(1l);
        processOrder.setUserId(userId);
//        processOrderService.insert();
        return null;
    }
}
