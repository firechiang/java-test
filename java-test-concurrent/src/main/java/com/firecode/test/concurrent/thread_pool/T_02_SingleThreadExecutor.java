package com.firecode.test.concurrent.thread_pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 单线程线程池
 * 单线程线程池里面只有一个线程，但是任务队列是无界队列，当任务无限多的时候，就会造成内存溢出
 * @author ChiangFire
 */
public class T_02_SingleThreadExecutor {
	
	public static void main(String[] args) throws InterruptedException {
		// （不推荐生产直接这样使用）建议参考其源码手动创建
		ExecutorService es = Executors.newSingleThreadExecutor();
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
