package com.firecode.java.test.memory;

/**
 * 演示内存可见性
 */
public class M_02_Visibility {
	
	int a = 1;
	int b = 2;
	
	public void change() {
		a = 3;
		b = a;
	}
	
	public void print() {
		System.err.println("b="+b+",a="+a);
	}
	
	public static void main(String[] args) throws InterruptedException {
		for(;;) {
			M_02_Visibility m = new M_02_Visibility();
			Thread thread1 = new Thread(()->{
				m.change();
			});
			Thread thread2 = new Thread(()->{
				m.print();
			});
			thread1.start();
			thread2.start();
			thread1.join();
			thread2.join();
		}
	}
}
