package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.request.PatentStartSearchCondition;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.InstanceLabel;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.core.TaskOrderLabel;
import com.cnuip.pmes2.service.PatentStartService;
import com.cnuip.pmes2.service.TaskOrderService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * PatentSearchController
 * 综合查询：专利查询
 *
 * @author: xiongwei
 * Date: 2018/2/4 下午2:08
 */
@RestController
@RequestMapping("/api/search/patent")
public class PatentSearchController extends BussinessLogicExceptionHandler {

    @Autowired
    private PatentStartService patentStartService;
    @Autowired
    private TaskOrderService taskOrderService;

    /**
     * 根据搜索条件搜索列表
     *
     * @return
     */
    @RequestMapping(value = "/")
    public ApiResponse<PageInfo<Patent>> search(
            @RequestParam(required = false) List<String> lastLegalStatus,
            @RequestParam(required = false) List<Long> types,
            @RequestParam(required = false) String ti,
            @RequestParam(required = false) String an,
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
        PatentStartSearchCondition condition = new PatentStartSearchCondition();
        condition.setIndexState(1); // 查询流程都处理完的专利
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
        resp.setResult(this.patentStartService.search(condition, pageNum, pageSize));
        return resp;
    }

    /**
     * 根据专利id查询专利详情，不关联定单
     * @param patentId
     * @return
     */
    @RequestMapping("/{patentId}")
    public ApiResponse<Patent> findPatentDetail(@PathVariable("patentId") Long patentId) {
        ApiResponse<Patent> resp = new ApiResponse<>();
        resp.setResult(this.patentStartService.selectSimpleByPrimaryKey(patentId));
        return resp;
    }

    /**
     * 根据专利id和流程类型查询最新的已完成的定单的标签
     * @param patentId
     * @param processType
     * @return
     */
    @RequestMapping("/{patentId}/{processType}/labels/latest")
    public ApiResponse<List<TaskOrderLabel>> findProcessLabels(@PathVariable("patentId") Long patentId, @PathVariable("processType") Integer processType) {
        ApiResponse<List<TaskOrderLabel>> resp = new ApiResponse<>();
        resp.setResult(this.taskOrderService.findAllLabels(patentId, processType));
        return resp;
    }

    /**
     * 根据专利id和流程类型查询最新的已完成的定单的标签
     * @param patentId
     * @param processType
     * @return
     */
    @RequestMapping("/{patentId}/{processType}/instancelabels/latest")
    public ApiResponse<List<InstanceLabel>> findPatentProcessInstanceLabel(@PathVariable("patentId") Long patentId, @PathVariable("processType") Integer processType) {
        ApiResponse<List<InstanceLabel>> resp = new ApiResponse<>();
        resp.setResult(this.taskOrderService.findAllInstanceLabels(patentId, processType));
        return resp;
    }


}
