/*
 * @author Сергей Студенков
 * @since "09.04.2022, 14:07"
 * @version V 1.0.0
 */

package com.self.taskintervale.demoREST.configuration;

import com.self.taskintervale.demoREST.interceptor.LogInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnMissingBean(value = {WebConfigWithKafka.class})
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor());
    }
}
