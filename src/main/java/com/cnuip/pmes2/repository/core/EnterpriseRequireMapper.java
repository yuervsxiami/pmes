package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.controller.api.request.EnterpriseRequireSearchCondition;
import com.cnuip.pmes2.domain.core.EnterpriseRequire;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/10/12.
 * Time: 14:53
 */
@Repository
public interface EnterpriseRequireMapper {
    List<EnterpriseRequire> selectEnterpriseRequireList(@Param("condition")EnterpriseRequireSearchCondition enterpriseRequireSearchCondition,@Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

    EnterpriseRequire findEnterpriseRequireById(Long erid);

    void updatePushState(@Param("rqId")long rqId);

    void save(EnterpriseRequire enterpriseRequire);
}
