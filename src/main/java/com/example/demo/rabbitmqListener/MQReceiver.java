package com.example.demo.rabbitmqListener;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

@Component
public class MQReceiver {
	
	@RabbitListener(queues = "direct_queue1")
	public void directReceive(Message message, Channel channel) throws IOException {
		// 手动应答
		// 第一个参数代表消费者拒绝一条或者多条消息，第二个参数表示一次是否拒绝多条消息，第三个参数表示是否把当前消息重新入队
		//channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);CorrelationData
		
		System.out.println("=============收到direct_queue消息:"+new String(message.getBody()));
		System.out.println("=============messageId:"+message.getMessageProperties().getMessageId());
		System.out.println("=============parm1:"+message.getMessageProperties().getHeaders().get("parm1"));
	}
	
	
	@RabbitListener(queues = "topic_queue1")
	public void topicReceive1(Message message, Channel channel) throws IOException {
		System.out.println("=============收到topic_queue1消息:"+new String(message.getBody()));
	}
	@RabbitListener(queues = "topic_queue2")
	public void topicReceive2(Message message, Channel channel) throws IOException {
		System.out.println("=============收到topic_queue2消息:"+new String(message.getBody()));
	}
	@RabbitListener(queues = "topic_queue3")
	public void topicReceive3(Message message, Channel channel) throws IOException {
		System.out.println("=============收到topic_queue3消息:"+new String(message.getBody()));
	}
	
	
	@RabbitListener(queues = "fanout_queue1")
	public void fanoutReceive1(Message message, Channel channel) throws IOException {
		System.out.println("=============收到fanout_queue1消息:"+new String(message.getBody()));
	}
	@RabbitListener(queues = "fanout_queue2")
	public void fanoutReceive2(Message message, Channel channel) throws IOException {
		System.out.println("=============收到fanout_queue2消息:"+new String(message.getBody()));
	}

}

