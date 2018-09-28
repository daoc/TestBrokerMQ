/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.ute.dordonez.testrabbit02;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author dordonez@ute.edu.ec
 */
public class MqttSubscriber implements MqttCallback {
    
    public static void main(String[] argv) throws MqttException {
        String broker = "tcp://192.168.56.101:1883";
        String topic = "holamqtt";
        String clientId = "TestMqttSubscriber_" + MqttClient.generateClientId();
        
        MqttClient client = new MqttClient(broker, clientId);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName("admin");
        connOpts.setPassword("nimda".toCharArray());
        client.setCallback(new MqttSubscriber());
        client.connect(connOpts);
        client.subscribe(topic);
        System.out.println(clientId + " Connected to broker: " + broker);
    }

    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        System.out.println("Message received:\n\t" + new String(mqttMessage.getPayload()));
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // not used in this example
    }
}    
