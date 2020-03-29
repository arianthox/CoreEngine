package com.arianthox.predictor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients
@EnableEurekaServer
@SpringBootApplication
public class CoreEngineApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoreEngineApplication.class, args);
    }
}
