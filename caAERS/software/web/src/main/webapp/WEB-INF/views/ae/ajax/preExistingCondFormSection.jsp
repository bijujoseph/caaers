<%--
Copyright SemanticBits, Northwestern University and Akaza Research

Distributed under the OSI-approved BSD 3-Clause License.
See http://ncip.github.com/caaers/LICENSE.txt for details.
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="ae" tagdir="/WEB-INF/tags/ae" %>
<tags:noform>
<c:forEach items="${indexes}" var="index">
	<c:set var="preCond" value="${preExistingConditions[index]}" />
	<ae:onePreExistingCond index="${index}" preExistingCondition="${preCond.preExistingCondition}" otherValue="${preCond.other}"/>
</c:forEach>
</tags:noform>
<%--
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="ae" tagdir="/WEB-INF/tags/ae"%>
<tags:noform>
    <ae:onePreExistingCond index="${param.index}" style="display: none"/>
</tags:noform>
--%>
