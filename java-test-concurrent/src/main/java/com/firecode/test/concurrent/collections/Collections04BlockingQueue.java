package com.firecode.test.concurrent.collections;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 队列分两种，阻塞队列和非阻塞队列
 * 
 * 阻塞队列简要说明
 * ArrayBlockingQueue 数组实现的阻塞队列相关函数说明：
 * put()     往队列里面添加元素，如果队列满了。阻塞
 * take()    从队列里面获取元素，如果队列里面没有数据了，阻塞
 * 
 * add()     往队列里面添加元素，如果队列满了，抛出异常
 * remove()  删除队列里里面的元素，如果队列里面没有元素了，抛出异常
 * element() 获取队列的头元素，如果为空，抛出异常
 * 
 * offer()   往队列里面添加元素，返回是否添加成功
 * poll()    获取队列里面的一个元素并删除，没有就返回null
 * peek()    获取队列里面的一个元素，没有就返回null
 * 
 * 
 * LinkedBlockingQueue 链表实现的阻塞队列相关说明：
 * 1，两把锁，就是添加和获取是两个相互不干扰的
 * 
 * PriorityBlockingQueue 支持优先级的队列简要说明：
 * 1，支持优先级，可自然排序（注意：不是先进先出）是可自己实现排序函数
 * 
 * SynchronousQueue 单元素直接传递队列
 * 
 * DelayQueue 延迟队列
 */
public class Collections04BlockingQueue {
	
	
	public static void main(String[] args) {
		LinkedBlockingQueue d = new LinkedBlockingQueue();
		/**
		 * @param capacity 队列容量
		 * @param fair     是否公平（如果是公平的，就会让等待最久优先处理），默认是非公平的
		 */
		ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(3,false);

		Interviewer r1 = new Interviewer(queue);
		Consumer r2 = new Consumer(queue);
		new Thread(r1).start();
		new Thread(r2).start();
	}
}

class Interviewer implements Runnable {

	BlockingQueue<String> queue;

	public Interviewer(BlockingQueue queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		System.out.println("10个候选人都来啦");
		for (int i = 0; i < 10; i++) {
			String candidate = "Candidate" + i;
			try {
				queue.put(candidate);
				System.out.println("安排好了" + candidate);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			queue.put("stop");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class Consumer implements Runnable {

	BlockingQueue<String> queue;

	public Consumer(BlockingQueue queue) {

		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String msg;
		try {
			while (!(msg = queue.take()).equals("stop")) {
				System.out.println(msg + "到了");
			}
			System.out.println("所有候选人都结束了");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
