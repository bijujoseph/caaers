<%@ include file="/WEB-INF/views/taglibs.jsp"%>

<html>
<head>
    <title>${tab.longTitle}</title>
    <tags:stylesheetLink name="ae"/>
    <tags:dwrJavascriptLink objects="createAE"/>
    <tags:stylesheetLink name="slider" />

<%--
    <tags:slider renderComments="${command.associatedToWorkflow }" renderAlerts="${command.associatedToLabAlerts}"
        display="${(command.associatedToWorkflow or command.associatedToLabAlerts) ? '' : 'none'}">
        <jsp:attribute name="comments">
            <div id="comments-id" style="display:none;">
                <tags:routingAndReviewComments />
            </div>
        </jsp:attribute>
        <jsp:attribute name="labs">
            <div id="labs-id" style="display:none;">
                <tags:labs labs="${command.assignment.labLoads}"/>
            </div>
        </jsp:attribute>
    </tags:slider>
--%>

    <style type="text/css">
        div.row div.label { width: 16em; }
        div.row div.value { margin-left: 17em;}
        textarea { width: 20em; height: 5em; }
        img._boxImage_ { border : 1px blue dotted;}
    </style>
</head>
<body>

<script language="JavaScript">
var divisions = new Hash();
var routingHelper = new RoutingAndReviewHelper(createAE, 'aeReport');

function refreshBoxes() {
    registerAll();
    closeAll();
}

function registerAll() {
    var list = $$('div.division');
    divisions = new Hash();
    for (i=0; i<list.length; i++) {
        divisions.set(list[i].id, true);
    }
}

function closeAll() {
    divisions.each(function(pair) {

        var _id = pair.key;
        panelDiv = $("contentOf-" + _id);
        imageId= 'image-' + _id;
        imageSource = $(imageId).src;
        
        CloseDown(panelDiv, arguments[1] || {});
        document.getElementById(imageId).src = imageSource.replace('down','right');

//        alert(pair.key + ' = "' + pair.value + '"');
    });
}

    Event.observe(window, "load", setupPage);
    divisions = new Hash(); 

    function setupPage(){
        //only show the workflow tab, if it is associated to workflow
<%--
        var associatedToWorkflow = ${command.associatedToWorkflow};
        if(associatedToWorkflow){
               routingHelper.retrieveReviewCommentsAndActions.bind(routingHelper)();
        }
--%>

        interventionInstance = new InterventionClass();

        if ($('btn-add-surgery'))
            Element.observe('btn-add-surgery', 'click', function(e) {
                this._addItem('surgery', e.element(), null, '_surgeries');
            }.bind(interventionInstance));

        if ($('btn-add-radiation'))
            Element.observe('btn-add-radiation', 'click', function(e) {
                this._addItem('radiation', e.element(), null, '_radiations');
            }.bind(interventionInstance));

        if ($('btn-add-device'))
            Element.observe('btn-add-device', 'click', function(e) {
                this._addItem('device', e.element(), null, '_devices');
            }.bind(interventionInstance));

        if ($('btn-add-agent'))
            Element.observe('btn-add-agent', 'click', function(e) {
                this._addItem('agent', e.element(), null, '_agents');
            }.bind(interventionInstance));

        // refreshBoxes();
    }
    
    function fireAction(itemType, index, location, elementId, css) {
        interventionInstance._deleteItem(itemType, index, location);
    }

    var interventionInstance = null;
 	var InterventionClass = Class.create();
    Object.extend(InterventionClass.prototype, {
        initialize: function() {
        },

        _populateDeafultParameters : function(itemType, paramHash) {
            var page = ${tab.number};
            var target = '_target' + ${tab.number};
            paramHash.set('_page', page);
            paramHash.set(target, page);
            paramHash.set('_asynchronous', true);
            paramHash.set('decorator', 'nullDecorator');
        },

        _addItem: function(itemType, src, val, location, options) {
            refreshBoxes();
            var container = $(location);
            var paramHash = new Hash(); 
            paramHash.set('task', 'add');
            paramHash.set('currentItem', itemType);
            paramHash.set(itemType, val);

            this._populateDeafultParameters(itemType, paramHash);

            var url = $('command').action + "?subview"; 
            this._insertContent(container, url, paramHash, function() {}.bind(this));
        },

        formElementsInSection : function(container) {
            return container.select('input', 'select', 'textarea');
        },

        _deleteItem: function(itemType, index, location) {
            if (index < 0) return;
            var confirmation = confirm("Do you really want to delete?");
            if (!confirmation) return;

            // this.showIndicator(itemType+"-indicator");
            var container = $(location);

            var paramHash = new Hash();
            paramHash.set('task', 'remove');
            paramHash.set('currentItem', itemType);
            paramHash.set('index', index);
            this._populateDeafultParameters(itemType, paramHash);
            var url = $('command').action + "?subview";
            var sectionHash = Form.serializeElements(this.formElementsInSection(container), true);
            this._updateContent(container, url, paramHash.merge(sectionHash), function (transport) {
            }.bind(this));

            if (itemType == 'agent') $('btn-add-agent').show();
        },

        _updateContent: function(container, url, params, onSuccessCallBack) {
            new Ajax.Request(url, {
                parameters: params.toQueryString(),
                onSuccess: function(transport) {
                    container.innerHTML = transport.responseText;
                    AE.registerCalendarPopups();
//                    refreshBoxes();
                }
            });

        },

        _insertContent: function(container, url, params, onCompleteCallBack) {
            new Ajax.Updater(container, url, {
                parameters: params.toQueryString(), onComplete: onCompleteCallBack, insertion: Insertion.Top, evalScripts: true
            });
        }
    })
