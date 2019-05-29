package com.cnuip.pmes2.service;

import com.cnuip.pmes2.controller.api.request.PatentStartSearchCondition;
import com.cnuip.pmes2.controller.api.request.TimedTaskDetailCondition;
import com.cnuip.pmes2.controller.api.request.TimedTaskSearchCondition;
import com.cnuip.pmes2.domain.core.HumanAssessmentPatent;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.core.TimedTask;
import com.cnuip.pmes2.domain.core.TimedTaskDetail;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PatentAssessmentServiceImpl
 * 查询专利快速评估相关业务逻辑
 *
 * @author: xiongwei
 * Date: 2018/2/2 下午2:12
 */
public interface PatentAssessmentService {

    /**
     * 专利价值快速评估结果查询
     * @param condition
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<Patent> searchHumanAssessment(@Param("condition")PatentStartSearchCondition condition,
                                       @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

    List<Patent> searchHumanAssessmentWithAns(@Param("ans")List<String> ans,
                                       @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
    /**
     * 查询专利快速评估详情
     * @param id
     * @return
     */
    HumanAssessmentPatent findAssessmentPatentById(Long id);

    List<TimedTask> searchTimedTasks(@Param("condition")TimedTaskSearchCondition condition,
                                          @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

    PageInfo<TimedTaskDetail> searchTimedTaskDetail(@Param("condition")TimedTaskDetailCondition condition,
                                                    @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

}
