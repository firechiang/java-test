package com.firecode.java.test.thread;

/**
 * 演示必然会发生死锁的情况
 */
public class T_10_Lock_Deadlock implements Runnable {
	
	int flag = 1;
	
	static Object lock1 = new Object();
	static Object lock2 = new Object();
	
	@Override
	public void run() {
		System.out.println("flag="+flag);
		if(flag == 1) {
			synchronized (lock1) {
				System.err.println("获取到锁lock1");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (lock2) {
					System.err.println("获取到锁lock2");
				}
			}
		}
		if(flag == 0) {
			synchronized (lock2) {
				System.err.println("获取到锁lock2");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (lock1) {
					System.err.println("获取到锁lock1");
				}
			}
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		T_10_Lock_Deadlock l1 = new T_10_Lock_Deadlock();
		T_10_Lock_Deadlock l2 = new T_10_Lock_Deadlock();
		l1.flag = 1;
		l2.flag = 0;
		Thread t1 = new Thread(l1);
		Thread t2 = new Thread(l2);
		t1.start();
		t2.start();
	}


}
