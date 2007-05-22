<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome"%>
<%@taglib prefix="ruleTags" tagdir="/WEB-INF/tags/rule"%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <tags:stylesheetLink name="ae"/>
    <tags:includeScriptaculous/>
    <tags:dwrJavascriptLink objects="authorRule"/>
    <title>Select Rule Set</title>

    <script type="text/javascript">
    
    	function setRuleSetName(newRuleSetElement)
    	{
    		if (newRuleSetElement.options[newRuleSetElement.selectedIndex].value =='Other')
    		{
    			Element.show("otherRuleSetDiv");
    		}
    		else
    		{
	    		$("hiddenRuleSetName").value=newRuleSetElement.options[newRuleSetElement.selectedIndex].value;    		
	    		Element.hide("otherRuleSetDiv");
	    	}
	    	
    	}
    	
	    function disableCreateRuleSet(radioRuleSetElement)
	    {
	    	Element.hide("newRuleSetDiv");
	    	Element.hide("otherRuleSetDiv");
	    	
	    	$("hiddenRuleSetName").value=radioRuleSetElement.value;
	    }
    	
    	function setOtherRuleSetName(otherRuleSetName)
    	{
    		$("hiddenRuleSetName").value=otherRuleSetName.value;
    	}
    	
	</script>

</head>
<body>

<p id="instructions">
        Select RuleSet name to Add or Modify the Rules. To create new RuleSet, click the "Create RuleSet" button.
</p>

<chrome:division>

    <tags:tabForm tab="${tab}" flow="${flow}" >
		<jsp:attribute name="singleFields">

		<div class="local-buttons">
			<input type="button" id="createRuleSet" value="Create RuleSet" align="right"/>
		</div>	
            <tags:errors path="*"/>
            
            <tags:tabFields tab="${tab}"/>


			<c:choose>
				<%-- Check whether RuleSets exist or not --%>
				<c:when test="${empty command.existingRuleSets}" >
	            	<div class="row">
    	            	Rule Sets does not exist. Please continue to create a new Rule Set.
        	    	</div>
				</c:when>
				<%-- Display all the existing rulesets --%>
				<c:otherwise>
					<input type="radio" id="radioRuleSetName1" name="radioRuleSetName" style="visibility:hidden"/>
					<c:forEach items="${command.existingRuleSets}" var="ruleSet">
				        <div class="row">
    	        		    <label><input type="radio" id="radioRuleSetName" name="radioRuleSetName" value="${ruleSet.description}" onclick="disableCreateRuleSet(this)"/> &nbsp; <b>${ruleSet.description} </b></label>
			            </div>
    				</c:forEach>
        	    </c:otherwise>
            </c:choose>

        <div class="row"  id="newRuleSetDiv" style="display:none">
            <div class="label"><label for="newRuleSetName">RuleSet Name</label></div>
            <div class="value">
                <select id="newRuleSetName" onchange="setRuleSetName(this)">
                    <option value="Please select a RuleSet Name">Please select a RuleSet Name </option>
                    <option value="AE Assessment RuleSet">AE Assessment RuleSet </option>
                    <option value="SAE Reporting RuleSet">SAE Reporting RuleSet </option>
                    <option value="Other">Other </option>
                </select>
            </div>
        </div>
        <div class="row" id="otherRuleSetDiv" style="display:none">
            <div class="label"><label for="otherRuleSetName">Other Name</label></div>
            <div class="value">
		<input type="text" id="otherRuleSetName" name="otherRuleSetName" onkeyup="setOtherRuleSetName(this)"/>
            </div>
        </div>
	
	<input type="hidden" id="hiddenRuleSetName" name="ruleSetName" value=""/>
	</jsp:attribute>
		
	</tags:tabForm> 
</chrome:division>

    <script type="text/javascript">
    	Event.observe("createRuleSet", "click", 
    		function(e)
    		{
		    Element.show("newRuleSetDiv");
		    document.getElementById("radioRuleSetName1").checked;
    		}
    	
    	)
    </script>
</body>
</html>
