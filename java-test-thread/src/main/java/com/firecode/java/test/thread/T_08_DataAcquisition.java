package com.firecode.java.test.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 两个线程对一个数字进行加加，我们判断哪些加加是两个线程同时加的，哪些不是。
 * 判断原理就是：在两个线程加加，执行完成以后，才让两个线程同向下走，再通过标记位来判断
 */
public class T_08_DataAcquisition implements Runnable {
	
	static T_08_DataAcquisition instance = new T_08_DataAcquisition();
	
	int count  = 0;
	// 记录总共执行次数
	AtomicInteger iiiii = new AtomicInteger(0);
	// 记录两个线程同时修改次数
	AtomicInteger jjjjj = new AtomicInteger(0);
	// 第一个栅栏保证在同一个循环里面（注意：如果不保证两个线程在同一个循环里面，可能本来是正常执行的却被我们当成非正常执行）
	CyclicBarrier c1 = new CyclicBarrier(2);
	// 第二个栅栏保证两个线程可以同时进入加加环节（这样和上一个栅栏配合就能保证我们的 加加 环节是公平的，就能准确的判断那些是两个线程同时 加加 ，那些不是）
    CyclicBarrier c2 = new CyclicBarrier(2);
    
	boolean[] bs = new boolean[30000];
	
	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(instance);
		Thread t2 = new Thread(instance);
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		System.err.println("表面上执行的次数："+instance.count);
		System.err.println("两个线程同时修改次数："+instance.jjjjj.get());
		System.err.println("表面上执行的次数 + 两个线程同时修改次数 = 最终执行的次数（"+instance.iiiii.get()+"）");
	}

	@Override
	public void run() {
		for(int i=0;i<10000;i++) {
			try {
				// 重置栅栏（注意：栅栏在放行以后，好像会默认重置）
				c2.reset();
				c1.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
			iiiii.incrementAndGet();
			count ++;
			try {
				// 重置栅栏（注意：栅栏在放行以后，好像会默认重置）
				c1.reset();
				c2.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
			synchronized (bs) {
				// 已经被加其它线程加过了
				// 如果发生了两个线程同时修改一个值，那么它的前一位应该是true，因为两个线程同时修改，最终导致count只会加1
				// 如果没有发生两个线程同时修改count，那么它的前一位应该是false，因为synchronized里面看到的都是最后修改的那个值，都会把最后的那个位置修改为true，而前一位就是没人管了，就应该是false
				if(bs[count] && bs[count - 1]) {
					System.out.println("两个线程同时修改 count="+count);
					jjjjj.incrementAndGet();
				} else {
					bs[count] = Boolean.TRUE;
				}
			}
		}
	}
}
