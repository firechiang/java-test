package com.firecode.test.concurrent.atom;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 引用类型的原子性简单使用（让一引用对象具有原子性修改）
 */
public class Atom03Reference {
	
	// 原子引用
    private AtomicReference<Thread> sign = new AtomicReference<>();

    public static void main(String[] args) {
    	Atom03Reference spinLock = new Atom03Reference();
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
        // 如果原子引用里面没有值我们就把当前线程的引用设置进去（注意：这个设置是原子操作，返回是否设置成功）
        while (!sign.compareAndSet(null, current)) {
            System.out.println("自旋获取失败，再次尝试");
        }
    }

    /**
     * 释放锁
     */
    public void unlock() {
        Thread current = Thread.currentThread();
        // 如果原子引用里面的值是我们当前线程的引用，我们就将原子引用设置为空（注意：这个设置是原子操作，返回是否设置成功）
        sign.compareAndSet(current, null);
    }
	
}
