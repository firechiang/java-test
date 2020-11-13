package com.firecode.test.concurrent.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 演示get的超时方法，需要注意超时后处理，调用future.cancel()。演示cancel传入true和false的区别，代表是否中断正在执行的任务。
 */
public class F03Timeout {

	private static final Ad DEFAULT_AD = new Ad("无网络时候的默认广告");
	private static final ExecutorService exec = Executors.newFixedThreadPool(10);

	static class Ad {

		String name;

		public Ad(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "Ad{" + "name='" + name + '\'' + '}';
		}
	}
	
    public static void main(String[] args) {
    	F03Timeout timeout = new F03Timeout();
        timeout.printAd();
    }

	static class FetchAdTask implements Callable<Ad> {

		@Override
		public Ad call() throws Exception {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				System.out.println("sleep期间被中断了");
				return new Ad("被中断时候的默认广告");
			}
			return new Ad("旅游订票哪家强？找某程");
		}
	}
	
	static class Task implements Callable<Integer> {
	    @Override
	    public Integer call() throws Exception {
	        System.out.println("子线程正在计算");
	        Thread.sleep(3000);
	        int sum = 0;
	        for (int i = 0; i < 100; i++) {
	            sum += i;
	        }
	        return sum;
	    }

	}

	public void printAd() {
		Future<Ad> f = exec.submit(new FetchAdTask());
		Ad ad;
		try {
			ad = f.get(2000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			ad = new Ad("被中断时候的默认广告");
		} catch (ExecutionException e) {
			ad = new Ad("异常时候的默认广告");
		} catch (TimeoutException e) {
			ad = new Ad("超时时候的默认广告");
			System.out.println("超时，未获取到广告");
			/**
			 * 取消任务（参数true表示如果取消的时候线程正在运行，就打断）
			 */
			boolean cancel = f.cancel(true);
			System.out.println("cancel的结果：" + cancel);
		}
		exec.shutdown();
		System.out.println(ad);
	}

}
