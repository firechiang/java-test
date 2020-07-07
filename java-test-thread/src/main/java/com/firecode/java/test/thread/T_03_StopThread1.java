package com.firecode.java.test.thread;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * run函数里面没有使用sleep和wait函数时停止线程的方式
 * 注意：interrupt()函数实际上是一个和线程交互的动作（就是线程里面使用isInterrupted()函数，判断外面是否有调用interrupt()函数中断线程）
 */
public class T_03_StopThread1 {
	
	public static void main(String[] args) throws IOException, InterruptedException {
//		CountDownLatch latch = new CountDownLatch(1);
//		latch.await();
		Thread t = new Thread(()->{
			int num = 0;
			/**
			 * 线程没有被中断并且num小于等于Integer.MAX_VALUE / 2
			 */
			while(!Thread.currentThread().isInterrupted() && num <= Integer.MAX_VALUE / 2) {
				System.err.println("num="+num+"是不是1000的倍数："+(num % 1000 == 0));
				num++;
			}
		});
		t.start();
		TimeUnit.SECONDS.sleep(1);
		// 中断线程
		t.interrupt();
		// 线程是否被中断（注意：这个函数所返回的中断状态是当前执行这个函数的线程的，而不是t对象线程的（也就是：t.interrupted() == Thread.interrupted()））
		//t.interrupted();
	}
}
