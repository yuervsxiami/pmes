package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.controller.api.response.TaskRemindNum;
import com.cnuip.pmes2.controller.api.response.TaskUseTime;
import com.cnuip.pmes2.controller.api.response.UserRemindNum;
import com.cnuip.pmes2.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * StatController
 *
 * @author: xiongwei
 * Date: 2018/3/11 下午8:51
 */
@RestController
@RequestMapping("/api/stat")
public class StatController extends BussinessLogicExceptionHandler {

    @Autowired
    private StatService statService;

    @RequestMapping("/patent/process/orders")
    public ApiResponse<Map<String, Long>> countPatentProcessOrders() {
        ApiResponse<Map<String, Long>> response = new ApiResponse<>();
        response.setResult(statService.countPatentProcessOrders());
        return response;
    }
    
    @RequestMapping("/process/orders/{processType}")
    public ApiResponse<Map<String, Long>> countProcessOrdersByProcessType(@PathVariable Long processType) {
    	ApiResponse<Map<String, Long>> response = new ApiResponse<>();
    	response.setResult(statService.countProcessOrdersByProcessType(processType));
    	return response;
    }
    
    @RequestMapping("/process/taskUseTime/{processId}")
    public ApiResponse<List<TaskUseTime>> countUseTimeByProcess(@PathVariable Long processId) {
    	ApiResponse<List<TaskUseTime>> response = new ApiResponse<>();
    	response.setResult(statService.countUseTimeByProcess(processId));
    	return response;
    }
    
    @RequestMapping("/process/taskAlertNum/{processId}")
    public ApiResponse<List<TaskRemindNum>> countAlertNumByProcess(@PathVariable Long processId) {
    	ApiResponse<List<TaskRemindNum>> response = new ApiResponse<>();
    	response.setResult(statService.countTaskAlertNumByProcess(processId));
    	return response;
    }

    @RequestMapping("/process/taskDueNum/{processId}")
    public ApiResponse<List<TaskRemindNum>> countDueNumByProcess(@PathVariable Long processId) {
    	ApiResponse<List<TaskRemindNum>> response = new ApiResponse<>();
    	response.setResult(statService.countTaskDueNumByProcess(processId));
    	return response;
    }
    
    @RequestMapping("/process/alertUser/{processId}")
    public ApiResponse<List<UserRemindNum>> countMaxUserAlert(@PathVariable Long processId) {
    	ApiResponse<List<UserRemindNum>> response = new ApiResponse<>();
    	response.setResult(statService.countMaxUserAlert(processId));
    	return response;
    }
    
    @RequestMapping("/process/dueUser/{processId}")
    public ApiResponse<List<UserRemindNum>> countMaxUserDue(@PathVariable Long processId) {
    	ApiResponse<List<UserRemindNum>> response = new ApiResponse<>();
    	response.setResult(statService.countMaxUserDue(processId));
    	return response;
    }
    
    @RequestMapping("/patent/process/orders/detail/due")
    public ApiResponse<Map<String, Long>> countDetailForDueProcessOrders() {
        ApiResponse<Map<String, Long>> response = new ApiResponse<>();
        response.setResult(statService.countDetailForDueProcessOrders());
        return response;
    }

    @RequestMapping("/patent/process/orders/detail/alert")
    public ApiResponse<Map<String, Long>> countDetailForAlertProcessOrders() {
        ApiResponse<Map<String, Long>> response = new ApiResponse<>();
        response.setResult(statService.countDetailForAlertProcessOrders());
        return response;
    }

    @RequestMapping("/patent/process/orders/detail/processing")
    public ApiResponse<Map<String, Long>> countDetailForProcessingOrders() {
        ApiResponse<Map<String, Long>> response = new ApiResponse<>();
        response.setResult(statService.countDetailForProcessingOrders());
        return response;
    }

    @RequestMapping("/patent/process/orders/detail/done")
    public ApiResponse<Map<String, Long>> countDetailForDoneOrders() {
        ApiResponse<Map<String, Long>> response = new ApiResponse<>();
        response.setResult(statService.countDetailForDoneOrders());
        return response;
    }

}
