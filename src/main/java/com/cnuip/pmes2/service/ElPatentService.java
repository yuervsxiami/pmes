package com.cnuip.pmes2.service;

import com.cnuip.pmes2.controller.api.request.PatentValueStatCondition;
import com.cnuip.pmes2.controller.api.response.PatentValueSpread;
import com.cnuip.pmes2.domain.core.ElPatent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * ElPatentService
 *
 * @author: xiongwei
 * Date: 2018/2/7 下午3:20
 */
public interface ElPatentService {

    /**
     * 查询最新更新的一条专利数据
     * @return
     */
    ElPatent findLastUpdate();

    Iterable<ElPatent> findAll(Sort sort);

    Iterable<ElPatent> findAll();

    Page<ElPatent> findAll(Pageable pageable);

    Iterable<ElPatent> save(Iterable<ElPatent> patents);

    void delete(ElPatent patent);

    void delete(Long id);

    void deleteAll();

    /**
     * 根据查询条件返回专利价值的分布统计信息
     * @param condition
     */
    PatentValueSpread statPatentValue(PatentValueStatCondition condition);

    /**
     * 根据条件查询特定一批专利的指定标签的覆盖率（标签有值的比例）
     * @param condition
     * @param labelKey
     * @return
     */
    double statPatentLabelCoverage(PatentValueStatCondition condition, String labelKey);

    /**
     * 批量自动索引
     */
    void autoIndex();

    /**
     * 按an尾号进行批量
     */
    void batchIndexForSuffix(String suffix);

    /**
     * 专利搜索
     * @param keywords
     * @param pageable
     * @return
     */
    Page<ElPatent> searchByKeywords(String keywords, Pageable pageable);

    /**
     * 按patentId尾号进行批量
     */
    void batchIndexForMode(int mode);

    /**
     * 批量更新专利的价值
     */
    void batchUpdatePatentValue();

    /**
     * 按patentId尾号进行批量:修正type=4的专利的保护年限字段
     */
    void batchFixIndexForMode(int mode);


}
