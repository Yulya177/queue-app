/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myproject.queue;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author nasretdinova.u.k
 */
public class MessageProcessorImpl implements MessageListener{
    public Consumer mainConsumer;
    public Producer mainProducer;
    
    public MessageProcessorImpl() {
        this.mainConsumer = new Consumer();
        this.mainProducer = new Producer();
    }
    
    public void init(String pathConfigConsumer, String pathConfigProducer) {
        this.mainConsumer.init(pathConfigConsumer);
        this.mainProducer.init(pathConfigProducer);
    }
    
    public void start() throws JMSException, IOException, InterruptedException {
        this.mainConsumer.start();
        this.mainProducer.start();
        
        this.mainConsumer.recive(this);
    }

    public void stop() {
        this.mainConsumer.stop();
        this.mainProducer.stop();
    }
    
    @Override
    public void onMessage(Message msg) {
        try {
            String messageBody = (((TextMessage)msg).getText());
            if ("".equals(messageBody))
                throw new NullPointerException("Error: message is empty");
            
            this.mainProducer.send(msg);
        } catch (JMSException ex) {
            this.stop();
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
