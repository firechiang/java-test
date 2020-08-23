package com.firecode.test.concurrent.thread_pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池简单使用
 * Executors工具类创建线程池（注意：实际生产当中不建议使用 Executors 工具类创建线程池，因为它没有限制队列的数量，可能会导致内存溢出）
 * @author JIANG
 */
public class T_01_ThreadPoolExecutor extends ThreadPoolExecutor {
	
	public T_01_ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// CPU 数量
		int coreProcessors = Runtime.getRuntime().availableProcessors();
		/** 
		 * 创建线程池（建议生产使用）
		 * 1，如果线程数小于corePoolSize即使其他线程处于空闲状态，也会创建新的线程来执行任务
		 * 2，如果线程数等于（大于）corePoolSize但小于maximumPoolSize则将任务放入队列
		 * 3，如果队列已满，并且线程数小于maximumPoolSize，则创建一个新线程来运行任务
		 * 4，如果队列已满，并且线程数大于等于maximumPoolSize，则拒绝改任务
		 * 
		 * @param corePoolSize     核心线程数
		 * （CPU密集型计算建议使用 CPU核数加1或乘以2）
		 * （IO密集型计算建议使用 CPU核数除以(1-0.9)，0.9是阻塞系数一般是0.8到0.9之间）或 线程数量=CPU核心数  * （1 + 平均等待事件（等待数据库）/平均工作时间（等待过后的代码执行时间））
		 * @param maximumPoolSize  最大线程数
		 * @param keepAliveTime    线程空闲了，多久之后将其回收（注意：只回收大于corePoolSize的线程数量，也就是只回收多出来的线程）
		 * @param unit             线程空闲了，多久之后将其回收的时间单位
		 * @param workQueue        线程队列（建议使用有界队列）
		 * @param threadFactory    线程工厂
		 * @param handler          当线程队列满了的时候我们的拒绝策略（注意：如果线程队列是无界队列的话，是不会执行拒绝策略的）
		 * 
		 */
		ThreadPoolExecutor pool = new ThreadPoolExecutor(coreProcessors,
				                                         coreProcessors * 2, 
				                                         60, 
				                                         TimeUnit.SECONDS, 
				                                         new ArrayBlockingQueue<>(100),
				                                         Executors.defaultThreadFactory(),
				                                         (Runnable r, ThreadPoolExecutor executor)->{
				                                             System.err.println("当线程队列满了，拒绝了");
		                                                 });
		System.err.println(pool);
		// 关闭线程池
		pool.shutdown();
	}

	/**
	 * 线程池里面的线程开始执行前执行
	 */
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		System.err.println("线程开始执行");
	}

	/**
	 * 线程池里面的线程执行完成后执行
	 */
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		System.err.println("线程执行结束");
	}
	
}
