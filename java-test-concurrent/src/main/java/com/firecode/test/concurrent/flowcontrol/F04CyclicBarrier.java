package com.firecode.test.concurrent.flowcontrol;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier（栏栅，多个线程同时开始执行）简单使用
 * 注意：CyclicBarrier 是可重用的（设置的是5个线程一起触发，现在有10个线程，它会5个5个一起走（也就是凑齐5个就触发））
 */
public class F04CyclicBarrier {
	
	public static void main(String[] args) {
		/**
		 * @param parties        阻塞数量（就是同时阻塞多少个线程，也是多少个线程同时开始执行）
		 * @param barrierAction  开始执行之前的回调
		 */
		CyclicBarrier cyclicBarrier = new CyclicBarrier(5, new Runnable() {
			@Override
			public void run() {
				System.err.println("所有人都到场了， 大家统一出发！");
			}
		});
		for (int i = 0; i < 10; i++) {
			new Thread(new Task(i, cyclicBarrier)).start();
		}
	}

	static class Task implements Runnable {
		private int id;
		private CyclicBarrier cyclicBarrier;

		public Task(int id, CyclicBarrier cyclicBarrier) {
			this.id = id;
			this.cyclicBarrier = cyclicBarrier;
		}

		@Override
		public void run() {
			System.out.println("线程" + id + "现在前往集合地点");
			try {
				Thread.sleep((long) (Math.random() * 10000));
				System.out.println("线程" + id + "到了集合地点，开始等待其他人到达");
				// 进入等待状态
				cyclicBarrier.await();
				System.out.println("线程" + id + "出发了");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
		}
	}
}
