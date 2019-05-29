package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.domain.core.Enterprise;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.repository.core.EnterpriseMapper;
import com.cnuip.pmes2.service.EnterpriseService;
import com.cnuip.pmes2.util.ResponseEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * EnterpriseServiceImpl
 *
 * @author: xiongwei
 * Date: 2018/2/6 上午10:04
 */
@Service
@Transactional(readOnly = true)
public class EnterpriseServiceImpl extends BaseServiceImpl implements EnterpriseService {

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    @Transactional(rollbackFor = BussinessLogicException.class)
    @Override
    public Enterprise save(Enterprise enterprise) throws BussinessLogicException {
        try {
            this.enterpriseMapper.save(enterprise);
        } catch (Exception e) {
            throw new BussinessLogicException(e, ResponseEnum.ENTERPRISE_SAVE_ERROR);
        }
        return this.enterpriseMapper.findById(enterprise.getId());
    }

    @Override
    public Enterprise findById(Long id) {
        return this.fillEnterprise(this.enterpriseMapper.findById(id), new String[]{"nationalEconomy", "province", "city", "district"});
    }

    @Transactional(rollbackFor = BussinessLogicException.class)
    @Override
    public Enterprise update(Enterprise enterprise) throws BussinessLogicException {
        try {
            this.enterpriseMapper.update(enterprise);
        } catch (Exception e) {
            throw new BussinessLogicException(e, ResponseEnum.ENTERPRISE_UPDATE_ERROR);
        }
        return this.enterpriseMapper.findById(enterprise.getId());
    }

    @Transactional(rollbackFor = BussinessLogicException.class)
    @Override
    public int delete(Enterprise enterprise) throws BussinessLogicException {
        int result;
        try {
            result = this.enterpriseMapper.delete(enterprise);
        } catch (Exception e) {
            throw new BussinessLogicException(e, ResponseEnum.ENTERPRISE_DELETE_ERROR);
        }
        return result;
    }

    @Override
    public List<Enterprise> find(Enterprise enterprise) {
        List<Enterprise> items = this.enterpriseMapper.find(enterprise);
        if (items != null) {
            for (Enterprise ep : items) {
                this.fillEnterprise(ep, new String[]{"nationalEconomy", "province", "city", "district"});
            }
        }
        return items;
    }

}
