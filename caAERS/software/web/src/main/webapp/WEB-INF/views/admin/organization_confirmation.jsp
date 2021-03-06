<%--
Copyright SemanticBits, Northwestern University and Akaza Research

Distributed under the OSI-approved BSD 3-Clause License.
See http://ncip.github.com/caaers/LICENSE.txt for details.
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Organization Confirmation</title>
</head>
<body>
<chrome:box title="Confirmation">
<chrome:flashMessage/>
<form:form>
<input type="hidden" name="CSRF_TOKEN" value="${CSRF_TOKEN }"/>
<chrome:division id="single-fields">
      <div class="content">
            <div class="row">
              <div class="label">Name</div>
              <div class="value">${command.name}</div>
           </div>
           <div class="row">
              <div class="label">Description</div>
              <div class="value">${command.descriptionText}</div>
           </div>
           <div class="row">
              <div class="label">Assigned Identifier</div>
              <div class="value">${command.nciInstituteCode}</div>
           </div>
       </div>
</chrome:division>
</form:form>
</chrome:box>
</body>
</html>
