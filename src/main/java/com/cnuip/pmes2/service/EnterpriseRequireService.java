package com.cnuip.pmes2.service;

import com.cnuip.pmes2.controller.api.request.EnterpriseRequireSearchCondition;
import com.cnuip.pmes2.domain.core.ElProfessorTemp;
import com.cnuip.pmes2.domain.core.EnterpriseRequire;
import com.cnuip.pmes2.domain.core.EnterpriseRequirement;
import com.cnuip.pmes2.exception.BussinessLogicException;

import java.util.List;
import java.util.Map;

/**
 * EnterpriseRequirementService
 *
 * @author: xiongwei
 * Date: 2018/2/6 下午3:12
 */
public interface EnterpriseRequireService{
    List<EnterpriseRequire> selectEnterpriseRequirement(EnterpriseRequireSearchCondition enterpriseRequireSearchCondition);

    EnterpriseRequire findById(Long erid);

    String findXmlDetail(String collegeName,String name);

    void saveKeyWords(ElProfessorTemp elProfessorTemp);

	List<Map> getDetailCollegePatentPin(String collegeName, String name);

}
