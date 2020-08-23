package com.firecode.java.test.thread;

/**
 * 演示两个账户相互转账发生死锁
 */
public class T_11_Lock_Deadlock implements Runnable {

	int flag = 1;

	static Account a = new Account(500);
	static Account b = new Account(500);

	@Override
	public void run() {
		System.out.println("flag=" + flag);

		try {
			if (flag == 1) {
				transferMoney(a, b, 200);
			}
			if (flag == 0) {
				transferMoney(b, a, 200);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
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
	void transferMoney(Account from, Account to, int amount) throws InterruptedException {
		synchronized (from) {
			//TimeUnit.SECONDS.sleep(1L); //放开注释发生死锁
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
		T_11_Lock_Deadlock l1 = new T_11_Lock_Deadlock();
		T_11_Lock_Deadlock l2 = new T_11_Lock_Deadlock();
		l1.flag = 1;
		l2.flag = 0;
		Thread t1 = new Thread(l1);
		Thread t2 = new Thread(l2);
		t1.start();
		t2.start();
	}

}
