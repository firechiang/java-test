package com.firecode.java.net_io.n_io.helloword;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 使用BIO且不使用缓冲区拷贝文件
 * @author ChiangFire
 *
 */
public class FileCopyBIONotBuffer implements FileCopy {

	@Override
	public void copyFile(File source, File target) {
		InputStream fin = null;
		OutputStream fout = null;
		try {
			fin = new FileInputStream(source);
			fout = new FileOutputStream(target);
			int result;
			// 注意：read() 函数其实是读一个字节（好像是4个字节），只不过被转成int来显示
			while((result = fin.read()) != -1) {
				fout.write(result);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != fin) {
					fout.close();
					fin.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
