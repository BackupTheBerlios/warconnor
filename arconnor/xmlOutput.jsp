<%@page contentType="text/xml"%>
<%@ page import="it.bma.comuni.*"%>
<%@ page import="it.bma.web.*"%>
<%
	BmaJsp jsp = new BmaJsp();
	BmaParametro pXml = (BmaParametro)request.getSession().getAttribute(jsp.BMA_JSP_BEAN_XML);
	if (pXml==null) pXml = new BmaParametro();
%>
<?xml version='1.0' encoding='ISO-8859-1' ?>
<%=pXml.getValore()%>
