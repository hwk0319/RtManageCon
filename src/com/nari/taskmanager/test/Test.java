package com.nari.taskmanager.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Test {
	public static void copyFolder(String oldPath, String newPath) {
		FileInputStream input = null;
		FileOutputStream output = null;
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			if(file!=null){
				for (int i = 0; i < file.length; i++) {
					if (oldPath.endsWith(File.separator)) {
						temp = new File(oldPath + file[i]);
					} else {
						temp = new File(oldPath + File.separator + file[i]);
					}
					if (temp.isFile()) {
						input = new FileInputStream(temp);
						output = new FileOutputStream(newPath + File.separator + (temp.getName()).toString());
						byte[] b = new byte[1024 * 5];
						int len;
						while ((len = input.read(b)) != -1) {
							output.write(b, 0, len);
						}
						output.flush();
						output.close();
						input.close();
					}
					if (temp.isDirectory()) {// 如果是子文件夹
						copyFolder(oldPath + File.separator + file[i], newPath + File.separator + file[i]);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] arg0)
	{
		String value = "111,222".replace("\\[*,\\]|\\[,*\\]", ",*******,");
		System.out.println(value);
		
	}
}
