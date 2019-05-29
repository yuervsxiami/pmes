package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.request.ResultSearchCondition;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.controller.api.response.IpcVo;
import com.cnuip.pmes2.controller.api.response.NicVo;
import com.cnuip.pmes2.controller.api.response.NtccVo;
import com.cnuip.pmes2.domain.core.Ipc;
import com.cnuip.pmes2.domain.core.Nic;
import com.cnuip.pmes2.domain.core.Ntcc;
import com.cnuip.pmes2.domain.core.Result;
import com.cnuip.pmes2.domain.el.ElResult;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.service.ResultService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2019/5/21.
 * Time: 14:06
 */
@RestController
@RequestMapping("/api/result")
public class ResultController extends BussinessLogicExceptionHandler {

    @Autowired
    ResultService resultService;

    @RequestMapping(value="/add/",method = RequestMethod.POST)
    public ApiResponse<Result> add(@RequestHeader("X-Request-UserId") Long userId,
                                    @RequestBody Result result) throws BussinessLogicException {
        ApiResponse resp = new ApiResponse<>();
        resultService.add(userId,result);
        resp.setResult("ok");
        return resp;
    }

    @RequestMapping(value="/update/",method = RequestMethod.POST)
    public ApiResponse<Result> update(@RequestHeader("X-Request-UserId") Long userId,
                                   @RequestBody Result result) throws BussinessLogicException {
        ApiResponse resp = new ApiResponse<>();
        resultService.update(userId,result);
        resp.setResult("ok");
        return resp;
    }

    @GetMapping(value = "/ipc")
    public ApiResponse<List<IpcVo>> getIpc(){
        ApiResponse<List<IpcVo>> resp=new ApiResponse<>();
        resp.setResult(resultService.getIpc());
        return resp;
    }

    @GetMapping(value = "/nic")
    public ApiResponse<List<NicVo>> getNic(){
        ApiResponse<List<NicVo>> resp=new ApiResponse<>();
        resp.setResult(resultService.getNic());
        return resp;
    }

    @GetMapping(value = "/ntcc")
    public ApiResponse<List<NtccVo>> getNtcc(){
        ApiResponse<List<NtccVo>> resp=new ApiResponse<>();
        resp.setResult(resultService.getNtcc());
        return resp;
    }

    @GetMapping(value = "/ipcFirst")
    public ApiResponse<List<Ipc>> ipcFirst(){
        ApiResponse<List<Ipc>> apiResponse=new ApiResponse<>();
        apiResponse.setResult( resultService.getIpcFirst());
        return apiResponse;
    }

    @GetMapping(value = "/childIpcByCode")
    public ApiResponse<List<Ipc>> childIpcByCode(@RequestParam String code){
        ApiResponse<List<Ipc>> apiResponse=new ApiResponse<>();
        apiResponse.setResult( resultService.childIpcByCode(code));
        return apiResponse;
    }

    @GetMapping(value = "/nicFirst")
    public ApiResponse<List<Nic>> nicFirst(){
        ApiResponse<List<Nic>> apiResponse=new ApiResponse<>();
        apiResponse.setResult( resultService.getNicFirst());
        return apiResponse;
    }

    @GetMapping(value = "/childNicByCode")
    public ApiResponse<List<Nic>> childNicByCode(@RequestParam String code){
        ApiResponse<List<Nic>> apiResponse=new ApiResponse<>();
        apiResponse.setResult( resultService.childNicByCode(code));
        return apiResponse;
    }

    @GetMapping(value = "/ntccFirst")
    public ApiResponse<List<Ntcc>> ntccFirst(){
        ApiResponse<List<Ntcc>> apiResponse=new ApiResponse<>();
        apiResponse.setResult( resultService.getNtccFirst());
        return apiResponse;
    }

    @GetMapping(value = "/childNtccByCode")
    public ApiResponse<List<Ntcc>> childNtccByCode(@RequestParam String code){
        ApiResponse<List<Ntcc>> apiResponse=new ApiResponse<>();
        apiResponse.setResult( resultService.childNtccByCode(code));
        return apiResponse;
    }

    @GetMapping(value = "/getResultList")
    public ApiResponse<List<ElResult>> getResultList(ResultSearchCondition resultSearchCondition){
        ApiResponse<List<ElResult>> apiResponse=new ApiResponse<>();
        apiResponse.setResult(resultService.getResultList(resultSearchCondition));
        return apiResponse;
    }

    @GetMapping(value = "/getResultDetail/{id}")
    public ApiResponse<ElResult> getResultDetail(@PathVariable("id") Long id){
        ApiResponse<ElResult> apiResponse=new ApiResponse<>();
        apiResponse.setResult(resultService.getResultDetail(id));
        return apiResponse;
    }

    @GetMapping(value = "/export")
    public ApiResponse<List<ElResult>> export(){
        ApiResponse<List<ElResult>> apiResponse=new ApiResponse<>();
        apiResponse.setResult(resultService.export());
        return apiResponse;
    }

}
