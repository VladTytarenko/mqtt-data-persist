package com.fractal.mqttdatapersist.mqtt;

import org.eclipse.paho.client.mqttv3.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SubscribeCallback implements MqttCallback {

    private static final long RECONNECT_INTERVAL = TimeUnit.SECONDS.toMillis(10);
    private static List<String> topicList = new ArrayList<>();
    private static String JDBC_URL = "jdbc:clickhouse://195.201.28.39:8123/default";
    private static final String SQL_INSERT = "INSERT INTO greenhouse (topic, ts, v) VALUES (?,?,?)";
    private MqttClient mqttClient;

    private PreparedStatement statement;

    public SubscribeCallback(final MqttClient mqttClient) {
        this.mqttClient = mqttClient;

        try {
            System.out.println("Connecting to db....");
            Connection conn = DriverManager.getConnection(JDBC_URL);
            statement = conn.prepareStatement(SQL_INSERT);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.exit(1);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Lost");
//        try {
//            // reconnect
//            System.out.println("W");
//            //mqttClient.connect();
//        } catch (MqttException e) {
//
//            try {
//                Thread.sleep(RECONNECT_INTERVAL);
//            } catch (InterruptedException e1) {
//                Thread.currentThread().interrupt();
//            }
//            connectionLost(e);
//        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        //System.out.println(topicList.toString());
        double temp;
        if (!topicList.contains(topic) && !topic.equals("0AA0/1001")) {
            topicList.add(topic);
            try {
                statement.setString(1, topic);
                statement.setLong(2, new Date().getTime() / 1000);
                temp = Double.parseDouble(new String(mqttMessage.getPayload()));
                if (!topic.equals("0AA0/0003") && !topic.equals("0AA0/0004")) {
                    temp /= 100.0;
                }
                statement.setDouble(3, temp);

                // persist to the database
                statement.executeUpdate();
                System.out.println("Persist topic " + topic + " " + new String(mqttMessage.getPayload()));

            } catch (SQLException e) {
                //log.error("Error while inserting", e);
            }
        }

        if (topicList.size() == 10) {
            topicList.clear();
            statement.getConnection().close();
            System.out.println("Disconnected");
            System.out.println("--------------------");
            mqttClient.disconnect();
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("DC");

    }

}
