<%-- 
	This page is used by Expedited AE flow. 
	Author : Biju Joseph
--%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="ae" tagdir="/WEB-INF/tags/ae"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/ui"%>

<%@attribute name="index" required="true" type="java.lang.Integer" %>
<%@attribute name="collapsed" required="true" type="java.lang.Boolean" %>
<%@attribute name="style"%>
<%@attribute name="adverseEvent" type="gov.nih.nci.cabig.caaers.domain.AdverseEvent" %>

<c:set var="ctcTermGroup">ctcTerm${index}</c:set>
<c:set var="ctcOtherGroup">ctcOther${index}</c:set>
<c:set var="mainGroup">main${index}</c:set>
<c:set var="title">${adverseEvent.adverseEventCtcTerm.ctcTerm.term}</c:set>
<c:set var="title_grade">${command.aeReport.adverseEvents[index].grade.code}</c:set>
<c:set var="title_lowlevel">${adverseEvent.lowLevelTerm.meddraTerm}</c:set>
<c:set var="v" value="aeReport.adverseEvents[${index}]" />
<c:set var="title_term">${adverseEvent.adverseEventTerm.medDRA ? adverseEvent.adverseEventTerm.term.meddraTerm : adverseEvent.adverseEventTerm.term.fullName}</c:set>
<c:set var="verbatim">${adverseEvent.adverseEventTerm.adverseEvent.detailsForOther}</c:set>

<a name="adverseEventTerm-${command.aeReport.adverseEvents[index].adverseEventTerm.term.id}"></a>

