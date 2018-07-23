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

/**
 * The Class LogByDirectoryResolverBean.
 */
public class LogByDirectoryResolverBean {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(LogByDirectoryResolverBean.class);

	private String logDirectoryPath = new String();

	{
		initDirectoryToMonitor();
	}

	/**
	 * Gets the log destinations.
	 *
	 * @param all
	 *            the all
	 * @return the log destinations
	 */
	public List<LogByDirectoryBean> getLogDirectory() {
		logger.info("[LogByDirectoryResolverBean]" + "enter directory");
		List<LogByDirectoryBean> logByDirectoryList = new ArrayList<LogByDirectoryBean>();
		File directoryPath = new File(logDirectoryPath);
		if (directoryPath.isDirectory()) {
			for (File file : directoryPath.listFiles()) {
				LogByDirectoryBean.setPath(logDirectoryPath);
				LogByDirectoryBean logByDirectoryBean = new LogByDirectoryBean();
				logByDirectoryBean.setFile(file);
				logByDirectoryBean.setName(logByDirectoryBean.getName());
				logByDirectoryBean.setSize(logByDirectoryBean.getSize());
				logByDirectoryBean.setType(file.isDirectory() ? FileType.Directory.name() : FileType.File.name());
				logByDirectoryBean.setLastModified(logByDirectoryBean.getLastModified());
				logByDirectoryList.add(logByDirectoryBean);
			}
		}
		return logByDirectoryList;
	}

	public LogByDirectoryBean getThisLogDirectory(String type, String name) {
		logger.info("[LogByDirectoryResolverBean]" + "set the file info");
		LogByDirectoryBean logByDirectoryBean = new LogByDirectoryBean();
		File file = new File(LogByDirectoryBean.getPath() + "\\" + name);
		if(file.isDirectory()){
			
		}
		logByDirectoryBean.setFile(file);
		logByDirectoryBean.setType(type);
		logByDirectoryBean.setName(name);
		logByDirectoryBean.setSize(logByDirectoryBean.getSize());
		logByDirectoryBean.setLastModified(logByDirectoryBean.getLastModified());

		return logByDirectoryBean;
	}

	public List<LogByDirectoryBean> enterThisDirectory(String name, boolean back){
		if(!back){
			logDirectoryPath = logDirectoryPath + "\\" + name;
		}else{
			logDirectoryPath = logDirectoryPath.substring(0, logDirectoryPath.lastIndexOf("\\"));
		}
		if(logDirectoryPath.equals("C:")){
			logDirectoryPath += "\\";
		}
		return getLogDirectory();
	}
	
	public void initDirectoryToMonitor() {
		logger.info("[LogByDirectoryResolverBean]" + "init directory path to monitor");
		logDirectoryPath = "C:\\SG4P";
	}

}
