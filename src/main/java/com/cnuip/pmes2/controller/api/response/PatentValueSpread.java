package com.cnuip.pmes2.controller.api.response;

import lombok.Data;

import java.util.List;

/**
 * PatentValueSpread
 *
 * @author: xiongwei
 * Date: 2018/3/29 下午2:57
 */
@Data
public class PatentValueSpread {

    private Long totalHits; // 查询总记录
    private Double avg; // 平均值
    private List<StatItem<Long>> ranges; // 范围分布

}
