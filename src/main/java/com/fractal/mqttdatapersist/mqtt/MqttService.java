package com.fractal.mqttdatapersist.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface MqttService {

    MqttClient getMqttConnection() throws MqttException;
    void mqttRead();

}
