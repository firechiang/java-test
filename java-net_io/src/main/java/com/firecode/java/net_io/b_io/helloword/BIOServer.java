package com.firecode.java.net_io.b_io.helloword;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings("resource")
public class BIOServer {
	
	static final int PORT = 9090;
	
	public static void main(String[] args) throws IOException {
		int count = 0;
		ServerSocket server = new ServerSocket(PORT);
		System.out.println("服务器启动完成，绑定端口："+PORT);
		for(;;) {
			// 等待客户端链接（注意：这个是阻塞的，每次只能接收一个客户端的连接，如果没有客户端连接就一直等待）
			Socket socket = server.accept();
			System.err.println("客户端："+socket.getPort()+"：已连接");
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			writer.write("你是第 "+(++count)+" 连接服务器的\r");
			writer.flush();
			
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String readLine = null;
			// 注意：lines() 函数是读取整个缓冲区，它会造成无限等待（就是代码在这里就阻塞住了）
			//String readStr = reader.lines().collect(Collectors.joining()); 
			while(!BIOClient.CLOSE_FLAG.equals(readLine = reader.readLine()) && readLine != null) {
				System.err.println("客户端："+socket.getPort()+"："+readLine);
			}
			reader.close();
			writer.close();
            System.err.println("客户端："+socket.getPort()+" 已断开连接");
		}
	}
}
