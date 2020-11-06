package com.firecode.test.concurrent.atom;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 演示高并发场景下，LongAdder比AtomicLong性能好
 * 注意：LongAdder 使用了分段锁所以速度比较快
 */
public class Atom05LongAdder {
	
    public static void main(String[] args) {
    	testLongAdder();
    	testAtomicLong();
    }
    
    
    /**
     * 测试 LongAdder 性能
     */
    private static void testLongAdder() {
        LongAdder counter = new LongAdder();
        ExecutorService service = Executors.newFixedThreadPool(20);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            service.submit(()->{
                for (int j = 0; j < 10000; j++) {
                	// 原子递增
                    counter.increment();
                }
            });
        }
        service.shutdown();
        while (!service.isTerminated()) {

        }
        long end = System.currentTimeMillis();
        System.out.println(counter.sum());
        System.out.println("LongAdder耗时：" + (end - start));
    }
    
    /**
     * 测试 AtomicLong 性能
     */
    private static void testAtomicLong() {
        AtomicLong counter = new AtomicLong(0);
        ExecutorService service = Executors.newFixedThreadPool(20);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            service.submit(()->{
                for (int j = 0; j < 10000; j++) {
                	// 原子递增
                    counter.incrementAndGet();
                }
            });
        }
        service.shutdown();
        while (!service.isTerminated()) {

        }
        long end = System.currentTimeMillis();
        System.out.println(counter.get());
        System.out.println("AtomicLong耗时：" + (end - start));
    }
}
