
package ec.edu.ute.dordonez.testrabbit02;

/**
 *
 * @author dordonez@ute.edu.ec
 */
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Mqtt_2_Kafka implements MqttCallback {

    /*
    OJO: o no envía nada o envía TODOS los argumentos
    args[0] = servidor mqtt: ip o nombre (default localhost)
    args[1] = topico mqtt (default prueba)
    args[2] = servidor kafka: ip o nombre (default localhost)
    args[3] = topico kafka (default prueba)
    */
    static Producer<String, String> producer;
    static Args args;
    public static void main(String[] a) {
        args = procesaArgs(a, new Args());
        try {
            MqttClient client = new MqttClient(args.brokerMqtt, args.clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(args.user);
            connOpts.setPassword(args.pwd);
            client.setCallback(new Mqtt_2_Kafka());
            client.connect(connOpts);
            client.subscribe(args.topicMqtt);
            System.out.println(args.clientId + " conectado al broker: " + args.brokerMqtt);
            
            Map<String, Object> producerConfig = new HashMap<>();
            producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, args.brokerKafka);
            producerConfig.put(ProducerConfig.ACKS_CONFIG, "1");
            producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            producer = new KafkaProducer<>(producerConfig);    
            System.out.println(args.clientId + " conectado a kafka en: " + args.brokerKafka);
            
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
    
    public static Args procesaArgs(String[] a, Args args) {
        //o no envía nada o envía TODOS los argumentos
        if(a != null) {
            args.brokerMqtt = "tcp://" + a[0] + ":1883";
            args.topicMqtt = a[1];
            args.brokerKafka = a[2] + ":9092";
            args.topicKafka = a[3];
        }
        return args;
    }

    @Override
    public void connectionLost(Throwable thrwbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static int msgCounter = 0;
    @Override
    public void messageArrived(String string, MqttMessage mm) throws Exception {
        String msg = new String(mm.getPayload());
        producer.send(new ProducerRecord(args.topicKafka, "key_" + msgCounter++, msg));
        System.out.println("Message transferred: " + msg);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static class Args {
        public String brokerMqtt = "tcp://localhost:1883";
        public String topicMqtt = "prueba";
        public String brokerKafka = "localhost:9092";
        public String topicKafka = "prueba";
        final String clientId = "Mqtt_2_Kafka_" + MqttClient.generateClientId();
        final String user = "admin";
        final char[] pwd = "nimda".toCharArray();
    }
    
}
