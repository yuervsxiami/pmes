package com.cnuip.pmes2.handler;

import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * PMESAccessDeniedHandler
 *
 * @author: xiongwei
 * Date: 2018/1/2 下午3:13
 */
public class PMESAccessDeniedHandler implements AccessDeniedHandler {
    private Logger logger = LoggerFactory.getLogger(PMESAccessDeniedHandler.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        logger.error("accessDeniedException: url=" + request.getRequestURL(), accessDeniedException);
        AntPathRequestMatcher matcher = new AntPathRequestMatcher("/api/**");
        if (matcher.matches(request)) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            ApiResponse resp = new ApiResponse();
            resp.setCode(HttpStatus.UNAUTHORIZED.value());
            resp.setMessage(accessDeniedException.getMessage());
            this.objectMapper.writeValue(response.getWriter(), resp);
        } else {
            response.sendRedirect("/login");
        }
    }
}
