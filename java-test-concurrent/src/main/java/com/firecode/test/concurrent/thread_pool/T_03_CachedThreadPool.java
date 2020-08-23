package com.firecode.test.concurrent.thread_pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 缓存线程池
 * 这个线程池使用的任务队列是SynchronousQueue，它是一个交换队列，最大容量是1，也就是每来一个任务就会创建一个线程来执行，
 * 它会无限创建线程，任务无限多就会内存溢出（当然线程空闲了，它也会回收）
 * 
 * @author ChiangFire
 *
 */
public class T_03_CachedThreadPool {
	
	public static void main(String[] args) {
		ExecutorService es = Executors.newCachedThreadPool();
		for(int i=0;i<1000;i++) {
			es.execute(() ->{
				System.err.println(Thread.currentThread().getName());
			});
		}
	}

}
