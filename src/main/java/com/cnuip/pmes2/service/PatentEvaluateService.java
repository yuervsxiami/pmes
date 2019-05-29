package com.cnuip.pmes2.service;

import java.util.Map;

/**
 * PatentEvaluateService
 *
 * @author: xiongwei
 * Date: 2018/3/5 下午3:48
 */
public interface PatentEvaluateService {

    /**
     * 专利解读服务，根据价值计算结果生成解读文本
     * @param an
     * @return
     */
    Map<String, String> evaluatePatentValue(String an);

    /**
     * 价值解读
     * @param values
     * @return
     */
    Map<String, String> expPatentValues(Map<String, String> values);

}
