package com.firecode.test.concurrent.cas;

/**
 * 模拟CAS实现，等价代码（注意：CAS全称是 CompareAndSwap，就是比较修改，如果预期的值和旧的值相同就修改，否则不修改）
 * CSA的缺点:
 * 1，因为是先比较再修改，如果值被别人修改了，又再改回来，这个时候我们去比较发现是相等的就修改，但实际是不对的，因为被别人改过了
 * 2，自旋问题，因为如果修改失败，会一直自旋重试（并发比较高可能比较消耗资源有点慢）
 */
public class CAS01SimulatedCAS {

	private volatile int value;

	/**
	 * 修改
	 * @param expectedValue 预期的值
	 * @param newValue      新的值
	 * @return
	 */
	public synchronized int compareAndSwap(int expectedValue, int newValue) {
		int oldValue = value;
		if (oldValue == expectedValue) {
			value = newValue;
		}
		return oldValue;
	}

}
