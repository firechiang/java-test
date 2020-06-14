package com.firecode.java.net_io.a_io.chat_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 聊天室客户端
 * @author ChiangFire
 *
 */
public class ChatClient {
	
	// 服务端地址
	String SERVER_HOST_NAME = "127.0.0.1";
	
	/**
	 * 缓冲区大小
	 */
	private static final int BUFFER_SIZE = 1024;
	/**
	 * 写缓冲区
	 */
	private ByteBuffer writeBuffer = ByteBuffer.allocate(BUFFER_SIZE);
	
	/**
	 * 发送消息到服务端
	 * @param message
	 * @throws IOException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public void send(AsynchronousSocketChannel clientChannel,String message) throws IOException, InterruptedException, ExecutionException {
		if(!message.isEmpty()) {
			// 将写出缓存区由读模式改为写模式
			writeBuffer.clear();
			// 写入数据
			writeBuffer.put(StandardCharsets.UTF_8.encode(message));
			// 将写出缓存区由写模式该为读模式（我们上面在写嘛）
			writeBuffer.flip();
			clientChannel.write(writeBuffer).get();
		}
	}
	
	/**
	 * 启动客户端
	 */
	public void start() {
		try(AsynchronousSocketChannel clientChannel = AsynchronousSocketChannel.open();){
			Future<Void> connect = clientChannel.connect(new InetSocketAddress("127.0.0.1", ChatServer.PORT));
			// 连接成功（注意：可以在调用connect函数时传入回调函数，具体可参考服务端的实现）
			connect.get();
			ByteBuffer allocate = ByteBuffer.allocate(BUFFER_SIZE);
			/**
			 * 读取服务端数据（注意：这个函数是异步的）
			 * @param dst        缓冲区
		     * @param attachment 自定义的任意对象，可在回调函数里面使用
		     * @param handler    回调函数（读取完成后回调）
			 */
			clientChannel.read(allocate, allocate,new CompletionHandler<Integer,ByteBuffer>(){
				/**
				 * 读取完成后回调
				 * @param result        
				 * @param attachment    自定义的任意对象（注意：这个参数是在调用read函数时传过来的）
				 */
				@Override
				public void completed(Integer result, ByteBuffer attachment) {
					// 将写出缓存区由写模式该为读模式
					attachment.flip();
					String msg = receive(attachment);
					// 将写出缓存区由读模式该为写模式
					attachment.clear();
					System.err.println(msg);
					// --------- 处理下一次服务端发来的数据
					ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
					/**
					 * 读取服务端发来的信息（注意：这个函数是异步调用的）
					 * @param dst        缓冲区
					 * @param attachment 自定义的任意对象，可在回调函数里面使用
					 * @param handler    回调函数（读取完成后回调）
					 */
					clientChannel.read(buffer,buffer,this);
				}

				/**
				 * 处理客户端连接异常
				 * @param exc        客户端连接错误
				 * @param attachment 自定义的任意对象（注意：这个参数是在调用accept函数时传过来的）
				 */
				@Override
				public void failed(Throwable exc, ByteBuffer attachment) {
					System.err.println("处理客户端连接异常");
					exc.printStackTrace();
				}
			});
			// -------------读取控制台数据并将其发送到服务端
			BufferedReader consoleRead = new BufferedReader(new InputStreamReader(System.in));
		    // 获取客户端在控制台输入的消息
			for(;;) {
		    	String msg = consoleRead.readLine();
		    	// 发送数据到服务端
		    	send(clientChannel,msg);
		    	// 程序退出
		    	if(ChatServer.QUIT_FLAG.equals(msg)) {
		    		break;
		    	}
		    }
		}catch(Exception e) {
			System.err.println("客户端连接发送异常");
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取服务端发来的消息
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
	
	public static void main(String[] args) {
		ChatClient client = new ChatClient();
		client.start();
	}
}
