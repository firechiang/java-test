package com.firecode.test.concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 重入锁简要说明
 * 可重入锁是指如果获取到这把锁了，在执行逻辑时又要获取这把锁，是可以直接重用的，而不是要等这把锁释放（注意：synchronized 也是可重入锁，但是是不可中断的锁。实现Lock（锁）接口的锁是可中断的锁）
 */
public class Lock03_Reentry {
	
	static ReentrantLock lock = new ReentrantLock();
	
	public static void main(String[] args) {
		// 注意：去除锁打印就会混乱
		OutPrint outPrint = new OutPrint();
		new Thread(()-> {
			for(int i=0;i<3;i++) {
				outPrint.print("第三方快递");
			}
		}).start();
		new Thread(()-> {
			for(int i=0;i<3;i++) {
				outPrint.print("认真");
			}
		}).start();
		System.err.println("当前线程是否获取锁："+lock.isHeldByCurrentThread());
		System.err.println("当前等待锁的队列有多长："+lock.getQueueLength());
		// 获取锁
		lock.lock();
		// 打印锁被获取的次数（注意：lock一次加1）
		System.err.println(lock.getHoldCount());
		// 获取锁
		lock.lock();
		// 打印锁被获取的次数（注意：lock一次加1）
		System.err.println(lock.getHoldCount());
		// 获取锁
		lock.lock();
		// 打印锁被获取的次数（注意：lock一次加1）
		System.err.println(lock.getHoldCount());
		// 解锁
		lock.unlock();
		// 打印锁被获取的次数（注意：unlock一次减1）
		System.err.println(lock.getHoldCount());
		// 解锁
		lock.unlock();
		// 打印锁被获取的次数（注意：unlock一次减1）
		System.err.println(lock.getHoldCount());
		// 解锁
		lock.unlock();
		// 打印锁被获取的次数（注意：unlock一次减1）
		System.err.println(lock.getHoldCount());
	}
	
	
	/**
	 * 重入锁的基本使用
	 */
	private static class OutPrint {
		
		static Lock lock = new ReentrantLock();
		
		public void print(String str) {
			int len = str.length();
			// 悲观锁（获取锁，没获取到就等待）
			lock.lock();
			try {
				for(int i=0;i<len;i++) {
					System.out.print(str.charAt(i));
				}
				System.out.println();
			}finally {
				// 释放锁
				lock.unlock();
			}
		}
	}
}
