package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.Expert;
import com.cnuip.pmes2.domain.core.IPCField;
import com.cnuip.pmes2.domain.core.User;
import com.cnuip.pmes2.service.ExpertService;
import com.cnuip.pmes2.util.UserUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @auhor Crixalis
 * @date 2018/3/21 17:37
 */
@RestController
@RequestMapping("/api/expert")
public class ExpertController {

    @Autowired
    private ExpertService expertService;

    @GetMapping("/ipc/all/")
    public ApiResponse<List<IPCField>> getAllIPC() {
        ApiResponse<List<IPCField>> res = new ApiResponse<>();
        res.setResult(expertService.selectAllIpcField());
        return res;
    }

    @GetMapping("/search/")
    public ApiResponse<PageInfo<Expert>> search(
            @RequestParam(required = false) String institutions,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String specialties,
            @RequestParam(required = false) String ipcField,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Date optDateFrom,
            @RequestParam(required = false) Date optDateTo,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNum
    ) {
        ApiResponse<PageInfo<Expert>> res = new ApiResponse<>();
        Expert expert = new Expert();
        expert.setInstitutions(institutions);
        expert.setName(name);
        expert.setIpcField(ipcField);
        expert.setSpecialties(specialties);
        expert.setKeyword(keyword);
        expert.setUserId(userId);
        expert.setOptDateFrom(optDateFrom);
        expert.setOptDateTo(optDateTo);
        res.setResult(expertService.search(expert,pageSize,pageNum));
        return res;
    }

    @GetMapping("/{id}")
    public ApiResponse<Expert> search(@PathVariable Long id) {
        ApiResponse<Expert> res = new ApiResponse<>();
        res.setResult(expertService.select(id));
        return res;
    }

    @PostMapping("/add/")
    public ApiResponse<Expert> add(
            Authentication authentication, @RequestBody Expert expert) {
        ApiResponse<Expert> res = new ApiResponse<>();
        User user = UserUtil.getUser(authentication);
        expert.setUserId(user.getId());
        res.setResult(expertService.insert(expert));
        return res;
    }

    @PostMapping("/update/")
    public ApiResponse<Expert> update(
            Authentication authentication, @RequestBody Expert expert) {
        ApiResponse<Expert> res = new ApiResponse<>();
        User user = UserUtil.getUser(authentication);
        expert.setUserId(user.getId());
        res.setResult(expertService.update(expert));
        return res;
    }

    @PostMapping("/uploadImage")
    public ApiResponse<String> uploadImage(@RequestParam("file") MultipartFile file) throws Exception{
        ApiResponse<String> res=new ApiResponse<>();
        res.setResult(expertService.uploadImage(file));
        return res;
    }
}
