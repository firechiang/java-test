package com.firecode.test.concurrent.finall;

import sun.misc.Unsafe;
import java.lang.reflect.Field;

/**
 * 打印内存地址和String复用问题
 * 
 * 总结：String类型只要是常量或者是静态赋值（就是直接等于多少而不是通过拼接或函数得来的值）的变量，在初始化的过程中，JAVA编译器会默认去内存中查找是否有相等的值存在，如果存在就直接使用该内存地址和值，不用重新分配内存
 * 值得注意的是：常量和其它静态值（静态值=直接的值而不是通过函数获取的）拼接后JAVA编译器也会去内存中查找是否有相等的值存在，如果存在就直接使用该内存地址和值，不用重新分配内存
 */
@SuppressWarnings({ "restriction"})
public class Final04AAA {
	
	static final Unsafe unsafe = getUnsafe();
	static final boolean is64bit = true;
	
	public static void main(String[] args) {
        String a = "wukong2";
        final String b = "wukong";
        String d = "wukong";
        String c = b + 2;
        String e = d + 2;
		printAddresses("a内存地址：",a);
		printAddresses("b内存地址：",b);
		printAddresses("c内存地址：",c);
		printAddresses("d内存地址：",d);
		/**
		 * a为什么和c是相等的（指向同一块内存地址），是因为我们事先定义了a而且赋了值，它在内存里面就有了。
		 * 我们再声明b使用final修饰并且静态赋值，这个时候b默认就当成常量了（注意：如果是函数获取值再赋予是不能成为常量的），常量和别的静态值（静态值=直接的值而不是通过函数获取的）拼接后，JAVA编译器会默认去内存中查找是否有相等的值存在，如果存在就直接使用该内存地址和值，不用重新分配内存
		 * 
		 * 总结：String类型只要是常量或者是静态赋值（就是直接等于多少而不是通过拼接或函数得来的值）的变量，在初始化的过程中，JAVA编译器会默认去内存中查找是否有相等的值存在，如果存在就直接使用该内存地址和值，不用重新分配内存
		 * 值得注意的是：常量和其它静态值（静态值=直接的值而不是通过函数获取的）拼接后JAVA编译器也会去内存中查找是否有相等的值存在，如果存在就直接使用该内存地址和值，不用重新分配内存
		 */
        System.out.println("a == c："+(a == c));
        System.out.println("a == e："+(a == e));
	}
	
    @SuppressWarnings("deprecation")
	public static void printAddresses(String label, Object... objects) {
        System.out.print(label + ": 0x");
        long last = 0;
        int offset = unsafe.arrayBaseOffset(objects.getClass());
        int scale = unsafe.arrayIndexScale(objects.getClass());
        switch (scale) {
            case 4:
                long factor = is64bit ? 8 : 1;
                final long i1 = (unsafe.getInt(objects, offset) & 0xFFFFFFFFL) * factor;
                System.out.print(Long.toHexString(i1));
                last = i1;
                for (int i = 1; i < objects.length; i++) {
                    final long i2 = (unsafe.getInt(objects, offset + i * 4) & 0xFFFFFFFFL) * factor;
                    if (i2 > last)
                        System.out.print(", +" + Long.toHexString(i2 - last));
                    else
                        System.out.print(", -" + Long.toHexString( last - i2));
                    last = i2;
                }
                break;
            case 8:
                throw new AssertionError("Not supported");
        }
        System.out.println();
    }
	
	/**
	 * 获取 Unsafe 实列
	 * @return
	 */
    private static Unsafe getUnsafe() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (Unsafe) theUnsafe.get(null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
