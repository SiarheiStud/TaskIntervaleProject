package com.self.taskintervale.demoREST.external.Kafka;

import com.self.taskintervale.demoREST.configuration.WebConfigWithKafka;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@ConditionalOnBean(value = WebConfigWithKafka.class)
@Component
public class KafkaMessageListener {


    @KafkaListener(topics = "audit", groupId = "groupIdName")
    public void listen(ConsumerRecord<String, String> consumerRecord){

        log.info("Сообщение пришло из топика: {} \nСообщение:{} ", consumerRecord.topic(), consumerRecord.value());

    }
}
