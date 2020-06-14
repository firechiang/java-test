package com.firecode.java.net_io.a_io.helloword;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;

public class Client {
	
	public void start() {
		try(AsynchronousSocketChannel clientChannel = AsynchronousSocketChannel.open();){
			Future<Void> connect = clientChannel.connect(new InetSocketAddress("127.0.0.1", Server.PORT));
			// 连接成功（注意：可以在调用connect函数时传入回调函数，具体可参考服务端的实现）
			connect.get();
			BufferedReader consoleRead = new BufferedReader(new InputStreamReader(System.in));
		    // 获取客户端在控制台输入的消息
			for(;;) {
		    	String msg = consoleRead.readLine();
		    	ByteBuffer msgBuffer = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
		    	// 将数据发送给服务端（最后调用get函数是因为write函数是异步的，调用get就是等数据发送完成）
		    	clientChannel.write(msgBuffer).get();
		    	// 将buffer由读模式改为写模式
		    	msgBuffer.clear();
		    	// 读取服务端发来的消息（最后调用get函数是因为read函数是异步的，调用get就是等数据读取完成）
		    	clientChannel.read(msgBuffer).get();
		    	// 将buffer由写模式改为读模式
		    	msgBuffer.flip();
		    	int position = msgBuffer.position();
		    	int limit = msgBuffer.limit();
		    	int size = limit - position;
		    	byte[] msgBytes = new byte[size];
		    	msgBuffer.get(msgBytes, position, limit);
		    	// 打印服务端发来的数据
		    	System.err.println("服务端："+new String(msgBytes,StandardCharsets.UTF_8));
		    }
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new Client().start();
	}
	

}
