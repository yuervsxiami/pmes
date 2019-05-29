package com.cnuip.pmes2.domain.core;

import lombok.Data;

import java.util.Map;

/**
 * HumanAssessmentPatent
 *
 * @author: xiongwei
 * Date: 2018/2/2 下午1:46
 */
@Data
public class HumanAssessmentPatent extends Patent {

    private ProcessOrder assessmentOrder;
    private Map<String, String> expPatentValues; // 价值解读

}
