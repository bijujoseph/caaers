<%@include file="/WEB-INF/views/taglibs.jsp"%>

<html>
<head>
    <title>${tab.longTitle}</title>
    <tags:dwrJavascriptLink objects="createAE,routingAndReview"/>
	<tags:slider renderComments="${command.associatedToWorkflow }" renderAlerts="${command.associatedToLabAlerts}" reports="${command.selectedReportsAssociatedToWorkflow}" 
		display="${(command.associatedToWorkflow or command.associatedToLabAlerts) ? '' : 'none'}" workflowType="report">
    	<jsp:attribute name="labs">
    		<div id="labs-id" style="display:none;">
    			<tags:labs labs="${command.assignment.labLoads}"/>
    		</div>
    	</jsp:attribute>
    </tags:slider>
    
    
    <script type="text/javascript">
    	var routingHelper = new RoutingAndReviewHelper(createAE, 'aeReport');
    
        var NAME_FIELDS = [
            'firstName', 'middleName', 'lastName','title', 'address.street', 'address.city', 'address.state', 'address.zip'
        ]
        var PERSON_FIELDS = NAME_FIELDS.concat([
            'contactMechanisms[e-mail]', 'contactMechanisms[phone]', 'contactMechanisms[fax]'
        ])

        function chooseStaff() {
            var id = document.getElementById("staff").value;
            if (id == '') {
                clear('reporter');
               
            } else {
                createAE.getResearchStaffDetails(id, updateReporterFromStaff)
            }
        }

        function choosePhysician(){
			var id = document.getElementById("physician").value;
			if (id == '') {
                clear('physician');
               
            } else {
                createAE.getInvestigator(id, updatePhysicianFromInvestigator)
            }
        }
		/* IE7 fix:- null text is displayed when staff[field] is empty or null*/
        function updateReporterFromStaff(staff) {
			updateUserData('aeReport.reporter', staff);			
        }
        
        function updatePhysicianFromInvestigator(investigator) {
            updateUserData('aeReport.physician', investigator);
        }
        
        function updateUserData(prefix, user) {
            NAME_FIELDS.each(function(field) {
            	if(user[field] != null) {
                    updateFieldValue(prefix + '.' + field, user[field]);
                    ValidationManager.setNormalState($(prefix + '.' + field));
                }
            })
            
            if(user['emailAddress'] != null) {
                updateFieldValue(prefix + '.' + 'contactMechanisms[e-mail]',user['emailAddress']);
                ValidationManager.setNormalState($(prefix + '.contactMechanisms[e-mail]'));
            }else{
            	updateFieldValue(prefix + '.' + 'contactMechanisms[e-mail]', '');
            	ValidationManager.setNormalState($(prefix + '.contactMechanisms[e-mail]'));
            }

            if(user['phoneNumber'] != null) {
                updateFieldValue(prefix + '.' + 'contactMechanisms[phone]',user['phoneNumber']);
                ValidationManager.setNormalState($(prefix + '.contactMechanisms[phone]'));
            }else{
            	updateFieldValue(prefix + '.' + 'contactMechanisms[phone]', '');
            	ValidationManager.setNormalState($(prefix + '.contactMechanisms[phone]'));
            }

            if(user['faxNumber'] != null) {
                updateFieldValue(prefix + '.' + 'contactMechanisms[fax]',user['faxNumber']);
                ValidationManager.setNormalState($(prefix + '.contactMechanisms[fax]'));
            }else{
            	updateFieldValue(prefix + '.' + 'contactMechanisms[fax]', '');
            	ValidationManager.setNormalState($(prefix + '.contactMechanisms[fax]'));
            }
            
            if(user['address'] != null){
            	updateFieldValue(prefix + '.' + 'address.street', user['address'].street);
            	updateFieldValue(prefix + '.' + 'address.city', user['address'].city);
            	updateFieldValue(prefix + '.' + 'address.state', user['address'].state);
            	updateFieldValue(prefix + '.' + 'address.zip', user['address'].zip);
            	ValidationManager.setNormalState($(prefix + '.address.street'));
            	ValidationManager.setNormalState($(prefix + '.address.city'));
            	ValidationManager.setNormalState($(prefix + '.address.state'));
            	ValidationManager.setNormalState($(prefix + '.address.zip'));
            }else{
            	updateFieldValue(prefix + '.' + 'address.street', '');
            	updateFieldValue(prefix + '.' + 'address.city', '');
            	updateFieldValue(prefix + '.' + 'address.state', '');
            	updateFieldValue(prefix + '.' + 'address.zip', '');
            	ValidationManager.setNormalState($(prefix + '.address.street'));
            	ValidationManager.setNormalState($(prefix + '.address.city'));
            	ValidationManager.setNormalState($(prefix + '.address.state'));
            	ValidationManager.setNormalState($(prefix + '.address.zip'));
            }
        }

        function clear(person) {
            PERSON_FIELDS.each(function (field) {
            	updateFieldValue('aeReport.' + person + '.' + field, '');
                ValidationManager.doFieldValidation($('aeReport.' + person + '.' + field));
            })
        }



        function updateFieldValue(uiField, value){
			var f = $(uiField);
			if(f){
				f.value = value;
			}
        }

        Event.observe(window, "load", function() {
            $('staff').observe("change", chooseStaff)
            $('physician').observe("change", choosePhysician)
            
            var id = $("staff").value;
            var selectElement = $("staff");
            if (id == '' && ${validPersonnel}){
            	for (var i = 0; i < selectElement.options.length; i++) {
   					if (selectElement.options[i].value == ${loggedInUserId}) {
   						selectElement.selectedIndex = i;
   					}
   				}
	        }
	        
	        id = $("physician").value;
	        selectElement = $("physician");
	        if (id == '' && ${validPersonnel}){
	        	for(var i = 0; i < selectElement.options.length; i++){
	        		if(selectElement.options[i].value == ${loggedInUserId}) {
	        			selectElement.selectedIndex = i;
	        			choosePhysician();
	        		}
	        	}
	        }
            
             //only show the workflow tab, if it is associated to workflow
            var associatedToWorkflow = ${command.associatedToWorkflow};
            if(associatedToWorkflow){
            	<c:forEach items="${command.selectedReportsAssociatedToWorkflow}" var="report" varStatus="status">
	 	          	routingHelper.retrieveReviewCommentsAndActions('${report.id}');
 	          	</c:forEach>
            }
        })

    </script>
	<!--[if lte IE 6]>
