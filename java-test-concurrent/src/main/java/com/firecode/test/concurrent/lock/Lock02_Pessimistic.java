package com.firecode.test.concurrent.lock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 悲观锁和乐观锁简单示列
 * select for update 就是悲观锁就会一直锁住记录，直到提交
 */
public class Lock02_Pessimistic {
	
	static Lock lock = new ReentrantLock();
	
	public static void main(String[] args) {
		// 悲观锁（获取锁，没获取到就等待）
		lock.lock();
		try {
			System.err.println("获取到了锁");
		}finally {
			// 释放锁
			lock.unlock();
		}
		
		// 使用原子变量来实现乐观锁
		AtomicInteger in = new AtomicInteger();
		// 递增并返回递增后的值（递增成功表示获取到锁）
		int val = in.incrementAndGet();
		System.err.println(val);
	}

}
