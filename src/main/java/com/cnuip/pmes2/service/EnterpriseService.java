package com.cnuip.pmes2.service;

import com.cnuip.pmes2.domain.core.Enterprise;
import com.cnuip.pmes2.exception.BussinessLogicException;

import java.util.List;

/**
 * EnterpriseService
 *
 * @author: xiongwei
 * Date: 2018/2/6 上午10:04
 */
public interface EnterpriseService extends BaseService {

    Enterprise save(Enterprise enterprise) throws BussinessLogicException;

    Enterprise findById(Long id);

    Enterprise update(Enterprise enterprise) throws BussinessLogicException;

    int delete(Enterprise enterprise) throws BussinessLogicException;

    List<Enterprise> find(Enterprise enterprise);

}
