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
import java.sql.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class LogByDirectoryBean.
 */
public class LogByDirectoryBean {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(LogByDirectoryBean.class);
	
	private File file;
	/** The stdout files. */
	private String name;
	
	private String path;

	private String type;

	private long size;
	
	private Timestamp lastModified;
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getSize() {
		File file = getFile();
		return file != null && file.exists() && !file.isDirectory() ? file.length() : -1;
	}
	
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	public Timestamp getLastModified() {
		File file = getFile();
		return file != null && file.exists() ? new Timestamp(file.lastModified()) : null;
	}

	public String getName() {
		File file = getFile();
		return file != null && file.exists() ? file.getName() : "";
	}

	public void setName(String name) {
		this.name = name;
	}
}
