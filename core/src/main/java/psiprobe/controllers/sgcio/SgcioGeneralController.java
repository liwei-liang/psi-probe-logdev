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
package psiprobe.controllers.sgcio;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

@Controller
public class SgcioGeneralController extends ParameterizableViewController {
	
	  /** The error view. */
	  private String errorView;

	  /**
	   * Gets the error view.
	   *
	   * @return the error view
	   */
	  public String getErrorView() {
	    return errorView;
	  }

	  /**
	   * Sets the error view.
	   *
	   * @param errorView the new error view
	   */
	  @Value("logs_notsupported")
	  public void setErrorView(String errorView) {
	    this.errorView = errorView;
	  }
	  
	  @RequestMapping(path = "/sgcio.htm")
	  @Override
	  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
	      throws Exception {
	    return super.handleRequest(request, response);
	  }

	  @Override
	  protected ModelAndView handleRequestInternal(HttpServletRequest request,HttpServletResponse response) throws Exception {
	      return new ModelAndView(getViewName());		  
	  }

	  @Value("sgcio")
	  @Override
	  public void setViewName(String viewName) {
	    super.setViewName(viewName);
	  }
}
