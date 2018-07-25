/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.beans;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import psiprobe.Constant.FileType;
import psiprobe.Exception.NoAccessAuthorizationException;

/**
 * The Class LogByDirectoryResolverBean.
 */
public class LogByDirectoryResolverBean {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(LogByDirectoryResolverBean.class);

	private static byte[] ZIP_HEADER_1 = new byte[] { 80, 75, 3, 4 };
	private static byte[] ZIP_HEADER_2 = new byte[] { 80, 75, 5, 6 };

	/**
	 * Gets the log destinations.
	 *
	 * @param all
	 *            the all
	 * @return the log destinations
	 */
	public List<LogByDirectoryBean> getLogDirectory(String path) throws NoAccessAuthorizationException {
		logger.info("Get this directory files");
		List<LogByDirectoryBean> logByDirectoryList = new ArrayList<LogByDirectoryBean>();
		File directoryPath = new File(path);
		if (directoryPath.isDirectory()) {
			try {
				for (File file : directoryPath.listFiles()) {
					LogByDirectoryBean logByDirectoryBean = new LogByDirectoryBean();
					logByDirectoryBean.setPath(directoryPath.getAbsolutePath());
					logByDirectoryBean.setFile(file);
					logByDirectoryBean.setName(logByDirectoryBean.getName());
					logByDirectoryBean.setSize(logByDirectoryBean.getSize());
					logByDirectoryBean.setType(file.isDirectory() ? FileType.Directory.name()
							: isZipFile(file) ? FileType.Zip.name() : FileType.File.name());
					logByDirectoryBean.setLastModified(logByDirectoryBean.getLastModified());
					logByDirectoryList.add(logByDirectoryBean);
				}
			} catch (NullPointerException e) {
				logger.error(e.toString());
				throw new NoAccessAuthorizationException();
			}

		}
		return logByDirectoryList;
	}

	public LogByDirectoryBean getThisLogDirectory(String type, String name, String path) {
		logger.info("Set the file info");
		LogByDirectoryBean logByDirectoryBean = new LogByDirectoryBean();
		File file = new File(path + "\\" + name);
		logByDirectoryBean.setPath(file.getAbsolutePath());
		logByDirectoryBean.setFile(file);
		logByDirectoryBean.setType(type);
		logByDirectoryBean.setName("");
		logByDirectoryBean.setSize(logByDirectoryBean.getSize());
		logByDirectoryBean.setLastModified(logByDirectoryBean.getLastModified());
		return logByDirectoryBean;
	}

	public List<LogByDirectoryBean> enterThisDirectory(String name, boolean back, String path) throws Exception {
		logger.info("Enter the directory");
		String newPath;
		if (!back) {
			newPath = path + "\\" + name;
		} else {
			newPath = path.substring(0, path.lastIndexOf("\\"));
		}
		if (newPath.equals("C:")) {
			newPath += "\\";
		}
		return getLogDirectory(newPath);
	}

	public boolean isZipFile(File file) {
		if (file == null) {
			return false;
		}

		if (file.isDirectory()) {
			return false;
		}

		boolean isArchive = false;
		InputStream input = null;
		try {
			input = new FileInputStream(file);
			byte[] buffer = new byte[4];
			int length = input.read(buffer, 0, 4);
			if (length == 4) {
				isArchive = (Arrays.equals(ZIP_HEADER_1, buffer)) || (Arrays.equals(ZIP_HEADER_2, buffer));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
		}

		return isArchive;
	}

	public List<LogByDirectoryBean> UnzipHere(String path, String name) throws NoAccessAuthorizationException {
		String sourceFilePath = path + "\\" + name;
		String destPath = sourceFilePath.substring(0, sourceFilePath.lastIndexOf("."));
		try {
			logger.info("begin unzip");
			File unzipFileDir = new File(destPath);
			File zipFile = new File(path + "\\" + name);
			if (!unzipFileDir.exists() || !unzipFileDir.isDirectory()) {
				unzipFileDir.mkdirs();
			} else {
				for(int i = 0;i<10;i++){
					unzipFileDir = new File(destPath+i);
					if(!unzipFileDir.exists()){
						unzipFileDir.mkdirs();
						logger.info("[LogByDirectoryResolverBean]"+ unzipFileDir.getPath() +"been made");

						destPath += i;
						break;
					}
					if(i==9){
						String error = "File"+ sourceFilePath + " unzip fail, has been upziped too many times";
						logger.error(error);
						throw new IOException(error);
					}
				}
			}

			ZipEntry entry = null;
			String entryFilePath = null, entryDirPath = null;
			File entryFile = null, entryDir = null;
			int index = 0, count = 0, bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			ZipFile zip = new ZipFile(zipFile);
			Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
			while (entries.hasMoreElements()) {
				entry = entries.nextElement();
				String enteyName = entry.getName().replaceAll("/", "\\\\");
				entryFilePath = destPath + File.separator + enteyName;
				index = entryFilePath.lastIndexOf(File.separator);
				if (index != -1) {
					entryDirPath = entryFilePath.substring(0, index);
				} else {
					entryDirPath = "";
				}
				entryDir = new File(entryDirPath);
				if (!entryDir.exists() || !entryDir.isDirectory()) {
					logger.info(entryDir.getPath() +"been made");
					entryDir.mkdirs();
				}
				if (index < entryFilePath.length() - 1) {
					entryFile = new File(entryFilePath);
					if (entryFile.exists()) {
						// SecurityManager securityManager = new
						// SecurityManager();
						// securityManager.checkDelete(entryFilePath);
						entryFile.delete();
					}
					bos = new BufferedOutputStream(new FileOutputStream(entryFile));
					bis = new BufferedInputStream(zip.getInputStream(entry));
					while ((count = bis.read(buffer, 0, bufferSize)) != -1) {
						bos.write(buffer, 0, count);
					}
					bos.flush();
					bos.close();
				}
			}
		} catch (IOException e) {
			logger.info("Unzip fail");
			e.printStackTrace();
		} catch (AccessControlException e) {
			logger.info("Unzip fail, file already exist, can't delete it");
			e.printStackTrace();
		}
		logger.info("unzip finished successfulley");

		return getLogDirectory(destPath);
	}
}
