package com.commonlib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	/**
	 * 解压“压缩文件”到指定路径
	 * 
	 * @param zipFileString
	 * @param outPathString
	 * @throws Exception
	 */
	public static void UnZipFolder(String zipFileString, String outPathString)
			throws Exception {
		// TODO add callBack ?
		ZipInputStream inZip = new ZipInputStream(
				new FileInputStream(zipFileString));
		ZipEntry zipEntry;
		String szName = "";
		System.out.println("start");
		while ((zipEntry = inZip.getNextEntry()) != null) {

			szName = zipEntry.getName();
			System.out.println(szName);
			if (zipEntry.isDirectory()) {
				// get the folder name of the widget
				szName = szName.substring(0, szName.length() - 1);
				File folder = new File(outPathString + File.separator + szName);
				// TODO file exists ?
				boolean readRight = folder.canRead();
				boolean writeRight = folder.canWrite();
				System.out.println("readRight====>" + readRight
						+ ";writeRight====>" + writeRight);
				folder.mkdirs();
			} else {

				File file = new File(outPathString + File.separator + szName);
				file.createNewFile();
				// get the output stream of the file
				FileOutputStream out = new FileOutputStream(file);
				int len;
				byte[] buffer = new byte[1024];
				// read (len) bytes into buffer
				while ((len = inZip.read(buffer)) != -1) {
					// write (len) byte from buffer at the position 0
					out.write(buffer, 0, len);
					out.flush();
				}
				out.close();
			}
		}
		inZip.close();
	}

	/**
	 * 压缩文件到指定目录
	 * 
	 * @param srcFileString
	 *            file or folder to be Compress
	 * @param zipFileString
	 *            the path name of result ZIP
	 * @throws Exception
	 */
	public static void ZipFolder(String srcFileString, String zipFileString)
			throws Exception {
		// create ZIP
		ZipOutputStream outZip = new ZipOutputStream(
				new FileOutputStream(zipFileString));
		// create the file
		File file = new File(srcFileString);
		// compress
		ZipFiles(file.getParent() + File.separator, file.getName(), outZip);
		// finish and close
		outZip.finish();
		outZip.close();
	}

	/**
	 * 解压文件
	 * 
	 * @param folderString
	 * @param fileString
	 * @param zipOutputSteam
	 * @throws Exception
	 */
	private static void ZipFiles(String folderString, String fileString,
			ZipOutputStream zipOutputSteam) throws Exception {
		if (zipOutputSteam == null)
			return;
		File file = new File(folderString + fileString);
		if (file.isFile()) {
			ZipEntry zipEntry = new ZipEntry(fileString);
			FileInputStream inputStream = new FileInputStream(file);
			zipOutputSteam.putNextEntry(zipEntry);
			int len;
			byte[] buffer = new byte[4096];
			while ((len = inputStream.read(buffer)) != -1) {
				zipOutputSteam.write(buffer, 0, len);
			}
			zipOutputSteam.closeEntry();
			inputStream.close();
		} else {
			// folder
			String fileList[] = file.list();
			// no child file and compress
			if (fileList.length <= 0) {
				ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
				zipOutputSteam.putNextEntry(zipEntry);
				zipOutputSteam.closeEntry();
			}
			// child files and recursion
			for (int i = 0; i < fileList.length; i++) {
				ZipFiles(folderString,
						fileString + File.separator + fileList[i],
						zipOutputSteam);
			} // end of for
		}
	}

	/**
	 * 获取压缩文件的输入流
	 * 
	 * @param zipFileString
	 *            name of ZIP
	 * @param fileString
	 *            name of file in the ZIP
	 * @return InputStream
	 * @throws Exception
	 */
	public static InputStream UpZip(String zipFileString, String fileString)
			throws Exception {
		@SuppressWarnings("resource")
		ZipFile zipFile = new ZipFile(zipFileString);
		ZipEntry zipEntry = zipFile.getEntry(fileString);
		return zipFile.getInputStream(zipEntry);
	}

	/**
	 * 获取压缩文件的文件结构
	 * 
	 * @param zipFileString
	 *            ZIP name
	 * @param bContainFolder
	 *            contain folder or not
	 * @param bContainFile
	 *            contain file or not
	 * @return
	 * @throws Exception
	 */
	public static List<File> GetFileList(String zipFileString,
			boolean bContainFolder, boolean bContainFile) throws Exception {
		List<File> fileList = new ArrayList<File>();
		ZipInputStream inZip = new ZipInputStream(
				new FileInputStream(zipFileString));
		ZipEntry zipEntry;
		String szName = "";
		while ((zipEntry = inZip.getNextEntry()) != null) {
			szName = zipEntry.getName();
			if (zipEntry.isDirectory()) {
				// get the folder name of the widget
				szName = szName.substring(0, szName.length() - 1);
				File folder = new File(szName);
				if (bContainFolder) {
					fileList.add(folder);
				}

			} else {
				File file = new File(szName);
				if (bContainFile) {
					fileList.add(file);
				}
			}
		}
		inZip.close();
		return fileList;
	}

}
