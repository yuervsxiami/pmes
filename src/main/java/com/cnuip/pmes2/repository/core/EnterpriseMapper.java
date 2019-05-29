package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.domain.core.Enterprise;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * EnterpriseMapper
 *
 * @author: xiongwei
 * Date: 2018/2/5 下午8:53
 */
@Repository
public interface EnterpriseMapper {

    int save(Enterprise enterprise);

    Enterprise findById(Long id);

    int update(Enterprise enterprise);

    int delete(Enterprise enterprise);

    List<Enterprise> find(Enterprise enterprise);
    
    List<Enterprise> search(@Param("enterprise")Enterprise enterprise, @Param("pageSize")int pageSize, @Param("pageNum")int pageNum);
    
    int changeState(@Param("id")Long id, @Param("state")Integer state);

    void changeIndexState(@Param("id")Long id, @Param("hasIndexing") Integer hasIndexing);
    
}
