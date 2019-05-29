package com.cnuip.pmes2.domain.core;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * ElPatent
 *
 * @author: xiongwei
 * Date: 2018/2/7 下午1:47
 */
@Data
public class ElPatent extends Patent {

    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String keyword; // 关键词
//    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
//    private String independentItem; // 主权项
    @Field(analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String description; // 说明书，内容来自xml文档
}
