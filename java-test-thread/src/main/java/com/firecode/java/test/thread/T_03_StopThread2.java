package com.firecode.java.test.thread;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * run函数里面带有sleep和wait函数时停止线程的方式
 */
public class T_03_StopThread2 {
	
	public static void main(String[] args) throws IOException, InterruptedException {
//		CountDownLatch latch = new CountDownLatch(1);
//		latch.await();
		Thread t = new Thread(()->{
			try {
				int num = 0;
				while(num <= 300) {
					System.out.println("num="+num+"是不是1000的倍数："+(num % 1000 == 0));
				    TimeUnit.SECONDS.sleep(5);
					num++;
				}
			} catch (InterruptedException e) {
				System.err.println("线程被打断异常："+e);
			}
		});
		
		t.start();
		TimeUnit.SECONDS.sleep(2);
		// 中断线程（注意：这个个函数一调用，只要线程处于睡眠状态，就会抛出 InterruptedException（线程被打断的异常））
		t.interrupt();
		// 线程是否被中断（注意：这个函数所返回的中断状态是当前执行这个函数的线程的，而不是t对象线程的（也就是：t.interrupted() == Thread.interrupted()））
		//t.interrupted();
	}
}
