package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.domain.core.PatentIndex;
import org.springframework.stereotype.Repository;

/**
 * @auhor Crixalis
 * @date 2018/3/23 19:20
 */
@Repository
public interface PatentIndexMapper {

    void insert(String an);

    PatentIndex selectByAn(String an);

    void update(PatentIndex patentIndex);

}
