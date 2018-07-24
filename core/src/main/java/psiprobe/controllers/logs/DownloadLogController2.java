package psiprobe.controllers.logs;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.Utils;
import psiprobe.beans.LogByDirectoryBean;

@Controller
public class DownloadLogController2 extends NewAbstractLogHandlerController{
	  private static final Logger logger = LoggerFactory.getLogger(DownloadLogController2.class);

	  @RequestMapping(path = "/download2")
	  @Override
	  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
	      throws Exception {
	    return super.handleRequest(request, response);
	  }
	  
	  @Override
	  protected ModelAndView handleLogFile(HttpServletRequest request, HttpServletResponse response,
			  LogByDirectoryBean logByDirectoryBean) throws Exception {

	    boolean compressed =
	        "true".equals(ServletRequestUtils.getStringParameter(request, "compressed"));

	    File file = logByDirectoryBean.getFile();
	    logger.info("Sending {}{} to {} ({})", file, compressed ? " compressed" : "",
	        request.getRemoteAddr(), request.getRemoteUser());
	    if (compressed) {
	      Utils.sendCompressedFile(response, file);
	    } else {
	      Utils.sendFile(request, response, file);
	    }
	    return null;
	  }

	  @Value("")
	  @Override
	  public void setViewName(String viewName) {
	    super.setViewName(viewName);
	  }
}
