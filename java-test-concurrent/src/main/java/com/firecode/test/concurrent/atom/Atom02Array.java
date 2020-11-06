package com.firecode.test.concurrent.atom;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 原子数组简单使用（注意：原子数组是指数组里面的每一个元素都可以原子更新的）
 */
public class Atom02Array {
	
	public static void main(String[] args) throws InterruptedException {
		// 创建一个拥有100个原子int类型数值的数组
		AtomicIntegerArray array = new AtomicIntegerArray(10);
		// 递增
		Increnentr incr = new Increnentr(array);
		// 递减
		Decrenentr decr = new Decrenentr(array);
		int threadLength = 100;
		Thread[] incrThreads = new Thread[threadLength];
		Thread[] decrThreads = new Thread[threadLength];
		
		for(int i=0;i<threadLength;i++) {
			incrThreads[i] = new Thread(incr);
			decrThreads[i] = new Thread(decr);
			incrThreads[i].start();
			decrThreads[i].start();
		}
		for(int i=0;i<threadLength;i++) {
			incrThreads[i].join();
			decrThreads[i].join();
		}
		// 打印最后的结果
		System.err.println(array);
	}
	
	
	static class Increnentr implements Runnable {
		
		AtomicIntegerArray array;
		
		Increnentr(AtomicIntegerArray array) {
			this.array = array;
		}

		@Override
		public void run() {
			for (int i = 0; i < array.length(); i++) {
				// 原子数组里面的第i个元素递增
				array.getAndIncrement(i);
			}
		}
	}
	
	static class Decrenentr implements Runnable {
		
		AtomicIntegerArray array;
		
		Decrenentr(AtomicIntegerArray array) {
			this.array = array;
		}

		@Override
		public void run() {
			for (int i = 0; i < array.length(); i++) {
				// 原子数组里面的第i个元素递减
				array.getAndDecrement(i);
			}
		}
	}
}
