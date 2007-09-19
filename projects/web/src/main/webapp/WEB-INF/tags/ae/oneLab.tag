<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="ae" tagdir="/WEB-INF/tags/ae" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@attribute name="index" required="true" type="java.lang.Integer" %>
<%@attribute name="style"%>

<ae:fieldGroupDivision fieldGroupFactoryName="lab" index="${index}" style="${style}">
    <div class="row">
     <div class="label">
      <input id="labname-${index}" name="testNameType${index}" value="name" type="radio"/>
      <tags:renderLabel field="${fieldGroup.fields[0]}"/>
     </div>
     <div class="value">
       <tags:renderInputs field="${fieldGroup.fields[0]}"/>
     </div>
    </div>
    <div class="row">
     <div class="label">
      <input id="labother-${index}" name="testNameType${index}" value="other" type="radio"/>
      <tags:renderLabel field="${fieldGroup.fields[1]}"/>
     </div>
     <div class="value">
       <tags:renderInputs field="${fieldGroup.fields[1]}"/>
     </div>
    </div>
    <tags:renderRow field="${fieldGroup.fields[2]}" />
    <c:forEach begin="3" end="8" step="2" var="i">
        <div class="row">
            <div class="label"><tags:renderLabel field="${fieldGroup.fields[i]}"/></div>
            <div class="value">
                <tags:renderInputs field="${fieldGroup.fields[i]}"/>
                <form:label path="${fieldGroup.fields[i+1].propertyName}">date</form:label>
                <tags:renderInputs field="${fieldGroup.fields[i+1]}"/>
            </div>
        </div>
    </c:forEach>
</ae:fieldGroupDivision>
