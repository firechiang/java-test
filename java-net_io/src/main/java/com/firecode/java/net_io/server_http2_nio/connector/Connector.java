package com.firecode.java.net_io.server_http2_nio.connector;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

import com.firecode.java.net_io.server_http2_nio.processor.ServletProcess;
import com.firecode.java.net_io.server_http2_nio.processor.StaticProcessor;

public class Connector implements Runnable {
	
	private static final int DEFAULT_PORT = 9999;
	
	private ServerSocketChannel server;
	
	private Selector selector;
	
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
			server = ServerSocketChannel.open();
			// 非阻塞模式
			server.configureBlocking(false);
			// 绑定服务端端口
			server.socket().bind(new InetSocketAddress(port));
			selector = Selector.open();
			// 注册连接事件
			server.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("启动服务器，监听端口："+port);
			for(;;) {
				// 获取事件
				selector.select();
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				for(SelectionKey key:selectedKeys) {
					// 处理触发的事件
					handler(key);
				}
				// 清理掉已经处理过的事件
				selectedKeys.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			System.err.println("服务器发生错误，正在关闭");
			// 关闭服务器
			close(selector);
		}
	}
	
	private void handler(SelectionKey key) throws IOException {
		// 连接事件
		if(key.isAcceptable()) {
			// 通过连接事件获取到服务端连接管道
			ServerSocketChannel serverChannel = (ServerSocketChannel)key.channel();
			// 通过连接事件获取客户端管道
			SocketChannel client = serverChannel.accept();
			// 设置客户端管道为非阻塞模式
			client.configureBlocking(false);
			// 注册监听客户端数据的读取事件
			client.register(selector, SelectionKey.OP_READ);
		}
		// 数据读取事件
		if(key.isReadable()) {
			// 获取客户端管道
			SocketChannel client = (SocketChannel)key.channel();
			// 将当前管道脱离Selector管理（注意：这样做的目的是要将数据读取使用阻塞模式，因为不脱离管理它的数据读取是非阻塞模式的）
			// 注意：脱离之后Selector将不再管理这个连接（用完之后一定要关闭这个连接）
			// 注意：生产环境不建议这样用（在这里这样用是因为我们的数据读取逻辑是阻塞式的，而我们又不想改代码，所以就这样用了）
			key.cancel();
			// 将客户端管道设置为阻塞模式
			client.configureBlocking(true);
			Socket socket = client.socket();
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
