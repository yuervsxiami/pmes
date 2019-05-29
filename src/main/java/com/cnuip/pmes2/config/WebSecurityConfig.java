package com.cnuip.pmes2.config;

import com.cnuip.pmes2.handler.PMESAccessDeniedHandler;
import com.cnuip.pmes2.handler.PMESAuthenticationEntryPoint;
import com.cnuip.pmes2.service.UserService;
import com.cnuip.pmes2.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by xiongwei on 2017/3/4.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PMESAccessDeniedHandler accessDeniedHandler() {
        return new PMESAccessDeniedHandler();
    }

    @Bean
    public PMESAuthenticationEntryPoint authenticationEntryPoint() {
        return new PMESAuthenticationEntryPoint();
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/app/**",
                "/webjarslocator/**", "/static/**", "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/error", "/loginv", "/user", "/api/v1/**").permitAll()
                .antMatchers("/druid/**").authenticated()
                .anyRequest().authenticated().and()
                .formLogin()
                    .loginPage("/login").defaultSuccessUrl("/").failureUrl("/login?error")
                    .permitAll().and()
                .logout()
                    .permitAll().and()
                .rememberMe()
                    .key("pmes2")
                    .rememberMeParameter("rememberMe")
                    .tokenValiditySeconds(5 * 24 * 3600)
                    .and()
                .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint())
                    .accessDeniedHandler(accessDeniedHandler());
    }

}
