<%@ include file="../incInit.jsp"%>

<%
	BmaFunzioneEdit funzioneEdit = (BmaFunzioneEdit)funzione;
	String codFunzioneDettaglio = funzioneEdit.getFunzioneDettaglioPrimaria();
	BmaDataList elenco = null;
	BmaDataList elencoMaster = (BmaDataList)sessione.getBeanApplicativo(jsp.BMA_JSP_BEAN_LISTA);
	BmaDataForm form = (BmaDataForm)sessione.getBeanApplicativo(jsp.BMA_JSP_BEAN_FORM);
	if (form!=null) {
		String xmlNote = form.getValoreCampo("NOT_VOLUME");
		if (xmlNote==null) xmlNote="";
		request.getSession().setAttribute(jsp.BMA_JSP_BEAN_XML, xmlNote);
	}
	BmaDataList elencoDetail = (BmaDataList)sessione.getBeanApplicativo(jsp.BMA_JSP_BEAN_LISTA_DETAIL);
	String codFunzioneEdit = funzione.getCodFunzione();
	Properties xslIndiciBase = new Properties();
	xslIndiciBase.setProperty("CD-BACKUP","media/indiceVolume_fo.xsl");
	xslIndiciBase.setProperty("CD-ALBUM","media/indiceVolume_fo.xsl");
	xslIndiciBase.setProperty("CD-SHOW","media/indiceVolume_fo.xsl");
	xslIndiciBase.setProperty("CD-MP3","media/indiceVolume_fo.xsl");
	xslIndiciBase.setProperty("CD-PGM","media/indiceProgrammi_fo.xsl");
%>

<html>
<head>
<title>BMA-PRODOTTI - <%=funzione.getDesFunzione()%></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<link rel="stylesheet" href="style.css" type="text/css">
</head>
<%@ include file="../bmaScript.js"%>

<body onload=<%=jsp.getOnLoadScript(sessione)%>>

<%@ include file="../incSchema.jsp"%>

<div class="Bordo" style="position: absolute; 
													top: 100; left: 205; 
													width: 600; height: 480;
													overflow: scroll;">
								
<form method="post" action="<%=jsp.config.getServletPath()%>BmaServlet">
	<input type="hidden" name="BmaFunzione" value="">
	<input type="hidden" name="BmaAzione" value="">
	<input type="hidden" name="BmaComando" value="">
	<input type="hidden" name="BmaSelezione" value="">
<% 		if (jsp.getElementiContesto(sessione).getSize()>0) { %>
<table width="578" align="center">
<%
			BmaVector elementi = jsp.getElementiContesto(sessione);
			for (int i=0;i < elementi.getSize();i++) {
				BmaParametro p = (BmaParametro)elementi.getElement(i);
%>
	<tr>
		<td class="DataLevel-2" width="40%">in <%=p.getNome()%> hai scelto:</td>
		<td class="DataLevel-2" width="60%"><%=p.getValore()%></td>
	</tr>
<%
			}
%>
</table>
<% } %>

	
<!-- TABELLA PER LA LISTA PRINCIPALE -->
<!-- START -->
<% if (elencoMaster!=null) { 
			elenco = elencoMaster;
%> 
<table width="578" align="center">
	<tr>
		<td class="DataLevel-1" width="40%"><%=funzione.getDesAzione()%> <%=sessione.getAlias(elenco.getChiave())%></td>
		<td class="Action" align="right" width="60%">
		<% if (funzione.isAmmessa(jsp.BMA_JSP_AZIONE_MODELLO_NEW)) { %>
			<%=jsp.getHtmlBottone(funzione, "", jsp.BMA_JSP_AZIONE_MODELLO_NEW, "","Nuovo da Modello")%>
		<% } %>
			<%=jsp.getHtmlBottone(funzione, "", jsp.BMA_JSP_AZIONE_NUOVO, "","Nuovo")%>
		</td>
	</tr>
</table>
<%@ include file="../incElenco.jsp"%> 
<% } %>
<!-- TABELLA PER LA LISTA PRINCIPALE -->
<!-- END -->


<!-- TABELLA PER IL FORM PRINCIPALE -->
<!-- START -->

