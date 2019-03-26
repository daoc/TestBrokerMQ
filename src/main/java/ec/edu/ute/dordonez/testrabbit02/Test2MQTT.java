/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.ute.dordonez.testrabbit02;

import java.util.Properties;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.internal.security.SSLSocketFactoryFactory;

/**
 *
 * @author dordonez@ute.edu.ec
 */
public class Test2MQTT implements MqttCallback {

    public static void main(String[] args) throws MqttException {
        //Este bloque es necesario solo si el CA.crt no está incluido en la TrustStore del sistema
        System.setProperty("javax.net.ssl.trustStore", "IoT-TrustStore.jks");
        //System.setProperty("javax.net.ssl.trustStorePassword", "");//Solo poner cuando hay password
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        
//        //Este bloque es necesario solo cuando el cliente también debe autenticarse
//        System.setProperty("javax.net.ssl.keyStore", "diego.pkcs12");
//        System.setProperty("javax.net.ssl.keyStorePassword", "diegojks");
//        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
        
        //Esto no parece ser necesario
        //System.setProperty("javax.net.ssl.clientAuthentication", "true");
        
        MqttClient client = new MqttClient("ssl://pb:8883", "TestClient");
        
        /*
        Forma alternativa
        MqttConnectOptions connOpt = new MqttConnectOptions();
        connOpt.setCleanSession(true);        
        Properties sslProperties = new Properties();
        sslProperties.put(SSLSocketFactoryFactory.TRUSTSTORE, "myTrustStore.jks");
        //sslProperties.put(SSLSocketFactoryFactory.TRUSTSTOREPWD, "");
        sslProperties.put(SSLSocketFactoryFactory.TRUSTSTORETYPE, "JKS");
        sslProperties.put(SSLSocketFactoryFactory.CLIENTAUTH, true);
        sslProperties.put(SSLSocketFactoryFactory.KEYSTORE, "diego.pkcs12");
        sslProperties.put(SSLSocketFactoryFactory.KEYSTOREPWD, "diego");
        sslProperties.put(SSLSocketFactoryFactory.KEYSTORETYPE, "PKCS12");

        connOpt.setSSLProperties(sslProperties);
        client.connect(connOpt);
        */
        client.connect();
        client.subscribe("$SYS/#", 0);
        client.setCallback(new Test2MQTT());
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
