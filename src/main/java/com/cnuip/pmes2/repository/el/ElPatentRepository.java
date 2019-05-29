package com.cnuip.pmes2.repository.el;

import com.cnuip.pmes2.domain.core.ElPatent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ElPatentRepository
 *
 * @author: xiongwei
 * Date: 2018/2/7 下午3:17
 */
@Repository
public interface ElPatentRepository extends ElasticsearchRepository<ElPatent, Long> {

    /**
     * 查询所有数据，按更新时间降序排列
     * @param instanceType
     * @return
     */
    List<ElPatent> findByInstanceTypeOrderByUpdateTimeDesc(Integer instanceType, Pageable pageable);

    /**
     * 根据an查询专利
     * @param an
     * @return
     */
    List<ElPatent> findByAn(String an);

    /**
     * 根据id查询专利
     * @param id
     * @return
     */
    ElPatent findById(Long id);

    @Query("{\"bool\" : {\"must\" : {\"exists\" : {\"field\" : \"latestLabels\"}}}}")
    Page<ElPatent> findBatchedPatents(Pageable pageable);

}
