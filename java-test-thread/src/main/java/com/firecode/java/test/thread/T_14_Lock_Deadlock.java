package com.firecode.java.test.thread;

import java.util.Random;

/**
 * 演示调整获取锁的顺序来解决多账户相互转账发生死锁的情况（无论谁给谁转账获取锁的顺序都是一致的，就可以解决死锁问题）
 */
public class T_14_Lock_Deadlock implements Runnable {
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
		// from.id大的先获取from的锁
		if(from.id > to.id) {
			synchronized (from) {
				from.balance -= amount;
				synchronized (to) {
					to.balance += amount;
				}
				System.err.println("转账成功：" + amount + " 元");
			}
		// from.id小的先获取to的锁	
		} else if(from.id < to.id) {
			synchronized (to) {
				to.balance += amount;
				synchronized (from) {
					from.balance -= amount;
				}
				System.err.println("转账成功：" + amount + " 元");
			}
		// 如果相等就先获取from的锁（当然如果是主键就不存在相等的问题）
		} else {
			synchronized (from) {
				from.balance -= amount;
				synchronized (to) {
					to.balance += amount;
				}
				System.err.println("转账成功：" + amount + " 元");
			}
		}
	}

	/**
	 * 账户
	 */
	static class Account {
		// 账户ID
		int id;
		// 账户余额
		int balance;

		public Account(int id,int balance) {
			super();
			this.id = id;
			this.balance = balance;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		T_14_Lock_Deadlock t = new T_14_Lock_Deadlock();
		// 初始化账户
		for(int i=0;i<accountSize;i++){
			accounts[i] = new Account(i,500);
		}
		// 模拟多线程多账户转账（注意：获取锁的顺序都是一样的就永远不会发生死锁）
		for(int i=0;i<100;i++) {
		    new Thread(t).start();
		}
	}

}
