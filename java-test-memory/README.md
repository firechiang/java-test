#### 一、 Java文件的翻译过程
 - 先将Java文件编译成Class文件，JVM虚拟机在将Class文件翻译成本地系统的机器指令
 
#### 二、JVM内存结构
 - 方法区（Method Area）和堆（Heap）是所有线程共享的
 - Java栈（Java Stack）、本地方法区（Native Method Stack） 、程序计数器（Progran Counter Register）是每个线程私有的
<table>
    <tr>
        <td colspan="3" align="center">Class文件  => 类加载器</td>
    </tr>
    <tr>
        <td colspan="3" align="center" border="0" cellpadding="0" cellspacing="0">↑↓</td>
    </tr>
</table> 

<table>
    <tr>
        <td colspan="3" align="center">运行时数据区（Runtime Data Area）</td>
    </tr>
    <tr>
        <td align="center"><span>方法区（Method Area）</span></br><span>存储静态变量的值或对象引用、常量的值或对象引用、Class相关信息</span></td>
        <td align="center"><span>Java栈（Java Stack）</span></br><span>存储基本数据类型的值和对象的引用</span></td>
        <td align="center"><span>本地方法区（Native Method Stack）</span></br><span>本地方法相关信息（就是Native（本地系统）函数相关）</span></td>
    </tr>
    <tr>
        <td align="center"><span>堆（Heap） </span></br><span>使用New或其它指令所创建的实列对象都在这里面</span></td>
        <td align="center" colspan="2"><span>程序计数器（Progran Counter Register）</span></br><span>存储当前线程所执行到的字节码行号数以及下一条所要执行的指令（存储线程运行指令相关信息）</span></td>
    </tr>
</table> 
```bash

|-----------------------------------------------------------------------------|
|                         Class文件  => 类加载器                               |
|-----------------------------------------------------------------------------|
                                 ↑↓                                           |
|-----------------------------------------------------------------------------|
|                     运行时数据区（Runtime Data Area）                         |             
|-------------------------------------------|---------------------------------|
|                     |                     |                                 |
| 方法区（Method Area）| Java栈（Java Stack）| 本地方法区（Native Method Stack） |
|  存储静态变量或引用、常量或常量引用、class相关信息                   |存储基本类型的值和对象的引用                     |   本地方法相关信息（就是Native（本地系统）函数相关）           |
|                     |                     |                                 |
----------------------|-------------------------------------------------------|
|                     |                                                       |
| 堆（Heap）                         |           程序计数器（Progran Counter Register）       |    
| 使用New或其它指令所创建的实列对象都在这里面                    |   存储当前线程所执行到的字节码行号数以及下一条所要执行的指令                         |
|                     |                                                       |
|-----------------------------------------------------------------------------|
         ↑↓                          ↑↓
|---------------------|    |---------------------|    |-----------------------|
|      执行引擎        |    |       本地接口      |    |       本地库           |
| （Execution Engine）| -> | （Native Interface）| -> |  （Native Libranies）  |
|---------------------|    |---------------------|    |-----------------------|
```                   
 
#### Java内存模型

#### Java对象模型
 - 将每一个class文件创建一个InstanceClass对象（记录Class的相关信息）存储到方法区（Method Area）
 - 所有实列对象都存储在堆（Heap） 里面（每一个对象都分为对象头信息和对象的实列数据存储在堆里面）
 - 如果某个对象被某个函数所调用那么这个对象的引用就会保存到栈（Stack）当中
