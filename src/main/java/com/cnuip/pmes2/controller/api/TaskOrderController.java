package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.request.PatentTaskOrderSearchCondition;
import com.cnuip.pmes2.controller.api.request.TaskOrderDealParam;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.controller.api.response.HumanIndexAuditResponse;
import com.cnuip.pmes2.controller.api.response.HumanIndexResponse;
import com.cnuip.pmes2.domain.core.*;
import com.cnuip.pmes2.exception.TaskOrderException;
import com.cnuip.pmes2.service.TaskOrderService;
import com.cnuip.pmes2.util.UserUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by wangzhibin on 2018/2/1.
 */
@RestController
@RequestMapping("/api/instance/process/order")
public class TaskOrderController extends BussinessLogicExceptionHandler {
    @Autowired
    private TaskOrderService taskOrderService;

    /**
     * 根据搜索条件搜索专利列表
     *
     * @return
     */
    @RequestMapping(value = "/searchPatentTaskOrder", method = RequestMethod.GET)
    public ApiResponse<PageInfo<TaskOrder>> searchPatentTaskOrder(PatentTaskOrderSearchCondition condition)
            throws TaskOrderException {
        ApiResponse<PageInfo<TaskOrder>> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        // 默认的分页参数
        if (condition.getPageNum() == null) {
            condition.setPageNum(1);
        }
        if (condition.getPageSize() == null) {
            condition.setPageSize(10);
        }
        resp.setResult(taskOrderService.getMyPatentTask(user, condition, condition.getPageNum(), condition.getPageSize()));
        return resp;
    }

    /**
     * 根据搜索条件搜索企业信息列表
     *
     * @return
     */
    @RequestMapping(value = "/searchEnterpriseInfoTaskOrder", method = RequestMethod.GET)
    public ApiResponse<PageInfo<TaskOrder>> searchEnterpriseInfoTaskOrder(Enterprise enterprise)
            throws TaskOrderException {
        ApiResponse<PageInfo<TaskOrder>> resp = new ApiResponse<>();
        // 默认的分页参数
        if (enterprise.getPageNum() == null) {
            enterprise.setPageNum(1);
        }
        if (enterprise.getPageSize() == null) {
            enterprise.setPageSize(10);
        }
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(taskOrderService.getMyEnterpriseTask(user, enterprise, enterprise.getPageNum(), enterprise.getPageSize()));
        return resp;
    }

    /**
     * 根据搜索条件搜索企业需求列表
     *
     * @return
     */
    @RequestMapping(value = "/searchEnterpriseRequirementTaskOrder", method = RequestMethod.GET)
    public ApiResponse<PageInfo<TaskOrder>> searchEnterpriseRequirementTaskOrder(EnterpriseRequirement requirement)
            throws TaskOrderException {
        ApiResponse<PageInfo<TaskOrder>> resp = new ApiResponse<>();
        // 默认的分页参数
        if (requirement.getPageNum() == null) {
            requirement.setPageNum(1);
        }
        if (requirement.getPageSize() == null) {
            requirement.setPageSize(10);
        }
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(taskOrderService.getMyEnterpriseRequirementTask(user, requirement, requirement.getPageNum(), requirement.getPageSize()));
        return resp;
    }

    /**
     * 根据搜索条件搜索企业需求匹配专利列表
     *
     * @return
     */
    @RequestMapping(value = "/searchEnterpriseRequirementMatchPatentTaskOrder", method = RequestMethod.GET)
    public ApiResponse<PageInfo<TaskOrder>> searchEnterpriseRequirementMatchPatentTaskOrder(Match match)
            throws TaskOrderException {
        ApiResponse<PageInfo<TaskOrder>> resp = new ApiResponse<>();
        // 默认的分页参数
        if (match.getPageNum() == null) {
            match.setPageNum(1);
        }
        if (match.getPageSize() == null) {
            match.setPageSize(10);
        }
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(taskOrderService.getMyMatchTask(user, match, match.getPageNum(), match.getPageSize()));
        return resp;
    }


