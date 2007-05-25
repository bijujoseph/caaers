<%--
    This is the decorator for all caAERS workflow pages.

    Controllers which use this page must include two special entries in
    their referenceData:
        - flow: a gov.nih.nci.cabig.caaers.web.Flow instance describing the flow
        - tab: a gov.nih.nci.cabig.caaers.web.Tab instance for the current page
--%>
<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome"%>	
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html
    PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title>caAERS || ${flow.name} || ${tab.longTitle}</title>
    <standard:head/>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:javascriptLink name="tabbedflow"/>
    <decorator:head/>
</head>
<body>
<standard:header/>
<div class="tabpane">
    <chrome:workflowTabs tab="${tab}" flow="${flow}"/>

    <chrome:body title="${flow.name}: ${tab.longTitle}">
        <c:set var="hasSummary" value="${not empty summary}"/>
        <!-- TODO: Summary should be disabled for Overview Pages, need a better logic than this -->
        <c:if test="${hasSummary and tab.viewName != 'study/study_reviewsummary'}">
            <div id="summary-pane" class="pane">
                <chrome:box title="Summary">
                    <c:forEach items="${summary}" var="summaryEntry">
                    <div class="row">
                        <div class="value"><b>${summaryEntry.key} :</b></div>
                    </div>
                     <div class="row">
                         <div class="value">${empty summaryEntry.value ? '<em class="none">None</em>' : summaryEntry.value}</div>
                    </div>        
                    </c:forEach>
                </chrome:box>
            </div>
        </c:if>

        <div id="main${hasSummary ? '' : '-no-summary'}-pane" class="pane">
            <decorator:body/>
        </div>
    </chrome:body>
</div>
<standard:footer/>
</body>
</html>
