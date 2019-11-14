package com.example.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
	
	@Autowired
	private ConnectionFactory connectionFactory;

	/*	 @Bean
		 public  ConnectionFactory connectionFactory(@Value("${spring.rabbitmq.port}") int port,
		                                        @Value("${spring.rabbitmq.host}") String host,
		                                        @Value("${spring.rabbitmq.username}") String userName,
		                                        @Value("${spring.rabbitmq.password}") String password,
		                                        @Value("${spring.rabbitmq.publisher-confirms}") boolean isConfirm,
		                                        @Value("${spring.rabbitmq.publisher-returns}") boolean isReturn,
		                                        @Value("${spring.rabbitmq.virtual-host}") String vhost) {
		        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		        connectionFactory.setHost(host);
		        connectionFactory.setVirtualHost(vhost);
		        connectionFactory.setPort(port);
		        connectionFactory.setUsername(userName);
		        connectionFactory.setPassword(password);
		        connectionFactory.setPublisherConfirms(isConfirm);
		        connectionFactory.setPublisherReturns(isReturn);
				return connectionFactory;
		 }*/

	
	//队列 ，参数：队列名字，是否持久化, 是否自动删除, 是否exclusive
	@Bean
	public Queue directQueue() {
		return new Queue("direct_queue", true, false, false); 
	}
	
	@Bean
	public Queue topicQueue1() {
		return new Queue("topic_queue1", true, false, false); 
	}
	@Bean
	public Queue topicQueue2() {
		return new Queue("topic_queue2", true, false, false); 
	}
	@Bean
	public Queue topicQueue3() {
		return new Queue("topic_queue3", true, false, false); 
	}
	
	
	@Bean
	public Queue fanoutQueue1() {
		return new Queue("fanout_queue1", true, false, false); 
	}
	@Bean
	public Queue fanoutQueue2() {
		return new Queue("fanout_queue2", true, false, false); 
	}

	//Direct交换机(1对1)，参数：交换器名称、是否持久化、是否自动删除
	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange("direct_exchange", true, false);
	}
	//Topic交换机(1对多)
	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange("topic_exchange", true, false);
	}
	//Fanout交换机(广播)
	@Bean
	public FanoutExchange fanoutExchange() {
		return new FanoutExchange("fanout_exchange", true, false);
	}
	
	
	//绑定  将队列和交换机绑定, 并设置用于匹配键：test_key
	 @Bean
	 public  Binding directBinding1(){
	     return BindingBuilder.bind(directQueue()).to(directExchange()).with("test_key");
	 }
	 
	 
	 @Bean
	 public  Binding topicBinding1(){
		 return  BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("pattern1.*.*");//*表示一个词,#表示零个或多个词
	 }
	 @Bean
	 public  Binding topicBinding2(){
		 return  BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("pattern1.1.*");
	 }
	 @Bean
	 public  Binding topicBinding3(){
		 return  BindingBuilder.bind(topicQueue3()).to(topicExchange()).with("pattern3.*.*");
	 }
	 
	 @Bean
	 public  Binding fanoutBinding1(){
	     return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
	 }
	 @Bean
	 public  Binding fanoutBinding2(){
	     return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
	 }
	 
	 //	指定admin信息，当前的exchange和queue会在rabbitmq服务器上自动生成
	@Bean
    public RabbitAdmin rabbitAdmin(){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    } 


	//mq消息确认
	 @Bean    
	 public RabbitTemplate rabbitTemplate() {        
		 RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		// rabbitTemplate.setMessageConverter(); //可以自定义消息转换器  默认使用的JDK的，所以消息对象需要实现Serializable
		 rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		 
		// rabbitTemplate.setMessagePropertiesConverter(new DefaultMessagePropertiesConverter());
		 
		 // 消息发送失败返回到队列中, yml需要配置 publisher-returns: true        
		 rabbitTemplate.setMandatory(true);      
		 
		 // 消息返回,  发送失败时调用此方法。      yml需要配置 publisher-returns: true        
		 rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
			 	System.out.println("消息id："+message.getMessageProperties().getMessageId());
			 	System.out.println("响应状态码-ReplyCode："+replyCode);
		        System.out.println("响应内容-ReplyText："+replyText);
		        System.out.println("Exchange:"+exchange);
		        System.out.println("RouteKey"+routingKey);
		        System.out.println("投递失败的消息："+ new String(message.getBody()));
		 });
		 
		 // 消息确认, yml需要配置 publisher-confirms: true        
		 rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> { 
			 System.out.print("=============消息id:"+correlationData.getId()+",");
			 if (ack) {                
				 System.out.println("发送成功"); 
			 } else {                
				 System.out.println("发送失败,"+cause);  	    
			 }        
		});        
		return rabbitTemplate;    
	 }

}
