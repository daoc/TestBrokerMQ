/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.ute.dordonez.testrabbit02;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;
import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLContextSpi;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.PrivateKeyDetails;
import org.apache.http.ssl.PrivateKeyStrategy;
import org.apache.http.ssl.SSLContexts;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

/**
 *
 * @author dordonez@ute.edu.ec
 */
public class SslMqttSubscriber {

    public static void main(String[] args) throws IOException, MqttException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
//        String host = "192.168.56.101";
//        Integer port = 8883;
//        SocketFactory sslsocketfactory = SSLSocketFactory.getDefault();
//        SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(host, port);
        
//        String broker = "ssl://192.168.56.101:8883";
//        String topic = "IoT";
//        String clientId = "TestMqttPublisher_" + MqttClient.generateClientId();

        KeyStore identityKeyStore = KeyStore.getInstance("jks");
        FileInputStream identityKeyStoreFile = new FileInputStream(new File("mqttClientIdStore.jks"));
        identityKeyStore.load(identityKeyStoreFile, "mqttClientJksPwd".toCharArray());

        KeyStore trustKeyStore = KeyStore.getInstance("jks");
        FileInputStream trustKeyStoreFile = new FileInputStream(new File("mqttClient.jks"));
        trustKeyStore.load(trustKeyStoreFile, "mqttClientJksPwd".toCharArray());
        
      SSLContext sslContext = SSLContexts.custom()
          // load identity keystore
          .loadKeyMaterial(identityKeyStore, "mqttClientJksPwd".toCharArray(), new PrivateKeyStrategy() {
              @Override
              public String chooseAlias(Map<String, PrivateKeyDetails> aliases, Socket socket) {
                  return "mqttClient";
              }
          })
          // load trust keystore
          .loadTrustMaterial(trustKeyStore, null)
          .build();
      
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
          new String[]{"TLSv1.2", "TLSv1.1"},
          null,
          SSLConnectionSocketFactory.getDefaultHostnameVerifier());
      
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName("ute1");
        connOpts.setPassword("1ute".toCharArray());
        //options.setSocketFactory(sslContext.getSocketFactory());
//        connOpts.setSocketFactory(sslConnectionSocketFactory);
//        client.connect(connOpts);         
        
        CloseableHttpClient client = HttpClients.custom()
          .setSSLSocketFactory(sslConnectionSocketFactory)
          .build();        
        
// Call a SSL-endpoint
//      callEndPoint (client, "https://www.example.com/endpoint", 
//          new JSONObject()
//          .put("param1", "value1")
//          .put("param2", "value2")
//          );        
        
//        MqttClient client = new MqttClient("ssl://localhost:8883", "paho-java-1");
//        MqttConnectOptions opts = new MqttConnectOptions();
//
//        final char[] passphrase = "bunnies".toCharArray();
//
//        // client key
//        KeyStore ks = KeyStore.getInstance("PKCS12");
//        ks.load(new FileInputStream("C:\\Users\\dordonez\\Documents\\cygwin64\\home\\dordonez\\mqttClient.key"), null);
//
//        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
//        kmf.init(ks, passphrase);
//
//        // server certificate
//        KeyStore tks = KeyStore.getInstance("JKS");
//        // created the key store with
//        // keytool -importcert -alias rmq -file ./server_certificate.pem -keystore ./jvm_keystore
//        tks.load(new FileInputStream("/Users/antares/Tools/rabbitmq/tls/jvm_keystore"), passphrase);
//
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
//        tmf.init(tks);
//
//        SSLContext ctx = SSLContext.getInstance("SSLv3");
//        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
//        opts.setSocketFactory(ctx.getSocketFactory());
//
//        client.connect(opts);        

        
//        MqttClient client = new MqttClient(broker, MqttClient.generateClientId());        
//        
//        MqttConnectOptions connOpts = new MqttConnectOptions();
//        connOpts.setUserName("ute1");
//        connOpts.setPassword("1ute".toCharArray());
//        //options.setSocketFactory(sslContext.getSocketFactory());
//        connOpts.setSocketFactory(SSLContext.getDefault().getSocketFactory());
//        client.connect(connOpts);        
        
//        InputStream in = sslsocket.getInputStream();
//        
//        OutputStream os = sslsocket.getOutputStream();
//
//        os.write(1);
//        while (in.available() > 0) {
//            System.out.print(in.read());
//        }

        System.out.println(" OJO Secured connection performed successfully");
    }
    
private static void callEndPoint (CloseableHttpClient aHTTPClient, String aEndPointURL, JSONObject aPostParams) {
 
    try {
      System.out.println("Calling URL: " + aEndPointURL);
        HttpPost post = new HttpPost(aEndPointURL);
      post.setHeader("Accept", "application/json");
      post.setHeader("Content-type", "application/json");
      
        StringEntity entity = new StringEntity(aPostParams.toString());
      post.setEntity(entity);
      
      System.out.println("**POST** request Url: " + post.getURI());
      System.out.println("Parameters : " + aPostParams);
      
        HttpResponse response = aHTTPClient.execute(post);
  
      int responseCode = response.getStatusLine().getStatusCode();
      System.out.println("Response Code: " + responseCode);
      System.out.println("Content:-\n");
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      String line = "";
      while ((line = rd.readLine()) != null) {
          System.out.println(line);
      }
    } catch (Exception ex) {
      System.out.println("Boom, we failed: " + ex);
      ex.printStackTrace();
    }
    
  }      
    
}
