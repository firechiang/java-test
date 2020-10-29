package com.firecode.test.concurrent.thread_pool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 调度线程池（它可以定时执行某个任务）
 * 这个线程池的最大线程数是Integer.MAX_VALUE，但任务越来越多时就是内存溢出
 * @author ChiangFire
 *
 */
public class T_04_ScheduledThreadPool {
	
	public static void main(String[] args) throws InterruptedException {
		ScheduledExecutorService es = Executors.newScheduledThreadPool(10);
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
