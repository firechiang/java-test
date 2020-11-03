package com.firecode.test.concurrent.lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 简单模拟自旋锁（自旋锁就是死循环修改状态来获取锁，直到修改成功）
 */
public class Lock07_SpinLock {
	
    private AtomicReference<Thread> sign = new AtomicReference<>();
    
    public static void main(String[] args) {
    	Lock07_SpinLock spinLock = new Lock07_SpinLock();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "开始尝试获取自旋锁");
                spinLock.lock();
                System.out.println(Thread.currentThread().getName() + "获取到了自旋锁");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    spinLock.unlock();
                    System.out.println(Thread.currentThread().getName() + "释放了自旋锁");
                }
            }
        };
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread1.start();
        thread2.start();
    }

    /**
     * 获取锁
     */
    public void lock() {
        Thread current = Thread.currentThread();
        while (!sign.compareAndSet(null, current)) {
            System.out.println("自旋获取失败，再次尝试");
        }
    }

    /**
     * 释放锁
     */
    public void unlock() {
        Thread current = Thread.currentThread();
        sign.compareAndSet(current, null);
    }
}
