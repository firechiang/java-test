package com.firecode.test.concurrent.aqs;

/**
 * 演示自己使用AQS实现一个简单的线程协作器（注意：AQS就是指 AbstractQueuedSynchronizer 类）
 * 注意：CountDownLatch，Semaphore，ReentrantLock 都是使用 AQS 实现的，就是里面都有一个内部类继承至 AbstractQueuedSynchronizer 类
 * 
 * 
 * AQS三大核心部分（注意：源码分析在下面）：
 * 1，state状态（这个状态由子类去控制具体要做什么）
 * 2，控制线程抢锁和配合的FIFO（先进先出）任务队列
 * 3，获取和释放锁的相关函数（注意：这些函数需要子类去实现）
 * 
 * 
 * 
 */
public class AQS01OneShotLatch {
	
	private final Sync sync = new Sync();
	
	public static void main(String[] args) throws InterruptedException {
		AQS01OneShotLatch oneShotLatch = new AQS01OneShotLatch();
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName() + "尝试获取latch，获取失败那就等待");
					oneShotLatch.await();
					System.out.println("开闸放行" + Thread.currentThread().getName() + "继续运行");
				}
			}).start();
		}
		Thread.sleep(5000);
		oneShotLatch.signal();

		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName() + "尝试获取latch，获取失败那就等待");
				oneShotLatch.await();
				System.out.println("开闸放行" + Thread.currentThread().getName() + "继续运行");
			}
		}).start();
		
		/**
		 *  CountDownLatch，Semaphore，ReentrantLock，AbstractQueuedSynchronizer（AQS） 重要函数源码解析
		 */
		new Thread(()->{

			try {
				MyReentrantLock lock = new MyReentrantLock();
				// 获取锁源码解析
				lock.lock();
				// 解锁函数源码解析
				lock.unlock();
				
				MyCountDownLatch l = new MyCountDownLatch(1);
				// 获取计数源码分析
				l.getCount();
				// 等待源码分析
				l.await();
				// 减少计数源码分析
				l.countDown();
				
				MySemaphore s = new MySemaphore(10);
				// 获取锁源码分析
				s.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void signal() {
		sync.releaseShared(0);
	}

	public void await() {
		sync.acquireShared(0);
	}

	/**
	 * 使用AQS实现自定义的锁
	 * 注意：AQS的源码分析在 MyAbstractQueuedSynchronizer 类里面
	 */ 
	private class Sync extends MyAbstractQueuedSynchronizer {

		/**
		 * 自定义实现获取独占锁（1 表示获取锁成功，-1 表示获取锁失败）
		 */
		@Override
		protected int tryAcquireShared(int arg) {
			return (getState() == 1) ? 1 : -1;
		}

		/**
		 * 自定义实现释放独占锁（true表示释放成功，false表示释放失败）
		 */
		@Override
		protected boolean tryReleaseShared(int arg) {
			setState(1);
			return true;
		}

		/**
		 * 自定义实现获取共享锁（计数锁，获取一个计数，比如 Semaphore）并返回是否获取成功
		 */
		@Override
		protected boolean tryAcquire(int arg) {
			// TODO Auto-generated method stub
			return super.tryAcquire(arg);
		}

		/**
		 * 自定义实现释放共享锁（计数锁，获取一个计数，比如 Semaphore）并返回是否完全释放，就是计数是否归零
		 */
		@Override
		protected boolean tryRelease(int arg) {
			// TODO Auto-generated method stub
			return super.tryRelease(arg);
		}
		
		
	}
}
