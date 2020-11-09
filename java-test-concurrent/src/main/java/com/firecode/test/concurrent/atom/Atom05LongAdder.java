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
     * 测试 LongAdder 性能（适用场景：多线程争用，统计求和）
     * LongAdder效率高的原因：它在每个线程都有独立的计数器（记录递增和递减的次数），最后获取值时，将所有线程的计数器相加就可以了
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
     * 测试 AtomicLong 性能（适用场景：少量线程争用，cas相关操作）
     * 注意：AtomicLong虽然没有锁但是JAVA虚拟机多线程修改都是将更新后的数值调用flush()函数刷到主内存，再调用refresh()函数将更新的值刷到各个线程中的工作内存。它是通过这两步来保证线程间的数据同步所以效率稍稍低一点
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
