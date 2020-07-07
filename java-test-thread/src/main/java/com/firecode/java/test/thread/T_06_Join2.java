package com.firecode.java.test.thread;

import java.util.concurrent.TimeUnit;

/**
 * 演示 join() 函数
 * join 函数的作用就是等该线程（调用join()函数的线程）执行完毕后才继续执行当前线程
 * 注意：join()函数实际上是使用wait()进行等待的，而唤醒是由JVM来做的，因为JVM在线程执行完成后，都会执行一个唤醒join的操作
 */
public class T_06_Join2 {
	
	public static void main(String[] args) {
		Thread thread = new Thread(() -> {
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.err.println("新线程执行完毕");
		});
		thread.start();
		// 等待1秒，如果没有执行完成就直接过，不等了
		try {
			thread.join(1000);
		} catch (InterruptedException e) {
			System.err.println("主线程被打断："+e);
		}
		System.out.println("主线程执行完毕");
	}
}
