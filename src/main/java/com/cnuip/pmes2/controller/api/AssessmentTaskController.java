package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.request.PatentStartSearchCondition;
import com.cnuip.pmes2.controller.api.request.TimedTaskDetailCondition;
import com.cnuip.pmes2.controller.api.request.TimedTaskSearchCondition;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.*;
import com.cnuip.pmes2.service.PatentAssessmentService;
import com.cnuip.pmes2.service.PatentEvaluateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangzhibin on 2018/3/13.
 */
@RestController
@RequestMapping("/api/patent/assessment/task")

public class AssessmentTaskController extends BussinessLogicExceptionHandler {

    @Autowired
    private PatentAssessmentService patentAssessmentService;

    /**
     * 根据搜索条件搜索列表
     *
     * @return
     */
    @RequestMapping(value = "/search")
    public ApiResponse<PageInfo<TimedTask>> search(
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) String types,
            @RequestParam(required = false) Integer state,
            @RequestParam(required = false) Date fromCreateTime,
            @RequestParam(required = false) Date toCreateTime,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNum) {
        ApiResponse<PageInfo<TimedTask>> resp = new ApiResponse<>();
        TimedTaskSearchCondition condition = new TimedTaskSearchCondition();
        condition.setType(type);
        condition.setTypes(types);
        condition.setState(state);
        condition.setFromCreateTime(fromCreateTime);
        condition.setToCreateTime(toCreateTime);
        Page<TimedTask> page = (Page<TimedTask>) this.patentAssessmentService.searchTimedTasks(condition, pageNum, pageSize);
        resp.setResult(page.toPageInfo());
        return resp;
    }

    /**
     * 根据搜索条件搜索详情
     *
     * @return
     */
    @RequestMapping(value = "/detail/search")
    public ApiResponse<PageInfo<TimedTaskDetail>> searchDetail(
            @RequestParam Long id,
            @RequestParam(required = false) List<String> lastLegalStatus,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNum) {
        ApiResponse<PageInfo<TimedTaskDetail>> resp = new ApiResponse<>();
        TimedTaskDetailCondition condition = new TimedTaskDetailCondition(id,lastLegalStatus);
        resp.setResult(patentAssessmentService.searchTimedTaskDetail(condition, pageNum, pageSize));
        return resp;
    }
}
