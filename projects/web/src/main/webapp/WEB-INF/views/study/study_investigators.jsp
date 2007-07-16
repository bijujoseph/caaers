<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el"%>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome"%>
<%@taglib prefix="study" tagdir="/WEB-INF/tags/study"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>${tab.longTitle}</title>
<style type="text/css">
        .label { width: 12em; text-align: right; padding: 4px; }
</style>

<tags:includeScriptaculous/>
<tags:dwrJavascriptLink objects="createStudy"/>

<script language="JavaScript" type="text/JavaScript">

var siteListEditor;
var invListEditor;
function fireAction(action, selectedInvestigator){
	if(action == 'addInv'){
		invListEditor = new ListEditor('ssi-section',createStudy, "Investigator",{
      		 addButton: "xxx",
             addIndicator: "ssi-add-indicator",
             addParameters: [],
             addFirstAfter: "ssi-bookmark",
             addCallback: function(nextIndex) {
          	   new jsInvestigator(nextIndex);
             }
         
    	});  
		invListEditor.add.bind(invListEditor)();
	}else{
		var form = document.getElementById('command')
		form._target.name='_noname';
		form._action.value=action;
		form._selectedInvestigator.value=selectedInvestigator;
		form.submit();
	}
}

var jsInvestigator = Class.create();
Object.extend(jsInvestigator.prototype, {
           initialize: function(index, siteInvestigatorName) {
            	this.index = index;
            	this.siteIndex = $F('studySiteIndex');
            	this.siteInvestigatorName = siteInvestigatorName;
            	this.siteInvestigatorPropertyName = "studySites["  + this.siteIndex + "].studyInvestigators[" + index + "].siteInvestigator";
            	this.siteInvestigatorInputId = this.siteInvestigatorPropertyName + "-input";
            	if(siteInvestigatorName) $(this.siteInvestigatorInputId).value = siteInvestigatorName;
            	AE.createStandardAutocompleter(this.siteInvestigatorPropertyName, 
            		this.siteInvestigatorPopulator.bind(this),
            		this.siteInvestigagorSelector.bind(this)
            	);
            },            
            siteInvestigatorPopulator: function(autocompleter, text) {
         		createStudy.matchSiteInvestigator(text,this.siteIndex, function(values) {
         			autocompleter.setChoices(values)
         		})
        	},
        	
        	siteInvestigagorSelector: function(sInvestigator) { 
        		return sInvestigator.investigator.fullName
        	}
        	
});

Event.observe(window, "load", function() {
	lastSelIndex = $F('studySiteIndex'); //init the last index
	siteListEditor = new ListEditor('ss-section',createStudy, "ChooseStudySite",{
      		 addButton: "xxx",
             addIndicator: "ss-chg-indicator",
             addParameters: [],
             addFirstAfter: "ss-bookmark",
             addCallback: function(nextIndex) {
          	  
             }
    });
	
                  
	//observe on the change event on study site dropdown.
	Event.observe('studySiteIndex',"change", function(event){
		var ssi = $('ss-section-0');
		  if(ssi){
			 //Effect.Fade('ss-section-0');
			 ssi.parentNode.removeChild(ssi);
		  }
		  selIndex = $F('studySiteIndex');
		siteListEditor.options.addParameters = [selIndex,'Investigator'];
		siteListEditor.add.bind(siteListEditor)();
	 });
})

</script>
</head>
<body>
<tags:tabForm tab="${tab}" flow="${flow}" formName="studyInvestigatorForm" hideErrorDetails="true">    
<jsp:attribute name="singleFields">
 <input type="hidden" name="_action" value="">
 <input type="hidden" name="_selectedSite" value="">
 <input type="hidden" name="_selectedInvestigator" value="">

 <table border="0" id="table1" cellspacing="1" cellpadding="0" width="100%">
	<tr>
		<td width="70%" valign="top" >
		<p> Please choose a study site and link investigators to that study site</p>
		<div class="value"><tags:renderInputs field="${fieldGroups.site.fields[0]}"/><tags:indicator id="ss-chg-indicator"/></div>
		<br />
		<hr>
		<div id="content-section">
			<c:if test="${command.studySiteIndex > -1 }">
				<study:oneStudySiteInvestigator index="${command.studySiteIndex}"/>
			</c:if>
			<span id="ss-bookmark" />
		</div>
	    </td>
      	<td valign="top" width="25%">
			<chrome:box title="Summary" id="participant-entry2"  autopad="true">
 				<c:forEach var="studySite" varStatus="status" items="${command.studySites}">
 					<div class =""><a href="#" onclick="javascript:chooseSitesfromSummary(${status.index});" 
						title="click here to edit investigator assigned to study"> <font size="2"> <b>  ${studySite.organization.name} </b> </font> </a>
 					</div>
 					<div class="">Investigators Assigned: <b> ${fn:length(studySite.studyInvestigators)} </b>
 					</div>
 				
 				</c:forEach>
 				<div>
 				   <img src="/caaers/images/chrome/spacer.gif" width="1" height="150" />
 				</div>
 			</chrome:box>
		</td>
	  </tr>
	</table>
 </jsp:attribute>	 
</tags:tabForm>
</body>
</html>
