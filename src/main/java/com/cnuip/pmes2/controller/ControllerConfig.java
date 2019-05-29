package com.cnuip.pmes2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import javax.servlet.http.HttpServletRequest;

/**
 * ControllerConfig
 *
 * @author: xiongwei
 * Date: 2017/12/20 下午3:18
 */
@ControllerAdvice
public class ControllerConfig {

    @Value("${spring.application.name}")
    private String appName;
    @Autowired
    private ResourceUrlProvider resourceUrlProvider;

    @ModelAttribute("urls")
    public ResourceUrlProvider urls() {
        return this.resourceUrlProvider;
    }

    @ModelAttribute("ctx")
    public String contextPath(HttpServletRequest request) {
        return this.resourceUrlProvider.getUrlPathHelper().getContextPath(request);
    }

    @ModelAttribute("appName")
    public String appName() {
        return this.appName;
    }
}
