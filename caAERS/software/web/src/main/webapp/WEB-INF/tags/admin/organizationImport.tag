<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<c:if test='${fn:length(command.importableOrganizations) > 0 }'>
		<chrome:division title="${fn:length(command.importableOrganizations)} Organization records got loaded" id="organization_load">
		
		<table id="test" width="100%" class="tablecontent">
    		<tr>
    			<th scope="col" align="left"><b>Assigned Identifier</b> </th>
    			<th scope="col" align="left"><b>Organization name</b> </th>
    		</tr>
    		<c:forEach var='item' items='${command.importableOrganizations}'>
			<tr class="results">
				<td align="left"><c:out value="${item.importedDomainObject.nciInstituteCode}" /></td>
				<td align="left"><c:out value="${item.importedDomainObject.name}" /></td>
   			</tr>
   			</c:forEach>
   		</table>	
   		</chrome:division>
	</c:if>
	<c:if test='${fn:length(command.importableOrganizations) == 0 }'>
		<chrome:division title="No Organization records to be loaded" id="organization_load">
			<table id="test" width="100%" class="tablecontent">
	   			<tr class="results">
	   				<td align="left"><i>None</i></td>
	   			</tr>
	   		</table>	 
   		</chrome:division>
   	</c:if>