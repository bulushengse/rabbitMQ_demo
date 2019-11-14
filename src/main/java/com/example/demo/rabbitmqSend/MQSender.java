package com.example.demo.rabbitmqSend;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQSender {
	
	@Autowired
    private RabbitTemplate  rabbitTemplate;
 
	
	/**
	 * 测试
	 * direct模式，一对一：
	 * 
	 * topic模式，一对多：
	 * 
	 * fanout模式，广播：
	 * 
	 */
	
	
	
    public void sendDirectMsg(Object object){
    	//消息唯一id
    	String messageId = String.valueOf((int)(Math.random() * 100000));
    	CorrelationData correlationData = new CorrelationData(messageId);
		//correlationData.setId(messageId);
		//消息的其他属性
		TenantMessageProcessor messagePostProcessor = new TenantMessageProcessor(messageId);
		//messagePostProcessor.setMessageId(messageId);
		System.out.println("=============sendDirectMsg:"+object.toString());
    	rabbitTemplate.convertAndSend("direct_exchange","test_key",object,messagePostProcessor,correlationData);
    }
    
    public void sendTopicMsg(Object object){
    	//消息唯一id
    	String messageId = String.valueOf((int)(Math.random() * 100000));
    	CorrelationData correlationData = new CorrelationData(messageId);
		//消息的其他属性
		TenantMessageProcessor messagePostProcessor = new TenantMessageProcessor(messageId);
		System.out.println("=============sendTopicMsg:"+object.toString());
    	rabbitTemplate.convertAndSend("topic_exchange","pattern1.1.1",object,messagePostProcessor,correlationData);
    	//rabbitTemplate.convertAndSend("topic_exchange","pattern2.1.1",object,messagePostProcessor,correlationData);
    }
    
    public void sendFanoutMsg(Object object){
    	//消息唯一id
    	String messageId = String.valueOf((int)(Math.random() * 100000));
    	CorrelationData correlationData = new CorrelationData(messageId);
		//消息的其他属性
		TenantMessageProcessor messagePostProcessor = new TenantMessageProcessor(messageId);
		System.out.println("=============sendFanoutMsg:"+object.toString());
    	rabbitTemplate.convertAndSend("fanout_exchange","",object,messagePostProcessor,correlationData);
    }
}
