package com.firecode.test.concurrent.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 演示ReentrantReadWriteLock可以降级，不能升级
 * 同一个线程降级锁（就是先获取写锁，在不释放写锁的情况下，直接获取读锁。这个是可行的）
 * 同一个线程升级锁（就是先获取读锁，在不释放读锁的情况下，直接获取写锁。这个是行不通的）
 */
public class Lock06_Upgrading {
	
    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(false);
            
    private static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    private static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
    
    
    public static void main(String[] args) throws InterruptedException {
//      System.out.println("先演示降级是可以的");
//      Thread thread1 = new Thread(() -> writeDowngrading(), "Thread1");
//      thread1.start();
//      thread1.join();
//      System.out.println("------------------");
//      System.out.println("演示升级是不行的");
      Thread thread2 = new Thread(() -> readUpgrading(), "Thread2");
      thread2.start();
  }
    
    
    /**
     * 降级锁（就是先获取写锁，在不释放写锁的情况下，直接获取读锁。这个是可行的）
     */
    @SuppressWarnings("unused")
	private static void writeDowngrading() {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "得到了写锁，正在写入");
            Thread.sleep(1000);
            readLock.lock();
            System.out.println("在不释放写锁的情况下，直接获取读锁，成功降级");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
            System.out.println(Thread.currentThread().getName() + "释放写锁");
            writeLock.unlock();
        }
    }

    /**
     * 升级锁（就是先获取读锁，在不释放读锁的情况下，直接获取写锁。这个是行不通的）
     */
    private static void readUpgrading() {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "得到了读锁，正在读取");
            Thread.sleep(1000);
            System.out.println("升级会带来阻塞");
            writeLock.lock();
            System.out.println(Thread.currentThread().getName() + "获取到了写锁，升级成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放读锁");
            readLock.unlock();
        }
    }
}
