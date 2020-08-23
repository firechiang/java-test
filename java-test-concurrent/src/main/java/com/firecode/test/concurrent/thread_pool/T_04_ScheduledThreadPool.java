package com.firecode.test.concurrent.thread_pool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 调度线程池（它可以定时执行某个任务）
 * 这个线程池的最大线程数是Integer.MAX_VALUE，但任务越来越多时就是内存溢出
 * @author ChiangFire
 *
 */
public class T_04_ScheduledThreadPool {
	
	public static void main(String[] args) {
		ScheduledExecutorService es = Executors.newScheduledThreadPool(10);
		for(int i=0;i<1000;i++) {
			es.execute(() ->{
				System.err.println(Thread.currentThread().getName());
			});
		}
	}

}
