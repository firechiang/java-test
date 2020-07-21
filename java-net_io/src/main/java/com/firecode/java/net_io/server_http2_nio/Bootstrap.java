package com.firecode.java.net_io.server_http2_nio;

import com.firecode.java.net_io.server_http2_nio.connector.Connector;

/**
 * 启动服务
 */
public class Bootstrap {
	
	public static void main(String[] args) {
		Connector connector = new Connector();
		connector.start();
	}

}
