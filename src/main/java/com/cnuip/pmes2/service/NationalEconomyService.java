package com.cnuip.pmes2.service;

import com.cnuip.pmes2.domain.core.NationalEconomy;

import java.util.List;

/**
 * Created by wangzhibin on 2018/3/6.
 */
public interface NationalEconomyService extends AbstractService<NationalEconomy> {

    List<NationalEconomy> findBottomFields(String keyword);

}
