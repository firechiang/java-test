package com.firecode.java.test.thread;

import java.util.concurrent.TimeUnit;

/**
 * 演示死锁
 */
public class T_09_Lock_Deadlock {
	
	static Object lock1 = new Object();
	static Object lock2 = new Object();
	
	public static void main(String[] args) throws InterruptedException {
		
		Thread thread1 = new Thread(() -> {
			synchronized (lock1) {
				System.err.println("线程1获取到锁1");
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (lock2) {
					System.err.println("线程1获取到锁2");
				}
			}
		});
		
		Thread thread2 = new Thread(() -> {
			synchronized (lock2) {
				System.err.println("线程2获取到锁2");
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (lock1) {
					System.err.println("线程2获取到锁1");
				}
			}
		});
		
		thread1.start();
		thread2.start();
	}
}
