package com.firecode.java.net_io.n_io.helloword;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 使用BIO和缓冲区拷贝文件
 * @author ChiangFire
 *
 */
public class FileCopyBIOBuffer implements FileCopy {

	@Override
	public void copyFile(File source, File target) {
		InputStream fin = null;
		OutputStream fout = null;
		try {
			fin = new BufferedInputStream(new FileInputStream(source));
			fout = new BufferedOutputStream(new FileOutputStream(target));
			byte[] buffer = new byte[1024];
			int size;
			// read 函数返回读取的大小
			while((size = fin.read(buffer)) != -1) {
				//fout.write(buffer);
				// 和上面的写法差不多（但是上面的写法简单点）
				fout.write(buffer, 0, size);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != fin) {
					fin.close();
				}
				if(null != fout) {
					fout.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
