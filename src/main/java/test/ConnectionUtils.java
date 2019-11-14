package test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUtils {

	//用户名
	private static String username = "zhoubc";
	//密码
	private static String password = "zhoubc";
	//主机IP
	private static String host = "127.0.0.1";
	//主机端口， 默认的客户端连接的端口是5672,
	private static int port = 5672;
	//虚拟主机，拥有自己的队列、交换器、绑定和权限机制。 默认的 vhost 是 / 。
	private static String vhost = "/";
	
	/**
	 * RabbitMQ的连接，方式一
	 * 
	 */
	public static Connection getRabbitConnection1() {
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setUsername(username);
	    factory.setPassword(password);
	    factory.setVirtualHost(vhost);
	    factory.setHost(host);
	    factory.setPort(port);
	    Connection conn = null;
	    try {
	        conn = factory.newConnection();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return conn;
	}
	
	/**
	 * RabbitMQ的连接，方式二
	 * 
	 */
	public static Connection getRabbitConnection2() {
	    ConnectionFactory factory = new ConnectionFactory();
	    // 连接格式：amqp://userName:password@hostName:portNumber/virtualHost
	    String uri = String.format("amqp://%s:%s@%s:%d%s", username, password, host, port, vhost);
	    Connection conn = null;
	    try {
	    	 factory.setUri(uri);
	        //factory.setVirtualHost(vhost);
	        conn = factory.newConnection();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return conn;
	}
	
	
	/**	 
	 * 关闭连接	 
	 */	
	public static void close(Channel channel,Connection con){		
		if(channel != null){			
			try {				
				channel.close();			
			} catch (IOException e) {				
				e.printStackTrace();			
			} catch (TimeoutException e) {				
				e.printStackTrace();			
			}		
		}		
		if(con != null){			
			try {				
				con.close();			
			} catch (IOException e) {				
				// TODO Auto-generated catch block				
				e.printStackTrace();			
			}		
		}	
	}

	
}
