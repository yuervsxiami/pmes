package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.controller.api.response.CurrentProcessRect;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * ActController
 *
 * @author: xiongwei
 * Date: 2018/1/28 下午4:32
 */
@RestController
@RequestMapping("/api/act")
public class ActController extends BussinessLogicExceptionHandler {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;

    @RequestMapping("/image/{taskId}")
    public void processImage(@PathVariable String taskId, HttpServletResponse response) {
    	Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
        List<ProcessDefinition> procDefs = repositoryService.createProcessDefinitionQuery()
//        		.processDefinitionKey(processKey)
//                .orderByDeploymentId().desc().list();
        		.processDefinitionId(task.getProcessDefinitionId()).list();
        if (procDefs.size() > 0) {
            ProcessDefinition procDef = procDefs.get(0);
            InputStream inputStream = repositoryService.getResourceAsStream(procDef.getDeploymentId(), procDef.getDiagramResourceName());
            OutputStream outputStream = null;
            response.setContentType("image/png");
            try {
                outputStream = response.getOutputStream();
                int count;
                byte[] buffer = new byte[1024 * 8];
                while ((count = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, count);
                    outputStream.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/image/{processKey}/{taskId}")
    @ResponseBody
    public ApiResponse<CurrentProcessRect> currentProcessInfo(@PathVariable String processKey, @PathVariable String taskId ) {
        ApiResponse<CurrentProcessRect> resp = new ApiResponse<>();
        ActivityImpl actImpl = null;
    	Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
        List<ProcessDefinition> procDefs = repositoryService.createProcessDefinitionQuery()
        		.processDefinitionId(task.getProcessDefinitionId()).list();
        if (procDefs.size() > 0) {
            ProcessDefinition procDef = procDefs.get(0);
            if (task != null) {
                Execution execution = this.runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
                if (execution != null) {
                    ProcessDefinitionEntity def = (ProcessDefinitionEntity)((RepositoryServiceImpl)this.repositoryService).getDeployedProcessDefinition(procDef.getId());
                    // 获取当前任务执行到哪个节点
                    String activitiId = execution.getActivityId();
                    for (ActivityImpl activityImpl : def.getActivities()) {
                        String id = activityImpl.getId();
                        if (id.equals(activitiId)) {
                            actImpl = activityImpl;
                            break;
                        }
                    }
                }
            }
        }
        if (actImpl != null) {
            CurrentProcessRect currentProcessRect = new CurrentProcessRect(actImpl.getX(), actImpl.getY(), actImpl.getWidth(), actImpl.getHeight());
            resp.setResult(currentProcessRect);
        }
        return resp;
    }

}
