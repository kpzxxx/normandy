package com.kpz.normandy.demo.biz;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@EnableKafka
public class KafkaConsumer {

    @KafkaListener(topics = "testTopic")
    public void processMessage(String content) {
        System.out.println(content);
    }

}
