package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.request.ProcessOrderSearchCondition;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.ProcessOrder;
import com.cnuip.pmes2.domain.core.TaskOrderLabel;
import com.cnuip.pmes2.service.ProcessOrderService;
import com.cnuip.pmes2.service.TaskOrderLabelService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProcessOrderController
 *
 * @author: xiongwei
 * Date: 2018/2/4 上午10:37
 */
@RestController
@RequestMapping("/api/process/order")
public class ProcessOrderController extends BussinessLogicExceptionHandler {

    @Autowired
    private ProcessOrderService processOrderService;
    @Autowired
    private TaskOrderLabelService taskOrderLabelService;

    /**
     * 查询定单详情
     *
     * @param processOrderId
     * @return
     */
    @RequestMapping("/{processOrderId}")
    public ApiResponse<ProcessOrder> processOrderDetail(@PathVariable("processOrderId") Long processOrderId) {
        ApiResponse<ProcessOrder> resp = new ApiResponse<>();
        resp.setResult(this.processOrderService.findByOrderId(processOrderId));
        return resp;
    }

    /**
     * 根据工单id查询工单的标签
     *
     * @param id
     * @return
     */
    @RequestMapping("/task/{id}/labels")
    public ApiResponse<List<TaskOrderLabel>> taskOrderLabels(@PathVariable("id") Long id) {
        List<TaskOrderLabel> labels = this.taskOrderLabelService.findByTaskOrderId(id);
        ApiResponse<List<TaskOrderLabel>> resp = new ApiResponse<>();
        resp.setResult(labels);
        return resp;
    }

    /**
     * 查询最新定单
     *
     * @return
     */
    @RequestMapping(value = "/searchPatent", method = RequestMethod.GET)
    public ApiResponse<PageInfo<ProcessOrder>> searchPatent(ProcessOrderSearchCondition condition) {
        ApiResponse<PageInfo<ProcessOrder>> resp = new ApiResponse<>();
        // 默认的分页参数
        if (condition.getPageNum() == null) {
            condition.setPageNum(1);
        }
        if (condition.getPageSize() == null) {
            condition.setPageSize(10);
        }
        resp.setResult(processOrderService.searchPatent(condition));
        return resp;
    }

    @RequestMapping(value = "/searchRequirement", method = RequestMethod.GET)
    public ApiResponse<PageInfo<ProcessOrder>> searchRequirement(ProcessOrderSearchCondition condition) {
        ApiResponse<PageInfo<ProcessOrder>> resp = new ApiResponse<>();
        // 默认的分页参数
        if (condition.getPageNum() == null) {
            condition.setPageNum(1);
        }
        if (condition.getPageSize() == null) {
            condition.setPageSize(10);
        }
        resp.setResult(processOrderService.searchRequirement(condition));
        return resp;
    }


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ApiResponse<List<ProcessOrder>> all(
            @RequestParam(required = false) Integer instanceType,
            @RequestParam(required = false) Long instanceId,
            @RequestParam(required = false) Long processId) {
        ApiResponse<List<ProcessOrder>> resp = new ApiResponse<>();
        resp.setResult(processOrderService.getAllProcessOrders(instanceType, instanceId, processId));
        return resp;
    }
}
