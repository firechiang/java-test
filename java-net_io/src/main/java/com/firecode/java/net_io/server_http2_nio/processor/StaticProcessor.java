package com.firecode.java.net_io.server_http2_nio.processor;

import java.io.IOException;

import com.firecode.java.net_io.server_http2_nio.connector.Request;
import com.firecode.java.net_io.server_http2_nio.connector.Response;

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
