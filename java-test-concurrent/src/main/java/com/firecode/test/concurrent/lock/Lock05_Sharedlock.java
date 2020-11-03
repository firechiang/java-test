package com.firecode.test.concurrent.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁简单使用
 * 排它锁（就是独占锁，获取了别人就获取不了）
 * 共享锁（就是读锁，获取共享锁可读取数据但不能删除和修改，其它线程也同时可获取共享锁用来读取数据）
 * 读写锁的规则：
 * 1，多个线程只申请读锁，都可以申请到
 * 2，如果有一个线程已经占用了读锁，则此时其它线程想要申请写锁，则申请写锁的线程会一直等待读锁的释放
 * 3，如果有一个线程已经占用了写锁，则此时其它线程想要申请写锁或读锁，则申请锁的线程会一直等待写锁的释放
 * 4，一句话总结：要么是一个或多个线程拥有读锁，要么是一个线程有写锁。总之两者不能同时出现（就是要么多读，要么一写）
 * 
 * 公平情况：读写锁都不能插队
 * 非公平情况：写锁可以插队，但读锁不能插队（原因是：现在正在使用读锁，而且有一个写锁在等待，如果读锁还没完成又有读锁，可以直接插进来那么可能导致写锁一直处于等待状态）
 */
public class Lock05_Sharedlock {
	
	/**
	 * true表示公平，false表示非公平（默认是非公平的）
     * 公平情况下：读写锁都不能插队
     * 非公平情况下：写锁可以插队，但是读锁的话，如果前面有写锁在排队就不能插队，如果没有本身就能获取到同时也能插队（不能插队的原因是：现在正在使用读锁，而且有一个写锁在等待，如果读锁还没完成又有读锁，可以直接插进来的话那么可能导致写锁一直处于等待状态）
	 */
    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);
    // 拆分读写锁
    private static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    private static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    private static void read() {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "得到了读锁，正在读取");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放读锁");
            readLock.unlock();
        }
    }

    private static void write() {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "得到了写锁，正在写入");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放写锁");
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        new Thread(()->read(),"Thread1").start();
        new Thread(()->read(),"Thread2").start();
        new Thread(()->write(),"Thread3").start();
        new Thread(()->write(),"Thread4").start();
    }
}
