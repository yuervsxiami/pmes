package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wangzhibin on 2018/3/6.
 */
public class AbstractController<T, S> {

    @Autowired
    public S service;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ApiResponse<List<T>> all() {
        ApiResponse<List<T>> resp = new ApiResponse<>();
        resp.setResult(((AbstractService<T>) service).getAll());
        return resp;
    }
}
