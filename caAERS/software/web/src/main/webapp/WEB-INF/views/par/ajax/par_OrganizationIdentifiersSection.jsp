<%--
Copyright SemanticBits, Northwestern University and Akaza Research

Distributed under the OSI-approved BSD 3-Clause License.
See http://ncip.github.com/caaers/LICENSE.txt for details.
--%>
<%@ include file="/WEB-INF/views/taglibs.jsp"%>

<tags:noform>
<c:forEach items="${indexes}" var="index" varStatus="status" begin="${remove eq 'remove' ? 1 : 0}">
<c:set var="readonly" value="${not empty remove ? 'true':'false'}"/>
<par:parIdentifier
    title="Subject Identifier ${status.index + 1}"
    disableDelete="false"
    sectionClass="organization-section-row"
    removeButtonAction="removeIdentifier"
    index="${index}"
    identifier="${command.participant.organizationIdentifiers[status.index]}"
    mainGroupName="mainOrg"
    containerName="addOrganizationIdentifierDiv"
    removeAction="removeOrganizationIdentifier"
    readonly="false"/>
    
</c:forEach>
</tags:noform>
