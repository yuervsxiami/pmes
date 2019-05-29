package com.cnuip.pmes2.repository.el;


import com.cnuip.pmes2.domain.el.ElResult;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2019/5/24.
 * Time: 15:59
 */
@Repository
public interface ElResultRepository extends ElasticsearchRepository<ElResult, Long> {
}
