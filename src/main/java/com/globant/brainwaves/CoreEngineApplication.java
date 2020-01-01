package com.globant.brainwaves;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaClient
@EnableEurekaServer
@SpringBootApplication
public class CoreEngineApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoreEngineApplication.class, args);
    }
}
