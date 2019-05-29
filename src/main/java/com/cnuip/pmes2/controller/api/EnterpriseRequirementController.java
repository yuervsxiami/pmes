package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.constant.Workflows;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.Enterprise;
import com.cnuip.pmes2.domain.core.EnterpriseRequirement;
import com.cnuip.pmes2.domain.core.ProcessOrder;
import com.cnuip.pmes2.domain.core.User;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.exception.ProcessOrderException;
import com.cnuip.pmes2.service.EnterpriseRequirementService;
import com.cnuip.pmes2.service.ProcessOrderService;
import com.cnuip.pmes2.service.ProcessService;
import com.cnuip.pmes2.util.UserUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * EnterpriseRequirementController
 *
 * @author: xiongwei
 * Date: 2018/2/6 上午10:11
 */
@RestController
@RequestMapping("/api/requirement")
public class EnterpriseRequirementController extends BussinessLogicExceptionHandler {

    @Autowired
    private EnterpriseRequirementService enterpriseRequirementService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private ProcessOrderService processOrderService;

    /**
     * 搜索企业需求信息
     * @param requirement
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ApiResponse<PageInfo<EnterpriseRequirement>> search(EnterpriseRequirement requirement) {
        ApiResponse<PageInfo<EnterpriseRequirement>> resp = new ApiResponse<>();
        // 默认的分页参数
        if (requirement.getPageNum() == null) {
            requirement.setPageNum(1);
        }
        if (requirement.getPageSize() == null) {
            requirement.setPageSize(10);
        }
        Page<EnterpriseRequirement> page = (Page<EnterpriseRequirement>) this.enterpriseRequirementService.find(requirement);
        resp.setResult(page.toPageInfo());
        return resp;
    }

    /**
     * 新增企业需求
     * @param requirement
     * @return
     * @throws BussinessLogicException
     */
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ApiResponse<EnterpriseRequirement> save(@RequestBody EnterpriseRequirement requirement) throws BussinessLogicException {
        ApiResponse<EnterpriseRequirement> resp = new ApiResponse<>();
        resp.setResult(this.enterpriseRequirementService.save(requirement));
        return resp;
    }

    /**
     * 更新企业需求
     * @param requirement
     * @return
     * @throws BussinessLogicException
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiResponse<EnterpriseRequirement> update(@RequestBody EnterpriseRequirement requirement) throws BussinessLogicException {
        ApiResponse<EnterpriseRequirement> resp = new ApiResponse<>();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null) {
            requirement.setUserId(user.getId());
        }
        resp.setResult(this.enterpriseRequirementService.update(requirement));
        return resp;
    }

    /**
     * 删除企业需求
     * @param requirement
     * @return
     * @throws BussinessLogicException
     */
    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public ApiResponse<Integer> delete(EnterpriseRequirement requirement) throws BussinessLogicException {
        ApiResponse<Integer> resp = new ApiResponse<>();
        resp.setResult(this.enterpriseRequirementService.delete(requirement));
        return resp;
    }

    /**
     * 企业需求详情
     * @param erid
     * @return
     */
    @RequestMapping("/{erid}")
    public ApiResponse<EnterpriseRequirement> detail(@PathVariable("erid") Long erid) {
        ApiResponse<EnterpriseRequirement> resp = new ApiResponse<>();
        resp.setResult(this.enterpriseRequirementService.findById(erid));
        return resp;
    }

    /**
     *  启动流程
     */
    @RequestMapping(value = "/start/process/{instanceType}/{processType}", method = RequestMethod.POST)
    public ApiResponse<String> process(@PathVariable Integer instanceType, @PathVariable Integer processType, @RequestBody List<EnterpriseRequirement> enterpriseRequirements) throws ProcessOrderException {
        ApiResponse<String> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        for (EnterpriseRequirement er : enterpriseRequirements) {
            ProcessOrder processOrder = new ProcessOrder();
            processOrder.setInstanceId(er.getId());
            processOrder.setInstanceType(instanceType);
            processOrder.setProcessType(processType);
            processOrder.setProcessCnfId(processService.getLastProcessCnfIdByType(processOrder.getProcessType()));
            processOrderService.startProcess(processOrder, user);
        }
        resp.setResult("流程启动成功！");
        return resp;
    }
}
