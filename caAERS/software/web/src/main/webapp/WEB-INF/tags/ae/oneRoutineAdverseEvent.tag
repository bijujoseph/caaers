<%-- 
	This page renders one adverse event row in Capture AE flow.
	Author : Biju Joseph
--%>

<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/ui"%>
<%@taglib prefix="ae" tagdir="/WEB-INF/tags/ae"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="caaers" uri="http://gforge.nci.nih.gov/projects/caaers/tags" %>

<%@attribute name="adverseEvent" type="gov.nih.nci.cabig.caaers.domain.AdverseEvent" required="true" description="The adverse event that is being rendered" %>
<%@attribute name="index" required="true" type="java.lang.Integer" description="The index of the AE that is rendered" %>
<%@attribute name="collapsed" required="true" type="java.lang.Boolean" description="If true, will display the box collapsed"%>
<%@attribute name="enableDelete" description="If true, the delete button will be enabled" %>
<%@attribute name="style" %>
<%@attribute name="isSolicited" type="java.lang.Boolean" %>
<%@attribute name="hasOtherMeddra" type="java.lang.Boolean" %>

<c:set var="terminologyMatch" value="${(command.study.aeTerminology.term eq 'MEDDRA' and adverseEvent.adverseEventTerm.medDRA) or (command.study.aeTerminology.term eq 'CTC' and !adverseEvent.adverseEventTerm.medDRA)}" />
<%--
    terminologyMatch - sometimes the user can change the the AE-Terminology of the study while the study is already associated with some adverse events through the AE flow.
                       changing the AE-ETrminology can cause JSP errors since this page is rendered considering the AE-Terminology on the study,
                       and all AEs are expected to have the same AE-terminology. 
--%>

<c:set var="title_term">${adverseEvent.adverseEventTerm.medDRA ? adverseEvent.adverseEventTerm.term.meddraTerm : adverseEvent.adverseEventTerm.term.fullName}</c:set>
<c:set var="mainGroup">main${index}</c:set>
<c:set var="indexCorrection" value="${adverseEvent.adverseEventTerm.otherRequired  ? 1  : 0}" />
<c:set var="title_otherMedDRA_term">${adverseEvent.lowLevelTerm.meddraTerm}</c:set>
<c:set var="title_grade">${adverseEvent.grade.code}</c:set>

<c:if test="${terminologyMatch}">

<c:set var="v" value="adverseEvents[${index}]" />
<c:set var="collapsedCheck" value="${!command.errorsForFields[v] && (isSolicited ? 'true' : adverseEvent.grade != null) && (adverseEvent.adverseEventTerm.otherRequired ? adverseEvent.lowLevelTerm != null : true)}" />
<c:if test="${command.adverseEvents[index].report.id > 0}"><c:set var="inReport"><jsp:attribute name="value"> <span class="inReport"> (Currently Included in a Report)</span></jsp:attribute></c:set></c:if>
<a name="adverseEventTerm-${adverseEvent.adverseEventTerm.term.id}"></a>

<c:set var="_TITLE">
    <jsp:attribute name="value">
        ${title_term}${not empty title_otherMedDRA_term ? ':' : '' }
        ${title_otherMedDRA_term}
        <c:if test="${title_grade > 0}">Grade: ${title_grade}</c:if>
        ${inReport}
        <c:if test="${adverseEvent.detailsForOther ne ''}">Verbatim: ${adverseEvent.detailsForOther}</c:if>
    </jsp:attribute>
</c:set>    

<input type="hidden" id="_ctcTermValue${index}" value="${adverseEvent.adverseEventTerm.term.id}">
<%--[<span id="_test${index}">-</span>]--%>

