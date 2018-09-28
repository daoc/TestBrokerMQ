/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.ute.dordonez.testrabbit02;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author dordonez@ute.edu.ec
 */
public class AmqpConsumer {
    
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws java.io.IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.56.101");
        factory.setUsername("admin");
        factory.setPassword("nimda");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                //solo se requiere si autoAck = true (ver abajo en basicConsume), pero en dicho caso, es imprescindible
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        //true envía acknowledgement automáticamente, pero, si el mensaje no se logra propcesar se perderá
        //false requiere acknowledgement manual, pero, si no se procesa el mensaje, el servidor lo envía a otro, al perderse la conexión con este
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);        
    }   
}
