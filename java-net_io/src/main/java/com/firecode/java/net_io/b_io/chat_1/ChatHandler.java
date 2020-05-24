package com.firecode.java.net_io.b_io.chat_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * 处理客户端请求
 * @author ChiangFire
 */
public class ChatHandler implements Runnable {
	
	/**
	 * 服务器
	 */
	private ChatServer chatServer;
	/**
	 * 客户端连接
	 */
	private Socket socket;
	
	public ChatHandler (ChatServer chatServer,Socket socket) {
		this.chatServer = chatServer;
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			// 储存客户端
			chatServer.addClient(socket);
			// 客户端ID
			String clientId = chatServer.getClientId(socket);
			// 读取客户端发送的消息
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		    String message = null;
		    // 读取一行消息
		    while((message = reader.readLine()) != null) {
		    	System.err.println("客户端【"+clientId+"】消息："+message);
		    	// 正常消息
		    	if(!ChatServer.QUIT_FLAG.equals(message)) {
			    	// 转发消息到所有客户端
			    	chatServer.forwardMessage(socket, "客户端【"+clientId+"】："+message+"\r");
			    // 退出消息	
		    	}else {
			    	// 发送退出消息到所有客户端
			    	chatServer.forwardMessage(socket, "客户端【"+clientId+"】：已退出\r");
				    // 客户端退出
				    chatServer.removeClient(socket);
				    break;
		    	}
		    }

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				chatServer.removeClient(socket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
