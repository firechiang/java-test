package com.firecode.test.concurrent.collections;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CopyOnWriteArrayList简要说明
 * 
 * 实现原理：新的数据进来将老数据和新数据拷贝到一块新的内存当中，然后将旧的数据地址修改成新的数据地址（就是将数据地址指向新的内存地址）。
 *          所以会产生一个问题，每次调用iterator()函数获取迭代器，所拿到的数据地址是当时的，如果后面修改了数据，该迭代器是不会知道的
 * 总结：迭代器所能拿到的数据取决于迭代器的诞生时间而不取决于迭代时间
 *          
 * 适用场景：读多写少（线程安全）
 * 
 * 读写规则：多线程同时可读（有一个线程在写也可以读），但是写只能同时拥有一个线程在写
 * 
 * 缺点：
 * 1，数据读取不能达到实时性（原因请查看上面的实现原理和总结）
 * 2，因为是内存拷贝所以会占用双份内存
 * 
 * 演示CopyOnWriteArrayList在迭代的过程中也可以修改数据（注意：ArrayList 是不行的）
 * 注意：迭代的过程中修改了数据，实际的CopyOnWriteArrayList修改了，但是迭代器的数据是，没有修改的（也就是你迭代到了那个位置，虽然那个位置的数据被修改了，但是迭代器next()函数拿出来的还是原来的数据）
 *      所以迭代的时候可能会拿到旧的数据
 */
public class Collections03CopyOnWriteArrayListDemo1 {
	
    public static void main(String[] args) {
        //ArrayList<String> list = new ArrayList<>();
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        Iterator<String> iterator = list.iterator();

        /**
         * 注意：迭代的过程中修改了数据，实际的CopyOnWriteArrayList修改了，但是迭代器的数据是，没有修改的（也就是你迭代到了那个位置，虽然那个位置的数据被修改了，但是迭代器next()函数拿出来的还是原来的数据）
         */
        while (iterator.hasNext()) {
            System.out.println("list is" + list);
            String next = iterator.next();
            System.out.println(next);

            if (next.equals("2")) {
                list.remove("5");
            }
            if (next.equals("3")) {
                list.add("3 found");
            }
        }
    }
}
