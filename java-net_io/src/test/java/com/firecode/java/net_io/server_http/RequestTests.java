package com.firecode.java.net_io.server_http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.firecode.java.net_io.server_http1.connector.Request;

import junit.framework.Assert;

public class RequestTests {
	
	@Test
	public void test1() {
		InputStream inputStream = new ByteArrayInputStream("GET index.html HTTP/1.1".getBytes());
		Request request = new Request(inputStream);
		request.parse();
		Assert.assertEquals("index.html", request.getRequestURI());
	}
}
