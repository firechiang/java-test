package com.firecode.java.test.thread;

import java.util.concurrent.TimeUnit;

/**
 * join() 函数的用法
 * join 函数的作用就是等该线程（调用join()函数的线程）执行完毕后才继续执行当前线程
 * 注意：join()函数实际上是使用wait()进行等待的，而唤醒是由JVM来做的，因为JVM在线程执行完成后，都会执行一个唤醒join的操作
 */
public class T_06_Join1 {
	
	public static void main(String[] args) throws InterruptedException {
		Thread thread = new Thread(()->{
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.err.println("新线程执行完毕");
		});
		thread.start();
		thread.join();
		System.out.println("主线程执行完毕");
	}
}
