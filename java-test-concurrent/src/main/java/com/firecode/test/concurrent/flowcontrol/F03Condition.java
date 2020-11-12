package com.firecode.test.concurrent.flowcontrol;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition（唤醒和等待）简单使用
 * 使用Condition实现生产者消费者模式
 */
public class F03Condition {
    private int queueSize = 10;
    private PriorityQueue<Integer> queue = new PriorityQueue<Integer>(queueSize);
    private Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();

    public static void main(String[] args) {
    	F03Condition conditionDemo2 = new F03Condition();
        Producer producer = conditionDemo2.new Producer();
        Consumer consumer = conditionDemo2.new Consumer();
        producer.start();
        consumer.start();
    }
    
    /**
     * 生产者
     */
    class Producer extends Thread {

        @Override
        public void run() {
            produce();
        }

        private void produce() {
            while (true) {
                lock.lock();
                try {
                    while (queue.size() == queueSize) {
                        System.out.println("队列满，等待有空余");
                        try {
                        	// 阻塞等待（注意：等待会释放锁，所以等待之前必须持有锁，否则报错）
                            notFull.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.offer(1);
                    // 唤醒所有等待线程（注意：释放需要锁，所以释放之前必须持有锁）
                    notEmpty.signalAll();
                    System.out.println("向队列插入了一个元素，队列剩余空间" + (queueSize - queue.size()));
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    /**
     * 消费者
     */
    class Consumer extends Thread {

        @Override
        public void run() {
            consume();
        }

        private void consume() {
            while (true) {
                lock.lock();
                try {
                    while (queue.size() == 0) {
                        System.out.println("队列空，等待数据");
                        try {
                        	// 阻塞等待（注意：等待会释放锁，所以等待之前必须持有锁，否则报错）
                            notEmpty.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.poll();
                    // 唤醒所有等待线程（注意：释放需要锁，所以释放之前必须持有锁）
                    notFull.signalAll();
                    System.out.println("从队列里取走了一个数据，队列剩余" + queue.size() + "个元素");
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
