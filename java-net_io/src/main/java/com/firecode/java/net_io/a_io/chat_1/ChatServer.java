package com.firecode.java.net_io.a_io.chat_1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
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
	 * 缓冲区大小
	 */
	private static final int BUFFER_SIZE = 1024;
	
	/**
	 * 管道组（就是利用线程池来处理管道）
	 */
	private AsynchronousChannelGroup channelGroup;
	/**
	 * 服务端管道
	 */
	private AsynchronousServerSocketChannel serverChannel;
	/**
	 * 房间（所有客户端管道）
	 */
	private Map<String,AsynchronousSocketChannel> clientChannelMap = new ConcurrentHashMap<String, AsynchronousSocketChannel>();
	/**
	 * 写缓冲区
	 */
	private ByteBuffer writeBuffer = ByteBuffer.allocate(BUFFER_SIZE);
	
	/**
	 * 转发消息到所有其它客户端
	 * @param clientChannel
	 * @param message
	 * @throws IOException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public void forwardMessage(String clientId,String message) throws IOException, InterruptedException, ExecutionException {
		// 遍历注册在管道选择器上的所有注客户端
		for(Map.Entry<String,AsynchronousSocketChannel> entiry: clientChannelMap.entrySet()) {
			AsynchronousSocketChannel clientChannel = entiry.getValue();
			if(null != clientChannel && clientChannel.isOpen() && !clientId.equals(entiry.getKey())) {
				// 将写出缓存区改为写模式
				writeBuffer.clear();
				// 写入数据
				writeBuffer.put(StandardCharsets.UTF_8.encode(message));
				// 将写出缓存区由写模式该为读模式（我们上面在写嘛）
				writeBuffer.flip();
				// 注意：为什么最后要调用get函数，是因为我们所有的写出数据用的是同一个缓冲区，
				// 而write函数是异步的（可能会有多线程同步的问题），所以我们调用get进行同步阻塞。
				// 生产环境不建议使用同一个缓冲区（影响效率）
			    clientChannel.write(writeBuffer).get();
			}
		}
	}
	
	/**
	 * 启动服务
	 */
	public void start () {
		try{
			// 线程池
			ExecutorService threadPool = Executors.newFixedThreadPool(8);
			// 创建管道组
			this.channelGroup = AsynchronousChannelGroup.withThreadPool(threadPool);
			this.serverChannel = AsynchronousServerSocketChannel.open(this.channelGroup);
			this.serverChannel.bind(new InetSocketAddress("127.0.0.1",PORT));
			System.out.println("启动服务器，监听端口："+PORT);
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
					// 客户端连接没有关闭
					if(socketChannel.isOpen()) {
						String clientId = getClientId(socketChannel);
						// 将客户端连接加如到房间
						clientChannelMap.put(clientId, socketChannel);
						System.out.println("客户端："+clientId+"，连接成功");
						ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
						/**
						 * 读取客户端发来的信息（注意：这个函数是异步调用的）
						 * @param dst        缓冲区
						 * @param attachment 自定义的任意对象，可在回调函数里面使用
						 * @param handler    回调函数（读取完成后回调）
						 */
						socketChannel.read(buffer,buffer,new ClientCompletionHandler(socketChannel));
					}
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
		}finally{
			try {
				if(null != this.serverChannel) {
					this.serverChannel.close();
				}
				if(null != this.channelGroup && !this.channelGroup.isShutdown()) {
					this.channelGroup.shutdownNow();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 处理客户端发来的数据读取完成后回调
	 * @author ChiangFire
	 *
	 */
	private class ClientCompletionHandler implements CompletionHandler<Integer,ByteBuffer> {

		private AsynchronousSocketChannel clientChannel;
		
		public ClientCompletionHandler(AsynchronousSocketChannel clientChannel) {
			this.clientChannel = clientChannel;
		}
		
		private void close(String clientId) {
			// 将客户端连接管道移除房间（注意：这个位置可能会抛异常（原因是多线程问题，有的线程在遍历，有的线程在删除就会抛异常））
			clientChannelMap.remove(clientId);
			try {
				clientChannel.close();
			} catch (IOException e) {
				System.err.println("关闭客户端连接异常");
				e.printStackTrace();
			}
		}
		
		/**
		 * 读取完成后回调
		 * @param result
		 * @param attachment 自定义的任意对象（注意：这个参数是在调用read函数时传过来的）
		 */
		@Override
		public void completed(Integer result, ByteBuffer attachment) {
			String clientId = getClientId(clientChannel);
			String message;
			// 客户端管道异常（可能已经关闭了）
			if(result <= 0) {
				// 关闭客户端连接
				close(clientId);
				message = "已退出";
			}else{
				// 将buffer由写模式改为读模式
				attachment.flip();
				// 读取到客户端发来的消息
				message = receive(attachment);
				// 消息为空或是退出消息（就把客户端管道事件取消掉，让客户端退出）
				if(message.isEmpty() || QUIT_FLAG.equals(message)) {
					// 关闭客户端连接
					close(clientId);
					message = "已退出";
				} 
				// 将buffer由读模式改为写模式
				attachment.clear();
				// -------------------不是退出消息监听下一次的读取回调--------------------------------
				if(!"已退出".equals(message)) {
					ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
					/**
					 * 读取客户端发来的信息（注意：这个函数是异步调用的）
					 * @param dst        缓冲区
					 * @param attachment 自定义的任意对象，可在回调函数里面使用
					 * @param handler    回调函数（读取完成后回调）
					 */
					clientChannel.read(buffer,buffer,this);
				}
			}
			try{
				// 转发消息到其它客户端
				forwardMessage(clientId,"客户端【"+clientId+"】："+message);
			}catch(Exception e){
				System.err.println("转发消息到其它客户端异常");
				e.printStackTrace();
			}
		}

		/**
		 * 异常回调
		 */
		@Override
		public void failed(Throwable exc, ByteBuffer attachment) {
			System.err.println("处理客户端数据读写的回调类异常");
			exc.printStackTrace();
		}
		
	}
	
	
	
	/**
	 * 读取客户端发来的消息
	 * @param socketChannel
	 * @return
	 */
	public String receive(ByteBuffer readBuffer) {
		int position = readBuffer.position();
		int limit = readBuffer.limit();
		int size = limit - position;
		byte[] bytes = new byte[size];
		readBuffer.get(bytes,position,limit);
		return new String(bytes,StandardCharsets.UTF_8);
	}
	
	/**
	 * 获取客户端ID
	 * @param socket
	 * @return
	 */
	public String getClientId(AsynchronousSocketChannel clientChannel) {
		/*int clientPort = socket.getPort();
		String hostAddress = socket.getInetAddress().getHostAddress();
		return String.join(":", hostAddress,String.valueOf(clientPort));*/
		try {
			return clientChannel.getRemoteAddress().toString().replaceFirst("/", "");
		} catch (IOException e) {
			throw new RuntimeException("获取客户端ID异常",e);
		}
	}
	
	
	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.start();
	}
}
