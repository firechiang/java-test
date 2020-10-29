package com.firecode.test.concurrent.thread_pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 具有子任务的线程池（就是一个任务还有子任务，比如树状结构的任务。注意：这种线程池的任务执行是没有顺序的，而且子任务和父任务可能同时执行）
 * 
 * @author 37982
 *
 */
public class T_05_WorkStealingPool {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService pool = Executors.newWorkStealingPool();
		for (int i = 0; i < 10; i++) {
			pool.execute(() -> {
				try {
					System.out.println(Thread.currentThread().getName());
				} catch (Exception e) {
					System.out.println(e);
				}
			});
		}
		System.err.println("线程池是调用了shutdown()函数："+pool.isShutdown());
		System.err.println("线程池是否完全停止了："+pool.isTerminated());
		System.err.println("线程等待了三秒后，线程池是否已经完全停止:"+pool.awaitTermination(3, TimeUnit.SECONDS));
		// 立即停止线程池（返回未执行的任务）
		//pool.shutdownNow();
	}
}
