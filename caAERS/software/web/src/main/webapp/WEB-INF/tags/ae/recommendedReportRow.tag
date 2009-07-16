<%--
 Flow : CaptureAdverseEventController
ae_review_report.jsp uses this to display a list of serious adverse events. 
--%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<%@attribute name="aeReportId" required="true" type="java.lang.Integer" description="Id of the data collection"%>
<%@attribute name="recommendedTableRows" required="true" type="java.util.List" description="The recommended report definitions to show" %>
<%@attribute name="applicableTableRows" required="true" type="java.util.List" description="The applicable report definitions to show" %>

<c:if test="${not empty recommendedTableRows}">
<div id="reports-header-dc-${aeReportId}" class="row" >
	<div class="leftpanel">
	  <b>Reports</b>
	</div>
	<div class="rightpanel" style="text-align:right;">
	 <span id="dc-${aeReportId}-restore" style="display:none;"><a href="#report-dc-${aeReportId}" onclick="restoreRecommended('recommended-reports-dc-${aeReportId}','applicable-reports-dc-${aeReportId}',${aeReportId})" >Restore recommended action</a></span>
	 <span id="dc-${aeReportId}-override" ><a href="#report-dc-${aeReportId}" onclick="overrideRecommendedActions('recommended-reports-dc-${aeReportId}','applicable-reports-dc-${aeReportId}',${aeReportId})" >Override</a></span>
	</div>
</div>

<a name="report-dc-${aeReportId}"></a>

<div id="recommended-reports-dc-${aeReportId}" class="recommended-reports">
<table width="100%" class="tablecontent">
    <tr>
      <th scope="col" align="center" style="text-align:center;" width="3%"><b>Select</b></th>
      <th scope="col" align="center" style="text-align:center;" width="30%">Report</th>
      <th scope="col" align="left" ><b>Status</b> </th>
      <th scope="col" align="left"><b>Due</b> </th>
      <th scope="col" align="left"><b>Action</b> </th>
    </tr>
    <c:forEach var="row" items="${recommendedTableRows}">
     <tr>
    	   <td align="center">
    	     <span>
    	   	  <img src="<chrome:imageUrl name="../${( (row.action eq 'Create') or (row.action eq 'Edit')) ?'check.png' : 'checkno.gif'}" />" />
    	     </span>
    	   </td>
	  	   <td><span>${row.reportDefinition.label}</span></td>
	  	   <td><span>${row.status }</span></td>
	  	   <td><span>${row.due }</span></td>
	  	   <td><span>${row.action }</span></td>
     </tr>
    </c:forEach>
    
</table>	
</div>   
</c:if>

<c:if test="${empty recommendedTableRows}">
<div id="recommended-reports-dc-${aeReportId}" class="recommended-reports">

<a name="report-dc-${aeReportId}"></a>

Click <a  style='cursor:pointer' class="link" href="#report-dc-${aeReportId}" onclick="showManualSelectOptions('applicable-reports-dc-${aeReportId}',${aeReportId})">here</a> <tags:message key="instruction_ae_manualselection_note" />
</div>
</c:if>

<div id="applicable-reports-dc-${aeReportId}" style="display:none;" class="applicable-reports">
<table width="100%" class="tablecontent">
    <tr>
      <th scope="col" align="center" style="text-align:center;" width="3%"><b>Select</b></th>
      <th scope="col" align="center" style="text-align:center;" width="30%">Report</th>
      <th scope="col" align="left" ><b>Status</b> </th>
      <th scope="col" align="left"><b>Due</b> </th>
      <th scope="col" align="left"><b>Action</b> </th>
    </tr>
    
 	<c:forEach var="row" items="${applicableTableRows}">
    <c:set var="_elID" value="rd_${aeReportId}_${row.reportDefinition.id}" />
    <tr id="${_elID}-row" class="optional" >
	  	   <td align="center">
	  	   <input id="${_elID}" 
	  	   		type="checkbox" name="rd_${aeReportId}_checked" 
	  	   		class="chk_${aeReportId} ${row.group}"
	  	   		value="${ row.reportDefinition.id}"
	  	   		onclick="deSelectItemsOfSameGroup(${aeReportId},${row.reportDefinition.id})"
	  	   		/> 
	  	   	</td>
	  	   <td>
	  	   	<span id="${_elID}-reportName">${row.reportDefinition.label}</span>
	  	   	<input type="hidden" name="rd_${aeReportId}" value="${row.reportDefinition.id}" />
	  	   	<input type="hidden" id="${_elID}_manual" name="${_elID}_manual" value="0" class="manual-indicator-${aeReportId}"/>
	  	   </td>
	  	   <td>
	  	   <span id="${_elID}-reportStatus">${row.status }</span>
	  	   <input type="hidden" id="${_elID}_status" value="${row.status }" />
	  	   <input type="hidden" id="${_elID}_grpstatus" value="${row.grpStatus }" />
	  	   <input type="hidden" id="${_elID}_otherstatus" value="${row.otherStatus }" />
	  	   <input type="hidden" id="${_elID}_actualstatus" name="${_elID}_actualstatus" value="" />
	  	   
	  	   </td>
	  	   <td><span id="${_elID}-reportDue">${row.due }</span>
	  	   <input type="hidden" id="${_elID}_due" value="${row.due }" />
	  	   <input type="hidden" id="${_elID}_grpdue" value="${row.grpDue }" />
	  	   <input type="hidden" id="${_elID}_otherdue" value="${row.otherDue }" />
	  	   <input type="hidden" id="${_elID}_actualdue" name="${_elID}_actualdue" value="" />
	  	   </td>
	  	   <td><span id="${_elID}-reportAction">${row.action }</span>
	  	   <input type="hidden" id="${_elID}_action" value="${row.action }" />
	  	   <input type="hidden" id="${_elID}_grpaction" value="${row.grpAction }" />
	  	   <input type="hidden" id="${_elID}_otheraction" value="${row.otherAction }" />
	  	   <input type="hidden" id="${_elID}_actualaction" name="${_elID}_actualaction" value="" class="actualAction" />
	  	   </td>
    </tr>
	</c:forEach>    
</table>	
</div>  
 
<%--
<div id="reports-footer-dc-${aeReportId}" class="row" >
	<div class="rightpanel" style="text-align:right;">
		<span id="dc-${aeReportId}-viewMore" ><a href="#report-dc-${aeReportId}" onclick="viewMore('reports-dc-${aeReportId}',${aeReportId}, true)" >View more options</a></span>
		<span id="dc-${aeReportId}-hideMore" style="display:none;"><a href="#report-dc-${aeReportId}" onclick="viewMore('reports-dc-${aeReportId}',${aeReportId}, false)" >Hide more options</a></span>
	</div>
</div>    
--%>
       