<chrome:division title="${_TITLE}" id="ae-section-${index}" cssClass="ae-section aeID-${adverseEvent.adverseEventTerm.term.id}" style="${style}"
	collapsable="true" deleteParams="${index}" enableDelete="${enableDelete}" collapsed="${collapsedCheck}">
   <jsp:attribute name="additionalInfo"></jsp:attribute>

    <jsp:body>
        <%-- Verbatim --%>
        <c:if test="${!command.study.verbatimFirst}">
            <tags:renderRow field="${fieldGroups[mainGroup].fields[0 + indexCorrection]}" />
        </c:if>
        <c:if test="${command.study.verbatimFirst && !isSolicited}">
            <div class="row">
                <div class="label"><ui:label path="*" text="*" labelProperty="aeReport.adverseEvents.detailsForOther"/></div>
                <div class="value"><caaers:value path="${fieldGroups[mainGroup].fields[0 + indexCorrection].propertyName}" /></div>
            </div>
            <caaers:message code="LBL_aeReport.adverseEvents.ctcTerm" var="ctcTermLabel" />
            <caaers:message code="LBL_aeReport.adverseEvents.meddraTerm" var="meddraTermLabel" />
            <div class="row">
                <div class="label">
                    <c:if test="${aeTermMandatory}"><tags:requiredIndicator/></c:if>
                    <c:if test="${command.study.aeTerminology.term eq 'CTC'}">${ctcTermLabel}</c:if>
                    <c:if test="${command.study.aeTerminology.term eq 'MEDDRA'}">${meddraTermLabel}</c:if>
                </div>
                <div class="value">
                    <c:if test="${command.study.aeTerminology.term eq 'CTC'}">
                    <ui:autocompleter path="adverseEvents[${index}].ctcTerm"  required="${aeTermMandatory}"
                                      title="${ctcTermLabel}"
                                      initialDisplayValue="${command.adverseEvents[index].ctcTerm.fullName}">
                        <jsp:attribute name="populatorJS">function(autocompleter, text) {
                            createAE.matchTerms(text, ${command.study.aeTerminology.ctcVersion.id}, '', 75, function(values) {
                                autocompleter.setChoices(values);
            				});
                        }
                        </jsp:attribute>
                        <jsp:attribute name="selectorJS">function(obj) {
                            return obj.fullName;
                        }
                        </jsp:attribute>
                        <jsp:attribute name="optionsJS">
							{
								afterUpdateElement: function(inputElement, selectedElement, selectedChoice) {
                                    $('adverseEvents[${index}].ctcTerm').value = selectedChoice.id; 
                                    updateExpected(${index}, selectedChoice.id, '-1', '${adverseEvent.detailsForOther}', true);
								}
							}
						</jsp:attribute>
                    </ui:autocompleter>
                </c:if>
                    <c:if test="${command.study.aeTerminology.term eq 'MEDDRA'}">
                    <ui:autocompleter path="adverseEvents[${index}].meddraTerm"  title="${meddraTermLabel}"
                                      required="${aeTermMandatory}" >
                        <jsp:attribute name="initialDisplayValue">
                            <c:if test="${command.adverseEvents[index].meddraTerm.fullName ne ''}">${command.adverseEvents[index].meddraTerm.fullName}</c:if>
                        </jsp:attribute>
                        <jsp:attribute name="populatorJS">function(autocompleter, text) {
                            createAE.matchLowLevelTermsByCode(${command.study.aeTerminology.meddraVersion.id}, text, function(values) {
                                autocompleter.setChoices(values)
                            })
                        }
                        </jsp:attribute>
                        <jsp:attribute name="selectorJS">function(lowLevelTerm) {
                            return lowLevelTerm.meddraTerm;
                        }
                        </jsp:attribute>
                        <jsp:attribute name="optionsJS">
							{
								afterUpdateElement: function(inputElement, selectedElement, selectedChoice) {
                                    $('adverseEvents[${index}].meddraTerm').value = selectedChoice.id;
                                    updateExpected(${index}, $('adverseEvents[${index}].ctcTerm').value, selectedChoice.id, '${adverseEvent.detailsForOther}', false);
								}
							}
						</jsp:attribute>
                    </ui:autocompleter>
                </c:if>
                </div>
            </div>
        </c:if>


        <div id="GRADES_AND_MEDDRA_${index}">
        <%-- Other MedDRA --%>
        <c:if test="${indexCorrection gt 0}">

            <c:if test="${command.study.verbatimFirst}">
                <c:set var="_verbatimValueFrom" value="'${adverseEvent.detailsForOther}'" />
            </c:if>
            <c:if test="${!command.study.verbatimFirst}">
                <c:set var="_verbatimValueFrom" value="$('adverseEvents[${index}].detailsForOther').value" />
            </c:if>

            <ui:row path="${fieldGroups[mainGroup].fields[0].propertyName}">
                <jsp:attribute name="label"><ui:label path="${fieldGroups[mainGroup].fields[0].propertyName}" text="${fieldGroups[mainGroup].fields[0].displayName}" required="true"/></jsp:attribute>
                <jsp:attribute name="value">
                    <c:if test="${hasOtherMeddra}">
                        <ui:autocompleter path="${fieldGroups[mainGroup].fields[0].propertyName}" initialDisplayValue="${adverseEvent.lowLevelTerm.meddraTerm}" required="true">
                        <jsp:attribute name="populatorJS">
                            function(autocompleter, text) {
                                var terminologyVersionId = ${empty command.adverseEventReportingPeriod.study.otherMeddra.id ? 0 :command.adverseEventReportingPeriod.study.otherMeddra.id};
                                createAE.matchLowLevelTermsByCode(terminologyVersionId, text, function(values) {
                                    autocompleter.setChoices(values)})
                            }
                        </jsp:attribute>
                        <jsp:attribute name="selectorJS">
                            function(lowLevelTerm) {
                                $('titleOf_ae-section-${index}').innerHTML = '${title_term} Grade: ${title_grade}';
                                return lowLevelTerm.meddraTerm;
                            }
                        </jsp:attribute>
                        <jsp:attribute name="optionsJS">
							{
								afterUpdateElement: function(inputElement, selectedElement, selectedChoice) {
                                    $('adverseEvents[${index}].lowLevelTerm').value = selectedChoice.id;
                                    updateExpected(${index}, $('_ctcTermValue${index}').value, selectedChoice.id, ${_verbatimValueFrom}, false);
                            // IOO
								}
							}
						</jsp:attribute>
                        </ui:autocompleter>
                    </c:if>
                    <c:if test="${not hasOtherMeddra}">
                        <ui:text path="${fieldGroups[mainGroup].fields[0].propertyName}" required="true"/>
                    </c:if>
                </jsp:attribute>
            </ui:row>

            <%--<tags:renderRow field="${fieldGroups[mainGroup].fields[0]}"/>--%>
        </c:if>

        
		<%-- Grade --%>
		<tags:renderRow field="${fieldGroups[mainGroup].fields[1 + indexCorrection]}"/>
        </div>

		<script>
		<%-- 
			Logic that handles the grade changes 
		--%>

			Event.observe('${fieldGroups[mainGroup].fields[1 + indexCorrection].propertyName}-longselect','click', function(evt) {
				var val = evt.element().value;
				var chkDeath = $('outcomes[' + ${index} + '][1]');
				if(chkDeath){
					chkDeath.checked = (val == 'DEATH');
				}
				//update the heading
				 $('titleOf_ae-section-${index}').innerHTML = "${title_term}${not empty title_otherMedDRA_term ? ':' : '' }${title_otherMedDRA_term} Grade: " + grades.indexOf(val) + " <c:if test="${adverseEvent.detailsForOther ne ''}">Verbatim: ${adverseEvent.detailsForOther}</c:if>";
			});
		</script>
	<div class="row" style="margin-top:0;margin-bottom:0;">
		<div class="leftpanel">
            <%-- Graded Date --%>
            <tags:renderRow field="${fieldGroups[mainGroup].fields[2 + indexCorrection]}" />
			<%-- Start Date --%>
			<tags:renderRow field="${fieldGroups[mainGroup].fields[3 + indexCorrection]}" />
			<%-- Attribution --%>
			<tags:renderRow field="${fieldGroups[mainGroup].fields[5 + indexCorrection]}" />
			<%-- Event time --%>
			<tags:renderRow field="${fieldGroups[mainGroup].fields[8 + indexCorrection]}" />
			<%-- Hospitalization --%>
			<tags:renderRow field="${fieldGroups[mainGroup].fields[6 + indexCorrection]}" />
		</div>
		<div class="rightpanel">
			<%-- End Date --%>
			<tags:renderRow field="${fieldGroups[mainGroup].fields[4 + indexCorrection]}" />
			<%-- Expectedness --%>
			<tags:renderRow field="${fieldGroups[mainGroup].fields[7 + indexCorrection]}" />
			<%-- Location --%>
			<tags:renderRow field="${fieldGroups[mainGroup].fields[10 + indexCorrection]}" />
			<%-- Risk --%>
			<tags:renderRow field="${fieldGroups[mainGroup].fields[9 + indexCorrection]}" />
		</div>
	</div>
	
	<%-- Outcome--%>
	<ae:oneOutcome index="${index}" isRoutineFlow="true" isMandatory="${outcomesMandatory && not adverseEvent.solicited}"/>
		<!--  field to store the sig -->
		<input type="hidden" id="ae-section-${index}-signature" name="ae-section-${index}-signature" value="${adverseEvent.signature}"/>
		<c:if test="${adverseEvent.submitted == true}">
			<input name="submittedAERow" type="hidden" class="submittedAERow" value="${index}" id="ae-section-${index}-submittedAERow"/>
			<input name="ae-section-${index}-reportID" type="hidden" id="ae-section-${index}-reportID" value="${adverseEvent.report.id}" />
		</c:if>	
		
		<%-- Script to register calendar controls --%>
		<script>
			AE.registerCalendarPopups('ae-section-${index}');
            if ($('adverseEvents[${index}].detailsForOther')) {
                Event.observe('adverseEvents[${index}].detailsForOther', 'change', function() {
                    var _llt = 0;
                    if ($('adverseEvents[${index}].lowLevelTerm').value && $('adverseEvents[${index}].lowLevelTerm').value > 0) _llt = $('adverseEvents[${index}].lowLevelTerm').value;
                    updateExpected(${index}, $('_ctcTermValue${index}').value, _llt, $('adverseEvents[${index}].detailsForOther').value, false);
                });
            }
            // IOO2
		</script>

    </jsp:body>
</chrome:division>

</c:if>
<c:if test="${!terminologyMatch}">
    <chrome:division title="&nbsp;${title_term}" collapsable="true" collapsed="false" id="WRONG-${index}">
        <div style="color:red;">Wrong AE Terminology for this term, expected AE Terminology is: <b>${command.study.aeTerminology.term}</b></div>
    </chrome:division>
</c:if>