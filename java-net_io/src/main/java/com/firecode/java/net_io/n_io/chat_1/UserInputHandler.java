package com.firecode.java.net_io.n_io.chat_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 处理用户在控制台输入信息
 * @author ChiangFire
 */
public class UserInputHandler implements Runnable {

	// 客户端
	private ChatClient chatClient;
	
	public UserInputHandler (ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@Override
	public void run() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(System.in));
			for(;;) {
				// 注意：这一行消息是带换行的
				String message = reader.readLine();
				// 发送消息到服务器
				chatClient.send(message);
				// 断开连接
				if(ChatServer.QUIT_FLAG.equals(message)) {
					reader.close();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != reader) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
