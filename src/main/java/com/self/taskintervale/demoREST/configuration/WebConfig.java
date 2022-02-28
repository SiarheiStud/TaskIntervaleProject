package com.self.taskintervale.demoREST.configuration;

import com.self.taskintervale.demoREST.interceptor.KafkaInterceptor;
import com.self.taskintervale.demoREST.interceptor.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public WebConfig(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LogInterceptor());
        registry.addInterceptor(new KafkaInterceptor(kafkaTemplate));
    }
}
