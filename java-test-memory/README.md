#### 一、Java文件的翻译过程
 - 先将Java文件编译成Class文件，JVM虚拟机在将Class文件翻译成本地系统的机器指令
 
#### 二、JVM内存结构
 - 方法区（Method Area）和堆（Heap）是所有线程共享的
 - Java栈（Java Stack）、本地方法区（Native Method Stack） 、程序计数器（Progran Counter Register）是每个线程私有的

<table>
    <tr>
        <th colspan="3" align="center">Class文件  => 类加载器</th>
    </tr>
    <tr>
        <th colspan="3" align="center">↑↓</th>
    </tr>
    <tr>
        <th colspan="3" align="center">运行时数据区（Runtime Data Area）</th>
    </tr>
    <tr>
        <td align="center"><span>方法区（Method Area）</span></br><span>存储静态变量的值或对象引用、常量的值或对象引用、Class（类）相关信息包括Method</span></td>
        <td align="center"><span>Java栈（Java Stack）</span></br><span>存储基本数据类型的值和对象的引用</span></td>
        <td align="center"><span>本地方法区（Native Method Stack）</span></br><span>存储本地方法相关信息（就是Native函数相关）</span></td>
    </tr>
    <tr>
        <td align="center"><span>堆（Heap） </span></br><span>存储使用New或其它指令所创建的实列对象</span></td>
        <td align="center" colspan="2"><span>程序计数器（Progran Counter Register）</span></br><span>存储当前线程所执行到的字节码行号数以及下一条所要执行的指令（存储线程运行指令相关信息）</span></td>
    </tr>
    <tr>
        <th align="center">↑↓</th>
        <th colspan="2" align="center">↑↓</th>
    </tr>
    <tr>
        <th><span>执行引擎</span></br><span>（Execution Engine）</span></br><span>=></span></th>
        <th><span>本地接口</span></br><span>（Native Interface）</span></br><span>=></span></th>
        <th><span>本地库</span></br><span>（Native Libranies）</span></th>
    </tr>
</table>

#### 三、Java对象模型（堆（Heap）和栈（Stack）构成了Java对象模型，堆（Heap）用来存储对象的实列数据，栈（Stack）用来存储对象的引用）
 - 将每一个class文件创建一个InstanceClass对象（记录Class的相关信息）存储到方法区（Method Area）
 - 所有实列对象都存储在堆（Heap） 里面（每一个对象的实列都分为对象头信息和对象的实列数据）
 - 如果某个对象被某个函数所调用那么这个对象的引用就会保存到栈（Stack）当中 
 
#### 四、JMM（Java内存模型）（主要包括：重排序，可见性，原子性）
##### 4.1、重排序是指在线程内部的两行代码实际执行顺序和代码在Java文件里面的顺序不一致，代码指令并不是严格按照代码语句顺序执行的，它们的顺序被改变了，这就是重排序
 - 重排序是JVM为优化指令而产生的，它会将一些没有依赖关系的指令做整合，以此来提高处理速度
 - 重排序分为编译器优化和CPU指令重排序两种，还有一种是因为内存可见的情况被我们误以为是重排序（就是线程1改了值，线程2没有及时发现）
 
##### 4.2、可见性就是两个线程使用同一个变量，线程1修改了变量的值，线程2不一定能及时看到修改（产生可见性的原因是因为工作内存和主内存不是实时同步的）
 - 每个线程都有自己独立的工作内存和所有线程还共享一个主内存
 - 所有的变量都是存储在主内存里面，线程要使用变量就从内存里面拷贝一份
 - 线程不能直接修改主内存的数据，要先拷贝再修改，再同步到主内存
 
##### 4.3、Happens-Before 可见性原则（就是哪些操作和哪些工具类是可以保证多线程可见的）
 - 单线程原则（就是同一个线程里面变量修改了都是及时可见的） 
 - volatile 关键字（就是被volatile修饰的变量被修改后，所有的线程都是及时可见的，如果变量a是volatile修饰的，而变量b不是，但是有一条语句a=b而且这条也刚好执行完成，那么这个时候b的值在当前线程可读到最新的（这个就是volatile近朱者赤的功能，就是volatile还可以影响与之相关的变量。注意：synchronized关键字也有近朱者赤的功能））
 - synchronized关键字会同步synchronized代码块里面的内存数据（就是线程1里面有个synchronized代码块，里面有共享变量。线程2也有synchronized代码块，并且里面有和线程1相同的共享变量。那么这些共享变量在两个线程里面都是可见的，因为代码会同步执行，同时也会同步内存）
 - 线程启动时所有的变量都是从主内存里面同步过来的，所有这个时候这个线程所使用的变量的值都是最新的
 - join()函数后的代码是及时可见线程里面所修改的变量的值
 - isInterrupted()函数是多个线程间可见的
 - CountDownLatch，ConcurrentHashMap，CyclicBarrier，Semaphore,Future.get()，线程池； 都是多个线程可见的
 
##### 4.4、Volatile 关键字相关（注意：volatile 关键字修饰的变量可使多线程可见和禁止重排序）
 - volatile 关键字不能保证a++这样的代码是原子性的（就是两个线程对同一个使用volatile修饰的变量进行++，不能保证原则性，会有丢失++的情况，最后的结果可能不对）
 - volatile 关键字不能保证非纯赋值代码是原子性的，纯赋值是指boolean a=false，但是a=!a或a=b就是非纯赋值（就是多线程执行a=!a这样的非纯赋值语句，不能保证原子性，会有丢失的情况，最后的结果可能不对）
 - volatile 关键字可以保证纯赋值代码是原子性的（就是两个线程对同一个使用volatile修饰的变量进行纯赋值（boolean a=false），只有线程1修改之后，线程2才能修改，都是同步的。注意：Long和Double本来纯赋值是不能保证原子性的，但是加volatile关键字就能保证其原子性）
 - volatile 关键字适用于触发器模式（就是变量c使用volatile修饰，int a=0;int b=2,c=0，当c=0执行完成以后，此时a和b在其它线程也是也是最新可见的（因为volatile修饰的变量被修改后，当前线程就会刷新主内存））
 
#### JAVA 哪些赋值是具备原子性的（注意：像 i++ 这样的操作是不具备原子性的，因为它是先获取i的值（一条指令），再++（第二条指令）），最后将值赋给i（第三条指令）。它是通过3条指令来完成i++的操作的
 - 除long和double之外的基本类型（int，byte，boolean，short，char，float）的赋值操作都是原子性的
 - 所有引用（reference）的纯赋值操作，不管32还是64位的机器都是原子性的（注意：new对象赋值不是原子性的，因为它要先new对象再初始化属性再赋值（还可能有重排序的问题，就是先赋值对象返回，之后再赋值属性，如果是双重校验的单列模式new对象就会有问题），所以他是多个操作不能保证原子性，如果要保证原子性加上volatile 关键字即可）
 - java.concurrent.Atomic.* 包中的所有类都是具备原子性赋值的 
 - long和double赋值不具备原子性的原因是因为它们所占的空间是64位，如果机器是32位的那么它们是分为两段来存储的，也就是两个32位的空间各存储一半，所以他们的存储会分为两个指令来执行，这可能导致线程第一次写入看到的是64位的前32位，另一次写入看到的是后32位的情况（注意：如果是64位的机器long和double赋值就是原子性的，只有在32位的机器上才不是）。
 - 要保证long和double的赋值是原子性的只需要在变量的前面加上volatile 关键字即可或者直接使用64位的机器



