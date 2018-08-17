package com.fractal.mqttdatapersist.schedule;

import com.fractal.mqttdatapersist.mqtt.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class TransferServiceImpl {

    @Autowired
    private MqttService mqttService;

    @Scheduled(cron = "0 * * * * *")
    public void getFromMqttAndSave() {
        mqttService.mqttRead();
        //mqttService.closeConnection();
    }
}
