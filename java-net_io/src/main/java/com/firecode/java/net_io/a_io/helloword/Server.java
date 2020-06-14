package com.firecode.java.net_io.a_io.helloword;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.Map;
/**
 * 服务端将客户端发来的数据再原封不动的在发给客户端
 * @author ChiangFire
 */
public class Server {
	public static final int PORT = 8090;
	
	public void start() {
		try(AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();){
			serverChannel.bind(new InetSocketAddress("127.0.0.1", PORT));
			System.out.println("服务器已启动，监听端口："+PORT);
			/**
			 * 监听客户端连接（注意：这个函数是异步调用的）
			 * @param attachment 自定义的任意对象，可在回调函数里面使用
			 * @param handler    回调函数（连接完成后回调）
			 */
			serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel,Object>(){

				/**
				 * 处理一个客户端连接
				 * @param socketChannel 客户端连接管道
				 * @param attachment    自定义的任意对象（注意：这个参数是在调用accept函数时传过来的）
				 */
				@Override
				public void completed(AsynchronousSocketChannel socketChannel, Object attachment) {
					// 如果服务端管道没有关闭，我们再监听（处理）下一个连接
					if(serverChannel.isOpen()) {
						serverChannel.accept(null, this);
					}
					
					ByteBuffer buffer = ByteBuffer.allocate(1024);
					// 自定义回调函数的参数
					Map<String,Object> attachmentMap = new HashMap<>(5);
					attachmentMap.put("type", "read");
					attachmentMap.put("buffer", buffer);
					/**
					 * 读取客户端发来的信息（注意：这个函数是异步调用的）
					 * @param dst        缓冲区
					 * @param attachment 自定义的任意对象，可在回调函数里面使用
					 * @param handler    回调函数（读取完成后回调）
					 */
					socketChannel.read(buffer,attachmentMap,new ClientCompletionHandler(socketChannel));
				}

				/**
				 * 处理客户端连接异常
				 * @param exc        客户端连接错误
				 * @param attachment 自定义的任意对象（注意：这个参数是在调用accept函数时传过来的）
				 */
				@Override
				public void failed(Throwable exc, Object attachment) {
					System.err.println("处理客户端连接异常");
					exc.printStackTrace();
				}
			});
			// 无限等待
			System.in.read();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 处理客户端数据读写的回调类（主要实现将客户端发来的数据，原封不动的再发个客户端）
	 * @author ChiangFire
	 *
	 */
	private static class ClientCompletionHandler implements CompletionHandler<Integer,Map<String,Object>> {

		private AsynchronousSocketChannel clientChannel;
		
		public ClientCompletionHandler(AsynchronousSocketChannel clientChannel) {
			this.clientChannel = clientChannel;
		}
		
		
		/**
		 * 读写回调完成的回调
		 * @param result
		 * @param attachment 自定义的任意对象（注意：这个参数是在调用read或write函数时传过来的）
		 */
		@Override
		public void completed(Integer result, Map<String, Object> attachment) {
			String type = (String)attachment.get("type");
			ByteBuffer buffer = (ByteBuffer)attachment.get("buffer");
			// 如果读取完成回调
			if("read".equals(type)) {
				// 将buffer由写模式改为读模式（注意：服务端的读取就是将客户端的数据写到这个buffer里面）
				buffer.flip();
				// 自定义写完成的回调函数的参数
				Map<String,Object> attachmentMap = new HashMap<>(5);
				attachmentMap.put("type", "write");
				attachmentMap.put("buffer", buffer);
				clientChannel.write(buffer,attachmentMap,this);
				// 将buffer由读模式改为写模式
				buffer.clear();
			}
			// 如果写取完成回调
			if("write".equals(type)) {
				// 自定义读完成的回调函数的参数
				Map<String,Object> attachmentMap = new HashMap<>(5);
				attachmentMap.put("type", "read");
				ByteBuffer allocate = ByteBuffer.allocate(1024);
				attachmentMap.put("buffer", allocate);
				clientChannel.read(allocate, attachmentMap, this);
			}
		}

		/**
		 * 异常回调
		 */
		@Override
		public void failed(Throwable exc, Map<String, Object> attachment) {
			System.err.println("处理客户端数据读写的回调类异常");
			exc.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new Server().start();
	}
}
