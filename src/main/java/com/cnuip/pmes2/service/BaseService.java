package com.cnuip.pmes2.service;

import com.cnuip.pmes2.domain.core.Enterprise;

/**
 * Created by wangzhibin on 2018/3/9.
 */
public interface BaseService {
    Enterprise fillEnterprise(Enterprise ep, String[] properties);
}
