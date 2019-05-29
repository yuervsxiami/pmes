package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.domain.TK;
import com.cnuip.pmes2.domain.core.EnterpriseRequirement;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TKMapper {

    @Transactional
    int updateTk(TK tk);

    List<TK> findAll();

}
