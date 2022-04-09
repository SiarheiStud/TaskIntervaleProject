package com.self.taskintervale.demoREST.configuration;

import com.self.taskintervale.demoREST.interceptor.KafkaInterceptor;
import com.self.taskintervale.demoREST.interceptor.LogInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@ConditionalOnProperty(value = "application.kafka-conditional.enabled", havingValue = "true", matchIfMissing = false)
public class WebConfigWithKafka implements WebMvcConfigurer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public WebConfigWithKafka(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LogInterceptor());
        registry.addInterceptor(new KafkaInterceptor(kafkaTemplate));
    }
}
