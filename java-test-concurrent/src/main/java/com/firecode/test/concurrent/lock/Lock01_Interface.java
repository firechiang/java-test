package com.firecode.test.concurrent.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock（锁）接口简要说明（实现Lock（锁）接口的锁是可中断的锁）
 */
public class Lock01_Interface {
	
	/**
	 * ReentrantLock 是可重入锁也是互斥锁
	 */
	static Lock lock = new ReentrantLock();
	
	public static void main(String[] args) {
		boolean tryLock = lock.tryLock();
		try {
			// 获取锁成功
			if(tryLock) {
				System.err.println("获取到锁了");
			}
		} finally {
			// 释放锁
			lock.unlock();
		}
		
		try {
			// 获取锁（如果锁已被其它线程所获取，就等待）但是可以使用 Thread.interrupt()打断等待
			lock.lockInterruptibly();
		} catch (InterruptedException e) {
			System.err.println("等待锁被打断");
		} finally {
			// 释放锁
			lock.unlock();
		}
	}
	
	@SuppressWarnings("unused")
	private class MyLock implements Lock {
		/**
		 * 释放锁
		 */
		@Override
		public void unlock() {
		}
		
		/**
		 * 获取锁并指定等待时间（如果获取成功就返回true，获取失败就返回false），如果等待超时也返回false
		 */
		@Override
		public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
			return false;
		}
		
		/**
		 * 获取锁立即返回（如果获取成功就返回true，获取失败就返回false）
		 */
		@Override
		public boolean tryLock() {
			return false;
		}
		
		@Override
		public Condition newCondition() {
			return null;
		}
		/**
		 * 获取锁（如果锁已被其它线程所获取，就等待）但是可以使用 Thread.interrupt()打断等待
		 */
		@Override
		public void lockInterruptibly() throws InterruptedException {
		}
		/**
		 * 正常获取锁（如果锁已被其它线程所获取，就等待）这个等待是无限的而且不能打断，直到获取到锁
		 */
		@Override
		public void lock() {
		}
		
	}
}
