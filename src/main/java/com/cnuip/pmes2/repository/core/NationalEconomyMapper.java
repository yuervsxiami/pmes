package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.domain.core.NationalEconomy;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wangzhibin on 2018/3/6.
 */
@Repository
public interface NationalEconomyMapper extends AbstractMapper<NationalEconomy> {

    List<NationalEconomy> findBottomFields(@Param("keyword") String keyword);

    List<NationalEconomy> findBySpecialties(@Param("specialties") String specialties);
}