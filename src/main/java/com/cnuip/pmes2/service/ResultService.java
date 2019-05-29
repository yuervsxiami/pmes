package com.cnuip.pmes2.service;

import com.cnuip.pmes2.controller.api.request.ResultSearchCondition;
import com.cnuip.pmes2.controller.api.response.IpcVo;
import com.cnuip.pmes2.controller.api.response.NicVo;
import com.cnuip.pmes2.controller.api.response.NtccVo;
import com.cnuip.pmes2.domain.core.Ipc;
import com.cnuip.pmes2.domain.core.Nic;
import com.cnuip.pmes2.domain.core.Ntcc;
import com.cnuip.pmes2.domain.core.Result;
import com.cnuip.pmes2.domain.el.ElResult;
import com.cnuip.pmes2.exception.BussinessLogicException;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2019/5/21.
 * Time: 14:34
 */
public interface ResultService {

    void add(Long userId,Result result) throws BussinessLogicException;

    void update(Long userId, Result result) throws BussinessLogicException;

    List<IpcVo> getIpc();

    List<NicVo> getNic();

    List<NtccVo> getNtcc();

    List<Ipc> getIpcFirst();

    List<Ipc> childIpcByCode(String code);

    List<Nic> getNicFirst();

    List<Nic> childNicByCode(String code);

    List<Ntcc> getNtccFirst();

    List<Ntcc> childNtccByCode(String code);

    List<ElResult> getResultList(ResultSearchCondition resultSearchCondition);

    ElResult getResultDetail(Long id);

    List<ElResult> export();
}
