package com.youcai.guest.config;

import com.google.gson.Gson;
import com.youcai.guest.dataobject.Guest;
import com.youcai.guest.enums.ResultEnum;
import com.youcai.guest.service.impl.GuestServiceImpl;
import com.youcai.guest.utils.ResultVOUtils;
import com.youcai.guest.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private GuestServiceImpl guestService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
            .and()
            .formLogin()
                .loginProcessingUrl("/login")
                .usernameParameter("phone")
                .passwordParameter("pwd")
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");

                        ResultVO error = ResultVOUtils.error(ResultEnum.LOGIN_ERROR);
                        httpServletResponse.getWriter().write(new Gson().toJson(error));
                    }
                })
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");

                        Guest guest = (Guest) authentication.getPrincipal();
                        guest.setPwd(null);
                        ResultVO success = ResultVOUtils.success(guest);
                        httpServletResponse.getWriter().write(new Gson().toJson(success));
                    }
                })
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");

                        ResultVO success = ResultVOUtils.success("注销成功");
                        httpServletResponse.getWriter().write(new Gson().toJson(success));
                    }
                })
            .and()
            .csrf()
                .disable().exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                httpServletResponse.setContentType("application/json;charset=utf-8");

                ResultVO error = ResultVOUtils.error(ResultEnum.NO_LOGIN);
                httpServletResponse.getWriter().write(new Gson().toJson(error));
            }
        });
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(guestService);
    }
}