package com.firecode.test.concurrent.thread_local;

/**
 * ThreadLocal里面空指针异常的演示和解决方案
 */
public class ThreadLocalNullPointException {
	
	static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
	
	public static void main(String[] args) {
		System.err.println(get());
	}
	
	
	/**
	 * 注意：如果ThreadLocal里面没有值这个函数就是报空指针异常，因为ThreadLocal里面取出来的值是包装类型的，而我们的函数要返回的是基础数据类型，null值是不能强转为基础数据类型的，所以报空指针异常。
	 * 解决方案：把当前函数的返回值改为包装类型的Long即可
	 * @return
	 */
	private static long get() {
		return threadLocal.get();
	}
}
