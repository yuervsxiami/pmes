package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.domain.core.Enterprise;
import com.cnuip.pmes2.domain.core.EnterpriseRequirement;
import com.cnuip.pmes2.domain.core.NationalEconomy;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.repository.core.EnterpriseRequirementMapper;
import com.cnuip.pmes2.repository.core.NationalEconomyMapper;
import com.cnuip.pmes2.repository.core.RegionMapper;
import com.cnuip.pmes2.service.EnterpriseRequirementService;
import com.cnuip.pmes2.util.ResponseEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * EnterpriseRequirementServiceImpl
 *
 * @author: xiongwei
 * Date: 2018/2/6 下午3:13
 */
@Service
@Transactional(readOnly = true)
public class EnterpriseRequirementServiceImpl extends BaseServiceImpl implements EnterpriseRequirementService {

    @Autowired
    private EnterpriseRequirementMapper enterpriseRequirementMapper;
    @Autowired
    private NationalEconomyMapper nationalEconomyMapper;
    @Autowired
    private RegionMapper regionMapper;

    @Transactional(rollbackFor = BussinessLogicException.class)
    @Override
    public EnterpriseRequirement save(EnterpriseRequirement requirement) throws BussinessLogicException {
        try {
            this.enterpriseRequirementMapper.save(requirement);
        } catch (Exception e) {
            throw new BussinessLogicException(e, ResponseEnum.ENTERPRISE_REQUIREMENT_SAVE_ERROR);
        }
        return this.enterpriseRequirementMapper.findById(requirement.getId());
    }

    @Override
    public EnterpriseRequirement findById(Long id) {
        EnterpriseRequirement epr = this.enterpriseRequirementMapper.findById(id);
        if (epr != null) {
            Enterprise enterprise = epr.getEnterprise();
            enterprise = this.fillEnterprise(enterprise, new String[]{"nationalEconomy", "province", "city", "district"});
        }
        return epr;
    }

    @Transactional(rollbackFor = BussinessLogicException.class)
    @Override
    public EnterpriseRequirement update(EnterpriseRequirement requirement) throws BussinessLogicException {
        try {
            this.enterpriseRequirementMapper.update(requirement);
        } catch (Exception e) {
            throw new BussinessLogicException(e, ResponseEnum.ENTERPRISE_REQUIREMENT_UPDATE_ERROR);
        }
        return this.enterpriseRequirementMapper.findById(requirement.getId());
    }

    @Transactional(rollbackFor = BussinessLogicException.class)
    @Override
    public int delete(EnterpriseRequirement requirement) throws BussinessLogicException {
        int result;
        try {
            result = this.enterpriseRequirementMapper.delete(requirement);
        } catch (Exception e) {
            throw new BussinessLogicException(e, ResponseEnum.ENTERPRISE_REQUIREMENT_DELETE_ERROR);
        }
        return result;
    }

    @Override
    public List<EnterpriseRequirement> find(EnterpriseRequirement requirement) {
        List<EnterpriseRequirement> items = this.enterpriseRequirementMapper.find(requirement);
        if (items != null) {
            for (EnterpriseRequirement epr : items) {
                Enterprise ep = epr.getEnterprise();
                ep = this.fillEnterprise(ep, new String[]{"nationalEconomy", "province", "city", "district"});
            }
        }
        return items;
    }
}
