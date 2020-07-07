package com.firecode.java.test.thread;

import java.util.concurrent.TimeUnit;

/**
 * 使用wait()和notify()函数实现两个线程交替打印基数和偶数
 */
public class T_04_Wait2 {
	
	public static void main(String[] args) throws InterruptedException {
		Object o = new Object();
		new Thread(() -> {
			synchronized (o) {
				for (int i = 0; i < 100; i++) {
					try {
						if(i % 2 == 0) {
							TimeUnit.SECONDS.sleep(1);
							System.out.println(Thread.currentThread().getName()+": i = "+i);
							o.notify();
						}else {
						    o.wait();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		new Thread(() -> {
			synchronized (o) {
				for (int i = 1; i < 100; i++) {
					try {
						if(i % 2 != 0) {
							TimeUnit.SECONDS.sleep(1);
							System.err.println(Thread.currentThread().getName()+": i = "+i);
							o.notify();
						}else {
						    o.wait();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
