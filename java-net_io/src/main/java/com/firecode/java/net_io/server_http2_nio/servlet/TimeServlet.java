package com.firecode.java.net_io.server_http2_nio.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.firecode.java.net_io.server_http1.connector.ConnectorUtil;
import com.firecode.java.net_io.server_http1.connector.HttpStatus;
/**
 * 动态资源，返回服务器当前时间
 */
public class TimeServlet implements Servlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
	}

	@Override
	public ServletConfig getServletConfig() {
		return null;
	}

	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		writer.println(ConnectorUtil.renderStatus(HttpStatus.SC_OK));
		writer.println("What time is it now?");
		String date = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
		writer.println(date);
		//writer.flush();
	}

	@Override
	public String getServletInfo() {
		return null;
	}

	@Override
	public void destroy() {
	}

}
