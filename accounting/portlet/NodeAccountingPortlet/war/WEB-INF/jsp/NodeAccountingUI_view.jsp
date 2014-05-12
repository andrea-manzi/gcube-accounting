<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%-- Uncomment below lines to add portlet taglibs to jsp
<%@ page import="javax.portlet.*"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>

<portlet:defineObjects />
--%>
<script type="text/javascript" language="javascript" src='<%=request.getContextPath()%>/nodeaccountingui/nodeaccountingui.nocache.js'></script>
<script type="text/javascript"> 
    Ext.newDate = function(time) {
	return new Date(time);
	};
	</script>
	

<div id="UIDiv">
</div>