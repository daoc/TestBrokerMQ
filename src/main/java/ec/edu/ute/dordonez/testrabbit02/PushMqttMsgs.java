
package ec.edu.ute.dordonez.testrabbit02;

/**
 *
 * @author dordonez@ute.edu.ec
 */
import java.util.Random;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class PushMqttMsgs {

    /*
    OJO: o no envía nada o envía TODOS los argumentos
    args[0] = servidor: ip o nombre (default localhost)
    args[1] = topico (default prueba)
    args[2] = int número de mensajes a enviar (default 100)
    args[3] = int tamaño del mensaje en caracteres (default 100)
    */
    public static void main(String[] a) {
        Args args = procesaArgs(a, new Args());
        try {
            MqttClient client = new MqttClient(args.broker, args.clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(args.user);
            connOpts.setPassword(args.pwd);
            client.connect(connOpts);
            System.out.println(args.clientId + " conectado al broker: " + args.broker);
            for(int i = 0; i < args.numMsgs; i++) {
                String payload = String.format("MsgId:%d;ClientId:%s;;%s", i, args.clientId, buildMsg(args.sizeMsgs));
                MqttMessage msg = new MqttMessage(payload.getBytes());
                //QoS: el mensaje se envía... 0=máximo una vez ; 1=al menos una vez ; 2=exáctamente una vez ; **Rabbit convierte 2 en 1
                msg.setQos(0);
                client.publish(args.topic, msg);
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
    
    public static Args procesaArgs(String[] a, Args args) {
        //o no envía nada o envía TODOS los argumentos
        if(a != null) {
            args.broker = "tcp://" + a[0] + ":1883";
            args.topic = a[1];
            args.numMsgs = Integer.parseInt(a[2]);
            args.sizeMsgs = Integer.parseInt(a[3]);
        }
        return args;
    }
    
    public static String buildMsg(int sizeMsg) {
        Random rnd = new Random();
        StringBuilder msg = new StringBuilder(sizeMsg);
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        char space = ' ';
        int numChars = chars.length;
        int probSpace = 5;
        for(int i = 0; i < sizeMsg; i++) {
            if(rnd.nextInt(probSpace) == 0) {
                msg.append(space);
            } else {
                msg.append(chars[rnd.nextInt(numChars)]);
            }
        }
        return msg.toString();
    }
    
    public static class Args {
        public String broker = "tcp://localhost:1883";
        public String topic = "prueba";
        public int numMsgs = 100;
        public int sizeMsgs = 100;
        final String clientId = "PushMqttMsgs_" + MqttClient.generateClientId();
        final String user = "admin";
        final char[] pwd = "nimda".toCharArray();
    }
    
}
