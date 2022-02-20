package com.self.taskintervale.demoREST.configuration;

import com.self.taskintervale.demoREST.interceptor.LogRestTemplateInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {

        return restTemplateBuilder.additionalInterceptors(new LogRestTemplateInterceptor()).build();
    }
}
