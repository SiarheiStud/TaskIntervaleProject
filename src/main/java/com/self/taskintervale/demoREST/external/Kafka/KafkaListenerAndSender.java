package com.self.taskintervale.demoREST.external.Kafka;

import com.self.taskintervale.demoREST.configuration.WebConfigWithKafka;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@ConditionalOnBean(value = WebConfigWithKafka.class)
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
