package com.firecode.test.concurrent.future;

/**
 * 演示在run方法中无法抛出checked Exception（就是需要try catch的Exception，不能抛出，只能try catch）
 * 注意：Callable 接口的 call 函数是可以抛出异常的
 */
public class F01RunnableCantThrowsException {
	
	public void ddd() throws Exception {
		
	}

	public static void main(String[] args) {
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// 只能try catch异常
				try {
					throw new Exception();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}
}
