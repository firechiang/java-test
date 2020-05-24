package com.firecode.java.net_io.b_io.chat_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * 聊天室客户端
 * @author ChiangFire
 *
 */
public class ChatClient {
	
	// 服务端地址
	String SERVER_HOST_NAME = "127.0.0.1";
	
	private Socket socket;
	
	private BufferedReader reader;
	
	private BufferedWriter writer;
	
	/**
	 * 发送消息到服务端
	 * @param message
	 * @throws IOException 
	 */
	public void send(String message) throws IOException {
		// 输出流没有关闭
		if(!this.socket.isOutputShutdown()) {
			this.writer.write(message+"\r");
			this.writer.flush();
		}
	}
	/**
	 * 从服务器读取消息
	 * @return
	 * @throws IOException 
	 */
	
	public String receive() throws IOException {
		String message = null;
		// 输入流没有关闭
		if(!this.socket.isInputShutdown()) {
			// 读取一行消息
			message = this.reader.readLine();
		}
		return message;
	}
	
	/**
	 * 启动客户端
	 */
	public void start() {
		try {
			this.socket = new Socket(SERVER_HOST_NAME,ChatServer.PORT);
			this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
			// 处理用户输入
			new Thread(new UserInputHandler(this)).start();
			// 读取服务器的消息
			String message = null;
			while((message = receive()) != null) {
				System.out.println(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public void close() {
		try {
			if(null != this.socket) {
				this.socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		ChatClient client = new ChatClient();
		client.start();
	}
}
