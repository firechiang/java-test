package com.firecode.test.concurrent.thread_local;

/**
 * 往ThreadLocal里面放东西，后面的函数把它再拿出来
 * 注意：ThreadLocal里面的值用完了建议将其删除，不然可能导致内存溢出（因为同一个线程里所有ThreadLocal的值都是存储在同一个ThreadLocalMap里面，如果值都不删除的话而且ThreadLocal又很多，就会把内存撑爆）
 * 注意：ThreadLocalMap解决Hash冲突的方法不是使用链表，而是直接找下一个空位置，然后直接设置进去
 * 注意：ThreadLocalMap里面的Entity是弱引用（它继承了弱引用），但是Entity的value是强引用（强引用就是使用等号直接赋值）
 */
public class ThreadLocalCommon {
	
	static ThreadLocal<String> threadLocal = new ThreadLocal<>();
	
	public static void main(String[] args) {
		threadLocal.set("我是Value");
		try {
			System.err.println(threadLocal.get());
		} finally {
			// 删除值以防止内存泄漏
			threadLocal.remove();
		}
	}
}
