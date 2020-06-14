package com.firecode.java.net_io.n_io.chat_1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Set;

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
	 * 客户端管道
	 */
	private SocketChannel client;
	/**
	 * 管道选择器
	 */
	private Selector selector;
	/**
	 * 读取缓冲区
	 */
	private ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
	/**
	 * 写缓冲区
	 */
	private ByteBuffer writeBuffer = ByteBuffer.allocate(BUFFER_SIZE);
	
	/**
	 * 发送消息到服务端
	 * @param message
	 * @throws IOException 
	 */
	public void send(String message) throws IOException {
		if(!message.isEmpty()) {
			// 将写出缓存区改为写模式
			writeBuffer.clear();
			// 写入数据
			writeBuffer.put(StandardCharsets.UTF_8.encode(message));
			// 将写出缓存区由写模式该为读模式（我们上面在写嘛）
			writeBuffer.flip();
			// 循环一直往外写（只要还有数据没有写完成，我们就一直读一直写）
			while(writeBuffer.hasRemaining()) {
				this.client.write(writeBuffer);
			}
			// 退出消息
			if(ChatServer.QUIT_FLAG.equals(message)) {
				// 注意：关闭管道选择器会自动关闭选择器里面的所有管道连接
				this.selector.close();
			}
		}
	}
	
	/**
	 * 启动客户端
	 */
	public void start() {
		try {
			this.client = SocketChannel.open();
			// 设置客户端管道为非阻塞式调用模式（注意：它默认阻塞式调用模式）
			this.client.configureBlocking(false);
			// 初始化管道管理器
			this.selector = Selector.open();
			// 为客户端管道注册连接事件（就是当服务器接受了我的连接请求，就会触发这个事件）
			this.client.register(this.selector, SelectionKey.OP_CONNECT);
			// 发送连接请求
			this.client.connect(new InetSocketAddress(SERVER_HOST_NAME,ChatServer.PORT));
			// 不停的监听事件
			for(;;) {
				// 查询一下是否有事件发生（注意：select() 函数是阻塞式的，如果所有的管道都没有事件，它就阻塞）
				this.selector.select();
				Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
				// 遍历所有事件
				for(SelectionKey selectionKey : selectionKeys) {
					handles(selectionKey);
				}
				// 将已经处理过的事件清空（因为这个集合是公用，如果不清空，下一次有新的事件过来，还会把原来的事件给我）
				selectionKeys.clear();
			}
		} catch(IOException e){
			e.printStackTrace();
		} catch (ClosedSelectorException e) {
			System.err.println("客户端已关闭");
		}finally {
			try {
				if(null != selector) {
					// 注意：关闭管道选择器会自动关闭选择器里面的所有管道连接
					selector.close();
				}
				if(null != client) {
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 事件处理
	 * @param selectionKey
	 * @throws IOException 
	 */
	public void handles(SelectionKey selectionKey) throws IOException {
		// 处理CONNECT事件（客户端连接事件就绪事件）
		if(selectionKey.isConnectable()) {
			// 获取客户端管道
			SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
			// 连接是否处于就绪状态
			if(socketChannel.isConnectionPending()){
				// 完成处于就绪状态的的连接
				socketChannel.finishConnect();
				// 处理用户输入的信息
				new Thread(new UserInputHandler(this)).start();
			}
			// 为客户端管道注册数据读取事件（就是接收服务端发来的消息）
			this.client.register(this.selector, SelectionKey.OP_READ);
		}
		
		// 处理READ事件（数据读取事件，就是服务器发来了消息）
		if(selectionKey.isReadable()) {
			SocketChannel channel = (SocketChannel)selectionKey.channel();
			// 读取服务端端发来的消息
			String message = receive(channel);
			// 消息为空，说明这个连接是有问题的，我们就让客户端退出
			if(message.isEmpty()) {
				this.selector.close();
			} else {
				System.err.println(message);
			}
		}
	}
	
	/**
	 * 读取服务端发来的消息
	 * @param socketChannel
	 * @return
	 */
	public String receive(SocketChannel socketChannel) {
		try {
			// 将读取缓存区改为写模式
			readBuffer.clear();
			// 只要还有数据我们就一直读取，写到读取缓存区里面
			while(socketChannel.read(readBuffer) > 0) {
				// 将读取缓存区由写模式该为读模式（我们上面在循环写嘛）
				readBuffer.flip();
				return String.valueOf(StandardCharsets.UTF_8.decode(readBuffer));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void main(String[] args) {
		ChatClient client = new ChatClient();
		client.start();
	}
}
