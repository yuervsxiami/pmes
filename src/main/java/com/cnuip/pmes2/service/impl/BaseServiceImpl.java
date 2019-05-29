package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.domain.core.Enterprise;
import com.cnuip.pmes2.domain.core.NationalEconomy;
import com.cnuip.pmes2.repository.core.NationalEconomyMapper;
import com.cnuip.pmes2.repository.core.RegionMapper;
import com.cnuip.pmes2.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by wangzhibin on 2018/3/9.
 */
public class BaseServiceImpl implements BaseService {
    @Autowired
    private NationalEconomyMapper nationalEconomyMapper;

    @Autowired
    private RegionMapper regionMapper;

    public Enterprise fillEnterprise(Enterprise ep, String[] properties) {
        if (ep == null) {
            return null;
        }
        for (String property : properties) {
            if ("nationalEconomy".equals(property)) {
                if (ep.getNationalEconomyField() != null) {
                    NationalEconomy nationalEconomy = this.nationalEconomyMapper.selectOneByField("code", ep.getNationalEconomyField());
                    ep.setNationalEconomy(nationalEconomy);
                }
            }
            if ("province".equals(property)) {
                if (ep.getProvinceId() != null) {
                    ep.setProvince(this.regionMapper.selectOneByField("id", ep.getProvinceId()));
                }
            }
            if ("city".equals(property)) {
                if (ep.getCityId() != null) {
                    ep.setCity(this.regionMapper.selectOneByField("id", ep.getCityId()));
                }
            }
            if ("district".equals(property)) {
                if (ep.getDistrictId() != null) {
                    ep.setDistrict(this.regionMapper.selectOneByField("id", ep.getDistrictId()));
                }
            }
        }
        return ep;
    }
}
