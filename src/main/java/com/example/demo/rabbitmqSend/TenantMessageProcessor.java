package com.example.demo.rabbitmqSend;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

public class TenantMessageProcessor implements MessagePostProcessor{

	private String messageId;
	
	public TenantMessageProcessor() {
		
	}
	public TenantMessageProcessor(String messageId) {
		this.messageId=messageId;
	}
	
	@Override
	public Message postProcessMessage(Message arg0) throws AmqpException {
		arg0.getMessageProperties().getHeaders().put("parm1", "val");
		arg0.getMessageProperties().getHeaders().put("parm2", "val");
		arg0.getMessageProperties().setMessageId(messageId);
		return arg0;
	}

	 
	
	public String getMessageId() {
		return messageId;
	}



	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

}
