package com.firecode.java.test.memory;

/**
 * 双重校验的方式的单列模式
 * @author JIANG
 */
public class M_06_Sign {
	
	private static volatile M_06_Sign m;
	
	private M_06_Sign() {}
	
	public static final M_06_Sign getInstance(){
		if(null == m) {
			synchronized (M_06_Sign.class) {
				if(null == m) {
					m = new M_06_Sign();
				}
			}
		}
		return m;
	}
	
}
