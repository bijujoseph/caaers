<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="title"%>
<%@attribute name="id"%>
<%@attribute name="cssClass"%>
<%@attribute name="style"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="division ${cssClass}"
    <tags:attribute name="id" value="${id}"/> <tags:attribute name="style" value="${style}"/>>

    <tr>
        <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabs">
            <tr>
                <td width="100%" class="tabDisplay">
                    <c:if test="${not empty title}">
                        <img src="<chrome:imageUrl name="tab3_h_L.gif"/>" width="1" height="16" align="absmiddle"><span class="current">
                        ${title}
                        </span><img src="<chrome:imageUrl name="tab3_h_R.gif"/>" width="7" height="16" align="absmiddle">
                    </c:if>
                    <img src="<chrome:imageUrl name="spacer.gif"/>" width="1" height="16" align="absmiddle">
                </td>
                <td><img src="<chrome:imageUrl name="spacer.gif"/>" width="7" height="1"></td>
            </tr>
            <tr>
                <td colspan="2" class="tabBotL"><img src="<chrome:imageUrl name="spacer.gif"/>"
                    width="1" height="7"></td>
            </tr>
            <tr>
                <td colspan="2" class="contentL division-content">
                    <jsp:doBody/>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="content_B"><img src="<chrome:imageUrl name="content_BL.gif"/>" align="left" hspace="0"><img src="<chrome:imageUrl name="content_BR.gif"/>" align="right" hspace="0"></td>
            </tr>
        </table>
        </td>
    </tr>
</table>
