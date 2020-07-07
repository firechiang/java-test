package com.firecode.java.test.thread;

import java.util.concurrent.TimeUnit;

/**
 * 演示Sleep函数不释放锁的问题
 */
public class T_05_Sleep {
	public static void main(String[] args) throws InterruptedException {
		ObjectSleep o = new ObjectSleep();
		
		new Thread(()->{
			synchronized (o) {
				try {
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		TimeUnit.SECONDS.sleep(2);
		System.err.println("2秒等待完成");
		// 注意：2秒打印完成以后，如果没有立马打印 "我执行了" 说明sleep()函数没有释放锁
		o.aa();
	}
	
	private static class ObjectSleep {
		public synchronized void aa() {
			System.out.println("我执行了");
		}
	}
}
