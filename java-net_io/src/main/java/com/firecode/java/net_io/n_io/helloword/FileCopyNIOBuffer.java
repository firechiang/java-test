package com.firecode.java.net_io.n_io.helloword;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用NIO和缓冲区拷贝文件
 * @author ChiangFire
 *
 */
public class FileCopyNIOBuffer implements FileCopy {

	@Override
	public void copyFile(File source, File target) {
		FileInputStream fin = null;
		FileOutputStream fout = null;
		try {
			fin = new FileInputStream(source);
			fout = new FileOutputStream(target);
			FileChannel finChannel = fin.getChannel();
			FileChannel foutChannel = fout.getChannel();
			// 创建一个拥有1024个字节的缓冲区
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while(finChannel.read(buffer) != -1) {
				// 将Buffer由写模式改为读模式（具体说明可查看首页md文件）
				buffer.flip();
				// 将Buffer里面的数据全部写出去（hasRemaining()函数返回Buffer里面是否还有数据）
				while(buffer.hasRemaining()) {
					// Buffer里面还有数据，就将写出去
					foutChannel.write(buffer);
				}
				// 将Buffer的读模式修改为写模式（具体说明可查看首页md文件）
				buffer.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != fout) {
					fout.close();
				}
				if(fin != null) {
					fin.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
