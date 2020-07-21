package com.firecode.java.net_io.server_http1.connector;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.firecode.java.net_io.server_http1.processor.ServletProcess;
import com.firecode.java.net_io.server_http1.processor.StaticProcessor;

public class Connector implements Runnable {
	
	private static final int DEFAULT_PORT = 9999;
	
	private ServerSocket server;
	
	private int port;
	
	public Connector() {
		this.port = DEFAULT_PORT;
	}
	
	public Connector(int port) {
		this.port = port;
	}
	
	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		try {
			server = new ServerSocket(port);
			System.out.println("启动服务器，监听端口："+port);
			for(;;) {
				Socket socket = server.accept();
				InputStream input = socket.getInputStream();
				OutputStream output = socket.getOutputStream();
				
				Request request = new Request(input);
				request.parse();
				
				Response response = new Response(output);
				response.setRequest(request);
				
				
				if(request.getRequestURI().startsWith("/servlet/")) {
					// Servlet 资源处理器
					ServletProcess servletProcess = new ServletProcess();
					servletProcess.process(request, response);
				}else{
					// 静态资源处理器
					StaticProcessor process = new StaticProcessor();
					process.process(request, response);
				}
				// 关闭连接
				close(socket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			System.err.println("服务器发生错误，正在关闭");
			// 关闭服务器
			close(server);
		}
	}
	
	private void close(Closeable closeable) {
		if(closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
