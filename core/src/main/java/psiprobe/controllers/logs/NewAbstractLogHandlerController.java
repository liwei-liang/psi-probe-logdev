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
package psiprobe.controllers.logs;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import psiprobe.beans.LogByDirectoryBean;
import psiprobe.beans.LogByDirectoryResolverBean;

public abstract class NewAbstractLogHandlerController extends ParameterizableViewController{
	private static final Logger logger = LoggerFactory.getLogger(NewAbstractLogHandlerController.class);

	@Inject
	private LogByDirectoryResolverBean logByDirectoryResolver;

	public LogByDirectoryResolverBean getLogByDirectoryResolver() {
		return logByDirectoryResolver;
	}

	public void setLogByDirectoryResolver(LogByDirectoryResolverBean logByDirectoryResolver) {
		this.logByDirectoryResolver = logByDirectoryResolver;
	}
	
	  @Override
	  protected ModelAndView handleRequestInternal(HttpServletRequest request,
	      HttpServletResponse response) throws Exception {


	    String type = ServletRequestUtils.getStringParameter(request, "type");
	    String name = ServletRequestUtils.getStringParameter(request, "name");
	    
	    LogByDirectoryBean logByDirectoryBean = logByDirectoryResolver.getThisLogDirectory(type, name);

	    ModelAndView modelAndView = null;
	    boolean logFound = false;
	    if (logByDirectoryBean != null) {
	      if (logByDirectoryBean.getFile() != null && logByDirectoryBean.getFile().exists()) {
	        modelAndView = handleLogFile(request, response, logByDirectoryBean);
	        logFound = true;
	      } else {
	        logger.error("{}: file not found", logByDirectoryBean.getFile());
	      }
	    }
	    if (!logFound) {
	      response.sendError(404);
	    }
	    return modelAndView;
	  }

	  protected abstract ModelAndView handleLogFile(HttpServletRequest request,
	      HttpServletResponse response, LogByDirectoryBean logByDirectoryBean) throws Exception;
}
