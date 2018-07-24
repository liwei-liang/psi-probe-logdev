<%--

    Licensed under the GPL License. You may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      https://www.gnu.org/licenses/old-licenses/gpl-2.0.html

    THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
    WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
    PURPOSE.

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="https://github.com/psi-probe/psi-probe/jsp/tags" prefix="probe" %>

<%-- Simple tabular list of log files and their attributes. The page is further linked to
 log file viewer and file download controller. --%>
<html>
	<head>
		<title>
			<spring:message code="probe.jsp.title.logs"/>
		</title>
	</head>

	<c:set var="navTabLogs2" value="active" scope="request"/>

	<body>

		<div class="blockContainer">
			<div class="shadow">
				<div class="info" style="padding-bottom: 10px;">
					<span class="value"> <probe:out value="${path}" maxLength="80"
							ellipsisRight="false" /></span>
					<ul class="options" >
						<li id="back">
								<c:url value="/logs2/entreDirectory.htm" var="backUrlTest">
									<c:param name="back" value="true"/>
									<c:param name="path" value="${path}"/>
								</c:url>
							<a class="logfile" href="${backUrlTest}"> <spring:message
								code="probe.jsp.logs2.menu.back" />
							</a>
						</li>
					</ul>
				</div>
			</div>
	
	
	
			<display:table name="logs2" class="genericTbl" style="border-spacing:0;border-collapse:separate;" uid="log" requestURI="">
	
					<display:column titleKey="probe.jsp.logs.col.type" sortable="true" property="type" class="leftmost"/>
	
					<display:column titleKey="probe.jsp.logs.col.file" sortable="true" sortProperty="file">
						<c:choose>

							<c:when test="${log.type=='File'}">
								<c:url value="/logs2/follow2.htm" var="followUrlTest">
									<c:param name="type" value="${log.type}"/>	
									<c:param name="name" value="${log.name}"/>
									<c:param name="path" value="${log.path}"/>
								</c:url>
							</c:when>
		
							<c:otherwise>
								<c:url value="/logs2/entreDirectory.htm" var="followUrlTest">
									<c:param name="name" value="${log.name}"/>
									<c:param name="path" value="${log.path}"/>
									<c:param name="back" value="false"/>
								</c:url>
							</c:otherwise>
		
						</c:choose>
						
						<a class="logfile" href="${followUrlTest}">
							<probe:out value="${log.name}" maxLength="80" ellipsisRight="false"/>
						</a>
					</display:column>
					
					<display:column titleKey="probe.jsp.logs.col.size" sortable="true" sortProperty="size">
						<probe:volume value="${log.size}"/>&#160;
					</display:column>
					
					<display:column titleKey="probe.jsp.logs.col.modified" sortable="true" sortProperty="lastModified">
						${log.lastModified}&#160;
					</display:column>
					
				</display:table>
		</div>
	</body>
</html>
