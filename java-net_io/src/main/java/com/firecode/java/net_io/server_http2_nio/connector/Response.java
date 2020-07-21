package com.firecode.java.net_io.server_http2_nio.connector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

/**
 * HTTP/1.1 200 OK
 */
public class Response implements ServletResponse {
	
	private static final int BUFFER_SIZE = 1024;
	
	private Request request;
	
	OutputStream output;
	
	public Response(OutputStream output) {
		this.output = output;
	}
	
	public void setRequest(Request request) {
		this.request = request;
	}
	
	/**
	 * 发送静态资源
	 * @throws IOException 
	 */
	public void sendStaticResource() throws IOException {
		File file = new File(ConnectorUtil.WEB_ROOT,request.getRequestURI());
		try {
			write(file, HttpStatus.SC_OK);
		} catch (IOException e) {
			write(new File(ConnectorUtil.WEB_ROOT,"404.html"),HttpStatus.SC_NOT_FOUND);
		}
	}
	
	private void write(File resource,HttpStatus httpStatus) throws IOException {
		try(FileInputStream fis = new FileInputStream(resource)) {
			// 先写HTTP协议要返回的状态相关信息
			output.write(ConnectorUtil.renderStatus(httpStatus).getBytes(StandardCharsets.UTF_8));
			byte[] buffer = new byte[BUFFER_SIZE];
			int length = 0;
			// 一直读，一直写
			while((length = fis.read(buffer,0,BUFFER_SIZE)) != -1) {
				output.write(buffer, 0, length);
			}
		}
	}

	@Override
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		// 自动 flush
		PrintWriter pw = new PrintWriter(output,true);
		return pw;
	}

	@Override
	public void setCharacterEncoding(String charset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContentLength(int len) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContentLengthLong(long len) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContentType(String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBufferSize(int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getBufferSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void flushBuffer() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetBuffer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCommitted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocale(Locale loc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}
}