    @RequestMapping(value = "/hold/", method = RequestMethod.POST)
    public ApiResponse<String> hold(@RequestBody TaskOrderDealParam taskOrderDealParam, Authentication authentication) throws TaskOrderException {
    	ApiResponse<String> resp = new ApiResponse<>();
    	User user = UserUtil.getUser(authentication);
    	taskOrderService.holdTaskOrder(taskOrderDealParam, user);
    	resp.setResult("保存成功");
    	return resp;
    }
    
    @RequestMapping(value = "/deal/", method = RequestMethod.POST)
    public ApiResponse<String> deal(@RequestBody TaskOrderDealParam taskOrderDealParam, Authentication authentication) throws TaskOrderException {
        ApiResponse<String> resp = new ApiResponse<>();
        User user = UserUtil.getUser(authentication);
        taskOrderService.dealTaskOrder(taskOrderDealParam, user);
        resp.setResult("处理成功");
        return resp;
    }

    /**
     * 提交工单 ()
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ApiResponse<String> save(@RequestBody List<TaskOrderDealParam> taskOrderDealParams) throws TaskOrderException {
        ApiResponse<String> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        for (TaskOrderDealParam taskOrderDealParam : taskOrderDealParams) {
            taskOrderService.dealTaskOrder(taskOrderDealParam, user);
        }
        resp.setResult("success");
        return resp;
    }

    /**
     * 根据工单获取用户列表
     */
    @RequestMapping(value = "/users/{taskOrderId}", method = RequestMethod.GET)
    public ApiResponse<PageInfo<User>> users(@PathVariable Long taskOrderId) throws TaskOrderException {
        ApiResponse<PageInfo<User>> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(taskOrderService.getCandidateUser(taskOrderId, user, 999, 1));
        return resp;
    }

    /**
     * 根据工单获取自动、半自动标引标签列表
     */
    @RequestMapping(value = "/labels/auto/{taskOrderId}", method = RequestMethod.GET)
    public ApiResponse<List<TaskOrderLabel>> auto(@PathVariable Long taskOrderId) throws TaskOrderException {
        ApiResponse<List<TaskOrderLabel>> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(taskOrderService.doAutoIndex(taskOrderId, user));
        return resp;
    }

    /**
     * 根据工单获取半自动标引标签列表
     */
    @RequestMapping(value = "/labels/semi/{taskOrderId}", method = RequestMethod.GET)
    public ApiResponse<HumanIndexResponse> semi(@PathVariable Long taskOrderId) throws TaskOrderException {
    	ApiResponse<HumanIndexResponse> resp = new ApiResponse<>();
    	User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
    	resp.setResult(taskOrderService.getSemiLabel(taskOrderId, user));
    	return resp;
    }
    
    /**
     * 根据工单获取人工标引标签列表
     */
    @RequestMapping(value = "/labels/manual/{taskOrderId}", method = RequestMethod.GET)
    public ApiResponse<HumanIndexResponse> manual(@PathVariable Long taskOrderId) throws TaskOrderException {
        ApiResponse<HumanIndexResponse> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(taskOrderService.getHumanLabel(taskOrderId, user));
        return resp;
    }

    /**
     * 根据工单获取价值标引标签列表
     */
    @RequestMapping(value = "/labels/value/{taskOrderId}", method = RequestMethod.GET)
    public ApiResponse<List<TaskOrderLabel>> value(@PathVariable Long taskOrderId) throws TaskOrderException {
        ApiResponse<List<TaskOrderLabel>> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(taskOrderService.doValueIndex(taskOrderId, user));
        return resp;
    }

    /**
     * 根据工单获取价格标引标签列表
     */
    @RequestMapping(value = "/labels/price/{taskOrderId}", method = RequestMethod.GET)
    public ApiResponse<List<TaskOrderLabel>> price(@PathVariable Long taskOrderId) throws TaskOrderException {
        ApiResponse<List<TaskOrderLabel>> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(taskOrderService.getLabelInTask(taskOrderId, user));
        return resp;
    }

    /**
     * 人工标引标签审核
     */
    @RequestMapping(value = "/labels/maudit/{taskOrderId}", method = RequestMethod.GET)
    public ApiResponse<HumanIndexAuditResponse> maudit(@PathVariable Long taskOrderId) throws TaskOrderException {
        ApiResponse<HumanIndexAuditResponse> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(taskOrderService.getHumanResult(taskOrderId, user));
        return resp;
    }
    
