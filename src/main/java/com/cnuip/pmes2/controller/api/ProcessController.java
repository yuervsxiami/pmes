package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.request.ProcessSearchCondition;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.TaskOrderLabel;
import com.cnuip.pmes2.domain.core.User;
import com.cnuip.pmes2.exception.ProcessException;
import com.cnuip.pmes2.service.ProcessService;
import com.cnuip.pmes2.util.UserUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.cnuip.pmes2.domain.core.Process;

import java.util.Date;
import java.util.List;

/**
 * Created by wangzhibin on 2018/1/15.
 */
@RestController
@RequestMapping("/api/process")
public class ProcessController extends BussinessLogicExceptionHandler {


    @Autowired
    private ProcessService processService;

    /**
     * 根据搜索条件搜索列表
     *
     * @return
     */
    @RequestMapping(value = "/labels/{id}", method = RequestMethod.GET)
    public ApiResponse<List<TaskOrderLabel>> search(@PathVariable Long id) {
        ApiResponse<List<TaskOrderLabel>> resp = new ApiResponse<>();
        resp.setResult(processService.selectAllLabel(id));
        return resp;
    }

    /**
     * 根据搜索条件搜索列表
     *
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ApiResponse<PageInfo<Process>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Date fromTime,
            @RequestParam(required = false) Date toTime,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNum) {
        ApiResponse<PageInfo<Process>> resp = new ApiResponse<>();
        ProcessSearchCondition condition = new ProcessSearchCondition(name, username, fromTime, toTime);
        resp.setResult(processService.search(condition, pageNum, pageSize));
        return resp;
    }

    /**
     * 根据实例类型获得流程模版 null 获得全部
     *
     * @return
     */
    @RequestMapping(value = "/all/{type}", method = RequestMethod.GET)
    public ApiResponse<List<Process>> all(@PathVariable Integer type) {
        ApiResponse<List<Process>> resp = new ApiResponse<>();
        resp.setResult(processService.selectAllByInstanceType(type));
        return resp;
    }

    /**
     * 添加、修改流程模版
     *
     * @param process
     * @return
     * @throws ProcessException
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ApiResponse<Process> edit(@RequestBody Process process) throws ProcessException {
        ApiResponse<Process> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        if (process.getId() == null) {
            resp.setResult(processService.addProcess(process, user.getId()));
        } else {
            resp.setResult(processService.updateProcess(process, user.getId()));
        }
        return resp;
    }

    /**
     * 启用、禁用
     *
     * @param id
     * @return
     * @throws ProcessException
     */
    @RequestMapping(value = "/changestate/{id}", method = RequestMethod.GET)
    public ApiResponse<String> changestate(@PathVariable Long id) throws ProcessException {
        ApiResponse<String> resp = new ApiResponse<>();
        Process process = processService.selectSimpleByPrimaryKey(id);
        if (process.getState()==0){
            processService.changeState(id, 1);
            resp.setResult("流程模版启用成功！");
        }
        else{
            processService.changeState(id, 0);
            resp.setResult("流程模版禁用成功！");
        }
        return resp;
    }

    /**
     * 配置标签体系
     *
     * @param process
     * @return
     * @throws ProcessException
     */
    @RequestMapping(value = "/setlabelset", method = RequestMethod.POST)
    public ApiResponse<String> setlabelset(@RequestBody Process process) throws ProcessException {
        ApiResponse<String> resp = new ApiResponse<>();
        processService.changeLabelset(process.getId(), process.getLabelsetId());
        resp.setResult("标签体系设置成功！");
        return resp;
    }

    /**
     * 删除标签体系
     *
     * @param id
     * @return
     * @throws ProcessException
     */
    @RequestMapping(value = "/removelabelset/{id}", method = RequestMethod.GET)
    public ApiResponse<String> removelabelset(@PathVariable Long id) throws ProcessException {
        ApiResponse<String> resp = new ApiResponse<>();
        processService.changeLabelset(id, null);
        resp.setResult("删除标签体系成功！");
        return resp;
    }

    /**
     * 配置时间
     *
     * @param process
     * @return
     * @throws ProcessException
     */
    @RequestMapping(value = "/changetime", method = RequestMethod.POST)
    public ApiResponse<String> changetime(@RequestBody Process process) throws ProcessException {
        ApiResponse<String> resp = new ApiResponse<>();
        processService.changeTime(process.getId(), process.getAlertTime(),process.getDueTime());
        resp.setResult("预警时间和超时时间设置成功！");
        return resp;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResponse<Process> get(@PathVariable Long id) throws ProcessException {
        ApiResponse<Process> resp = new ApiResponse<>();
        resp.setResult(processService.selectByPrimaryKey(id));
        return resp;
    }


}