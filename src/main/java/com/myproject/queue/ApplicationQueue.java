package com.myproject.queue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.jms.*;

/**
 *
 * @author nasretdinova.u.k
 */
public class ApplicationQueue {

    /**
     * @param args the command line arguments
     * @throws javax.jms.JMSException
     */
    public static void main(String[] args) throws JMSException, Exception {
        final String pathConfigConsumer = "appqueue/consumer.properties";
        final String pathConfigProducer = "appqueue/producer.properties";
        
        MessageProcessorImpl processor = new MessageProcessorImpl();
        processor.init(pathConfigConsumer, pathConfigProducer);
        processor.start();


        try {
            processor.start();            
        } catch (JMSException e) {
            System.err.println("Exception occurred: " + e.toString());
        }
        finally {
            processor.stop();
        }
    }
    
}
