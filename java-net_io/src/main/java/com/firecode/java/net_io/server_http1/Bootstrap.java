package com.firecode.java.net_io.server_http1;

import com.firecode.java.net_io.server_http1.connector.Connector;

/**
 * 启动服务
 */
public class Bootstrap {
	
	public static void main(String[] args) {
		Connector connector = new Connector();
		connector.start();
	}

}
