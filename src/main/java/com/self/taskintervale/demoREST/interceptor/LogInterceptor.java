package com.self.taskintervale.demoREST.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.*;


@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler){

        log.info("Response status: {}.   Response Date: {}", response.getStatus(), Instant.now());

        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                           @NonNull Object handler, ModelAndView modelAndView){

        log.info("Response status: {}.   Response Date: {}", response.getStatus(), Instant.now());
    }
}
