/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ute.griinf.iot.mqtt;

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
public class MqttSubscriberSsl implements MqttCallback {
    
    public static void main(String[] argv) throws MqttException {
        //Este bloque es necesario solo si el CA.crt no está incluido en la TrustStore del sistema
        System.setProperty("javax.net.ssl.trustStore", "Client_GrIInf_TrustStore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "griinf");//Solo poner cuando hay password
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        
        String broker = "ssl://localhost:8883";
        String topic = "IoT"; //"$SYS/#";//SYS no parecen funcionar en moquette
        String clientId = "SslSubscriber_" + MqttClient.generateClientId();
        String USERNAME = "testuser";
        String PASSWORD = "passwd";
        
        MqttClient client = new MqttClient(broker, clientId);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName(USERNAME);
        connOpts.setPassword(PASSWORD.toCharArray());
        client.setCallback(new MqttSubscriberSsl());
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

    public void deliveryComplete(IMqttDeliveryToken imdt) {
    	System.out.println("Delivery complete:\n\t" + imdt.isComplete());
    }
}    
