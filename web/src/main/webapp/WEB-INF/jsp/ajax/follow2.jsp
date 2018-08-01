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
<script type="text/javascript">
var fixed_width ='fixed_width'
</script>
<%-- An AJAX HTML bit, representing log file content. --%>

<div id = "lineContent" >
	<c:forEach items="${lines}" var="line">
		<div class="line"><c:out value="${line}" escapeXml="true" /></div>
	</c:forEach>
</div>
<a class="Operation" href="javaScript:highlight('[INFO]')">
HightError
</a>
<div>Keyword: <input type="text" name="keyword" id="highLishtBtn" >
    <button id="btn" onclick="javaScript:heightKeyWord()">HightLight</button>
</div>
<div id="text">Hight light the key word</div>