package com.firecode.java.test.memory;

public class M_03_Test {
	
	static M_03_Test t = new M_03_Test();
	
	static int i = 1;
	
	public M_03_Test() {
		i++;
	}
	
	public static void main(String[] args) {
		System.err.println(i);
	}

}
