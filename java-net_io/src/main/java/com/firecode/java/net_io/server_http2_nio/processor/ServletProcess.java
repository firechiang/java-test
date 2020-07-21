package com.firecode.java.net_io.server_http2_nio.processor;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.firecode.java.net_io.server_http2_nio.connector.ConnectorUtil;
import com.firecode.java.net_io.server_http2_nio.connector.Request;
import com.firecode.java.net_io.server_http2_nio.connector.RequestFacade;
import com.firecode.java.net_io.server_http2_nio.connector.Response;
import com.firecode.java.net_io.server_http2_nio.connector.ResponseFacade;
import com.firecode.java.net_io.server_http2_nio.servlet.TimeServlet;

/**
 * Servlet处理器
 */
public class ServletProcess {
	
	/**
	 * 获取一个只加载某个路径的Class文件的ClassLoader
	 * @return
	 * @throws MalformedURLException
	 */
	URLClassLoader getServletLoader() throws MalformedURLException {
		File webroot = new File(ConnectorUtil.WEB_ROOT);
		URL webrootUrl = webroot.toURI().toURL();
		return new URLClassLoader(new URL[]{webrootUrl});
	}
	
    @SuppressWarnings("unused")
	Servlet getServlet(ClassLoader loader,Request request) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		String uri = request.getRequestURI();
		String servletName = uri.substring(uri.lastIndexOf("/") + 1);
		//Class<?> servletClass = loader.loadClass(servletName);
		//Servlet servlet = (Servlet)servletClass.newInstance();
		return new TimeServlet();
	}
	
	public void process(Request request,Response response) throws MalformedURLException {
		ClassLoader loader = getServletLoader();
		try {
			Servlet servlet = getServlet(loader,request);
			RequestFacade requestFacade = new RequestFacade(request);
			ResponseFacade responseFacade = new ResponseFacade(response);
			servlet.service(requestFacade, responseFacade);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ServletException | IOException e) {
			e.printStackTrace();
		}
	}

}
