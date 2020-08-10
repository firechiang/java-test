package com.firecode.java.test.thread;

/**
 * 子线程未捕获异常的处理（就是在线程里面发生了异常而我们代码里面并没有使用try catch去捕获）
 */
public class T_07_UncaugException {
	
	public static void main(String[] args) {
		
		Thread thread = new Thread(()->{
			throw new RuntimeException("测试错误");
		});
		Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.err.println("线程出现了异常："+e);
			}
		});
		thread.start();
		
		for (int i = 0; i < 10; i++) {
			System.err.println(i);
		}
	}
}
