package com.firecode.test.concurrent.atom;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 把普通变量升级为原子变量
 */
public class Atom04IntegerFieldUpdater implements Runnable {
	
    static Candidate tom;
    static Candidate peter;

    /**
     * 升级变量为原子变量
     * @param tclass    要将那个类升级为原子类
     * @param fieldName 要将类里面的那个属性升级为原子变量（注意：这个变量不能被定义为静态或private否则无法使用）
     */
    public static AtomicIntegerFieldUpdater<Candidate> scoreUpdater = AtomicIntegerFieldUpdater .newUpdater(Candidate.class, "score");
    
    public static void main(String[] args) throws InterruptedException {
        tom=new Candidate();
        peter=new Candidate();
        Atom04IntegerFieldUpdater r = new Atom04IntegerFieldUpdater();
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("普通变量："+peter.score);
        System.out.println("升级后的结果"+ tom.score);
    }
            

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
        	// 正常非原子操作
            peter.score++;
            // 对scoreUpdater对象里面的score属性进行原子递增
            scoreUpdater.getAndIncrement(tom);
        }
    }

    public static class Candidate {

        volatile int score;
    }

}
