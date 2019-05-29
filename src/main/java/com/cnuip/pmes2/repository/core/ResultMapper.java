package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.controller.api.response.IpcVo;
import com.cnuip.pmes2.domain.core.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2019/5/21.
 * Time: 14:44
 */
@Repository
public interface ResultMapper {
    int save(Result result);
    int update(Result result);
    Ipc findIpcByCode(String code);
    Nic findNicByCode(String code);
    Ntcc findNtccByCode(String code);
    int saveIpc(ResultIpc resultIpc);
    int saveNic(ResultNic resultNic);
    int saveNtcc(ResultNtcc resultNtcc);
    void deleteIpc(Integer resultId);
    void deleteNic(Integer resultId);
    void deleteNtcc(Integer resultId);
    List<Ipc> findIpcAll();
    List<Nic> findNicAll();
    List<Ntcc> findNtccAll();
    int saveResultExt(ResultExt resultExt);
    int saveResultKeyword(ResultKeyWord resultKeyWord);
    int updateResultExt(ResultExt resultExt);
    int updateResultKeyWord(ResultKeyWord resultKeyWord);

    List<Ipc> getIpcFirst();

    List<Ipc> childIpcByCode(String code);

    List<Nic> getNicFirst();

    List<Nic> childNicByCode(String code);

    List<Ntcc> getNtccFirst();

    List<Ntcc> childNtccByCode(String code);
}
