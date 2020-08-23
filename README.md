#### 一、[JAVA-11官方文档（包含各个方面的东西也包含GC又换）](https://docs.oracle.com/en/java/javase/11/)
#### 二、[域名解析过程][1]
#### 三、[七层网络模型][2]
#### 四、枚举类单例模式的优点和示例代码（推荐生产使用）
 - 写法简单
 - 线程安全
 - 懒加载（类不用到不会加载）
 - 避免反序列化破坏单列（因为反射是可以绕过私有构造器的）
```bash
public enum M_05_Sign {
	
	INSTANCE;
	
	private int i = 0;
	
	public Integer getI() {
		return i;
	}
}
``` 

[1]: https://github.com/firechiang/java-test/tree/master/java-net_io/docs/dns_doamin.md
[2]: https://github.com/firechiang/linux-test/tree/master/linux-test-lvs#osi%E4%B8%83%E5%B1%82%E7%BD%91%E7%BB%9C%E6%A8%A1%E5%9E%8B%E8%AF%B4%E6%98%8E%E6%B3%A8%E6%84%8F1-7%E6%98%AF%E6%95%B0%E6%8D%AE%E8%A7%A3%E7%A0%81%E8%BF%87%E7%A8%8B7-1%E6%98%AF%E6%95%B0%E6%8D%AE%E7%BC%96%E7%A0%81%E8%BF%87%E7%A8%8B

