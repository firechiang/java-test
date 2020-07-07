package com.firecode.java.test.thread;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * run函数里面带有sleep和wait函数时并且在循环里面catch线程中断的错误
 * 注意：如果监听到了InterruptedException错误，那么Thread.currentThread().isInterrupted()将会失效（因为发生InterruptedException错误时，线程就会把interrupted标记还原）
 */
public class T_03_StopThread3 {
	
	public static void main(String[] args) throws IOException, InterruptedException {
//		CountDownLatch latch = new CountDownLatch(1);
//		latch.await();
		Thread t = new Thread(()->{
			int num = 0;
			/**
			 * 线程没有被中断并且num小于等于300
			 */
			while(!Thread.currentThread().isInterrupted() && num <= 300) {
				System.out.println("num="+num+"是不是1000的倍数："+(num % 1000 == 0));
			    try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.err.println("线程被打断异常："+e);
					// 注意：如果不在这里再次调用中断函数，Thread.currentThread().isInterrupted() 函数将是失效的，因为发生InterruptedException错误时，线程就会把interrupted标记还原
					//Thread.currentThread().interrupt();
				}
				num++;
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
