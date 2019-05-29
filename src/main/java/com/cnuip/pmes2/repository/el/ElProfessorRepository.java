package com.cnuip.pmes2.repository.el;

import com.cnuip.pmes2.domain.core.ElObject;
import com.cnuip.pmes2.domain.el.ElProfessor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * ElPatentRepository
 *
 * @author: xiongwei
 * Date: 2018/2/7 下午3:17
 */
@Repository
public interface ElProfessorRepository extends ElasticsearchRepository<ElProfessor, Long> {
    ElProfessor findByNameAndCollegeName(String Name,String collegeName);
}
