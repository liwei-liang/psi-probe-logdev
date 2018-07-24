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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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

	/**
	 * Gets the log destinations.
	 *
	 * @param all
	 *            the all
	 * @return the log destinations
	 */
	public List<LogByDirectoryBean> getLogDirectory(String path) throws NoAccessAuthorizationException{
		logger.info("[LogByDirectoryResolverBean]" + "enter directory");
		List<LogByDirectoryBean> logByDirectoryList = new ArrayList<LogByDirectoryBean>();
		File directoryPath = new File(path);
		if (directoryPath.isDirectory()) {
			try{
				for (File file : directoryPath.listFiles()) {
					LogByDirectoryBean logByDirectoryBean = new LogByDirectoryBean();
					logByDirectoryBean.setPath(directoryPath.getAbsolutePath());
					logByDirectoryBean.setFile(file);
					logByDirectoryBean.setName(logByDirectoryBean.getName());
					logByDirectoryBean.setSize(logByDirectoryBean.getSize());
					logByDirectoryBean.setType(file.isDirectory() ? FileType.Directory.name() : FileType.File.name());
					logByDirectoryBean.setLastModified(logByDirectoryBean.getLastModified());
					logByDirectoryList.add(logByDirectoryBean);
				}
			}catch(NullPointerException e){
				logger.error(e.toString());
				throw new NoAccessAuthorizationException();
			}

		}
		return logByDirectoryList;
	}

	public LogByDirectoryBean getThisLogDirectory(String type, String name, String path) {
		logger.info("[LogByDirectoryResolverBean]" + "set the file info");
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

	public List<LogByDirectoryBean> enterThisDirectory(String name, boolean back, String path) throws Exception{
		String newPath;
		if(!back){
			newPath = path + "\\" + name;
		}else{
			newPath = path.substring(0, path.lastIndexOf("\\"));
		}
		if(newPath.equals("C:")){
			newPath += "\\";
		}
		return getLogDirectory(newPath);
	}

}
