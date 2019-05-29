package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.el.ElProfessor;
import com.cnuip.pmes2.domain.el.Requirement;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.service.RabbitMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.cnuip.pmes2.domain.el.User;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/11/28.
 * Time: 9:59
 */
@RestController
@RequestMapping("/api/v1/rabbit")
public class RabbitMqController {

    @Autowired
    RabbitMqService rabbitMqService;

    @RequestMapping(value = "/saveProfessorToDb",method = RequestMethod.PUT)
    public ApiResponse<ElProfessor> saveProfessorToDb(@RequestBody String userStr) throws Exception {
        ApiResponse<ElProfessor> apiResponse=new ApiResponse<>();
        ElProfessor elProfessor=rabbitMqService.saveProfessor(userStr);
        apiResponse.setResult(elProfessor);
        return apiResponse;
    }

    @RequestMapping(value = "/saveRequirementToES",method = RequestMethod.PUT)
    public ApiResponse<Requirement> saveRequirementToES(@RequestBody String requirementStr) throws Exception {
        ApiResponse<Requirement> apiResponse=new ApiResponse<>();
        Requirement requirement=rabbitMqService.saveRequirement(requirementStr);
        apiResponse.setResult(requirement);
        return apiResponse;
    }

}
