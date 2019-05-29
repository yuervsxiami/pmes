package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.NationalEconomy;
import com.cnuip.pmes2.service.NationalEconomyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wangzhibin on 2018/3/6.
 */
@RestController
@RequestMapping("/api/nationaleconomy")
public class NationalEconomyController extends AbstractController<NationalEconomy, NationalEconomyService> {

    /**
     * 查询所有的底层分类
     * @return
     */
    @RequestMapping("/bottoms")
    public ApiResponse<List<NationalEconomy>> findAllBottomCategories(String keyword) {
        ApiResponse<List<NationalEconomy>> resp = new ApiResponse<>();
        resp.setResult(this.service.findBottomFields(keyword));
        return resp;
    }

}
