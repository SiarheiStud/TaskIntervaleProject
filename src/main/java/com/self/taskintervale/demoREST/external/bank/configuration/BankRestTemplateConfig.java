package com.self.taskintervale.demoREST.external.bank.configuration;


import com.self.taskintervale.demoREST.interceptor.LogRestTemplateInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BankRestTemplateConfig {

    @Value(value = "${bank.base-url}")
    private String baseUrl;

    @Bean(name = "bankRestTemplate")
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {

        return restTemplateBuilder.rootUri(baseUrl)
                .additionalInterceptors(new LogRestTemplateInterceptor())
                .build();
    }
}