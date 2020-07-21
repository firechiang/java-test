package com.firecode.java.net_io.server_http1.processor;

import java.io.IOException;

import com.firecode.java.net_io.server_http1.connector.Request;
import com.firecode.java.net_io.server_http1.connector.Response;

/**
 * 静态资源处理器
 */
public class StaticProcessor {
	
	public void process(Request request,Response response) {
		try {
			response.sendStaticResource();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
