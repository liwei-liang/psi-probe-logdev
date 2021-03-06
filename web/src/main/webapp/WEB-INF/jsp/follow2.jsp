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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="https://github.com/psi-probe/psi-probe/jsp/tags" prefix="probe" %>

<%-- Log file view. The view is a simple markup that gets updated via AJAX calls. Top menu does not go to the server but
 rather does DOM tricks to modify content appearance. --%>
<style>
::selection {
	color: red;
}
</style>
<html>
	<head>
		<title><spring:message code="probe.jsp.title.follow"/></title>
		<script type="text/javascript" src="<c:url value='/js/jquery-3.3.1.min.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/js/prototype.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/js/scriptaculous/scriptaculous.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/js/func.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/js/behaviour.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/js/hightLight.js'/>"></script>
	</head>

	<c:set var="navTabLogs2" value="active" scope="request"/>

	<body>

		<ul class="options">
			<li id="back">
				<c:url value="/logs2/entreDirectory.htm" var="backUrl">
					<c:param name="name" value="${log.name}"/>
					<c:param name="path" value="${log.path}"/>
					<c:param name="back" value="true"/>
				</c:url>
				<a href="${backUrl}">
					<spring:message code="probe.jsp.follow.menu.back"/>
				</a>
			</li>
			<li id="pause">
				<a href="#">
					<spring:message code="probe.jsp.follow.menu.pause"/>
				</a>
			</li>
			<li id="resume" style="display: none;">
				<a href="#">
					<spring:message code="probe.jsp.follow.menu.resume"/>
				</a>
			</li>
			<li id="zoomin">
				<a href="#">
					<spring:message code="probe.jsp.follow.menu.zoomin"/>
				</a>
			</li>
			<li id="zoomout">
				<a href="#">
					<spring:message code="probe.jsp.follow.menu.zoomout"/>
				</a>
			</li>
			<li id="wrap">
				<a href="#">
					<spring:message code="probe.jsp.follow.menu.wrap"/>
				</a>
			</li>
			<li id="nowrap" style="display: none;">
				<a href="#">
					<spring:message code="probe.jsp.follow.menu.nowrap"/>
				</a>
			</li>
			<li id="clear">
				<a href="#">
					<spring:message code="probe.jsp.follow.menu.clear"/>
				</a>
			</li>
			<li>
				<spring:message code="probe.jsp.follow2.number.line"/>
				<select id="lineNum" name="lineNumVal" style="width: 90px" onchange="javaScrpit:changeLineNum()">
					<option value="100">100</option>
					<option value="500">500</option>
					<option value="1000" selected>1000</option>
					<option value="5000">5000</option>
				</select>
			</li>
	
			<li id="download">
				<c:url value="/logs2/download2" var="downloadUrl">
					<c:param name="name" value=""/>
					<c:param name="path" value="${log.path}"/>
				</c:url>
				<a href="${downloadUrl}">
					<spring:message code="probe.jsp.follow.menu.download"/>
				</a>
			</li>
		</ul>


		<div class="blockContainer">
			<h3><spring:message code="probe.jsp.follow.h3.fileInfo"/></h3>

			<div class="shadow">
				<div id="info" class="info">
					<div class="ajax_activity"></div>
				</div>
			</div>

		<h3 id="h3Activity">
			<spring:message code="probe.jsp.follow.h3.fileContent" />&nbsp  &nbsp &nbsp  &nbsp
			<a class="Operation" href="javaScript:highlight()">
				  <spring:message code="probe.jsp.logs2.highlight.level"/> </a>
