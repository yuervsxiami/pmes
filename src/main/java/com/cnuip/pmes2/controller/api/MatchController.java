package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.constant.Workflows;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.Enterprise;
import com.cnuip.pmes2.domain.core.Match;
import com.cnuip.pmes2.domain.core.ProcessOrder;
import com.cnuip.pmes2.domain.core.User;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.exception.ProcessOrderException;
import com.cnuip.pmes2.service.MatchService;
import com.cnuip.pmes2.service.ProcessOrderService;
import com.cnuip.pmes2.service.ProcessService;
import com.cnuip.pmes2.util.UserUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MatchController
 *
 * Date: 2018/2/6 上午10:11
 */
@RestController
@RequestMapping("/api/match")
public class MatchController extends BussinessLogicExceptionHandler {

    @Autowired
    private MatchService matchService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private ProcessOrderService processOrderService;

    /**
     * 搜索匹配信息
     * @param match
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ApiResponse<PageInfo<Match>> search(Match match) {
        ApiResponse<PageInfo<Match>> resp = new ApiResponse<>();
        // 默认的分页参数
        if (match.getPageNum() == null) {
            match.setPageNum(1);
        }
        if (match.getPageSize() == null) {
            match.setPageSize(10);
        }
        Page<Match> page = (Page<Match>) this.matchService.find(match);
        resp.setResult(page.toPageInfo());
        return resp;
    }

    /**
     * 新增匹配信息
     * @param match
     * @return
     * @throws BussinessLogicException
     */
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ApiResponse<Match> save(@RequestBody Match match) throws BussinessLogicException {
        ApiResponse<Match> resp = new ApiResponse<>();
        resp.setResult(this.matchService.save(match));
        return resp;
    }

    /**
     * 更新匹配信息
     * @param match
     * @return
     * @throws BussinessLogicException
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiResponse<Match> update(@RequestBody Match match) throws BussinessLogicException {
        ApiResponse<Match> resp = new ApiResponse<>();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null) {
            match.setUserId(user.getId());
        }
        resp.setResult(this.matchService.update(match));
        return resp;
    }

    /**
     * 删除匹配信息
     * @param match
     * @return
     * @throws BussinessLogicException
     */
    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public ApiResponse<Integer> delete(Match match) throws BussinessLogicException {
        ApiResponse<Integer> resp = new ApiResponse<>();
        resp.setResult(this.matchService.delete(match));
        return resp;
    }

    /**
     * 匹配信息详情
     * @param erid
     * @return
     */
    @RequestMapping("/{erid}")
    public ApiResponse<Match> detail(@PathVariable("erid") Long erid) {
        ApiResponse<Match> resp = new ApiResponse<>();
        resp.setResult(this.matchService.findById(erid));
        return resp;
    }
    
}
