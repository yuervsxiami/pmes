package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.request.SpecialistInfo;
import com.cnuip.pmes2.controller.api.request.SpecialistRequest;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.College;
import com.cnuip.pmes2.domain.core.Title;
import com.cnuip.pmes2.service.SpecialistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/specialist")
public class SpecialistController {
    private SpecialistService specialistService;

    public SpecialistController(SpecialistService specialistService) {
        this.specialistService = specialistService;
    }

    @RequestMapping(value="/add",method = RequestMethod.POST)
    public ApiResponse<String> add(@RequestHeader("X-Request-UserId") Long userId,
                                       @RequestBody SpecialistInfo specialistInfo) {
        ApiResponse resp = new ApiResponse<>();
        specialistService.insert(userId, specialistInfo);
        resp.setResult("ok");
        return resp;
    }

    @RequestMapping(value="/update",method = RequestMethod.POST)
    public ApiResponse<String> update(@RequestHeader("X-Request-UserId") Long userId,
                                   @RequestBody SpecialistInfo specialistInfo) {
        ApiResponse resp = new ApiResponse<>();
        specialistService.update(userId, specialistInfo);
        resp.setResult("ok");
        return resp;
    }

    @RequestMapping(value = "/colleges", method = RequestMethod.GET)
    public ApiResponse<Map<String, List<College>>> colleges() {
        ApiResponse<Map<String, List<College>>> resp = new ApiResponse<>();
        resp.setResult(specialistService.colleges());
        return resp;
    }

    @RequestMapping(value = "/titles", method = RequestMethod.GET)
    public ApiResponse<List<Title>> titles() {
        ApiResponse<List<Title>> resp = new ApiResponse<>();
        resp.setResult(specialistService.titles());
        return resp;
    }

    @GetMapping("/")
    public ApiResponse<SpecialistInfo> findOne(Long id) {
        ApiResponse<SpecialistInfo> resp = new ApiResponse<>();
        resp.setResult(specialistService.findOne(id));
        return resp;
    }

    @PostMapping("/")
    public ApiResponse<Map<String, Object>> specialistPage(@RequestBody SpecialistRequest specialistRequest) {
        ApiResponse<Map<String, Object>> resp = new ApiResponse<>();
        resp.setResult(specialistService.specialistPage(specialistRequest));
        return resp;
    }
}
