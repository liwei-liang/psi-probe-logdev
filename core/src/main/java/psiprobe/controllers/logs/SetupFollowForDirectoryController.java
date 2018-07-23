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

import java.util.List;


import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import psiprobe.beans.LogByDirectoryBean;
import psiprobe.beans.LogByDirectoryResolverBean;

@Controller
public class SetupFollowForDirectoryController extends ParameterizableViewController{

	@Inject
	private LogByDirectoryResolverBean logByDirectoryResolver;
	
	private String errorView;

	public String getErrorView() {
		return errorView;
	}

	@Value("logs_notsupported")
	public void setErrorView(String errorView) {
		this.errorView = errorView;
	}
	public LogByDirectoryResolverBean getLogByDirectoryResolver() {
		return logByDirectoryResolver;
	}

	public void setLogByDirectoryResolver(LogByDirectoryResolverBean logByDirectoryResolver) {
		this.logByDirectoryResolver = logByDirectoryResolver;
	}
	
	  @RequestMapping(path = "/entreDirectory.htm")
	  @Override
	  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
	      throws Exception {
	    return super.handleRequest(request, response);
	  }
	  
	  @Override
	  protected ModelAndView handleRequestInternal(HttpServletRequest request,
	      HttpServletResponse response) throws Exception {


	    String name = ServletRequestUtils.getStringParameter(request, "name");
	    boolean back = ServletRequestUtils.getBooleanParameter(request, "back", false);

	    List<LogByDirectoryBean> logByDirectoryList = logByDirectoryResolver.enterThisDirectory(name, back);
		if (logByDirectoryList != null) {
		    ModelAndView mv = new ModelAndView(getViewName());
		    mv.addObject("logs2", logByDirectoryList);
		    mv.addObject("path",LogByDirectoryBean.getPath());
		    return mv;
		}
		return new ModelAndView(errorView);
	  }

	  @Value("logs2")
	  @Override
	  public void setViewName(String viewName) {
	    super.setViewName(viewName);
	  }

}
