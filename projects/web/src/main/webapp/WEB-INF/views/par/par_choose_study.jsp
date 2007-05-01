<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/extremecomponents.css"/>">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>choose a Study</title>
<script>
function submitPage(s){
	document.getElementById("command").submit();
}
function navRollOver(obj, state) {
  document.getElementById(obj).className = (state == 'on') ? 'resultsOver' : 'results';
}
function doNothing(){
}

function updateTargetPage(s){
		document.checkEligibility.nextView.value=s;
		document.checkEligibility.submit();
}
</script>
</head>
<body>
<!-- TOP LOGOS END HERE -->
<!-- TOP NAVIGATION STARTS HERE -->

<chrome:search title="">
    <form:form id="searchForm" method="post">
        <table border="0" cellspacing="0" cellpadding="0" class="search">
            <tr>
            </tr>
            <tr>
                <td class="searchType">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </td>
                <td><form:select path="searchType">
						<form:options items="${searchType}" itemLabel="desc"itemValue="code" />
					</form:select></td>
                <td><form:input path="searchText" size="25" /></td>
                <td><input type="submit" value="go" name="_target1" alt="GO"></td>
            </tr>
            <tr>
                <td></td>
                <td colspan="4" class="notation">
                    <span class="labels">(<span class="red">*</span><em>Required
                        Information</em>)</span>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    ^ Minimum two characters for search.
                </td>
            </tr>
        </table>
    </form:form>
</chrome:search>

<tags:tabForm tab="${tab}" flow="${flow}">
    <jsp:attribute name="singleFields">
        <ec:table items="command.studies" var="study"
            action="${pageContext.request.contextPath}/pages/newParticipant"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"
            showPagination="false" form="command"
            cellspacing="0" cellpadding="0" border="0" width="80%" style=""
            styleClass="">
            <ec:row highlightRow="true">
                <ec:column property="kk" style="width:10px" filterable="false"
                    sortable="false" title=" ">
                    <form:checkbox path="studySiteArray" value="${study.studySites[0].id}" />
                </ec:column>
                <ec:column property="shortTitle" width="6" title="Short Title" />
                <ec:column property="longTitle" title="Long Title" />
                <ec:column property="description" title="Description" />
                <ec:column property="principalInvestigatorCode"
                    title="InvestigatorCode" />
                <ec:column property="principalInvestigatorName"
                    title="InvestigatorName" />
                <ec:column property="primarySponsorCode" title="Sponsor Code" />
                <ec:column property="primarySponsorName" title="Sponsor Name" />
            </ec:row>
        </ec:table>
    </jsp:attribute>
</tags:tabForm>
</body>
</html>