<style>
		#reporter-summary {
			margin-top:80px;
			margin-bottom:0;
		}
	</style>
<![endif]-->
</head>
<body>
<tags:tabForm tab="${tab}" flow="${flow}" pageHelpAnchor="section3reporter">
    <jsp:attribute name="instructions">
        <c:choose>
            <c:when test="${oneOrMoreSevere}">
               <tags:instructions  code="instruction_ae_reporterAE"/>
            </c:when>
            <c:otherwise>
            	<tags:instructions code="instruction_ae_reporterNoAE" />
                <tags:instructions code="instruction_ae_reporterNote" heading="Note: " />
            </c:otherwise>
        </c:choose>
    </jsp:attribute>
    <jsp:attribute name="repeatingFields">
        <chrome:division title="Reporter Details" id="reporter">
        		<!--[if lte IE 6]>
					<br/>
					<br/>
					<br/>
				<![endif]-->
            <div class="row">
                <div class="label">Reporter</div>
                <div class="value">
                    <select id="staff" name="aeReport.reporter.user">
                        <option value="">Please select</option>
                        <optgroup label="Reporter">
                        <c:forEach var="researchStaff" items="${researchStaffList }">
                        	<option value="${researchStaff.id }" ${researchStaff.id eq command.aeReport.reporter.user.id ? 'SELECTED' : '' }>${researchStaff.firstName } ${researchStaff.lastName }</option>
                        </c:forEach>
                        </optgroup>
                        <optgroup label="Investigators">
                        <c:forEach var="investigator" items="${investigatorList }">
                        	<option value="${investigator.id }" ${investigator.id eq command.aeReport.reporter.user.id ? 'SELECTED' : '' }>${investigator.firstName } ${investigator.lastName }</option>
                        </c:forEach>
                        </optgroup>
                    </select>
                </div>
            </div>

            <c:forEach items="${fieldGroups['reporter'].fields}" var="field">
                <tags:renderRow field="${field}"/>
            </c:forEach>
        </chrome:division>

        <chrome:division title="Treating Physician Details" id="treating">
        	<!--[if lte IE 6]>
					<br/>
					<br/>
					<br/>
				<![endif]-->
        <div class="row">
                <div class="label">Physician</div>
                <div class="value">
                    <select id="physician" name="aeReport.physician.user">
                        <option value="">Please select</option>
                        <c:forEach var="investigator" items="${investigatorList }">
                        	<option value="${investigator.id }" ${investigator.id eq command.aeReport.physician.user.id ? 'SELECTED' : '' }>${investigator.firstName } ${investigator.lastName }</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <c:forEach items="${fieldGroups['physician'].fields}" var="field">
                <tags:renderRow field="${field}"/>
            </c:forEach>
        </chrome:division>
		<ae:reportingContext allReportDefinitions="${command.applicableReportDefinitions}" selectedReportDefinitions="${command.selectedReportDefinitions}" />
    </jsp:attribute>
</tags:tabForm>
</body>
</html>
