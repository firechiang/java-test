package com.firecode.java.test.thread;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 演示使用获取锁的超时时间来解决死锁问题
 */
public class T_17_TryLockDeadlock implements Runnable {
	
	int flag = 1;
	
	static Lock lock1 = new ReentrantLock();
	static Lock lock2 = new ReentrantLock();

	@Override
	public void run() {
		try{
			if(flag == 0) {
				// 获取锁1且超时是1秒
				if(lock1.tryLock(1L, TimeUnit.SECONDS)) {
					// 随机随眠
					Thread.sleep(new Random().nextInt(1000));
					// 获取锁2且超时是1秒
					if(lock2.tryLock(1L, TimeUnit.SECONDS)) {
						System.out.println("flag=0 成功的获取到了两把锁");
						// 释放锁
						lock2.unlock();
						lock1.unlock();
					}else{
						System.err.println("线程获取锁2失败");
						// 释放锁
						lock1.unlock();
					}
				}else{
					System.err.println("线程获取锁1失败");
				}
			}
			if(flag == 1) {
				// 获取锁2且超时是1秒
				if(lock2.tryLock(1L, TimeUnit.SECONDS)) {
					// 随机随眠
					Thread.sleep(new Random().nextInt(1000));
					// 获取锁1且超时是1秒
					if(lock1.tryLock(1L, TimeUnit.SECONDS)) {
						System.out.println("flag=1 成功的获取到了两把锁");
						// 释放锁
						lock1.unlock();
						lock2.unlock();
					}else{
						System.err.println("线程获取锁1失败");
						// 释放锁
						lock2.unlock();
					}
				}else{
					System.err.println("线程获取锁2失败");
				}
			}
		} catch(InterruptedException e) {
			System.err.println("线程被打断");
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		T_17_TryLockDeadlock l1 = new T_17_TryLockDeadlock();
		T_17_TryLockDeadlock l2 = new T_17_TryLockDeadlock();
		l1.flag = 1;
		l2.flag = 0;
		Thread t1 = new Thread(l1);
		Thread t2 = new Thread(l2);
		t1.start();
		t2.start();
	}

}
