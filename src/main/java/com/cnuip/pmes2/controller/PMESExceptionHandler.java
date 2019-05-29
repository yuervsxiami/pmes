package com.cnuip.pmes2.controller;


import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * ApiExceptionHandler
 *
 * @author: xiongwei
 * Date: 2017/5/11 下午2:18
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Controller
@RequestMapping("/error")
public class PMESExceptionHandler extends AbstractErrorController {

    private final Logger logger = LoggerFactory.getLogger(PMESExceptionHandler.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${server.error.path:${error.path:/error}}")
    private static String errorPath = "/error";

    @Autowired
    private ResourceUrlProvider resourceUrlProvider;

    public PMESExceptionHandler(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @Override
    public String getErrorPath() {
        return errorPath;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView restErrorHandler(HttpServletRequest req, HttpServletResponse response, Exception ex) throws Exception {
        logger.error("restErrorHandler url=" + req.getRequestURI(), ex);
        AntPathRequestMatcher matcher = new AntPathRequestMatcher("/api/**");
        if (matcher.matches(req)) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            ApiResponse resp = new ApiResponse();
            HttpStatus status = getStatus(req);
            resp.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage(status.getReasonPhrase());
            resp.setError(ex);
            resp.setDetailMessage(ex.getLocalizedMessage());
            this.objectMapper.writeValue(response.getWriter(), resp);
            return null;
        }
        Map<String, Object> model = new HashMap<>();
        model.put("urls", this.resourceUrlProvider);
        model.put("exception", ex);
        return new ModelAndView("error", model);
    }

}
