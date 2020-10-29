package com.firecode.test.concurrent.thread_pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 缓存线程池
 * 这个线程池使用的任务队列是SynchronousQueue，它是一个交换队列，最大容量是1，也就是每来一个任务就会创建一个线程来执行，
 * 它会无限创建线程，任务无限多就会内存溢出（当然线程空闲了，它也会回收）
 * 
 * @author ChiangFire
 *
 */
public class T_03_CachedThreadPool {
	
	public static void main(String[] args) throws InterruptedException {
		ExecutorService es = Executors.newCachedThreadPool();
		for(int i=0;i<1000;i++) {
			es.execute(() ->{
				System.err.println(Thread.currentThread().getName());
			});
		}
		System.err.println("线程池是调用了shutdown()函数："+es.isShutdown());
		System.err.println("线程池是否完全停止了："+es.isTerminated());
		System.err.println("线程等待了三秒后，线程池是否已经完全停止:"+es.awaitTermination(3, TimeUnit.SECONDS));
		// 立即停止线程池（返回未执行的任务）
		//pool.shutdownNow();
	}

}
