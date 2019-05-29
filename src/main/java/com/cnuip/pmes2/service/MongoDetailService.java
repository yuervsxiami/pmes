package com.cnuip.pmes2.service;

import com.cnuip.pmes2.domain.inventory.PatentInfoAn;

import java.util.List;

/**
 * MongoService
 *
 * @author: zzh
 * Date: 2018/6/16 上午10:31
 */
public interface MongoDetailService {

    List<PatentInfoAn> findPatentInfoAn(PatentInfoAn patentInfoAn);
    List<PatentInfoAn> findPatentInfoAnList(List ans);
}
