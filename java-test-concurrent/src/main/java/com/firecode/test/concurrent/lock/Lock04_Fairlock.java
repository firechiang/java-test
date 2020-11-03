package com.firecode.test.concurrent.lock;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平锁和非公平锁简单使用
 * 公平锁就是获取锁都是要排队的（注意：tryLock()函数是不遵循公平原则的）
 * 非公平锁获取锁不需要排队，有利于提升代码的执行效率（比如现在的锁是空档期就是没有线程在用锁，有一个线程正在运行突然需要锁了，就可以直接给它，而不需要排队等待。如果排队等待唤醒线程是需要资源消耗的）
 */
public class Lock04_Fairlock {
	
	   public static void main(String[] args) {
	        PrintQueue printQueue = new PrintQueue();
	        for (int i = 0; i < 10; i++) {
	            new Thread(new Job(printQueue)).start();
	            try {
	                Thread.sleep(100);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}

	class Job implements Runnable {

	    PrintQueue printQueue;

	    public Job(PrintQueue printQueue) {
	        this.printQueue = printQueue;
	    }

	    @Override
	    public void run() {
	        System.out.println(Thread.currentThread().getName() + "开始打印");
	        printQueue.printJob(new Object());
	        System.out.println(Thread.currentThread().getName() + "打印完毕");
	    }
	}

	class PrintQueue {

		// true 表示公平（默认是非公平）注意：tryLock()函数是不遵循公平原则的
		// 注意：公平锁它的打印是按顺序来的（就是先第一次打印按顺序执行完成，在按顺序执行第二次打印），因为每获取一次锁就会在队列里面添加一次任务，当执行到该任务就会唤醒线程
		// 注意：非公平锁它是第一次打印和第二次打印都执行完成以后才将锁交给下一个线程
	    private Lock queueLock = new ReentrantLock(true);

	    public void printJob(Object document) {
	    	// 第一次打印
	        queueLock.lock();
	        try {
	            int duration = new Random().nextInt(10) + 1;
	            System.out.println(Thread.currentThread().getName() + "正在打印，需要" + duration);
	            Thread.sleep(duration * 1000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        } finally {
	            queueLock.unlock();
	        }
            // 第二次打印
	        queueLock.lock();
	        try {
	            int duration = new Random().nextInt(10) + 1;
	            System.out.println(Thread.currentThread().getName() + "正在打印，需要" + duration+"秒");
	            Thread.sleep(duration * 1000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        } finally {
	            queueLock.unlock();
	        }
	    }
	
}
