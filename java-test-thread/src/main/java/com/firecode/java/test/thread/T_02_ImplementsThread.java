package com.firecode.java.test.thread;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
/**
 * 两种实现线程的方式同时使用
 */
public class T_02_ImplementsThread {
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				System.err.println("Runnable: "+Thread.currentThread().getName());
			}
		}) {
            /**
             * 重写的run函数（注意：最后线程只会调用这个函数）
             */
			@Override
			public void run() {
				latch.countDown();
				System.err.println("Thread: "+Thread.currentThread().getName());
			}
		};
		thread.start();
		latch.await();
	}
}
