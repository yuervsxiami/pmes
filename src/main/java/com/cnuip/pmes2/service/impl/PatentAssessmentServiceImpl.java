package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.controller.api.request.PatentStartSearchCondition;
import com.cnuip.pmes2.controller.api.request.TimedTaskDetailCondition;
import com.cnuip.pmes2.controller.api.request.TimedTaskSearchCondition;
import com.cnuip.pmes2.domain.core.HumanAssessmentPatent;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.core.TimedTask;
import com.cnuip.pmes2.domain.core.TimedTaskDetail;
import com.cnuip.pmes2.repository.core.PatentMapper;
import com.cnuip.pmes2.repository.core.TimedTaskMapper;
import com.cnuip.pmes2.service.PatentAssessmentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * PatentAssessmentServiceImpl
 *
 * @author: xiongwei
 * Date: 2018/2/2 下午2:13
 */
@Service
@Transactional(readOnly = true)
public class PatentAssessmentServiceImpl implements PatentAssessmentService {

    @Autowired
    private PatentMapper patentMapper;

    @Autowired
    private TimedTaskMapper timedTaskMapper;

    @Override
    public List<Patent> searchHumanAssessment(PatentStartSearchCondition condition, int pageNum, int pageSize) {
        return this.patentMapper.searchHumanAssessment(condition, pageNum, pageSize);
    }

    @Override
    public List<Patent> searchHumanAssessmentWithAns(List<String> ans, int pageNum, int pageSize) {
        return this.patentMapper.searchHumanAssessmentWithAns(ans, pageNum, pageSize);
    }

    @Override
    public HumanAssessmentPatent findAssessmentPatentById(Long id) {
        return this.patentMapper.findAssessmentPatentById(id);
    }

    @Override
    public List<TimedTask> searchTimedTasks(TimedTaskSearchCondition condition, int pageNum, int pageSize) {
        return this.timedTaskMapper.search(condition, pageNum, pageSize);
    }

    @Override
    public PageInfo<TimedTaskDetail> searchTimedTaskDetail(TimedTaskDetailCondition condition, int pageNum, int pageSize) {
        Page<TimedTaskDetail> page = (Page<TimedTaskDetail>) timedTaskMapper.searchDetail(condition,pageSize,pageNum);
        return page.toPageInfo();
    }
}
