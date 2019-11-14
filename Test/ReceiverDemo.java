package com.zbc.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ReceiverDemo {

	private static final String QUEUE = "myQueue";
	
	public static void main(String[] args) throws IOException, TimeoutException {		
		// 获取连接		
		Connection con = ConnectionUtils.getRabbitConnection1();		
		// 从连接中创建通道			
		Channel channel = con.createChannel();		
		// 创建一个队列     
		//【参数说明：参数一：队列名称（队列名称重复则不创建新队列），参数二：是否持久化；参数三：是否独占模式；参数四：消费者断开连接时是否删除队列；参数五：消息其他参数】		
		channel.queueDeclare(QUEUE, true, false, false, null);	
		// 保证一次只分发一条消息
		//channel.basicQos(1);
		// 创建消费者		
		Consumer consumer = new DefaultConsumer(channel) {                        
			// 获取消息			
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)	throws IOException {				
				String routingKey = envelope.getRoutingKey(); // 队列名称
                String contentType = properties.getContentType(); // 内容类型
				String msg = new String(body, "utf-8");	// 消息正文
				System.out.println("队列名称："+routingKey+"，接收到消息——" + msg);	
				// 手动确认消息【参数说明：参数一：该消息的index；参数二：是否批量应答，true批量确认小于index的消息】
				//channel.basicAck(envelope.getDeliveryTag(), false); 
			} 		
		};		
		// 监听队列，自动确认消息
		//【参数说明：参数一：队列名称；参数二：是否自动应答；参数三：回调方法。】
		channel.basicConsume(QUEUE, true, consumer);	
		//channel.basicConsume(QUEUE, false, consumer);	
		
	}
	
	
	
	
}
