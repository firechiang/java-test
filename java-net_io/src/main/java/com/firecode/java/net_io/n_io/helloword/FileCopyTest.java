package com.firecode.java.net_io.n_io.helloword;

import java.io.File;

/**
 * 用BIO和NIO分别实现文件拷贝
 * @author ChiangFire
 *
 */
public class FileCopyTest {
	
	public static void main(String[] args) {
		// 使用BIO且不使用缓冲区拷贝文件（性能最差）
		FileCopy fileCopy1 = new FileCopyBIONotBuffer();
		// 使用BIO和缓冲区拷贝文件（小文件较好，大文件一般）
		FileCopy fileCopy2 = new FileCopyBIOBuffer();
		// 使用NIO和缓冲区拷贝文件（小文件一般，大文件较好）
		FileCopy fileCopy3 = new FileCopyNIOBuffer();
		// 使用NIO里面的管道到管道传输拷贝文件（性能最好）
		FileCopy fileCopy = new FileCopyNIOTransfer();
		fileCopy.copyFile(new File("D:\\test_copy\\source.txt"), new File("D:\\test_copy\\target.txt"));
	}

}
