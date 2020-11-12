package com.firecode.test.concurrent.collections;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap相关说明
 * 注意：HashMap允许一个元素的Key是null，但是 ConcurrentHashMap 不允许，Map在遍历的时候不能删除数据，否则会报异常
 * 
 * JDK 1.7：实现是一个分段锁每一段是一个 Segment。每一个Segment里面再是一个Map（注意：ConcurrentHashMap初始默认是一个拥有16个Segment的数组（16个并发度），Segment的个数一旦指定就不能再扩容了）
 * JDK 1.8：实现是一个一个的Node就是链表，每个Node是独立而且每个Node下面也是链表或是红黑树，Key的hash值确定该键值对在哪个Node里面
 * 注意：JDK 1.8的ConcurrentHashMap不是使用分段锁，而是使用CAS来插入每个hash位第一个Node，使用synchronized来插入Node后续的节点（每个Node是独立），以提高效率
 */
public class Collections02ConcurrentHashMap implements Runnable {
	
	private static ConcurrentHashMap<String, Integer> scores = new ConcurrentHashMap<String, Integer>();
	
	public static void main(String[] args) {
		MyConcurrentHashMap<String,String> map = new MyConcurrentHashMap<>();
		// put有源码分析
		map.put("1", "A");
		map.put("2", "B");
		// get g有源码分析
		String string = map.get("1");
		System.err.println("1="+string);
		System.err.println(map);
	}

	@Override
	public void run() {
		for (int i = 0; i < 1000; i++) {
            while (true) {
                Integer score = scores.get("小明");
                Integer newScore = score + 1;
                // 如果当前key不存在值就放进去，否则就取出来（注意：这个操作是原子的）
                scores.putIfAbsent("", 1);
                /**
                 * 原子修改数据
                 * @param key      键
                 * @param oldValue 旧的值
                 * @param newValue 新的值
                 * return          返回是否修改成功
                 */
                boolean b = scores.replace("小明", score, newScore);
                if (b) {
                    break;
                }
            }
        }
	}
}
