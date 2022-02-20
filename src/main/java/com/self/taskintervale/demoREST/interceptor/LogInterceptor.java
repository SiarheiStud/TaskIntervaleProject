package com.self.taskintervale.demoREST.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;


@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        log.info("-------------------- preHandle LogInterceptor -------------------");
        log.info("Request method: {}   Request URL: {}", request.getMethod(), request.getRequestURL());
        String dateRequest = new SimpleDateFormat("dd.MM.yyyy  HH:mm:ss").
                format(Calendar.getInstance().getTime());
        log.info("Response Date: {}", dateRequest);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

        log.info("------------------- postHandle LogInterceptor -------------------");
        log.info("Response status: {}", response.getStatus());
        String dateRequest = new SimpleDateFormat("dd.MM.yyyy  HH:mm:ss").
                format(Calendar.getInstance().getTime());
        log.info("Response Date: {}", dateRequest);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {

        if (ex != null) {
            log.warn("Response status: {}", response.getStatus());
            log.warn(ex.getMessage());
        }
    }
}
