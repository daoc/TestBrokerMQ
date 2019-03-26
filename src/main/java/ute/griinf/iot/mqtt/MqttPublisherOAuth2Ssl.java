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
public class MqttPublisherOAuth2Ssl implements MqttCallback {

    public static void main(String[] args) throws MqttException {
        //Este bloque es necesario solo si el CA.crt no está incluido en la TrustStore del sistema
        System.setProperty("javax.net.ssl.trustStore", "Client_GrIInf_TrustStore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "griinf");//Solo poner cuando hay password
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
          
        int NUM_MSGS = 30;
        String broker = "ssl://localhost:8883";
        String topic = "IoT";
        String clientId = "SslPublisher_" + MqttClient.generateClientId();
        String USERNAME = "oauth2";
        //obtener token actualizado
        //curl -X POST --insecure --user uno:onu -d grant_type=client_credentials http://localhost:7777/oauth/token
        String TOKEN = "1fcbec2e-eb10-4cbf-8899-d3becf61c242";

        try {
            MqttClient client = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(USERNAME);
            connOpts.setPassword(TOKEN.toCharArray());
            client.connect(connOpts);
            System.out.println(clientId + " conectado al broker: " + broker);
            for(int i = 0; i < NUM_MSGS; i++) {
                String payload = "Test Mqtt SSL Num: " + i;
                MqttMessage msg = new MqttMessage(payload.getBytes());
                //QoS: el mensaje se envía...
                //0=máximo una vez ; 1=al menos una vez ; 2=exáctamente una vez ;
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

    @Override
    public void connectionLost(Throwable thrwbl) {
        System.out.println("Connection to MQTT broker lost!");
    }

    @Override
    public void messageArrived(String string, MqttMessage mm) throws Exception {
        System.out.println("Message received:\n\t" + new String(mm.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
    	System.out.println("Delivery complete:\n\t" + imdt.isComplete());
    }
}
