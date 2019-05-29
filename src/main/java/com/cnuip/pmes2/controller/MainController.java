package com.cnuip.pmes2.controller;

import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * MainController
 *
 * @author: xiongwei
 * Date: 2017/12/20 下午4:16
 */
@Controller
public class MainController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 登陆页面
     * @return
     */
    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    /**
     * 登陆页面
     * @return
     */
    @RequestMapping(value="/loginv", method = RequestMethod.GET)
    public String loginV() {
        return "login_v";
    }

    /**
     * 当前登陆用户
     * @return
     */
    @RequestMapping(value="/api/user", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<User> currentUser() {
        ApiResponse<User> resp = new ApiResponse<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            resp.setResult((User)authentication.getPrincipal());
        }
        return resp;
    }

}
