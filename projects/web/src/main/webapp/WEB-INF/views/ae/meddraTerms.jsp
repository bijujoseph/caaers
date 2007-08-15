<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome"%>
<%@taglib prefix="ae" tagdir="/WEB-INF/tags/ae" %>

<%@page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <title>Enter basic AE information</title>
    <tags:stylesheetLink name="ae"/>
   
    <tags:includeScriptaculous/>
    <tags:dwrJavascriptLink objects="createAE"/>
     <script type="text/javascript">
        var aeReportId = ${empty command.aeReport.id ? 'null' : command.aeReport.id}

        var LowLevelTerm = Class.create()
        Object.extend(LowLevelTerm.prototype, {
            initialize: function(index, lowLevelTermCode) {
                this.index = index
                var cmProperty = "aeRoutineReport.adverseEvents[" + index + "]";
                this.lowLevelTermProperty = cmProperty + ".adverseEventMeddraLowLevelTerm.lowLevelTerm"
                //this.otherProperty = cmProperty + ".other"

                if (lowLevelTermCode) $(this.lowLevelTermProperty + "-input").value = lowLevelTermCode

      
                AE.createStandardAutocompleter(
                    this.lowLevelTermProperty, this.termPopulator.bind(this),
                    function(lowLevelTerm) { return lowLevelTerm.fullName })

                //this.initializePriorTherapyOrOther()
            },

            termPopulator: function(autocompleter, text) {
                createAE.matchLowLevelTermsByCode(text, function(values) {
                    autocompleter.setChoices(values)
                })
            }

        })

        Element.observe(window, "load", function() {
            <c:forEach items="${command.aeRoutineReport.adverseEvents}" varStatus="status" var="aeLowLevelTerm">
            new LowLevelTerm(${status.index}, '${aeLowLevelTerm.adverseEventMeddraLowLevelTerm.lowLevelTerm.fullName}')
            </c:forEach>
            
             new ListEditor("ae-section", createAE, "RoutineAeMeddra", {
             	addFirstAfter: "koi",
                addParameters: [aeReportId],
                addCallback: function(nextIndex) {
                    new LowLevelTerm(nextIndex);
                }
             })    

        }) 
    </script>
</head>
<body>
    <tags:tabForm tab="${tab}" flow="${flow}" pageHelpAnchor="section2enterbasicaeinformation">
        <jsp:attribute name="instructions">
            You are entering adverse events  for ${participantSummaryLine} on
            ${studySummaryLine}.
        </jsp:attribute>
        <jsp:attribute name="singleFields">
        	
            <div class="report-fields">
            	<b>Periods of Observation </b><br>
            	<div class="row">
            	<div class="label"><tags:renderLabel field="${fieldGroups.report.fields[0]}"/></div>
            	<div class="value">
                	<tags:renderInputs field="${fieldGroups.report.fields[0]}"/>
                	<strong><tags:renderLabel field="${fieldGroups.report.fields[1]}"/></strong>
                	<tags:renderInputs field="${fieldGroups.report.fields[1]}"/>
            	</div>
        		</div>
            </div>
        </jsp:attribute>
        <jsp:attribute name="repeatingFields">
        	<center>
        	<table id="test" width="100%" class="tablecontent">
    			<tr>
    				<th scope="col" align="left"><b><tags:requiredIndicator/>Term:</b> </th>
    				<th ><b><tags:requiredIndicator/>Grade:</b> </th>
    				<th scope="col" align="left"><b><tags:requiredIndicator/>Attribution:</b> </th>
    				<th scope="col" align="left"><b><tags:requiredIndicator/>Hospitalization:</b> </th>
    				<th scope="col" align="left"><b><tags:requiredIndicator/>Expected:</b> </th>
    			</tr>
    			<tr id="koi" />
    			
            	<c:forEach items="${command.aeRoutineReport.adverseEvents}" varStatus="status">
                	<ae:oneMeddraTerm index="${status.index}"/>
            	</c:forEach>
            	</table>
            	</center>
        </jsp:attribute>
        <jsp:attribute name="localButtons">
            <tags:listEditorAddButton divisionClass="ae-section" label="Add another AE"/>
        </jsp:attribute>
    </tags:tabForm>
</body>
</html>