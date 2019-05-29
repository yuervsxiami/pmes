package com.cnuip.pmes2.controller;

import com.cnuip.pmes2.controller.api.BussinessLogicExceptionHandler;
import com.cnuip.pmes2.controller.api.request.EnterpriseRequireSearchCondition;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.*;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.exception.ProcessOrderException;
import com.cnuip.pmes2.service.EnterpriseRequireService;
import com.cnuip.pmes2.service.EnterpriseRequirementService;
import com.cnuip.pmes2.service.ProcessOrderService;
import com.cnuip.pmes2.service.ProcessService;
import com.cnuip.pmes2.util.UserUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * EnterpriseRequireController
 *
 * @author: zzh
 * Date: 2018/2/6 上午10:11
 */
@RestController
@RequestMapping("/api/enterpriseRequire")
public class EnterpriseRequireController extends BussinessLogicExceptionHandler {

    @Autowired
    private EnterpriseRequireService enterpriseRequireService;


    /**
     * 搜索企业需求信息
     * @param
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ApiResponse<PageInfo<EnterpriseRequire>> search(EnterpriseRequireSearchCondition enterpriseRequireSearchCondition
                                                           ) {
        ApiResponse<PageInfo<EnterpriseRequire>> resp = new ApiResponse<>();
        // 默认的分页参数
        if (enterpriseRequireSearchCondition.getPageNum() == null) {
            enterpriseRequireSearchCondition.setPageNum(1);
        }
        if (enterpriseRequireSearchCondition.getPageSize() == null) {
            enterpriseRequireSearchCondition.setPageSize(10);
        }
        Page<EnterpriseRequire> page = (Page<EnterpriseRequire>) this.enterpriseRequireService.selectEnterpriseRequirement(enterpriseRequireSearchCondition);
        resp.setResult(page.toPageInfo());
        return resp;
    }


    /**
     * 企业需求详情
     * @param erid
     * @return
     */
    @RequestMapping("/{erid}")
    public ApiResponse<EnterpriseRequire> detail(@PathVariable("erid") Long erid) {
        ApiResponse<EnterpriseRequire> resp = new ApiResponse<>();
        resp.setResult(this.enterpriseRequireService.findById(erid));
        return resp;
    }


    @PostMapping("/saveProfessorKeyWords")
    public ApiResponse<String> saveProfessorKeyWords(@RequestBody ElProfessorTemp elProfessorTemp) {
        ApiResponse<String> resp = new ApiResponse<>();
        enterpriseRequireService.saveKeyWords(elProfessorTemp);
        resp.setResult("ok");
        return resp;
    }
}
