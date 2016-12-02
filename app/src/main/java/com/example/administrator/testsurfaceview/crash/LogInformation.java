package com.example.administrator.testsurfaceview.crash;


import com.example.administrator.testsurfaceview.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LogInformation {

	public static void writeFileData(String fileName, String message) {
		try {
			String path = "/mnt/sdcard/JiaXingOneMap/crash/";
			File dir = new File(path);
			File file = new File(path +fileName);
			if (!dir.exists()) {
				dir.mkdir();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			String str=readFileSdcardFile(path +fileName);
			FileOutputStream fout = new FileOutputStream(path +fileName);
			byte[] bytes = (str+"\n"+message).getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//    read SDCard
	private static String readFileSdcardFile(String fileName) throws IOException {
//		String res = "";
//		try {
//
//			FileInputStream fin = new FileInputStream(fileName);
//			int length = fin.available();
//			byte[] buffer = new byte[length];
//			fin.read(buffer);
//			res = EncodingUtils.getString(buffer, "UTF-8");
//
//			fin.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return res;
		return FileUtils.readFile(fileName).toString();
	}
}
