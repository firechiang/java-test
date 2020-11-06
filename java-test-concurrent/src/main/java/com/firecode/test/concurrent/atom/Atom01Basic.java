package com.firecode.test.concurrent.atom;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基本原子类的简单使用
 */
public class Atom01Basic {
	
	
	public static void main(String[] args) throws InterruptedException {
		TestInt testInt = new TestInt();
		Thread thread1 = new Thread(testInt);
		Thread thread2 = new Thread(testInt);
		
		thread1.start();
		thread2.start();
		
		thread1.join();
		thread2.join();
		
		System.err.println(TestInt.atomInt);
		System.err.println(TestInt.basicInt);
	}
	
	private static class TestInt implements Runnable {
		
		static AtomicInteger atomInt = new AtomicInteger();
		static int basicInt = 0;
		
		@Override
		public void run() {
			for (int i = 0; i < 100; i++) {
				basicIn();
				atomIn();
			}
		}
		
		/**
		 * 原子类累加1
		 */
		private void atomIn() {
			atomInt.getAndIncrement();
			// 添加指定数值（注意：传入负数就是减）
			// atomInt.addAndGet(10);
		}
		
		/**
		 * 基础数据类型累加1
		 */
		public void basicIn() {
			basicInt += 1;
		}
	}
}
