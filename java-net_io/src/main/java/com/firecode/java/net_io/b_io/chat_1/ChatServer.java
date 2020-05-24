package com.firecode.java.net_io.b_io.chat_1;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 聊天室服务端
 * @author ChiangFire
 *
 */
public class ChatServer {
	
	/**
	 * 默认端口
	 */
	static final int PORT = 8291;
	/**
	 * 推出标识
	 */
	static final String QUIT_FLAG = "quit";
	
	/**
	 * 储存客户端
	 */
	private Map<String,Writer> connectedClients;
	
	/**
	 * 线程池（注意：这个线程池其实没什么用，因为很快线程池就会满了（因为线程里面的逻辑没有退出，除非客户端退出），而且大量的连接上来会造成内存溢出（因为队列满了））
	 */
	private ExecutorService executorService;
	
	private ChatServer () {
		this.executorService = Executors.newFixedThreadPool(10);
		this.connectedClients = new ConcurrentHashMap<>();
	}
	
	/**
	 * 添加客户端
	 * @param socket
	 * @throws IOException
	 */
	public void addClient(Socket socket) throws IOException {
		if(null != socket) {
			String clientId = getClientId(socket);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		    connectedClients.put(clientId, writer);
			System.out.println("客户端【"+clientId+"】已连接服务器");
		}
	}
	
	/**
	 * 移除客户端
	 * @param socket
	 * @throws IOException
	 */
	public void removeClient(Socket socket) throws IOException {
		if(null != socket) {
			String clientId = getClientId(socket);
			Writer writer = connectedClients.get(clientId);
			if(null != writer) {
				socket.close();
				connectedClients.remove(clientId);
				System.err.println("客户端【"+clientId+"】已断开连接");
			}
		}
	}
	
	/**
	 * 转发消息到所有客户端
	 * @param socket
	 * @param message
	 * @throws IOException 
	 */
	public void forwardMessage(Socket socket,String message) throws IOException {
		String clientId = getClientId(socket);
		for(String id:connectedClients.keySet()) {
			// 转发消息到其他所有客户端（不包括自己）
			if(!clientId.equals(id)) {
				Writer writer = connectedClients.get(id);
				writer.write(message);
				writer.flush();
			}
		}
	}
	
	/**
	 * 启动服务
	 */
	public void start () {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("启动服务器，监听端口："+PORT);
			for(;;) {
				// 等待客户端连接（注意：这个函数是同步阻塞的）
				Socket socket = serverSocket.accept();
				// 处理客户端连接
				this.executorService.execute(new ChatHandler(this,socket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != serverSocket) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取客户端ID
	 * @param socket
	 * @return
	 */
	public String getClientId(Socket socket) {
		int clientPort = socket.getPort();
		String hostAddress = socket.getInetAddress().getHostAddress();
		return String.join(":", hostAddress,String.valueOf(clientPort));
	}
	
	
	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.start();
	}
}
