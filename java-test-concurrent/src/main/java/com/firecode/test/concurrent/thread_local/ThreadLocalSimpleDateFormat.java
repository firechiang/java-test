package com.firecode.test.concurrent.thread_local;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 每个线程独有一个SimpleDateFormcat实列
 * 
 */
public class ThreadLocalSimpleDateFormat {
	
	static ThreadLocal<SimpleDateFormat> threadLocal1 = ThreadLocal.withInitial(()->{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	});
	
	/**
	 * 这个写法和上面的一致
	 */
	static ThreadLocal<SimpleDateFormat> threadLocal2 = new ThreadLocal<SimpleDateFormat>() {
		/**
		 * 当获取数据为空时会调用该方法
		 */
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};
	
	
	public static void main(String[] args) {
		SimpleDateFormat sdf = threadLocal1.get();
		System.err.println(sdf.format(new Date()));
	}
}
