package com.fractal.mqttdatapersist.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;

@Service
public class MqttServiceImpl implements MqttService {

    private final static String PROPERTIES_FILE_NAME = "/mqtt.properties";
    Properties props = new Properties();

    @Override
    public MqttClient getMqttConnection() throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            props.load(MqttServiceImpl.class.getResourceAsStream(PROPERTIES_FILE_NAME));
        } catch (IOException e) {
            System.err.println("Not able to read the properties file, exiting..");
            System.exit(-1);
        }
        System.out.println("Connecting to MQTT broker with the following parameters: - BROKER_URL=" + props.getProperty("BROKER_URL")+" CLIENT_ID="+props.getProperty("CLIENT_ID"));
        MqttClient sampleClient = new MqttClient(props.getProperty("BROKER_URL"), props.getProperty("CLIENT_ID"),persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        sampleClient.connect(connOpts);

        System.out.println("Connection finish");
        return sampleClient;
    }

    @Override
    public void mqttRead() {
        try {
            MqttClient sampleClient = getMqttConnection();
            sampleClient.subscribe(props.getProperty("TOPIC_NAME"), 1);
            sampleClient.setCallback(new SubscribeCallback(sampleClient));
        } catch(MqttException me){
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("except " + me);
            me.printStackTrace();
        }
    }



}
