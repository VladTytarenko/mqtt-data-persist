package com.fractal.mqttdatapersist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MqttDataPersistApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqttDataPersistApplication.class, args);
    }
}
