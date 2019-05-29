package com.cnuip.pmes2.repository.el;

import com.cnuip.pmes2.domain.core.ElObject;
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
public interface ElObjectRepository extends ElasticsearchRepository<ElObject, Long> {
}
