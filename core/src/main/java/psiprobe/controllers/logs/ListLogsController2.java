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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import psiprobe.Exception.NoAccessAuthorizationException;
import psiprobe.beans.LogByDirectoryBean;
import psiprobe.beans.LogByDirectoryResolverBean;
import psiprobe.beans.PathLevelBean;

/**
 * The Class ListLogsController.
 */
@Controller
public class ListLogsController2 extends ParameterizableViewController {

	/** The error view. */
	private String errorView;

	private String noAuthorizationView;

	private LogByDirectoryBean logByDirectoryBean;

	/** The log resolver. */
	@Inject
	private LogByDirectoryResolverBean logByDirectoryResolver;

	public String getErrorView() {
		return errorView;
	}

	@Value("logs_notsupported")
	public void setErrorView(String errorView) {
		this.errorView = errorView;
	}
	public String getNoAuthorizationView() {
		return noAuthorizationView;
	}

	@Value("logs_noDirectoryAccessAuthorization")
	public void setNoAuthorizationView(String noAuthorizationView) {
		this.noAuthorizationView = noAuthorizationView;
	}
	
	public LogByDirectoryBean getLogByDirectoryBean() {
		return logByDirectoryBean;
	}

	public void setLogByDirectoryBean(LogByDirectoryBean logByDirectoryBean) {
		this.logByDirectoryBean = logByDirectoryBean;
	}

	public LogByDirectoryResolverBean getLogByDirectoryResolver() {
		return logByDirectoryResolver;
	}

	public void setLogByDirectoryResolver(LogByDirectoryResolverBean logByDirectoryResolver) {
		this.logByDirectoryResolver = logByDirectoryResolver;
	}

	@RequestMapping(path = {"/logs2", "/list2.htm"})
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.handleRequest(request, response);
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String initPath = "C:\\";
		try{
			List<PathLevelBean> pathLevelBeans = new ArrayList<>();
			List<LogByDirectoryBean> logByDirectoryList = logByDirectoryResolver.getLogDirectory(initPath);
			if (logByDirectoryList != null) {
				List<File> rootsList = logByDirectoryResolver.getRootsList();
				PathLevelBean pathLevelBean = new PathLevelBean();
				pathLevelBean.setPathLevel(initPath);
				pathLevelBean.setCurrentDirectory(initPath.substring(0, initPath.length()-1));
				pathLevelBeans.add(pathLevelBean);
			    ModelAndView mv = new ModelAndView(getViewName());
			    mv.addObject("logs2", logByDirectoryList);
			    mv.addObject("path", logByDirectoryList.get(0).getPath());
			    mv.addObject("pathLevels", pathLevelBeans);
			    mv.addObject("rootsList", rootsList);
			    return mv;
			}
		}catch(NoAccessAuthorizationException e){
			return new ModelAndView(noAuthorizationView);
		}

		return new ModelAndView(errorView);
	}
	
	@Value("sgcio/logs2")
	@Override
	public void setViewName(String viewName) {
		super.setViewName(viewName);
	}
}
