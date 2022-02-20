package com.self.taskintervale.demoREST.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;


@Slf4j
@Component
public class LogRestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        log.info("------------------RestTemplateLogInterceptor------------------------");
        log.info("Request method: {}", request.getMethodValue());
        log.info("Request URI: {}", request.getURI());
        log.info("Request Headers: {}", request.getHeaders());

        ClientHttpResponse response = execution.execute(request, body);
        log.info("Response status code: {}", response.getStatusCode().value());
        HttpHeaders headers = response.getHeaders();
        String dateResponse = new SimpleDateFormat("dd.MM.yyyy  HH:mm:ss").format(headers.getDate());
        log.info("Response date: {}", dateResponse);

        return response;
    }
}
