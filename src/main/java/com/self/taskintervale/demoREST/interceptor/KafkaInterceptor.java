package com.self.taskintervale.demoREST.interceptor;

import com.self.taskintervale.demoREST.configuration.WebConfigWithKafka;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Enumeration;

@Slf4j
@ConditionalOnBean(value = WebConfigWithKafka.class)
@Component
public class KafkaInterceptor implements HandlerInterceptor {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaInterceptor(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler){

        //Только по GET запросу localhost:8080/price/ , сообщения будут отправляться в очередь "audit"
        if(request.getRequestURI().startsWith("/price/")){

            String messageToKafka = "\nip: " + getIp(request) + "\n\nHeaders:\n" + getHeadersInfo(request) +
                    "\nTime: " + LocalDateTime.now();

            log.info("Сообщение для отправки: {}", messageToKafka);
            kafkaTemplate.send("audit", messageToKafka);
        }
        return true;
    }

    //Получаем всю информацию о хэдерах в запросе
    private String getHeadersInfo(HttpServletRequest request){

        StringBuilder stringBuilder = new StringBuilder();
        Enumeration<String> headerNames =  request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            stringBuilder.append(headerName)
                    .append(" : ")
                    .append(request.getHeader(headerName))
                    .append("\n");
        }
        return stringBuilder.toString();
    }

    //получаем Ip адрес клиента
    private String getIp(HttpServletRequest request){

        String ip = request.getHeader("x-forwarded-for");
        if(ip == null){
            return request.getRemoteAddr();
        }

        log.debug("ip от прокси сервера(значение хэдера x-forwarded-for)" + ip);
        return ip;
    }
}
