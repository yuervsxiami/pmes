package com.cnuip.pmes2.controller;

import com.cnuip.pmes2.controller.api.BussinessLogicExceptionHandler;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.ElPatent;
import com.cnuip.pmes2.service.ElPatentService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ElasticController
 *
 * @author: xiongwei
 * Date: 2018/2/8 上午10:55
 */
@RestController
@RequestMapping("/api/elsearch")
public class ElasticController extends BussinessLogicExceptionHandler {

    @Autowired
    private ElPatentService elPatentService;

    @RequestMapping(value = "/patent", produces = "application/json")
    public ApiResponse<PageInfo<ElPatent>> searchPatent(@RequestParam(required = false, defaultValue = "") String keywords,
                                                        @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        ApiResponse<PageInfo<ElPatent>> resp = new ApiResponse<>();
        Page<ElPatent> page = this.elPatentService.searchByKeywords(keywords, new PageRequest(pageNum - 1, pageSize));
        com.github.pagehelper.Page<ElPatent> patentPage = new com.github.pagehelper.Page<>(pageNum, pageSize);
        patentPage.setTotal(page.getTotalElements());
        patentPage.setPages(page.getTotalPages());
        patentPage.addAll(page.getContent());
        PageInfo<ElPatent> pageInfo = new PageInfo<>(patentPage);
        resp.setResult(pageInfo);
        return resp;
    }


}
