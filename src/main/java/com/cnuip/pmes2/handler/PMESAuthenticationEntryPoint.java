package com.cnuip.pmes2.handler;

import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * PMESAuthenticationEntryPoint
 *
 * @author: xiongwei
 * Date: 2018/1/2 下午3:54
 */
public class PMESAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private Logger logger = LoggerFactory.getLogger(PMESAuthenticationEntryPoint.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.error("AuthenticationEntryPoint: url=" + request.getRequestURL(), authException);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AntPathRequestMatcher matcher = new AntPathRequestMatcher("/api/**");
        if (matcher.matches(request)) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            int status = authentication != null ? HttpStatus.FORBIDDEN.value() : HttpStatus.UNAUTHORIZED.value();
            response.setStatus(status);
            ApiResponse resp = new ApiResponse();
            resp.setCode(status);
            resp.setMessage(authException.getMessage());
            this.objectMapper.writeValue(response.getWriter(), resp);
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