<% if (form!=null) { %>

<table width="578" align="center">
	<tr>
		<td class="DataLevel-1" width="40%"><%=funzione.getDesAzione()%> <%=sessione.getAlias(form.getChiave())%></td>
		<td class="Action" align="right" width="60%">
<% 
		if (funzione.getCodAzione().equals(jsp.BMA_JSP_AZIONE_NUOVO)) {
			out.println(jsp.getHtmlBottone(funzione, "", "", jsp.BMA_JSP_COMANDO_AGGIORNA, "Aggiorna"));
			out.println(jsp.getHtmlBottone(funzione, "", "", jsp.BMA_JSP_COMANDO_ANNULLA, "Annulla"));
		}
		else if (funzione.getCodAzione().equals(jsp.BMA_JSP_AZIONE_MODELLO_NEW)) {
			out.println(jsp.getHtmlBottone(funzione, "", "", jsp.BMA_JSP_COMANDO_AGGIORNA, "Aggiorna"));
			out.println(jsp.getHtmlBottone(funzione, "", "", jsp.BMA_JSP_COMANDO_ANNULLA, "Annulla"));
		}
		else if (funzione.getCodAzione().equals(jsp.BMA_JSP_AZIONE_MODIFICA)) {
			if (funzione.isAmmessa(jsp.BMA_JSP_AZIONE_MODELLO_RES)) {
				out.println(jsp.getHtmlBottone(funzione, "", jsp.BMA_JSP_AZIONE_MODELLO_RES, jsp.BMA_JSP_COMANDO_AGGIORNA, "Ripristina Modello"));
			}
			out.println(jsp.getHtmlBottone(funzione, "", "", jsp.BMA_JSP_COMANDO_AGGIORNA, "Aggiorna"));
			out.println(jsp.getHtmlBottone(funzione, "", "", jsp.BMA_JSP_COMANDO_ANNULLA, "Annulla"));
			out.println(jsp.getHtmlBottone(funzione, "", "", jsp.BMA_JSP_COMANDO_ELIMINA, "Elimina"));
		}
%>		
		</td>
	</tr>
</table>
<%@ include file="../incForm.jsp"%> 
<% } %>
<!-- TABELLA PER IL FORM PRINCIPALE -->
<!-- END -->

<!-- TABELLA PER IL MENU DETTAGLI -->
<!-- START -->
<br>
<% if (funzione.getCodAzione().equals(jsp.BMA_JSP_AZIONE_MODIFICA)) { 
		String codTipoVolume = form.getValoreCampo("COD_TIPOVOLUME");
		String xslBase = xslIndiciBase.getProperty(codTipoVolume); 
%>
<table width="578" align="center">
	<tr>
		<td class="DataLevel-1" width="100%">
			<img src='images/pallino_arancio.gif' border='0' align="top">
			<a class='MenuOpzioni' href='Xml2PdfServlet?xsl=<%=xslBase%>&dummy=d.pdf' target='_blank'>Indice Volume</a>
			&nbsp;
<%
			for (int i=0;i<azioniMenu.getSize();i++) {
				BmaMenu m = (BmaMenu)azioniMenu.getElement(i);

				if (m.getTipo().equals(jsp.BMA_JSP_MENU_BARRA)) { %>
			<img src='images/pallino_arancio.gif' border='0' align="top">
			<a class="MenuOpzioni" href='javascript:invia("<%=m.getFunzione()%>","<%=m.getAzione()%>","<%=jsp.BMA_JSP_COMANDO_PREPARA%>","")'><%=m.getLabel()%></a>
			&nbsp;
<%
				}
			}		
%>
		</td>
	</tr>
</table>
<% } %>
<!-- TABELLA PER IL MENU DETTAGLI -->
<!-- END -->


<!-- TABELLA PER LA LISTA SECONDARIA -->
<!-- START -->
<% if (elencoDetail!=null) { 
		elenco = elencoDetail;
		codFunzioneEdit = codFunzioneDettaglio;
%> 
<table width="578" align="center">
	<tr>
		<td class="DataLevel-1" width="40%"><%=sessione.getAlias(elenco.getChiave())%></td>
		<td class="Action" align="right" width="60%">
<%		
			out.println(jsp.getHtmlBottone(funzione, codFunzioneEdit, jsp.BMA_JSP_AZIONE_MODELLO_NEW,"","Nuovo da Modello"));
			out.println(jsp.getHtmlBottone(funzione, codFunzioneEdit, jsp.BMA_JSP_AZIONE_NUOVO,"","Nuovo"));
%>
		</td>
	</tr>
</table>
<%@ include file="../incElenco.jsp"%> 
<% } %>
<!-- TABELLA PER LA LISTA SECONDARIA -->
<!-- END -->




</form>
</div>
</body>
</html>

