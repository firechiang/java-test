#### NIO Buffer 结构，可以看作是一个Byte数组，它有三个指针 position limit capacity
 - position 表示Buffer可写的开始位置 或者是 可读的开始位置
 - limit 表示Buffer可写的最大位置 或者是 可读的最后位置
 - capacity 表示Buffer最大位置
```bash
 position                    limit,capacity
  |                               |
|---|---|---|---|---|---|---|---|---|
| 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 |
|---|---|---|---|---|---|---|---|---|
```

#### NIO Buffer 读写模式切换的三个函数 flip() clear() compact() 相关说明
 - flip() 函数，将Buffer的写模式改为读模式（它会将position指到数据可读的开始位置，limit指到数据可读的最大位置）
 - clear() 函数，将Buufer的读模式改为写模式，当Buffer里面的数据被全部读出来以后，它会还原Buffer里面三个指针的位置（注意：这个函数只是调整三个指针的位置）
 - compact() 函数，将Buufer的读模式改为写模式，当Buffer里面的数据没有被全部读出来，它会将没有读出来的数据拷贝到Buffer的最前面，然后将可写的开始位置position，调整到没有读出来的数据的后面
 
#### NIO Channel 管道类型说明
 - FileChannel 文件管道，用于一次只读取或写入文件固定字节
 - ServerSocketChannel 服务端网络读写管道
 - SocketChaneel 客户端网络读写管道
 
#### NIO Channel 管道状态说明（注意：这个指针对网络管道）
 - SocketChaneel 和服务器建立了连接它的状态是 CONNECT，当服务器端有接收到客户端的连接请求 ServerSocketChannel 的状态就是 ACCEPT
 - ServerSocketChannel和SocketChaneel当它们上面有可读信息的时候，它们的状态就是 READ（可读状态）
 - ServerSocketChannel和SocketChaneel当它们上面有可写信息的时候，它们的状态就是 WRITE（可写状态）
 
#### NIO Selector 管道选择器（就是监控Channel管道是否可操作（就是否可读或可写））相关说明 （注意：Channel要注册到Selector上才能被托管，而且会有托管的selectionKey（就是托管的标识对象））
 - Channel注册到Selector上时可指定，只监听Channel的某个状态，比如 READ（可读状态） 
 - SelectionKey的interestOps() 返回Channel注册时所指定要监听的状态
 - SelectionKey的readyOps() 返回Channel当前可操作的状态（比如READ读或者WRITE写）
 - SelectionKey的channel() 返回当前的Channel管道
 - SelectionKey的selector() 返回当前Channel所注册的Selector选择器
 - SelectionKey的attachment() 返回当前Channel的自定义属性对象（注意：这个对象是你自己通过attachment函数设进去的）
 - Selector 的 select() 函数返回当前有几个Channel管道处于可操作状态
 - Channel的可操作状态被我们处理完成以后，需要我们手动将其修改为CONNECT状态（就是不可操作状态），因为Selector只会控制不可操作的Channel