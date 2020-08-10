package com.firecode.java.test.memory;

/**
 * 演示重排序（什么是重排序：在线程内部的两行代码实际执行顺序和代码在Java文件里面的顺序不一致，代码指令并不是严格按照代码语句顺序执行的，它们的顺序被改变了，这就是重排序）
 * 代码执行完成以后会有4种结果：
 * 1，正常情况，第一个线程先执行，导致的结果就是 x=0,y=1
 * 2，正常情况，第二个线程先执行，导致的结果就是 x=1,y=0
 * 3，正常情况，两个线程同时执行，导致的结果就是 x=1,y=1
 * 4，重排序情况也可能是内存不可见的情况（就是线程1改了值，线程2没有及时发现），两个线程同时执行，但是 x = b 或  y = a 先执行，导致 x=0,y=0
 */
public class M_01_Reorder {
	
	private static int x = 0,y = 0;
	private static int a = 0,b = 0;
	
	public static void main(String[] args) throws InterruptedException {
		int i = 0;
		for(;;) {
			x = 0;
			y = 0;
			a = 0;
			b = 0;
			Thread one = new Thread(()->{
				a = 1;
				x = b;
			});
			Thread two = new Thread(()->{
				b = 1;
				y = a;
			});
			one.start();
			two.start();
			one.join();
			two.join();
			if(x == 0 && y == 0) {
				System.out.println("第"+(++i)+"次(x="+x+",y="+y+")");
				break;
			}else {
				System.err.println("第"+(++i)+"次(x="+x+",y="+y+")");
			}
		}
	}
}
