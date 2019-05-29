package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.request.ProcessTaskSearchCondition;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.ProcessTask;
import com.cnuip.pmes2.domain.core.ProcessTaskLabel;
import com.cnuip.pmes2.domain.core.User;
import com.cnuip.pmes2.exception.ProcessTaskException;
import com.cnuip.pmes2.service.ProcessTaskService;
import com.cnuip.pmes2.util.UserUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by wangzhibin on 2018/1/18.
 */
@RestController
@RequestMapping("/api/processtask")
public class ProcessTaskController extends BussinessLogicExceptionHandler {


    @Autowired
    private ProcessTaskService processTaskService;

    /**
     * 根据搜索条件搜索列表
     *
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ApiResponse<PageInfo<ProcessTask>> search(
            @RequestParam(required = false) Long processId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer state,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Date fromTime,
            @RequestParam(required = false) Date toTime,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNum) {
        ApiResponse<PageInfo<ProcessTask>> resp = new ApiResponse<>();
        ProcessTaskSearchCondition condition = new ProcessTaskSearchCondition(processId, name, state, username, fromTime, toTime);
        resp.setResult(processTaskService.search(condition, pageNum, pageSize));
        return resp;
    }

    @RequestMapping(value = "/labels/{id}", method = RequestMethod.GET)
    public ApiResponse<List<ProcessTaskLabel>> selectProcessTaskLabels(@PathVariable Long id) {
        ApiResponse<List<ProcessTaskLabel>> resp = new ApiResponse<>();
        resp.setResult(processTaskService.selectProcessTaskLabels(id));
        return resp;
    };

    /**
     * 添加环节
     *
     * @param processTask
     * @return
     * @throws ProcessTaskException
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ApiResponse<ProcessTask> edit(@RequestBody ProcessTask processTask) throws ProcessTaskException {
        ApiResponse<ProcessTask> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(processTaskService.addProcessTask(processTask, user.getId()));
        return resp;
    }

    /**
     * 启用、禁用
     *
     * @param id
     * @return
     * @throws ProcessTaskException
     */
    @RequestMapping(value = "/changestate/{id}", method = RequestMethod.GET)
    public ApiResponse<String> changestate(@PathVariable Long id) throws ProcessTaskException {
        ApiResponse<String> resp = new ApiResponse<>();
        ProcessTask processTask = processTaskService.selectByPrimaryKey(id);
        if (processTask.getState() == 0) {
            processTaskService.changeState(id, 1);
            resp.setResult("环节启用成功！");
        } else {
            processTaskService.changeState(id, 0);
            resp.setResult("环节禁用成功！");
        }
        return resp;
    }

    /**
     * 配置时间
     *
     * @param processTask
     * @return
     * @throws ProcessTaskException
     */
    @RequestMapping(value = "/changetime", method = RequestMethod.POST)
    public ApiResponse<String> changetime(@RequestBody ProcessTask processTask) throws ProcessTaskException {
        ApiResponse<String> resp = new ApiResponse<>();
        processTaskService.changeTime(processTask.getId(), processTask.getAlertTime(), processTask.getDueTime());
        resp.setResult("预警时间和超时时间设置成功！");
        return resp;
    }

    /**
     * 配置环节标签
     *
     * @param processTask
     * @return
     * @throws ProcessTaskException
     */
    @RequestMapping(value = "/addtasklabels", method = RequestMethod.POST)
    public ApiResponse<ProcessTask> addtasklabels(@RequestBody ProcessTask processTask) throws ProcessTaskException {
        ApiResponse<ProcessTask> resp = new ApiResponse<>();
        processTask.getLabels().forEach(label -> {
            label.setTaskId(processTask.getId());
        });
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(processTaskService.updateProcessTask(processTask, user.getId()));
        return resp;
    }

    /**
     * 配置角色
     *
     * @param processTask
     * @return
     * @throws ProcessTaskException
     */
    @RequestMapping(value = "/changerole", method = RequestMethod.POST)
    public ApiResponse<String> changerole(@RequestBody ProcessTask processTask) throws ProcessTaskException {
        ApiResponse<String> resp = new ApiResponse<>();
        processTaskService.changeRole(processTask.getId(), processTask.getRoleId());
        resp.setResult("环节角色设置成功！");
        return resp;
    }
}
