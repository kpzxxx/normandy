package com.kpz.normandy.demo.biz;

import com.alibaba.fastjson2.JSON;
import lombok.Getter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class TestProducer {

    @Getter
    private final KafkaTemplate<String, String> kafkaTemplate;

    public TestProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void testSend(String data) throws ExecutionException, InterruptedException {
        CompletableFuture<SendResult<String, String>> result = kafkaTemplate.send("testTopic", data);

        System.out.println(JSON.toJSONString(result.get()));
    }

}
