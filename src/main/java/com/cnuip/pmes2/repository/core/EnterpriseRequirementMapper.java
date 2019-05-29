package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.domain.core.EnterpriseRequirement;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * EnterpriseRequirementMapper
 *
 * @author: xiongwei
 * Date: 2018/2/5 下午8:56
 */
@Repository
public interface EnterpriseRequirementMapper {

    int save(EnterpriseRequirement requirement);

    EnterpriseRequirement findById(Long id);

    int update(EnterpriseRequirement requirement);

    int delete(EnterpriseRequirement requirement);

    List<EnterpriseRequirement> find(EnterpriseRequirement requirement);
    
    int changeState(@Param("id")Long id, @Param("state")Integer state);

    void changeIndexState(@Param("id")Long id, @Param("hasIndexing")Integer hasIndexing, @Param("hasPatentMatching")Integer hasPatentMatching, @Param("hasProfessorMatching")Integer hasProfessorMatching);

}
