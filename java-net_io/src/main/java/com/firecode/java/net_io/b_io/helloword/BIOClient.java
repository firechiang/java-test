package com.firecode.java.net_io.b_io.helloword;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class BIOClient {
	
	static final String CLOSE_FLAG = "quit";
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress("127.0.0.1", BIOServer.PORT), 5000);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		// 注意：lines() 函数是读取整个缓冲区，它会造成无限等待（就是代码在这里就阻塞住了）
		//String readStr = reader.lines().collect(Collectors.joining());
		String readStr = reader.readLine();
		System.err.println("收到服务器端返回的消息："+readStr);
		
		// 等待以用户输入
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		String readLine = null;
		while(!CLOSE_FLAG.equals(readLine = r.readLine())) {
			writer.write(readLine+"\r");
			writer.flush();
		}
		// 发送关闭标识
		writer.write("quit");
		writer.flush();
		socket.close();
	}
}
