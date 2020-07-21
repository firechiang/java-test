package com.firecode.java.net_io.server_http2_nio.connector;

public enum HttpStatus {
	SC_OK(200,"OK"),
	SC_NOT_FOUND(404,"File Not Found");
	
	private int statusCode;
	private String reason;
	
	HttpStatus(int statusCode,String reason) {
		this.statusCode = statusCode;
		this.reason = reason;
	}
	
	public int getStatusCode() {
		return this.statusCode;
	}
	
	public String getReason() {
		return this.reason;
	}

}
