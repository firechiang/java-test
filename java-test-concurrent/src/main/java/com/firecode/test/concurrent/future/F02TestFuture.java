package com.firecode.test.concurrent.future;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Future相关函数简单说明
 * 
 * get()函数相关说明：
 * 1，get()函数获取执行结果，如果任务没有执行完成，就阻塞等待。
 * 2，如果任务被取消了，get()函数会抛出CancellationException错误。
 * 3，如果执行任务期间出现异常get()函数会抛出ExecutionException错误（注意：是要到get()函数执行的时候或者是get()函数正在的等待的时候才会抛出异常）。
 * 4，任务超时，get()函数有一个重载的函数可以指定等待超时时间，如果等待超时get()函数就会抛出TimeoutException错误。
 * 
 * cancel()函数：用于取消任务，返回是否取消成功（取消失败的原因任务已经开始执行了，开始执行的任务不能取消，但是可以中断，在调用cancel()函数时传入true即可）
 * 
 * isDone()函数：判断线程是否执行完毕（注意：是线程是否执行完毕）
 * 
 * isCanceled()函数：判断任务是否被取消
 */
public class F02TestFuture {
	
	/**
	 * 演示：批量提交任务时，用List来批量接收结果
	 */
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(20);
        ArrayList<Future> futures = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Future<Integer> future = service.submit(new CallableTask());
            futures.add(future);
        }
        Thread.sleep(5000);
        for (int i = 0; i < 20; i++) {
            Future<Integer> future = futures.get(i);
            try {
                Integer integer = future.get();
                System.out.println(integer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    static class CallableTask implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            Thread.sleep(3000);
            return new Random().nextInt();
        }
    }
}