<!-- &nbsp &nbsp<span> -->
<!-- 				Keyword: <input type="text" name="keyword" id="highLishtBtn"> -->
<!-- 				<button id="btn" onclick="javaScript:heightKeyWord()">HightLight</button> -->
<!-- 			</span> -->
		</h3>

		<div class="shaper">
				<div id="file_content" class="fixed_width">
					<div id="ajaxContent" class="ajax_activity"></div>
				</div>
		</div>
	</div>

		<script type="text/javascript">
			var logLevel = new Array("TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL");
			var file_content_div = 'file_content';
			var topPosition = -1;
			var tailingEnabled = true;
			var maxLines = 5000;
			var initialLines = $('lineNum').value;
			var lastLogSize = -1;
			var logSizeRegex = /<span title="(\d*)">/;
			
			function logSize(responseText) {
				var captures = logSizeRegex.exec(responseText);
				return captures.length > 1 ? captures[1] : lastLogSize;
			}

			var infoUpdater = new Ajax.PeriodicalUpdater('info', '<c:url value="/logs2/ff_info2.ajax"/>', {
				method:'get',
				parameters: {
					type: '${probe:escapeJS(log.type)}',
					path: '${probe:escapeJS(log.path)}',
					name: ''
				},
				frequency: 3,
				onSuccess: function(response) {
					if (tailingEnabled) {
						var currentLogSize = logSize(response.responseText);
						if (lastLogSize != currentLogSize) {
							followLog(currentLogSize);
							lastLogSize = currentLogSize;
						}
					}
				}
			});
			
			function followLog(currentLogSize) {
				new Ajax.Updater(file_content_div, '<c:url value="/logs2/follow2.ajax"/>', {
					method:'get',
					parameters: {
						type: '${probe:escapeJS(log.type)}',
						path: '${probe:escapeJS(log.path)}',
						name: '',
						lastKnownLength: (lastLogSize == -1 ? 0 : lastLogSize),
						currentLength: currentLogSize,
						maxReadLines: (lastLogSize == -1 ? initialLines : undefined)
					},
					insertion: (lastLogSize == -1 ? undefined : 'bottom'),
					onComplete: function() {
					    var element = document.getElementById("h3Activity");
					    element.classList.remove("ajax_activity");
					    highlight();
					    highlight();
						objDiv = document.getElementById(file_content_div);
						if (topPosition == -1) {
							objDiv.scrollTop = objDiv.scrollHeight;
						} else {
							objDiv.scrollTop = topPosition
						}
						
						var lines = $(objDiv).childElements();
						var numOfLines = lines.length;
						var toBeRemoved = new Array();
						for (var i = 0; i < numOfLines - maxLines; i++) {
							toBeRemoved.push(lines[i]);
						}
						for (var i = 0; i < toBeRemoved.length; i++) {
							toBeRemoved[i].remove();
						}
					},

					onCreate: function() {
						objDiv = document.getElementById(file_content_div);
						if (objDiv.scrollTop + objDiv.clientHeight == objDiv.scrollHeight) {
							topPosition = -1;
						} else {
							topPosition = objDiv.scrollTop;
						}
					}
				});
			}

			function changeLineNum(){
				lastLogSize = -1;
				initialLines = $('lineNum').value;
			    var element = document.getElementById("h3Activity");
			    element.classList.add("ajax_activity");
			}
			//
			// unfortunately it is not possible to set the size of "file_content" div in percent.
			// i'm not sure why, but most likely it is a browser bug.
			// Therefore we need to adjust the size of the view div when browser window resizes,
			// hence this hook:
			//
			window.onresize = function() {
				var h = (getWindowHeight() - 390) + 'px';
				Element.setStyle(file_content_div, {height: h});
			}

			//
			// now resize the view div on page load
			//
			window.onresize();

			//
			// install "onClick" rules
			//
			var rules = {
				'#pause' : function (element) {
					element.onclick = function () {
						tailingEnabled = false;
						Element.hide('pause');
						Element.show('resume');
						return false;
					}
				},
				'#resume': function (element) {
					element.onclick = function () {
						tailingEnabled = true;
						Element.hide('resume');
						Element.show('pause');
						return false;
					}
				},
				'#zoomin': function(element) {
					element.onclick = function () {
						var e = $(file_content_div);
						if (e) {
							var old_size = e.getStyle('fontSize').replace('px', '');
							var new_size = (old_size - 1 + 3);
							if (new_size <= 32) {
								setFontSize(e, new_size, true);
							}
						}
						return false;
					}
				},
				'#zoomout': function(element) {
					element.onclick = function () {
						var e = $(file_content_div);
						if (e) {
							var old_size = e.getStyle('fontSize').replace('px', '');
							var new_size = (old_size - 3 + 1);
							if (new_size >= 4) {
								setFontSize(e, new_size, true);
							}
						}
						return false;
					}
				},
				'#wrap': function(element) {
					element.onclick = function () {
						Element.setStyle(file_content_div, {'whiteSpace': 'normal'});
						Element.hide('wrap');
						Element.show('nowrap');
						return false;
					}
				},
				'#nowrap': function(element) {
					element.onclick = function () {
						Element.setStyle(file_content_div, {'whiteSpace': 'nowrap'});
						Element.hide('nowrap');
						Element.show('wrap');
						return false;
					}
				},
				'#clear': function(element) {
					element.onclick = function() {
						$(file_content_div).update();
						followLog(undefined);
						return false;
					}
				}

			}
			Behaviour.register(rules);

			function setFontSize(elm, new_size, save) {
				elm.setStyle({'fontSize': new_size + 'px'});
				if (save) {
					new Ajax.Request('<c:url value="/remember.ajax"/>?cn=file_content_font_size&state=' + new_size, {method:'get',asynchronous:true});
				}
			}
			
			jQuery(document).ready(function(){
				highlight();
 				jQuery(".fixed_width").dblclick(function(){
 					if (window.getSelection) {
 	                	txt = window.getSelection()+"";
 	       			}
 					heightByKeyWord(txt);
 				  });
			});
		</script>

		<c:if test="${cookie['file_content_font_size'] != null}">
			<script type="text/javascript">
				Event.observe(window, 'load', function() {
					setFontSize($(file_content_div), `<c:out value='${cookie["file_content_font_size"].value}' />`, false);
				});
			</script>
		</c:if>

	</body>
</html>
