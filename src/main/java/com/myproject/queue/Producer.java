/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myproject.queue;

import java.io.IOException;
import java.util.Properties;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

/**
 *
 * @author nasretdinova.u.k
 */
public class Producer {

    private String urlBroker;
    private String password;
    private String userName;
    private String queueName;
    private String queueNameCopy;
    
    public MessageProducer producer;
    public MessageProducer producerAdd;
    
    public Connection connection;
    
    public Producer() {
        this.urlBroker = "localhost";
        this.userName = "admin";
        this.password = "admin";
        this.queueName = "to.test.queue";
        this.queueNameCopy = "to.test.copy.queue";
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
        this.queueNameCopy = props.getProperty("queue-copy");
    }
    
    public void start() throws JMSException {
        Session session;      
        Queue queue; 
        //String listQueueName;
        
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();

        connectionFactory.setBrokerURL(urlBroker);
        
        this.connection = connectionFactory.createConnection(this.userName, this.password);
        
        session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        /*
            it doesn't work for Artemis ActiveMQ 
            listQueueName = this.queueName + "," + this.queueNameCopy;
            queue = new ActiveMQQueue(listQueueName);
        */
        
        queue = new ActiveMQQueue(this.queueName);
        
        this.producer = session.createProducer(queue);
        
        queue = new ActiveMQQueue(this.queueNameCopy);
        
        this.producerAdd = session.createProducer(queue);
    }
    
    public void send(Message message) throws JMSException {
        this.producer.send(message);
        this.producerAdd.send(message);
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
