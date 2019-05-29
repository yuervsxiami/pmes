package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.constant.Workflows;
import com.cnuip.pmes2.controller.api.request.PatentStartSearchCondition;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.core.ProcessOrder;
import com.cnuip.pmes2.domain.core.User;
import com.cnuip.pmes2.exception.ProcessOrderException;
import com.cnuip.pmes2.service.PatentStartService;
import com.cnuip.pmes2.service.ProcessOrderService;
import com.cnuip.pmes2.service.ProcessService;
import com.cnuip.pmes2.util.UserUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by wangzhibin on 2018/1/27.
 */
@RestController
@RequestMapping("/api/patent/start")
public class PatentStartController extends BussinessLogicExceptionHandler {
    @Autowired
    private PatentStartService patentStartService;

    @Autowired
    private ProcessOrderService processOrderService;

    @Autowired
    private ProcessService processService;

    /**
     * 根据搜索条件搜索列表
     *
     * @return
     */
    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public ApiResponse<PageInfo<Patent>> search(
            @PathVariable Integer mode,
            @RequestParam(required = false) List<String> lastLegalStatus,
            @RequestParam(required = false) List<Long> types,
            @RequestParam(required = false) String ti,
            @RequestParam(required = false) String an,
            @RequestParam(required = false) List<String> ans,
            @RequestParam(required = false) String onm,
            @RequestParam(required = false) String pnm,
            @RequestParam(required = false) String pa,
            @RequestParam(required = false) String pin,
            @RequestParam(required = false) Date fromAd,
            @RequestParam(required = false) Date toAd,
            @RequestParam(required = false) Date fromOd,
            @RequestParam(required = false) Date toOd,
            @RequestParam(required = false) Date fromPd,
            @RequestParam(required = false) Date toPd,
            @RequestParam(required = false, defaultValue = "1") Integer hasBatchIndexed,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNum) {
        ApiResponse<PageInfo<Patent>> resp = new ApiResponse<>();
        if (mode!=2) {
            PatentStartSearchCondition condition = new PatentStartSearchCondition();
            condition.setLastLegalStatus(lastLegalStatus);
            condition.setTypes(types);
            condition.setTi(ti);
            condition.setAn(an);
            condition.setOnm(onm);
            condition.setPnm(pnm);
            condition.setPa(pa);
            condition.setPin(pin);
            condition.setFromAd(fromAd);
            condition.setToAd(toAd);
            condition.setFromOd(fromOd);
            condition.setToOd(toOd);
            condition.setFromPd(fromPd);
            condition.setToPd(toPd);
            condition.setHasBatchIndexed(hasBatchIndexed);
            resp.setResult(patentStartService.search(condition, pageNum, pageSize));
        }
        else {
            resp.setResult(patentStartService.searchWithAns(ans, pageNum, pageSize));
        }
        return resp;
    }

    /**
     *  启动流程
     */
    @RequestMapping(value = "/process/{processType}", method = RequestMethod.POST)
    public ApiResponse<String> process(@PathVariable Integer processType, @RequestBody List<Patent> patents) throws ProcessOrderException {
        ApiResponse<String> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        for (Patent p : patents) {
            ProcessOrder processOrder = new ProcessOrder();
            processOrder.setInstanceId(p.getId());
            processOrder.setInstanceType(Workflows.InstanceType.Patent.getValue());
            processOrder.setProcessType(processType);
            processOrder.setProcessCnfId(processService.getLastProcessCnfIdByType(processOrder.getProcessType()));
            processOrderService.startProcess(processOrder, user);
        }
        resp.setResult("流程启动成功！");
        return resp;
    }
}
