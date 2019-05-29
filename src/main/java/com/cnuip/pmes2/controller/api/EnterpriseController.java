package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.constant.Workflows;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.Enterprise;
import com.cnuip.pmes2.domain.core.ProcessOrder;
import com.cnuip.pmes2.domain.core.User;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.exception.ProcessOrderException;
import com.cnuip.pmes2.service.EnterpriseService;
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
 * EnterpriseController
 *
 * @author: xiongwei
 * Date: 2018/2/6 上午10:11
 */
@RestController
@RequestMapping("/api/enterprise")
public class EnterpriseController extends BussinessLogicExceptionHandler {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private ProcessOrderService processOrderService;

    @Autowired
    private ProcessService processService;

    /**
     * 搜索企业
     * @param enterprise
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ApiResponse<PageInfo<Enterprise>> search(Enterprise enterprise) {
        ApiResponse<PageInfo<Enterprise>> resp = new ApiResponse<>();
        // 默认的分页参数
        if (enterprise.getPageNum() == null) {
            enterprise.setPageNum(1);
        }
        if (enterprise.getPageSize() == null) {
            enterprise.setPageSize(10);
        }
        Page<Enterprise> page = (Page<Enterprise>) this.enterpriseService.find(enterprise);
        resp.setResult(page.toPageInfo());
        return resp;
    }

    /**
     * 企业信息自动完成
     * @param enterprise
     * @return
     */
    @RequestMapping(value = "/auto/complete", method = RequestMethod.GET)
    public ApiResponse<List<Enterprise>> autoComplete(Enterprise enterprise) {
        ApiResponse<List<Enterprise>> resp = new ApiResponse<>();
        // 不分页
        enterprise.setPageNum(null);
        enterprise.setPageSize(null);
        resp.setResult(this.enterpriseService.find(enterprise));
        return resp;
    }

    /**
     * 新增企业
     * @param enterprise
     * @return
     * @throws BussinessLogicException
     */
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ApiResponse<Enterprise> save(@RequestBody Enterprise enterprise) throws BussinessLogicException {
        ApiResponse<Enterprise> resp = new ApiResponse<>();
        resp.setResult(this.enterpriseService.save(enterprise));
        return resp;
    }

    /**
     * 更新企业信息
     * @param enterprise
     * @return
     * @throws BussinessLogicException
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiResponse<Enterprise> update(@RequestBody Enterprise enterprise) throws BussinessLogicException {
        ApiResponse<Enterprise> resp = new ApiResponse<>();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null) {
            enterprise.setUserId(user.getId());
        }
        resp.setResult(this.enterpriseService.update(enterprise));
        return resp;
    }

    /**
     * 删除企业信息
     * @param enterprise
     * @return
     * @throws BussinessLogicException
     */
    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public ApiResponse<Integer> delete(Enterprise enterprise) throws BussinessLogicException {
        ApiResponse<Integer> resp = new ApiResponse<>();
        resp.setResult(this.enterpriseService.delete(enterprise));
        return resp;
    }

    /**
     * 企业详情
     * @param eid
     * @return
     */
    @RequestMapping("/{eid}")
    public ApiResponse<Enterprise> detail(@PathVariable("eid") Long eid) {
        ApiResponse<Enterprise> resp = new ApiResponse<>();
        resp.setResult(this.enterpriseService.findById(eid));
        return resp;
    }

    /**
     *  启动流程
     */
    @RequestMapping(value = "/start/process/{processType}", method = RequestMethod.POST)
    public ApiResponse<String> process(@PathVariable Integer processType, @RequestBody List<Enterprise> enterprises) throws ProcessOrderException {
        ApiResponse<String> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        for (Enterprise e : enterprises) {
            ProcessOrder processOrder = new ProcessOrder();
            processOrder.setInstanceId(e.getId());
            processOrder.setInstanceType(Workflows.InstanceType.EnterpriseInfo.getValue());
            processOrder.setProcessType(processType);
            processOrder.setProcessCnfId(processService.getLastProcessCnfIdByType(processOrder.getProcessType()));
            processOrderService.startProcess(processOrder, user);
        }
        resp.setResult("流程启动成功！");
        return resp;
    }

}
