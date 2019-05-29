package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.domain.core.RequirementProfessor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RequirementProfessorMapper {
    void insert(@Param("professors") List<RequirementProfessor> professors);

    List<RequirementProfessor> findByRqId(@Param("rqId") long rqId, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
}