    /**
     * 半自动标引标签审核
     */
    @RequestMapping(value = "/labels/semiaudit/{taskOrderId}", method = RequestMethod.GET)
    public ApiResponse<HumanIndexAuditResponse> semiaudit(@PathVariable Long taskOrderId) throws TaskOrderException {
    	ApiResponse<HumanIndexAuditResponse> resp = new ApiResponse<>();
    	User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
    	resp.setResult(taskOrderService.getSemiAutoResult(taskOrderId, user));
    	return resp;
    }

    /**
     * 查询专利价值评估详情
     */
    @RequestMapping(value = "/detail/{id}")
    public ApiResponse<ValueIndexPatent> detail(@PathVariable("id") Long id) {
        ApiResponse<ValueIndexPatent> resp = new ApiResponse<>();
        ValueIndexPatent patent = taskOrderService.findValueIndexPatentById(id);
        resp.setResult(patent);
        return resp;
    }

    /**
     * 查询定单价值评估详情
     */
    @RequestMapping(value = "/value/{id}")
    public ApiResponse<HumanAssessmentPatent> orderDetail(@PathVariable("id") Long id) {
        ApiResponse<HumanAssessmentPatent> resp = new ApiResponse<>();
        HumanAssessmentPatent patent = taskOrderService.findPatentByOrderId(id);
        resp.setResult(patent);
        return resp;
    }


    @RequestMapping(value = "/due", method = RequestMethod.GET)
    public ApiResponse<PageInfo<TaskOrder>> due(
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNum) {
        ApiResponse<PageInfo<TaskOrder>> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(taskOrderService.findDueOrders(user, pageNum, pageSize));
        return resp;
    }

    @RequestMapping(value = "/alert", method = RequestMethod.GET)
    public ApiResponse<PageInfo<TaskOrder>> alert(
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNum) {
        ApiResponse<PageInfo<TaskOrder>> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(taskOrderService.findAlertOrders(user, pageNum, pageSize));
        return resp;
    }

    @RequestMapping(value = "/unfinished", method = RequestMethod.GET)
    public ApiResponse<PageInfo<TaskOrder>> unfinished(
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNum) {
        ApiResponse<PageInfo<TaskOrder>> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(taskOrderService.findUnfinishedOrders(user, pageNum, pageSize));
        return resp;
    }

    @RequestMapping(value = "/finished", method = RequestMethod.GET)
    public ApiResponse<PageInfo<TaskOrder>> finished(
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNum) {
        ApiResponse<PageInfo<TaskOrder>> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(taskOrderService.findFinishedOrders(user, pageNum, pageSize));
        return resp;
    }

    @RequestMapping(value = "/back", method = RequestMethod.GET)
    public ApiResponse<PageInfo<TaskOrder>> back(
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNum) {
        ApiResponse<PageInfo<TaskOrder>> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(taskOrderService.findBackOrders(user, pageNum, pageSize));
        return resp;
    }

    /**
     * 根据工单获取转派用户列表
     */
    @RequestMapping(value = "/redeploy/users/{taskOrderId}", method = RequestMethod.GET)
    public ApiResponse<PageInfo<User>> getRedeployCandidateUser(@PathVariable Long taskOrderId) throws TaskOrderException {
        ApiResponse<PageInfo<User>> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(taskOrderService.getRedeployCandidateUser(taskOrderId, user, 999, 1));
        return resp;
    }

    /**
     * 转派
     * @return
     */
    @RequestMapping(value = "/redeploy", method = RequestMethod.POST)
    public ApiResponse<String> redeploy(@RequestBody List<TaskOrderDealParam> taskOrderDealParams) throws TaskOrderException {
        ApiResponse<String> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        for (TaskOrderDealParam taskOrderDealParam : taskOrderDealParams) {
            taskOrderService.dealRedeployOrder(taskOrderDealParam, user);
        }
        resp.setResult("success");
        return resp;
    }
}
