/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myproject.queue;
import java.io.IOException;
import java.util.Properties;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

/**
 *
 * @author nasretdinova.u.k
 */
public class Consumer {
    
    private String urlBroker;
    private String userName;
    private String password;
    private String queueName;
    
    public MessageConsumer consumer;
    public Connection connection;
    
    public Consumer() {
        this.urlBroker = "localhost";
        this.userName = "admin";
        this.password = "admin";
        this.queueName = "from.test.queue";
    }
    
    public void init(String pathConfig) {
        Properties props = new Properties();
        
        try {
            props.load(ClassLoader.getSystemResourceAsStream(pathConfig));
        } catch ( IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
        
        props.forEach((Object key,Object value) -> {
            if ("".equals(key.toString()) || "".equals(value.toString()))
                throw new NullPointerException("Error: settings for a pair " +
                                               "are not filled key: " + key +
                                               " value: " + value);
        });
        
        this.urlBroker = props.getProperty("url");
        this.userName = props.getProperty("user");
        this.password = props.getProperty("password");
        this.queueName = props.getProperty("queue");
    }
    
    public void start() throws JMSException {
        Session session;
        Queue queue;
        
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();

        connectionFactory.setBrokerURL(this.urlBroker);
        
        this.connection = connectionFactory.createConnection(this.userName, this.password);
        session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        queue = session.createQueue(this.queueName);
        
        this.consumer = session.createConsumer(queue);
        
        try {
            this.connection.start();
        } catch (JMSException e) {
            System.err.println("Exception occurred: " + e.toString());
        }
    }
    
    public void recive(MessageListener listener) throws JMSException, IOException, InterruptedException {
        while (true) {
            this.consumer.setMessageListener(listener);
        }
    }
    public void stop() {
        if (this.connection != null) {
            try {
                this.connection.close();
                
            } catch (JMSException e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }
    }
}
