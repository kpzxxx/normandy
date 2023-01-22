package com.kpz.normandy.demo.controller;

import com.alibaba.fastjson2.JSON;
import com.kpz.normandy.demo.biz.TestProducer;
import com.kpz.normandy.demo.dao.LocationMapper;
import com.kpz.normandy.demo.model.Location;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private LocationMapper locationMapper;

    @Resource
    private TestProducer testProducer;

    @GetMapping("/location")
    public String test(@RequestParam(value = "id") Long id){

        Location location = locationMapper.getById(id);

        return JSON.toJSONString(location);
    }

    @GetMapping("/testKafka")
    public String testKafka(@RequestParam(value = "value") String data){

        try {
            testProducer.testSend(data);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return "Success!";
    }
}
