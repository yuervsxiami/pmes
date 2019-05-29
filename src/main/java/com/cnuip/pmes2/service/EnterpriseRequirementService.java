package com.cnuip.pmes2.service;

import com.cnuip.pmes2.domain.core.EnterpriseRequirement;
import com.cnuip.pmes2.exception.BussinessLogicException;

import java.util.List;

/**
 * EnterpriseRequirementService
 *
 * @author: xiongwei
 * Date: 2018/2/6 下午3:12
 */
public interface EnterpriseRequirementService extends BaseService {

    EnterpriseRequirement save(EnterpriseRequirement requirement) throws BussinessLogicException;

    EnterpriseRequirement findById(Long id);

    EnterpriseRequirement update(EnterpriseRequirement requirement) throws BussinessLogicException;

    int delete(EnterpriseRequirement requirement) throws BussinessLogicException;

    List<EnterpriseRequirement> find(EnterpriseRequirement requirement);

}
