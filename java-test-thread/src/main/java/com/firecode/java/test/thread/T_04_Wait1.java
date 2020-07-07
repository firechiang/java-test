package com.firecode.java.test.thread;

import java.util.concurrent.TimeUnit;

/**
 * wait()和notify()函数的简单使用
 */
public class T_04_Wait1 {
	
	public static void main(String[] args) throws InterruptedException {
		Object o = new Object();
		new Thread(() -> {
			synchronized (o) {
				System.out.println("线程1开始执行了");
				try {
					o.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("线程1获取到锁");
			}
		}).start();
		
		TimeUnit.SECONDS.sleep(1);
		
		new Thread(() -> {
			synchronized (o) {
				o.notify();
				System.err.println("线程2调用notify函数");
			}
		}).start();
	}
}
