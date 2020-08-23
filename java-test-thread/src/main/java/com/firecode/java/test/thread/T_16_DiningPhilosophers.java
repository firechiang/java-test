package com.firecode.java.test.thread;
/**
 * 改变一个哲学家拿筷子的顺序策略：就是一般都是先拿左边再拿右边，如果有一个哲学家是先拿右边再拿左边，就不会造成环路问题，就不会造成死锁
 */
public class T_16_DiningPhilosophers {
	
	public static void main(String[] args) {
		// 5 为哲学家
		Philosopher[] ps = new Philosopher[5];
		// 5 根筷子
		Object[] chopstichs = new Object[ps.length];
		// 初始化筷子
		for (int i = 0; i < chopstichs.length; i++) {
			chopstichs[i] = new Object();
		}
		for (int i = 0; i < chopstichs.length; i++) {
			// 左边的筷子
			Object leftChopstich = chopstichs[i];
			// 右边的筷子的下标（注意：如果是最后一个人，它右边的筷子就是第一个，因为是个圆圈嘛）
			int rightIndex = (i + 1) == chopstichs.length ? 0 : (i + 1);
			// 右边的筷子
			Object rightChopstich = chopstichs[rightIndex];
			// 哲学家
			Philosopher p = new Philosopher(leftChopstich,rightChopstich);
			// 改变最后一个哲学家拿筷子的顺序（先拿右边再拿左边）
			if((i + 1) == chopstichs.length) {
				p = new Philosopher(rightChopstich,leftChopstich);
			}
			// 启动线程
			new Thread(p,(i+1)+"号哲学家").start();
		}
	}
	
	
	/**
	 * 哲学家
	 */
	private static class Philosopher implements Runnable {
		// 左边的筷子
		private Object leftChopstich;
		// 右边的筷子
		private Object rightChopstich;
		
		public Philosopher(Object leftChopstich, Object rightChopstich) {
			super();
			this.leftChopstich = leftChopstich;
			this.rightChopstich = rightChopstich;
		}

		@Override
		public void run() {
			doAction("开始思考");
			// 拿到左边的筷子（就是锁）
			synchronized (leftChopstich) {
				doAction("拿到了左边的筷子");
				// 拿到右边的筷子（就是锁）
				synchronized (rightChopstich) {
					doAction("拿到了右边的筷子");
				}
			}
			doAction("放下左右两边的筷子");
		}
		
		private void doAction(String action) {
			System.out.println(Thread.currentThread().getName()+action);
			// 随机休眠
			try {
				Thread.sleep((long)Math.random() * 100);
			} catch (InterruptedException e) {
				System.err.println("线程被打断："+e);
			}
		}
	}
}
