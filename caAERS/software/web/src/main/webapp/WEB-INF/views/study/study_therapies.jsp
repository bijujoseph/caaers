<%--
Copyright SemanticBits, Northwestern University and Akaza Research

Distributed under the OSI-approved BSD 3-Clause License.
See http://ncip.github.com/caaers/LICENSE.txt for details.
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome"%>
<%@ taglib prefix="study" tagdir="/WEB-INF/tags/study" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>${tab.longTitle}</title>

<!--[if lte IE 6]>
 <style>
 	#main {
 		top:50px;
 	}
 </style>
<![endif]-->
</head>
<body>
<study:summary />
<tags:tabForm tab="${tab}" flow="${flow}" formName="studyTherapiesForm" >
    
    <jsp:attribute name="repeatingFields">
    <p><tags:instructions code="study.study_therapies.top" /></p>
        	<div>
			<input type="hidden" name="_action" value="">
			<input type="hidden" name="_selected" value="">
		</div>
		
		<table id="test2" class="single-fields" >
        	<tr>
    				<td> 
    				<c:forEach items="${fieldGroups.studyTherapies.fields}" var="field">
                    <tags:renderRow field="${field}"/>
                	</c:forEach>
    				</td>
    		</tr>
    	</table> 
		<c:forEach items="${fieldGroups[mainGroup].fields}" var="field">
				<tags:renderInputs field="${field}" />
		
		</c:forEach>
		
		</jsp:attribute>
	
</tags:tabForm>
</body>
</html>