</script>

<c:set var="hasSurgery" value="${command.study.surgeryPresent}" />
<c:set var="hasDevice" value="${command.study.devicePresent}" />
<c:set var="hasRadiation" value="${command.study.radiationPresent}" />
<c:set var="hasAgent" value="${command.study.drugAdministrationPresent}" />
<c:set var="hasBehavioral" value="${command.study.behavioralPresent}" />

<div class="row">
    <div class="summarylabel">Treatment</div>
    <div class="summaryvalue">${command.aeReport.treatmentInformation.treatmentDescription != null ? command.aeReport.treatmentInformation.treatmentDescription : command.aeReport.treatmentInformation.treatmentAssignment.description}</div>
</div>

<form:form id="command">
        <chrome:flashMessage/>
        <tags:hasErrorsMessage />
    
    <c:if test="${hasAgent}">
        <chrome:box title="${ (agentMandatorySection) ? '<span class=\"required-indicator\">*</span> ' : ''}Agent" collapsable="true">
            <jsp:attribute name="additionalTitle" />
            <jsp:body>
                <div style="padding-left:20px;">
                    <input type="button" value="Add Agent" id="btn-add-agent" >
                <div id="_agents">

                    <c:set var="size" value="${fn:length(command.aeReport.treatmentInformation.courseAgents)}" />
                    <c:forEach items="${command.aeReport.treatmentInformation.courseAgents}" varStatus="status" var="agent">
                        <c:set var="newIndex" value="${size - (status.index + 1)}" />
                        <c:set var="collapsed" value="${agent.studyAgent != null}" />
                        <ae:oneCourseAgent index="${newIndex}" agent="${agent}" collapsed="${collapsed}"/>
                    </c:forEach>
                </div>
                </div>
            </jsp:body>
        </chrome:box>
    </c:if>

    <c:if test="${hasDevice}">
        <chrome:box title="${ (deviceMandatorySection) ? '<span class=\"required-indicator\">*</span> ' : ''}Device" collapsable="true">
            <jsp:attribute name="additionalTitle" />
            <jsp:body>
                <div style="padding-left:20px;">
                    <input type="button" value="Add Device" id="btn-add-device">
                <div id="_devices">
                <c:set var="size" value="${fn:length(command.aeReport.medicalDevices)}" />
                <c:forEach items="${command.aeReport.medicalDevices}" varStatus="status" var="device">
                    <c:set var="newIndex" value="${size - (status.index + 1)}" />
                    <ae:oneMedicalDevice index="${newIndex}" device="${device}" collapsed="true"/>
                </c:forEach>
            </div>
            </div>
            </jsp:body>
        </chrome:box>
    </c:if>

    <c:if test="${hasRadiation}">
        <chrome:box title="${ (radiationMandatorySection) ? '<span class=\"required-indicator\">*</span> ' : ''}Radiation" collapsable="true">
            <jsp:attribute name="additionalTitle"/>
            <jsp:body>
                <div style="padding-left:20px;">
                    <input type="button" value="Add Radiation" id="btn-add-radiation">
                <div id="_radiations">
                    <c:set var="size" value="${fn:length(command.aeReport.radiationInterventions)}" />
                    <c:forEach items="${command.aeReport.radiationInterventions}" varStatus="status" var="radiation">
                        <c:set var="newIndex" value="${size - (status.index + 1)}" />
                        <ae:oneRadiationIntervention index="${newIndex}" radiation="${radiation}" collapsed="true"/>
                    </c:forEach>
                </div>
                </div>
            </jsp:body>
        </chrome:box>
    </c:if>

    <c:if test="${hasSurgery}">
        <chrome:box title="${ (surgeryMandatorySection) ? '<span class=\"required-indicator\">*</span> ' : ''}Surgery" collapsable="true">
            <jsp:attribute name="additionalTitle"/>
            <jsp:body>
                <div style="padding-left:20px;">
                    <input type="button" value="Add Surgery" id="btn-add-surgery">
                <div id="_surgeries">
                    <c:set var="size" value="${fn:length(command.aeReport.surgeryInterventions)}" />
                    <c:forEach items="${command.aeReport.surgeryInterventions}" varStatus="status" var="surgery">
                        <c:set var="newIndex" value="${size - (status.index + 1)}" />
                        <ae:oneSurgeryIntervention index="${newIndex}" surgery="${surgery}" collapsed="true"/>
                    </c:forEach>
                </div>
                </div>
            </jsp:body>
        </chrome:box>
    </c:if>

<%--
    <c:if test="${hasBehavioral}">
        <chrome:box title="Behavioral" collapsable="true"></chrome:box>
    </c:if>
--%>

    <tags:tabControls flow="${flow}" tab="${tab}" />
    <tags:tabFields tab="${tab}" />
</form:form>

</body>
</html>
