package com.firecode.test.concurrent.collections;

import java.util.HashMap;
import java.util.Map;

/**
 * 演示HashMap CPU占100%和死循环问题（原因：就是多个线程同时扩容时可能会造成链表的死循环（就是A指向B，B也指向A））
 * 注意：HashMap允许一个元素的Key是null，Map在遍历的时候不能删除数据，否则会报异常
 */
public class Collections01HashMapEndLessLoop {
	
	/**
	 * @param initialCapacity 初始大小
	 * @param loadFactor      扩容
	 */
	static Map<Integer,String> map = new HashMap<>(2,1.5f);
	
	public static void main(String[] args) {
		map.put(5, "A");
		map.put(7, "B");
		map.put(3, "C");
		
		new Thread(()->{
			map.put(15, "D");
			System.err.println(map);
		}).start();
		
		new Thread(()->{
			map.put(1, "E");
			System.err.println(map);
		}).start();
	}

}
