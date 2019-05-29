package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.domain.core.Region;
import com.cnuip.pmes2.service.RegionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangzhibin on 2018/3/6.
 */
@RestController
@RequestMapping("/api/region")
public class RegionController extends AbstractController<Region, RegionService> {
}
