/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ute.griinf.iot.mqtt;

/**
 *
 * @author dordonez@ute.edu.ec
 */
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttPublisherOAuth2NoSsl {

    public static void main(String[] args) {
        int NUM_MSGS = 30;
        String broker = "tcp://localhost:1883";
        String topic = "IoT";
        String clientId = "NoSslPublisher_" + MqttClient.generateClientId();
        String USERNAME = "oauth2";
        //obtener token actualizado
        //curl -X POST --insecure --user uno:onu -d grant_type=client_credentials http://localhost:7777/oauth/token
        String TOKEN = "6722d293-c93d-431d-9199-0829cbaacab6";

        try {
            MqttClient client = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(USERNAME);
            connOpts.setPassword(TOKEN.toCharArray());
            client.connect(connOpts);
            System.out.println(clientId + " conectado al broker: " + broker);
            for(int i = 0; i < NUM_MSGS; i++) {
                String payload = "Test Mqtt NoSSL Num: " + i;
                MqttMessage msg = new MqttMessage(payload.getBytes());
                //QoS: el mensaje se envía... 0=máximo una vez ; 1=al menos una vez ; 2=exáctamente una vez ; **Rabbit convierte 2 en 1
                msg.setQos(0);
                client.publish(topic, msg);
                System.out.println("Publicado mensaje " + payload);
            }
            client.disconnect();

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}
