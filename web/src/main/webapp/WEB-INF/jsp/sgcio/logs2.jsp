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
			<script type="text/javascript" src="<c:url value='/js/jquery-3.3.1.min.js'/>"></script>
	</head>

	<c:set var="navTabLogs2" value="active" scope="request"/>

	<body>


		<div class="blockContainer">
			<div class="shadow">
				<div class="info" style="padding-bottom: 10px;">
				<span style="font-size: 0px">
					<ul class="options" style="padding-bottom: 0px;">
						<li style="padding-left: 0px; padding-right: 4px;"><a class="imglink"
							onclick="showDisKList(this)" href="#"> <img class="lnk"
								src="${pageContext.request.contextPath}<spring:theme code='delete.png'/>"
								alt="<spring:message code='probe.jsp.threads.stop.alt'/>" />
						</a></li>
						<li style="padding-left: 0px;">
						<c:forEach var="level" items="${pathLevels}">
								<c:url value="/logs2/entreDirectory.htm" var="levelPath">
									<c:param name="back" value="true" />
									<c:param name="path" value="${level.pathLevel}" />
								</c:url>
								<a class="logfile" href="${levelPath}" style="font-size: 13px"><probe:out
										value="${level.currentDirectory}\\" /></a>
							</c:forEach></li>
						<li id="chooseDisk" style="display: none;">
						<span>change root</span>				
						<select style="width: auto"
							onchange="javaScrpit:changeDisk(this.value)">
								<option value="C:\" selected="selected"><spring:message code='probe.jsp.logs2.select.root'/> </option>
								<c:forEach items="${rootsList}" var="rootsList">
									<option value="${rootsList}"><probe:out
											value="${rootsList}" /></option>
								</c:forEach>
						</select></li>
					</ul>
				</span>
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
	
					<display:column titleKey="probe.jsp.logs.col.type" sortable="true" class="leftmost">
						<c:choose>
							<c:when test="${log.type=='Directory'}">
								<i class="imglink" ><img
									class="lnk" src="${pageContext.request.contextPath}<spring:theme code='directory.png'/>"/>
								</i>
							</c:when>
							<c:when test="${log.type=='Zip'}">
								<i class="imglink" ><img
									class="lnk" src="${pageContext.request.contextPath}<spring:theme code='zip.png'/>"/>
								</i>
							</c:when>
							<c:otherwise>
								<i class="imglink" ><img
									class="lnk" src="${pageContext.request.contextPath}<spring:theme code='file.png'/>"/>
								</i>
							</c:otherwise>
						</c:choose>
					
					
					</display:column>
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

					<display:column titleKey="probe.jsp.logs.col.Action">
						<c:if test="${log.type == 'File' || log.type == 'Zip'}">
						<c:url value="/logs2/download2" var="downloadUrl">
							<c:param name="name" value="${log.name}"/>
							<c:param name="path" value="${log.path}"/>
						</c:url>
						<a class="imglink" href="${downloadUrl}"><img
							class="lnk" title="download" src="${pageContext.request.contextPath}<spring:theme code='download.png'/>"
							alt="<spring:message code='probe.jsp.logs.download.alt'/>"/>
						</a>
							<c:if test="${log.type == 'File'}">
						<a class="imglink" title="download compressed" href="${downloadUrl}&compressed=true"><img
								class="lnk" src="${pageContext.request.contextPath}<spring:theme code='download_compressed.png'/>"
								alt="<spring:message code='probe.jsp.logs.download.alt'/>"/></a>
								</c:if>
						</c:if>
						<c:if test="${log.type == 'Zip'}">
						<c:url value="/logs2/Unzip" var="unZipUrl">
							<c:param name="name" value="${log.name}"/>
							<c:param name="path" value="${log.path}"/>
						</c:url>
						<a class="logfile" title="unzip" href="${unZipUrl}"> <spring:message
								code="probe.jsp.logs2.unzip" />
						</a>
						</c:if>
						
					</display:column>
					
					<display:column titleKey="probe.jsp.logs.col.size" sortable="true" sortProperty="size">
						<probe:volume value="${log.size}"/>&#160;
					</display:column>
					
					<display:column titleKey="probe.jsp.logs.col.modified" sortable="true" sortProperty="lastModified">
						${log.lastModified}&#160;
					</display:column>
					
				</display:table>
		</div>
		<script type="text/javascript">

			function showDisKList(obj){
		        var top = jQuery(obj).offset().top;
		        var left = jQuery(obj).offset().left + $(obj).width() - 7;
			    var element = document.getElementById("chooseDisk");
			    //jQuery("#chooseDisk").css({'top':top + "px",'left':left+"px"}).show();
				if(element.style.display == ""){
					element.style.display = "none";
				}else{
				    element.style.display = "";
				}
			}
			
			function changeDisk(root){
				root=root.substring(0,root.length-2)
				var url = "/probe/logs2/entreDirectory.htm?back=true&path="+root+"%3a%5c#";
				window.location.href=url;
			}
		</script>
	</body>
</html>
