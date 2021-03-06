<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="ae" tagdir="/WEB-INF/tags/ae" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@attribute name="index" required="true" type="java.lang.Integer" %>
<%@attribute name="style"%>
<%@attribute name="lab" type="gov.nih.nci.cabig.caaers.domain.Lab" %>
<%@attribute name="expanded" type="java.lang.Boolean" %>
<%@taglib prefix="caaers" uri="http://gforge.nci.nih.gov/projects/caaers/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="v" value="aeReport.labs[${index}]" />

<c:set var="fieldGroupName">lab${index}</c:set>
<c:set var="fieldGroup" value="${fieldGroups[fieldGroupName]}"/>

<c:set var="_t" value="" />
<c:if test="${not empty lab.labTerm.term}">
    <c:set var="_t" value="${lab.labTerm.category.name} : ${lab.labTerm.term}" />
</c:if>

<chrome:division title="&nbsp;${empty lab.labTerm.term ? (empty lab.other ? '' : lab.other) : _t}" cssClass="lab" id="lab-${index}" style="${style}" collapsable="true" enableDelete="true" collapsed="${!empties[v] && !expanded}">

	 <div class="row">
            <div class="label"><label for="aeReport.labs[${index}].lab-category">Lab category</label></div>
            <div class="value">

              <div class="labCategoryValueDiv">
                  <select id="aeReport.labs[${index}].lab-category" <c:if test="${lab.id > 0 && (lab.labTerm != null || fn:trim(lab.other) != '')}">disabled</c:if>>
                    <option value="0">Any</option>
                    <c:forEach items="${labCategories}" var="cat">
                        <option value="${cat.id}">${cat.name}</option>
                    </c:forEach>
                </select>

              </div>
            </div>
    </div>

    <div class="row">
        <div class="label"><tags:renderLabel field="${fieldGroup.fields[0]}"/></div>
        <div class="value"><tags:renderInputs field="${fieldGroup.fields[0]}" readonly="${lab.id > 0 && (lab.labTerm != null || fn:trim(lab.other) != '')}"/></div>
    </div>

    <c:set var="display"><jsp:attribute name="value">${lab.name eq '' ? 'inline' : 'none'}</jsp:attribute></c:set>
    <tags:renderRow field="${fieldGroup.fields[1]}"  style="display: ${display};">
        <jsp:attribute name="label">
            <label>${fieldGroup.fields[1].displayName}</label>
        </jsp:attribute>
    </tags:renderRow>

    <tags:renderRow field="${fieldGroup.fields[2]}" />
    <tags:renderRow field="${fieldGroup.fields[3]}" />
	<div id="not-microbiology-${index}" style="<c:if test="${lab.id > 0 && empty lab.labTerm.term}">display:'';</c:if><c:if test="${empty lab.id || not empty lab.labTerm.term}">display:none;</c:if>">
    <c:forEach begin="4" end="9" step="2" var="i">
        <caaers:renderFilter elementID="${fieldGroup.fields[i].propertyName}">
            <table border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td valign="top">
                        <div class="row">
                            <div class="label"><tags:renderLabel field="${fieldGroup.fields[i]}"/></div>
                            <div class="value"><tags:renderInputs field="${fieldGroup.fields[i]}"/></div>
                        </div>
                    </td>
                    <td valign="top">
                        <div class="row">
                            <div class="label" style="width:3em;">date</div>
                            <div class="value" style="margin-left:4em;"><tags:renderInputs field="${fieldGroup.fields[i+1]}"/></div>
                        </div>
                    </td>
                </tr>
            </table>
        </caaers:renderFilter>
    </c:forEach>
    </div>
    
    <div id="microbiology-${index}" style="<c:if test="${lab.id > 0 && empty lab.labTerm.term}">display:'';</c:if><c:if test="${empty lab.id || not empty lab.labTerm.term}">display:none;</c:if>">
        <tags:renderRow field="${fieldGroup.fields[10]}" />
        <tags:renderRow field="${fieldGroup.fields[11]}" />
        <tags:renderRow field="${fieldGroup.fields[12]}" />
    </div>
    
<%--</ae:fieldGroupDivision>--%>
</chrome:division>



<script>
function setTitleLab_${index}() {
    var titleID = $('titleOf_lab-${index}');
    var textField = $("aeReport.labs[${index}].other");
    var value = textField.value;
    $(titleID).innerHTML = value;
}


Event.observe($("aeReport.labs[${index}].other"), "change", function() {
    setTitleLab_${index}();
});

</script>