<chrome:division title="${title_term} ${verbatim} ${title_lowlevel}, Grade: ${title_grade}" id="ae-section-${index}"
	cssClass="ae-section aeID-${adverseEvent.adverseEventTerm.term.id}" style="${style}" collapsed="${!empties[v]}" collapsable="true">
	<jsp:attribute name="titleFragment">&nbsp;<span id="title-frag-${index}" class="primary-indicator">${ index gt 0 ? '' : '[Primary]' }</span> </jsp:attribute>
	<jsp:body>
    <div id="${v}.ctc-details" class="ctc-details">

        <div class="row" style="display:none;">
            <div class="label"><label for="${v}.ctc-category">CTC category</label></div>
            <div class="value">

              <div class="ctcCategoryValueDiv">
                <select id="${v}.ctc-category" class="ctcCategoryClass" onchange="javascript:enableDisableAjaxTable(${index})" >
                    <option value="">Any</option>
                </select>

              </div>
            </div>
        </div>

        <tags:renderRow field="${fieldGroups[ctcTermGroup].fields[0]}" style="display:none;" extraParams="<a id=\"showAllTerm${index}\" href=\"javascript:showAjaxTable(this,$F('${v}.ctc-category'),'table${index}','table${index}-outer')\">Show All</a>" />
		</div>

		<!--  Other MedDRA or Other specify text-->
        <div style="display:${adverseEvent.adverseEventTerm.otherRequired ? 'block' : 'none'}">
            <c:if test="${not empty command.adverseEventReportingPeriod.study.otherMeddra}">
                <%--other meddra--%>
                <ui:row path="${fieldGroups[ctcOtherGroup].fields[0].propertyName}">
                    <jsp:attribute name="label"><ui:label path="${fieldGroups[ctcOtherGroup].fields[0].propertyName}" text="${fieldGroups[ctcOtherGroup].fields[0].displayName}"/></jsp:attribute>
                    <jsp:attribute name="value">
                        <c:out value="${adverseEvent.lowLevelTerm.fullName}" />
                        <%--<ui:text path="${fieldGroups[ctcOtherGroup].fields[0].propertyName}" readonly="true"/>--%>
                    </jsp:attribute>
                </ui:row>
            </c:if>
            <c:if test="${empty command.adverseEventReportingPeriod.study.otherMeddra}">
                  <%--other specify text--%>
                <ui:row path="${fieldGroups[ctcOtherGroup].fields[0].propertyName}">
                    <jsp:attribute name="label"><ui:label path="${fieldGroups[ctcOtherGroup].fields[0].propertyName}" text="${fieldGroups[ctcOtherGroup].fields[0].displayName}"/></jsp:attribute>
                    <jsp:attribute name="value">
                       <ui:text path="${fieldGroups[ctcOtherGroup].fields[0].propertyName}" readonly="true" />
                    </jsp:attribute>
                </ui:row>
            </c:if>

        </div>


    <div id="main-fields-${index}" class="main-fields">
		<%-- Verbatim --%>
		<tags:renderRow field="${fieldGroups[mainGroup].fields[0]}"/>
		<%-- Grade --%>
        <ui:row path="${fieldGroups[mainGroup].fields[1].propertyName}">
            <jsp:attribute name="label">
                <ui:label path="${fieldGroups[mainGroup].fields[1].propertyName}" labelProperty="${fieldGroups[mainGroup].fields[1].attributes.labelProperty}" text="${fieldGroups[mainGroup].fields[1].displayName}" mandatory="${fieldGroups[mainGroup].fields[1].attributes.mandatory}" required="${fieldGroups[mainGroup].fields[1].required}"/>
            </jsp:attribute>
            <jsp:attribute name="value">
                <c:set var="grdOptions" value="${command.aeGradeOptionsMap[adverseEvent.id]}"/>
                <!-- grdOptions : ${grdOptions}-->
                <ui:longSelect path="${fieldGroups[mainGroup].fields[1].propertyName}" options="${grdOptions}" />
            </jsp:attribute>
        </ui:row>
		<div class="row">
			<div class="leftpanel">
                    <%-- Awareness Date --%>
                <tags:renderRow field="${fieldGroups[mainGroup].fields[2]}" />
				<%-- Start Date --%>
				<tags:renderRow field="${fieldGroups[mainGroup].fields[3]}" />
				<%-- Attribution --%>
				<tags:renderRow field="${fieldGroups[mainGroup].fields[5]}" />
                <%-- Event Time --%>
                <tags:renderRow field="${fieldGroups[mainGroup].fields[6]}"/>
                <%-- Hospitalization --%>
				<tags:renderRow field="${fieldGroups[mainGroup].fields[8]}"/>
            </div>
			<div class="rightpanel">
				<%-- End Date --%>
				<tags:renderRow field="${fieldGroups[mainGroup].fields[4]}" />
				<%-- Expected --%>
				<tags:renderRow field="${fieldGroups[mainGroup].fields[9]}"/>
    			<%-- Location --%>
                <tags:renderRow field="${fieldGroups[mainGroup].fields[7]}"/>
				<%-- Risk --%>
				<tags:renderRow field="${fieldGroups[mainGroup].fields[10]}"/>
            </div>
		</div>

		<%-- Outcomes --%>
		<ae:oneOutcome index="${index}" />
    </div>
    </jsp:body>
</chrome:division>

<script>
    var ae_title_one_${index} = "${title}";
    var ae_title_lowlevel_${index} = '${adverseEvent.lowLevelTerm.meddraTerm}';
    var ae_title_grade_${index} = ${not empty title_grade ? title_grade : 0};

    Event.observe($('aeReport.adverseEvents[${index}].adverseEventCtcTerm.ctcTerm-input'), "blur", function() {
        // $('titleOf_ae-section-${index}').innerHTML = $('aeReport.adverseEvents[${index}].adverseEventCtcTerm.ctcTerm-input').value;
        updateAETitle_${index}();
    });
    
    Event.observe($('aeReport.adverseEvents[${index}].lowLevelTerm-input'), "blur", function() {
        // $('titleOf_ae-section-${index}').innerHTML = $('aeReport.adverseEvents[${index}].lowLevelTerm-input').value;
        ae_title_lowlevel_${index} = $('aeReport.adverseEvents[${index}].lowLevelTerm-input').value;
        updateAETitle_${index}();
    });

    Event.observe('aeReport.adverseEvents[${index}].grade-longselect','click', function(evt){
        var val = evt.element().value;
        ae_title_grade_${index} = grades.indexOf(val);
        updateAETitle_${index}();
    });

    function updateAETitle_${index}() {
        $('titleOf_ae-section-${index}').innerHTML = ae_title_one_${index} + "&nbsp;" + ae_title_lowlevel_${index}  + ", Grade: " + ae_title_grade_${index};
    }

</script>
