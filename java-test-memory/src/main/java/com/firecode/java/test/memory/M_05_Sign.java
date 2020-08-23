package com.firecode.java.test.memory;

/**
 * 枚举类类方式的单列模式（推荐生产使用）
 * @author JIANG
 */
public enum M_05_Sign {
	
	INSTANCE;
	
	private int i = 0;
	
	public Integer getI() {
		return i;
	}
}
