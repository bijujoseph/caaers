<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="ae" tagdir="/WEB-INF/tags/ae" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@attribute name="index" required="true" type="java.lang.Integer" %>
<%@attribute name="style"%>
<%@attribute name="agentCount" type="java.lang.Integer" %>

<ae:fieldGroupDivision fieldGroupFactoryName="priorTherapy" index="${index}" style="${style}">
    <tags:errors path="aeReport.saeReportPriorTherapies[${index}]"/>
    <tags:renderRow field="${fieldGroup.fields[0]}" />
    <tags:renderRow field="${fieldGroup.fields[1]}" />
    <tags:renderRow field="${fieldGroup.fields[2]}"/>
    <tags:renderRow field="${fieldGroup.fields[3]}"/>

    <c:forEach begin="${1}" end="${agentCount}" var="s">
         <ae:onePriorTherapyAgent index="${s - 1}" parentIndex="${index}"/>
    </c:forEach>
      
    <div id="pptAgent${index}" style="display: none">
        <tags:listEditorAddButton divisionClass="ptAgent${index}" label="Add an Agent"/>
    </div>
</ae:fieldGroupDivision>
