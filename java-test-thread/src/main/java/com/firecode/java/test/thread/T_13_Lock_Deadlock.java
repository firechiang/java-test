package com.firecode.java.test.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 演示多账户相互转账发生死锁再使用代码来查找死锁
 */
public class T_13_Lock_Deadlock implements Runnable {
	// 账户数量
	static int accountSize = 500;
	// 账户
	static Account[] accounts = new Account[accountSize];

	static Random random = new Random();

	@Override
	public void run() {
		for(int i=0;i<100000;i++) {
			int fromAcct = random.nextInt(accountSize);
			int toAcct = random.nextInt(accountSize);
			transferMoney(accounts[fromAcct],accounts[toAcct],1);
		}
	}

	/**
	 * 转账
	 * 
	 * @param from
	 * @param to
	 * @param amount
	 * @throws InterruptedException
	 */
	void transferMoney(Account from, Account to, int amount) {
		synchronized (from) {
			if (from.balance - amount < 0) {
				System.err.println("余额不足，转账失败。");
			}
			from.balance -= amount;
			synchronized (to) {
				to.balance += amount;
				System.err.println("转账成功：" + amount + " 元");
			}
		}
	}

	/**
	 * 账户
	 */
	static class Account {
		int balance;

		public Account(int balance) {
			super();
			this.balance = balance;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		T_13_Lock_Deadlock t = new T_13_Lock_Deadlock();
		// 初始化账户
		for(int i=0;i<accountSize;i++){
			accounts[i] = new Account(500);
		}
		// 模拟多线程多账户转账（注意：线程越多发生死锁概率越高，发生死锁了程序就不会正常退出了）
		for(int i=0;i<100;i++) {
		    new Thread(t).start();
		}
		// 等待2秒
		TimeUnit.SECONDS.sleep(2L);
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		// 获取发生死锁的线程id数组
		long[] findMonitorDeadlockedThreads = threadMXBean.findMonitorDeadlockedThreads();
		for(int i=0;i<findMonitorDeadlockedThreads.length;i++) {
			ThreadInfo threadInfo = threadMXBean.getThreadInfo(findMonitorDeadlockedThreads[i]);
			System.out.println("发生死锁了，线程是："+threadInfo.getThreadName());
		}
	}

}
