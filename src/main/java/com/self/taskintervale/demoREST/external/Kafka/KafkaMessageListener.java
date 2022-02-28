package com.self.taskintervale.demoREST.external.Kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaMessageListener {


    @KafkaListener(topics = "audit", groupId = "groupIdName")
    public void listen(ConsumerRecord<String, String> consumerRecord){

        log.info("Сообщение пришло из топика: {} \nСообщение:{} ", consumerRecord.topic(), consumerRecord.value());

    }
}
