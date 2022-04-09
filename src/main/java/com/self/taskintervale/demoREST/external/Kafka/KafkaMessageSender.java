package com.self.taskintervale.demoREST.external.Kafka;

import com.self.taskintervale.demoREST.configuration.WebConfigWithKafka;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@ConditionalOnBean(value = WebConfigWithKafka.class)
@Component
public class KafkaMessageSender {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaMessageSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topicName, String message) {

        //Реализация отправки сообщения перенесена в KafkaInterceptor
        //kafkaTemplate.send(topicName, message);
    }
}
