package com.firecode.java.net_io.n_io.chat_1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Set;

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
	 * 服务端管道
	 */
	private ServerSocketChannel server;
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
	 * 转发消息到所有其它客户端
	 * @param socketChannel
	 * @param message
	 * @throws IOException 
	 */
	public void forwardMessage(SocketChannel socketChannel,String message) throws IOException {
		Set<SelectionKey> keys = this.selector.keys();
		// 遍历注册在管道选择器上的所有注客户端
		for(SelectionKey key : keys) {
			SelectableChannel clientChannel = key.channel();
			// 不是服务端管道  并且 管道处于正常状态 并且 管道不是自己
			if(!(clientChannel instanceof ServerSocketChannel) && key.isValid() && !socketChannel.equals(clientChannel)) {
				// 将写出缓存区改为写模式
				writeBuffer.clear();
				// 写入数据
				writeBuffer.put(StandardCharsets.UTF_8.encode(message));
				// 将写出缓存区由写模式该为读模式（我们上面在写嘛）
				writeBuffer.flip();
				// 循环一直往外写（只要还有数据没有写完成，我们就一直读一直写）
				while(writeBuffer.hasRemaining()) {
					((SocketChannel)clientChannel).write(writeBuffer);
				}
			}
		}
	}
	
	/**
	 * 启动服务
	 */
	public void start () {
		try {
			// 初始化服务端连接管道
			this.server = ServerSocketChannel.open();
			// 设置服务端管道为非阻塞式调用模式（注意：它默认阻塞式调用模式）
			this.server.configureBlocking(false);
			// 获取socket并绑定端口
			this.server.socket().bind(new InetSocketAddress(PORT));
			
			// 初始化管道管理器
			this.selector = Selector.open();
			// 为服务器管道注册连接事件
			this.server.register(this.selector, SelectionKey.OP_ACCEPT);
			System.out.println("启动服务器，监听端口："+PORT);
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
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if(null != selector) {
					// 注意：关闭管道选择器会自动关闭选择器里面的所有管道连接
					selector.close();
				}
				if(null != server) {
					server.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 事件处理
	 * @param selectionKey
	 */
	public void handles(SelectionKey selectionKey) {
		try { 
			// 处理ACCEPT事件（客户端连接事件）
			if(selectionKey.isAcceptable()) {
				// 获取服务端管道（注意：连接事件所返回的管道一定是服务端的）
				ServerSocketChannel serevrChannel = (ServerSocketChannel)selectionKey.channel();
				// 获取客户端管道
				SocketChannel socketChannel = serevrChannel.accept();
				// 设置客户端管道为非阻塞式调用模式（注意：它默认阻塞式调用模式）
				socketChannel.configureBlocking(false);
				// 为客户端管道注册数据读取事件（就是接收客户端发来的消息）
				socketChannel.register(this.selector, SelectionKey.OP_READ);
				String clientId = getClientId(socketChannel.socket());
				System.out.println("客户端【"+clientId+"】已连接服务器");
			}
			// 处理READ事件（数据读取事件，就是客户端发送了消息）
			if(selectionKey.isReadable()) {
				// 获取客户端管道（注意：数据读取事件所返回的管道一定是客户端的）
				SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
				String clientId = getClientId(socketChannel.socket());
				// 读取客户端发来的消息
				String message = receive(socketChannel);
				// 消息为空或是退出消息（就把客户端管道事件取消掉，让客户端退出）
				if(message.isEmpty() || QUIT_FLAG.equals(message)) {
					// 取消监听该客户端管道READ数据读取事件
					selectionKey.cancel();
					// 通知Selector刷新事件监听，包括把当前阻塞住的select()函数强制返回，重新刷新事件监听（就是我有取消了一个事件监听，你更新一下）
					this.selector.wakeup();
					message = "已退出";
				} 
				// 转发消息到其它客户端
				forwardMessage(socketChannel,"客户端【"+clientId+"】："+message);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 读取客户端发来的消息
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
