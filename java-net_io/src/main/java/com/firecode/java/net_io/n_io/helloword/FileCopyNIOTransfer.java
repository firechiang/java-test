package com.firecode.java.net_io.n_io.helloword;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 使用NIO里面的管道到管道传输拷贝文件
 * @author ChiangFire
 *
 */
public class FileCopyNIOTransfer implements FileCopy {

	@Override
	public void copyFile(File source, File target) {
		FileInputStream fin = null;
		FileOutputStream fout = null;
		try {
			fin = new FileInputStream(source);
			fout = new FileOutputStream(target);
			FileChannel finChannel = fin.getChannel();
			FileChannel foutChannel = fout.getChannel();
			long copySize = 0;
			// 将源文件管道里面的数据传输到目标管道（finChannel.size() 返回源文件数据的大小）
			for(;;) {
				// 传输数据的大小
				copySize += finChannel.transferTo(0, finChannel.size(), foutChannel);
				// 文件数据全部传输完成
				if(copySize == finChannel.size()) {
					break;
				}
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
