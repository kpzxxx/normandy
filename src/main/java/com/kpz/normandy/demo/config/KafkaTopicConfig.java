package com.kpz.normandy.demo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.requests.CreateTopicsRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic testTopic(){
        return new NewTopic("testTopic", 5, CreateTopicsRequest.NO_REPLICATION_FACTOR);
    }
}
