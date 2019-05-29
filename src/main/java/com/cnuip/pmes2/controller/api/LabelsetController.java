package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.request.LabelsetSearchCondition;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.Labelset;
import com.cnuip.pmes2.domain.core.LabelsetLabel;
import com.cnuip.pmes2.domain.core.User;
import com.cnuip.pmes2.exception.LabelsetException;
import com.cnuip.pmes2.service.LabelsetService;
import com.cnuip.pmes2.util.UserUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * LabelsetController
 *
 * @author: xiongwei
 * Date: 2018/1/13 下午3:44
 */
@RestController
@RequestMapping("/api/labelset")
public class LabelsetController extends BussinessLogicExceptionHandler {


    @Autowired
    private LabelsetService labelsetService;

    /**
     * 根据搜索条件搜索列表
     *
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ApiResponse<PageInfo<Labelset>> search(
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String version,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Date fromTime,
            @RequestParam(required = false) Date toTime,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNum) {
        ApiResponse<PageInfo<Labelset>> resp = new ApiResponse<>();
        LabelsetSearchCondition condition = new LabelsetSearchCondition(type, name, version, username, fromTime, toTime);
        resp.setResult(labelsetService.search(condition, pageNum, pageSize));
        return resp;
    }

    @RequestMapping(value = "/type/{type}", method = RequestMethod.GET)
    public ApiResponse<List<Labelset>> findByType(@PathVariable Integer type) {
        ApiResponse<List<Labelset>> resp = new ApiResponse<>();
        resp.setResult(labelsetService.findByType(type));
        return resp;
    }

    /**
     * 添加标签体系
     * @param labelset
     * @return
     * @throws LabelsetException
     */
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ApiResponse<Labelset> addLabelset(@RequestBody Labelset labelset) throws LabelsetException {
        ApiResponse<Labelset> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(labelsetService.addLabelset(labelset, user.getId()));
        return resp;
    }

    /**
     * 更新标签体系
     * @param labelset
     * @return
     * @throws LabelsetException
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiResponse<Labelset> updateLabelset(@RequestBody Labelset labelset) throws LabelsetException {
        ApiResponse<Labelset> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(labelsetService.updateLabelset(labelset, user.getId()));
        return resp;
    }

    /**
     * 更新标签体系名称
     * @param labelset
     * @return
     * @throws LabelsetException
     */
    @RequestMapping(value = "/edit/", method = RequestMethod.POST)
    public ApiResponse<Labelset> updateLabelsetName(@RequestBody Labelset labelset) throws LabelsetException {
        ApiResponse<Labelset> resp = new ApiResponse<>();
        User user = UserUtil.getUser(SecurityContextHolder.getContext().getAuthentication());
        resp.setResult(labelsetService.updateLabelsetName(labelset, user.getId()));
        return resp;
    }

    /**
     * 更新标签体系状态
     * @param labelset
     * @return
     * @throws LabelsetException
     */
    @RequestMapping(value = "/state", method = RequestMethod.POST)
    public ApiResponse<Object> updateLabelsetState(@RequestBody Labelset labelset) throws LabelsetException {
        ApiResponse<Object> resp = new ApiResponse<>();
        labelsetService.changeLabelsetState(labelset.getId(), labelset.getState());
        return resp;
    }

    /**
     * 获取标签体系下的标签
     */
    @RequestMapping(value = "/labels/{labelsetId}", method = RequestMethod.GET)
    public ApiResponse<List<LabelsetLabel>> getLabelsetlabel(@PathVariable Long labelsetId) {
        ApiResponse<List<LabelsetLabel>> resp = new ApiResponse<>();
        resp.setResult(labelsetService.selectLabelsetLabels(labelsetId));
        return resp;
    }

}
