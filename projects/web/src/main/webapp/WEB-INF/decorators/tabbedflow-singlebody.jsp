<%--
    This is the decorator for all caAERS workflow pages which only use a single
    <chrome:body> section.
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
    <title>caAERS || ${flow.name} || <decorator:title/></title>
    <standard:head/>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:javascriptLink name="tabbedflow"/>
    <decorator:head/>
</head>
<body>
<standard:header/>
<div class="tabpane">
    <chrome:levelTwoTabs tab="${tab}" flow="${flow}"/>

    <div class="tabcontent workArea">
    <chrome:body title="${flow.name}: ${tab.longTitle}">
        <div class="body">
            <decorator:body/>
        </div>
        <div class="tabcontrols autoclear">
            <c:if test="${tab.number > 0}"><a id="flow-prev" class="tab${tab.number - 1}">&laquo; Previous</a></c:if>
            <a id="flow-next">Save<c:if test="${tab.number < flow.tabCount - 1}"> &amp; Continue &raquo;</c:if></a>
        </div>
    </chrome:body>
    </div>
</div>
<form:form id="flowredirect">
    <input type="hidden" name="_target${tab.targetNumber}" id="flowredirect-target"/>
    <input type="hidden" name="_page${tab.number}"/>
</form:form>
<standard:footer/>
</body>
</html>
