<%@ include file="incInit.jsp"%>

<%

	BmaDataList elenco = null;
	BmaDataList elencoMaster = (BmaDataList)sessione.getBeanApplicativo(jsp.BMA_JSP_BEAN_LISTA);
	BmaDataForm form = (BmaDataForm)sessione.getBeanApplicativo(jsp.BMA_JSP_BEAN_FORM);
	String codFunzioneEdit = funzione.getCodFunzione();
%>
<html>
<head>
<title>BMA-SERVIZI - <%=funzione.getDesFunzione()%></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<link rel="stylesheet" href="<%=webPath%>style.css" type="text/css">
</head>
<%@ include file="bmaScript.js"%>

<body onload=<%=jsp.getOnLoadScript(sessione)%>>

<%@ include file="incSchema.jsp"%>

<div class="Bordo" style="position: absolute; 
													top: 100; left: 205; 
													width: 600; height: 480;
													overflow: scroll;">
								
<form method="post" action="<%=jsp.config.getServletPath()%>BmaServlet">
	<input type="hidden" name="BmaFunzione" value="">
	<input type="hidden" name="BmaAzione" value="">
	<input type="hidden" name="BmaComando" value="">
	<input type="hidden" name="BmaSelezione" value="">

<!-- TABELLA PER IL FORM PRINCIPALE -->
<!-- START -->
<% if (form!=null) { %> 

<table width="578" align="center">
	<tr>
		<td class="DataLevel-1" width="40%"><%=sessione.getAlias(form.getChiave())%></td>
		<td class="Action" align="right" width="60%">
<%
	for (int i=0;i<azioniMenu.getSize();i++) {
		BmaMenu m = (BmaMenu)azioniMenu.getElement(i);
		if (m.getTipo().equals(jsp.BMA_JSP_MENU_BARRA)) { %>
			<%=jsp.getHtmlBottone(funzione, m.getFunzione(), m.getAzione(), jsp.BMA_JSP_COMANDO_PREPARA, m.getLabel())%>
<%	}	
		else if (m.getTipo().equals(jsp.BMA_JSP_MENU_AZIONE)) { %>
			<%=jsp.getHtmlBottone(funzione, "", "", jsp.BMA_JSP_COMANDO_AGGIORNA, "Esegui")%>		
			<%=jsp.getHtmlBottone(funzione, "", "", jsp.BMA_JSP_COMANDO_ANNULLA, "Annulla")%>		
<%	} 	
	}
%>		
		</td>
	</tr>
</table>
<%@ include file="incForm.jsp"%> 
<% } %>
<!-- TABELLA PER IL FORM PRINCIPALE -->
<!-- END -->

<!-- TABELLA PER LA LISTA PRINCIPALE -->
<!-- START -->
<% if (elencoMaster!=null) { 
			elenco = elencoMaster;
%> 
<table width="578" align="center">
	<tr>
		<td class="DataLevel-1" width="40%"><%=sessione.getAlias(elenco.getChiave())%></td>
		<td class="Action" align="right" width="60%">
			<a class='Button' href='xmlOutput.jsp' target='_blank'>XML</a>
		</td>
	</tr>
</table>
<%@ include file="incElenco.jsp"%> 
<% } %>
<!-- TABELLA PER LA LISTA PRINCIPALE -->
<!-- END -->


</form>
</div>
</body>
</html>

