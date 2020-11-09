package com.firecode.test.concurrent.atom;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.stream.IntStream;

/**
 * 演示LongAccumulator的用法（多线程并行原子计算，它的主要实现原理和LongAdder相似，每个线程都有独立的计数器，最后取值时在合并计算）
 */
public class Atom06Accumulator {

	public static void main(String[] args) {
		/**
		 * @param longBinaryOperator 计算规则（注意：x 的值是通过accumulate()函数传递过来的，而accumulate()函数就是计算，并且计算结果就是下一次计算y的值）
		 * @param identity           y 的初始值
		 */
		LongAccumulator accumulator = new LongAccumulator((x, y) -> 2 + x * y, 1);
		ExecutorService executor = Executors.newFixedThreadPool(8);
		// accumulate()函数计算是原子操作
		IntStream.range(1, 10).forEach(i -> executor.submit(() -> accumulator.accumulate(i)));

		executor.shutdown();
		// 线程池是否完全停止
		while (!executor.isTerminated()) {

		}
		// 打印结算结果
		System.out.println(accumulator.getThenReset());
		/**
		 * @param longBinaryOperator 计算规则（注意：x 的值是通过accumulate()函数传递过来的，而accumulate()函数就是计算，并且计算结果就是下一次计算y的值）
		 * @param identity           y 的初始值
		 */
		LongAccumulator accumulator1 = new LongAccumulator((x, y) -> 2 + x + y, 3);
		// 计算（2 + 1 + 3 = 6）（注意：accumulate()函数计算是原子操作）
		accumulator1.accumulate(1);
		// 打印结果
		System.err.println(accumulator1.getThenReset());
	}

}
