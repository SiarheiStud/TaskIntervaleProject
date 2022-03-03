package com.self.taskintervale.demoREST.external.Kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaListenerAndSender {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaListenerAndSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @KafkaListener(topics = "audit", groupId = "groupIdName")
    public void listen(ConsumerRecord<String, String> consumerRecord){

        kafkaTemplate.send("audit-reply", consumerRecord.value());
        log.info("Сообщение отправлено в топик audit-reply");

    }
}
