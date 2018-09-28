/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.ute.dordonez.testrabbit02;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author dordonez@ute.edu.ec
 */
public class AmqpPublisher {
    
    private final static String EXCHANGE_NAME = "myexchange";

    public static void main(String[] argv) throws java.io.IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.56.101");
        factory.setUsername("admin");
        factory.setPassword("nimda");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        
        //un exchange publica mensajes para todos lo suscritos
        //fanout es el tipo de publicación
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
              
        //Send messages
        int NUM_MSGS = 30;
        for(int i = 0; i < NUM_MSGS; i++) {
            String message = i + " Hello World!";
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
        channel.close();
        connection.close();
    }   
}
