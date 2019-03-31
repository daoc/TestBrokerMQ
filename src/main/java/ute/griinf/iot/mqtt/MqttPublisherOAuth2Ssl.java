/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ute.griinf.iot.mqtt;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author dordonez@ute.edu.ec
 */
public class MqttPublisherOAuth2Ssl implements MqttCallback {
	private static final String REGISTRY_URL = "https://localhost:8761/eureka/apps/";
	private static final String OAUTH2_ID = "IOT.OAUTH2.UAA";
	private static final String BROKER_ID = "IOT.SRVC.MQTT.BROKER";
	
    public static void main(String[] args) throws MqttException {
    	MqttPublisherOAuth2Ssl conn = new MqttPublisherOAuth2Ssl();
    	conn.initSsl();
    	String oauthUrl = conn.getServiceUrl(REGISTRY_URL, OAUTH2_ID, "HTTP");
    	String token = conn.getOAuth2Token(oauthUrl);
    	String mqttBrokerUrl = conn.getServiceUrl(REGISTRY_URL, BROKER_ID, "MQTT");
    	MqttClient client = conn.connect(mqttBrokerUrl, token);
//        int NUM_MSGS = 30;
//        String topic = "IoT";
//
//        try {
//            MqttClient client = new MqttClient(broker, clientId);
//            MqttConnectOptions connOpts = new MqttConnectOptions();
//            connOpts.setUserName(USERNAME);
//            connOpts.setPassword(TOKEN.toCharArray());
//            client.connect(connOpts);
//            System.out.println(clientId + " conectado al broker: " + broker);
//            for(int i = 0; i < NUM_MSGS; i++) {
//                String payload = "Test Mqtt SSL Num: " + i;
//                MqttMessage msg = new MqttMessage(payload.getBytes());
//                //QoS: el mensaje se envía...
//                //0=máximo una vez ; 1=al menos una vez ; 2=exáctamente una vez ;
//                msg.setQos(0);
//                client.publish(topic, msg);
//                System.out.println("Publicado mensaje " + payload);
//            }
//            client.disconnect();
//
//        } catch (MqttException me) {
//            System.out.println("reason " + me.getReasonCode());
//            System.out.println("msg " + me.getMessage());
//            System.out.println("loc " + me.getLocalizedMessage());
//            System.out.println("cause " + me.getCause());
//            System.out.println("excep " + me);
//            me.printStackTrace();
//        }
    }
    
    public void initSsl() {
        //Este bloque es necesario solo si el CA.crt no está incluido en la TrustStore del sistema
        System.setProperty("javax.net.ssl.trustStore", "Client_GrIInf_TrustStore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "griinf");//Solo poner cuando hay password
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");    	
    }

    public String getServiceUrl(String registryUrl, String serviceId, String protocol) {
    	try {
			URL url = new URL(registryUrl + serviceId);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.addRequestProperty("Accept", "application/json");
			conn.connect();
			JSONObject json = new JSONObject();
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(conn.getInputStream());
			Element application = (Element) document.getElementsByTagName("application").item(0);
			Element instance = (Element) application.getElementsByTagName("instance").item(0);
			String host = instance.getElementsByTagName("hostname").item(0).getTextContent();
			System.out.println(host);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String broker = "ssl://localhost:8883";
    	return null;
    }
    
    public String getOAuth2Token(String oauthUrl) {
    	
    	return null;
    }
    
    public MqttClient connect(String mqttBrokerUrl, String token) {
        
        String topic = "IoT";
        String USERNAME = "oauth2";
        //obtener token actualizado
        //curl -X POST --insecure --user uno:onu -d grant_type=client_credentials https://localhost:8762/oauth/token
        MqttClient client;
		try {
			client = new MqttClient(mqttBrokerUrl, "SslPublisher_" + MqttClient.generateClientId());
	        MqttConnectOptions connOpts = new MqttConnectOptions();
	        connOpts.setUserName(USERNAME);
	        connOpts.setPassword(token.toCharArray());
	        client.connect(connOpts);
	        System.out.println(client.getClientId() + " conectado al broker: " + mqttBrokerUrl);    	
	    	return client;			
		} catch (MqttException e) {
			e.printStackTrace();
			return null;
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
