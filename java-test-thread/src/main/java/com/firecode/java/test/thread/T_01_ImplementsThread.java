package com.firecode.java.test.thread;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 实现线程的两种方式
 */
public class T_01_ImplementsThread {
	
	static CountDownLatch latch = new CountDownLatch(2);
	
	public static void main(String[] args) throws IOException, InterruptedException {
		ThreadThread threadThread = new ThreadThread();
		Thread runnableThread = new Thread(new RunnableThread());
		threadThread.start();
		// 注意：一个线程不能调用2次start()函数，否则第二次调用时会报错，因为线程状态改变了
		//threadThread.start();
		runnableThread.start();
		latch.await();
	}
	
	/**
	 * 继承Thread类
	 */
	private static class ThreadThread extends Thread {

		@Override
		public void run() {
			latch.countDown();
			System.out.println(ThreadThread.class.getSimpleName()+": "+Thread.currentThread().getName());
		}
	}
	
	/**
	 * 实现Runnable接口
	 */
	private static class RunnableThread implements Runnable {
		@Override
		public void run() {
			latch.countDown();
			System.out.println(RunnableThread.class.getSimpleName()+": "+Thread.currentThread().getName());
		}
	}
}
