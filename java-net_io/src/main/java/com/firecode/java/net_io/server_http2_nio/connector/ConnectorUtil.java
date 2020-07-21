package com.firecode.java.net_io.server_http2_nio.connector;

import java.io.File;

public class ConnectorUtil {
	/**
	 * 静态资源根目录
	 */
	public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webapp";
	
	public static final String PROTOCOL = "HTTP/1.1";
	
	public static final String CARRIAGE = "\r";
	
	public static final String NEWLINE = "\n";
	
	public static final String SPACE = " ";
	
	/**
	 * HTTP协议返回的状态相关信息（注意：状态信息是固定格式的，而且在状态信息后面跟着的是具体要返回的数据）
	 * @param httpStatus
	 * @return
	 */
	public static String renderStatus(HttpStatus httpStatus) {
		StringBuilder sb = new StringBuilder(PROTOCOL)
				               .append(SPACE)
				               .append(httpStatus.getStatusCode())
				               .append(SPACE)
				               .append(httpStatus.getReason())
				               .append(CARRIAGE).append(NEWLINE)
				               .append(CARRIAGE).append(NEWLINE);
				               
		return sb.toString();
	}
}
