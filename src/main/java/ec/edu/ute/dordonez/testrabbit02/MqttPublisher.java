/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.ute.dordonez.testrabbit02;

/**
 *
 * @author dordonez@ute.edu.ec
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Properties;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPublisher {

    public static void main(String[] args) throws MqttException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, FileNotFoundException, IOException, CertificateException {
        String keyStoreFile = "C:\\Users\\dordonez\\GitHub\\ApDist\\JmsOpenMq\\mqttkeystore";
        System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\dordonez\\GitHub\\ApDist\\JmsOpenMq\\mqttkeystore");
        int NUM_MSGS = 30;
        String broker = "ssl://192.168.56.101:8883";
        String topic = "IoT";
        String clientId = "TestMqttPublisher_" + MqttClient.generateClientId();


        
        MqttClient client = new MqttClient(broker, MqttClient.generateClientId(), new MemoryPersistence());

        SSLContext sslContext = SSLContext.getInstance("SSL");
        //SSLContext.getDefault().getSocketFactory();
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream keyStoreData = new FileInputStream(keyStoreFile);
        keyStore.load(keyStoreData, "changeit".toCharArray());
        trustManagerFactory.init(keyStore);
        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

        MqttConnectOptions options = new MqttConnectOptions();
        //options.setSocketFactory(sslContext.getSocketFactory());
        options.setSocketFactory(SSLContext.getDefault().getSocketFactory());
        client.connect(options);
        
//        try {
//            MqttClient client = new MqttClient(broker, clientId);
//            MqttConnectOptions connOpts = new MqttConnectOptions();
//            connOpts.setUserName("ute1");
//            connOpts.setPassword("1ute".toCharArray());
////            Properties props = new Properties();
////            props.setProperty("com.ibm.ssl.keyStore", "C:\\Users\\dordonez\\GitHub\\ApDist\\JmsOpenMq\\mqttkeystore");
////            props.setProperty("com.ibm.ssl.keyStorePassword","changeit");            
////            connOpts.setSSLProperties(props);
//            connOpts.setSocketFactory(SSLSocketFactory.getDefault());
//            client.connect(connOpts);
//            System.out.println(clientId + " conectado al broker: " + broker);
//            for(int i = 0; i < NUM_MSGS; i++) {
//                String payload = "Test Mqtt Num: " + i;
//                MqttMessage msg = new MqttMessage(payload.getBytes());
//                //QoS: el mensaje se envía... 0=máximo una vez ; 1=al menos una vez ; 2=exáctamente una vez ; **Rabbit convierte 2 en 1
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
}
