package com.firecode.java.test.memory;

/**
 * 静态内部类方式的单列模式
 * @author ChiangFire
 *
 */
public class M_04_Sign {
	
	public static final M_04_Sign getInstance() {
		return Sing.M;
	}
	
	public static void main(String[] args) {
		System.err.println(M_04_Sign.getInstance());
	}
	
	private static class Sing {
		private static final M_04_Sign M = new M_04_Sign();
	}

}
