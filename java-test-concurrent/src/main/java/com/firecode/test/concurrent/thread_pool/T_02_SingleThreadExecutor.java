package com.firecode.test.concurrent.thread_pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 单线程线程池
 * 单线程线程池里面只有一个线程，但是任务队列是无界队列，当任务无限多的时候，就会造成内存溢出
 * @author ChiangFire
 */
public class T_02_SingleThreadExecutor {
	
	public static void main(String[] args) {
		// （不推荐生产直接这样使用）建议参考其源码手动创建
		ExecutorService es = Executors.newSingleThreadExecutor();
		for(int i=0;i<1000;i++) {
			es.execute(() ->{
				System.err.println(Thread.currentThread().getName());
			});
		}
	}
}
