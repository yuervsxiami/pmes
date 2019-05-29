package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.domain.core.NationalEconomy;
import com.cnuip.pmes2.repository.core.NationalEconomyMapper;
import com.cnuip.pmes2.service.NationalEconomyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangzhibin on 2018/3/6.
 */
@Service
public class NationalEconomyServiceImpl extends AbstractServiceImpl<NationalEconomy> implements NationalEconomyService {

    @Autowired
    private NationalEconomyMapper nationalEconomyMapper;

    @Override
    public List<NationalEconomy> findBottomFields(String keyword) {
        return this.nationalEconomyMapper.findBottomFields(keyword);
    }
}
