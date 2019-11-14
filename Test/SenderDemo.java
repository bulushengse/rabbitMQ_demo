package com.zbc.Test;

import java.io.IOException;
import java.util.Scanner;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.ReturnListener;

public class SenderDemo {
	
	private static final String QUEUE = "myQueue";
	
	/**
	 *  了解RabbitMQ一些基础概念和路由策略，阅读《RabbitMQ基础概念.docx》
	 *  http://localhost:15672/
	 *  
	 *  RabbitMQ 消息确认和持久化机制
	 *  1消息发送确认
	 *  首先必须开启确认模式: channel.confirmSelect()，
	 *  两种编程模式，一种同步的，一种异步通知的：
	 *  
	 *  2消息接收确认
	 *  手动确认或自动确认
	 *  
	 *  3消息持久化
	 *  消息在传输过程中,可能会出现各种异常失败甚至宕机情况,
	 *  为了保证消息传输的可靠性,需要进行持久化,也就是把数据写在磁盘上，但性能会降低。
	 *  channel.queueDeclare(QUEUE, true, false, false, null);	
	 *  
	 *  消息模型：Direct路由模式，fanout发布/订阅模式，topic主题模式，主要在web管理页设置exchange交换机类型，
	 *  Direct<直接>：1对1-----一个消息只能被一个消费者消费
	 *	Topic<主题>：1对多-----一个消息可以被多个消费者消费
 	 * 	Fanout<分列>：广播
 	 * 
 	 * 			
	 */
	
	//消息发送确认机制的两种方式
	public static void main(String[] args) {
		SenderDemo sender = new SenderDemo();
		//sender.sendMsg1();
		sender.sendMsg2();
	}
	
	
	/**
	 *  消息发送确认机制 ,同步
	 */
	public void  sendMsg1() {
		Connection con = null;		
		Channel channel = null;		
		try {			
			// 获取连接			
			con = ConnectionUtils.getRabbitConnection1();			
			// 从连接中创建通道			
			channel = con.createChannel();			
			// 创建一个队列     
			//【参数说明：参数一：队列名称（队列名称重复则不创建新队列），参数二：是否持久化；参数三：是否独占模式；参数四：消费者断开连接时是否删除队列；参数五：消息其他参数】		
			channel.queueDeclare(QUEUE, true, false, false, null);	
			// 限制发送给同一个消费者不超过1条消息
			//channel.basicQos(1);
			// 消息发送确认，消息的投递模式：confirm 确认模式
			channel.confirmSelect();
			// 消息内容			
			String msg = "hello rabbitmq!";			
			// 发送消息
			//【参数说明：参数一：指定交换机名称；参数二：指定队列名称；参数三：消息的其他属性-routing headers；参数四：消息主体】
			channel.basicPublish("", QUEUE, MessageProperties.PERSISTENT_BASIC, msg.getBytes());	
			//channel.waitForConfirms(10)   等待消息的投递结果，可选参数，就是阻塞等待的时间，
			if (channel.waitForConfirms()) {
				System.out.println("发送成功");
			}else{
				System.out.println("发送失败");	//进行消息重发
			}
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {			
			// 关闭连接			
			ConnectionUtils.close(channel, con);		
		}		
	}
	
	/**
	 * 消息发送确认机制 ,异步回调
	 */
	public void  sendMsg2() {
		Connection con = null;		
		Channel channel = null;		
		try {			
			// 获取连接			
			con = ConnectionUtils.getRabbitConnection1();			
			// 从连接中创建通道			
			channel = con.createChannel();			
			// 创建一个队列     
			//【参数说明：参数一：队列名称（队列名称重复则不创建新队列），参数二：是否持久化；参数三：是否独占模式；参数四：消费者断开连接时是否删除队列；参数五：消息其他参数】		
			channel.queueDeclare(QUEUE, true, false, false, null);	
			// 限制发送给同一个消费者不超过1条消息
			//channel.basicQos(1);
			// 消息发送确认，消息的投递模式：confirm 确认模式
			channel.confirmSelect();
		    //添加一个确认监听， 这里就不关闭连接了，为了能保证能收到监听消息
	        channel.addConfirmListener(new ConfirmListener() {
	             //返回成功的回调函数
	            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
	                System.out.println("succuss ack");
	            }
	             //返回失败的回调函数
	            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
	                System.out.printf("failed ack");
	            }
	        });
	        //添加一个 return 监听，当消息被RabbitMQ拒绝或者说没有成功投递的时候，则会触发这个方法
	        channel.addReturnListener(new ReturnListener() {
	            public void handleReturn(int i, String s, String s1, String s2, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
	                System.out.println("响应状态码-ReplyCode："+i);
	                System.out.println("响应内容-ReplyText："+s);
	                System.out.println("Exchange:"+s1);
	                System.out.println("RouteKey"+s2);
	                System.out.println("投递失败的消息："+ new String(bytes,"UTF-8") );
	            }
	        });
	        
	        while (true) {
	        	try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        	 // 消息内容
		        System.out.println("输入要发送的消息：");
		        Scanner sc = new Scanner(System.in);
		        String msg = sc.nextLine();  //读取字符串型输入
		     	if("q".equals(msg)) {
					break;  //退出循环，关闭mq连接
		     	}
			    // 发送消息
		     	// 消息的其他属性-routing headers
//		     	Map<String, Object> headers = new HashMap<String, Object>();
//	            headers.put("skey" ,666);
//	            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().deliveryMode(2).headers(headers).build();
	            //【参数说明：参数一：指定交换机名称；参数二：指定队列名称；参数三：消息的其他属性-routing headers；参数四：消息主体】
		     	channel.basicPublish("", QUEUE, MessageProperties.PERSISTENT_BASIC, msg.getBytes());	
		     	//channel.basicPublish("", QUEUE, properties, msg.getBytes());	
	        }
		} catch (IOException e) {			
			e.printStackTrace();
		}  finally {			
			// 关闭连接			
			ConnectionUtils.close(channel, con);		
		}	
	}
		
	
}
