<%@ page import="java.util.*"%>
<%@ page import="it.bma.comuni.*"%>
<%@ page import="it.bma.web.*"%>
<%
	BmaJsp jsp = new BmaJsp();
	Hashtable props = new Hashtable();
	props.put("SessionId", request.getSession().getId());
	props.put("CreationTime", Long.toString(request.getSession().getCreationTime()));
	props.put("LastAccessedTime", Long.toString(request.getSession().getLastAccessedTime()));
	props.put("RealPath", request.getSession().getServletContext().getRealPath(""));
	props.put("ContextPath", request.getContextPath());
	Enumeration att = request.getSession().getServletContext().getInitParameterNames();
	while (att.hasMoreElements()) {
		String name = (String)att.nextElement();
		props.put("ContextInit: " + name, request.getSession().getServletContext().getInitParameter(name));
	}
	BmaSessione sessione = (BmaSessione)request.getAttribute(jsp.BMA_JSP_BEAN_SESSIONE);
	if (sessione==null) sessione = new BmaSessione(request.getSession().getId());
	BmaUserConfig conf = (BmaUserConfig)sessione.getUserConfig();
	if (conf==null) conf = new BmaUserConfig();
	BmaJdbcSource jSource = null;
	jSource = (BmaJdbcSource)conf.getFontiJdbc().getElement(conf.getFonteDefault());
	if (jSource==null) jSource = new BmaJdbcSource("No Source");
	BmaHashtable listaSessioni = (BmaHashtable)sessione.getBeanApplicativo(jsp.BMA_JSP_BEAN_LISTA);
	if (listaSessioni==null) listaSessioni = new BmaHashtable("ListaSessioniVuota");
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
	<title>ARCONNOR - ADMIN PAGE</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<link rel="stylesheet" href="style.css" type="text/css">
</head>

<body>
<h1 align="center">Administration Page</h1>
<table class="Bordo" width="80%" align="center">
<!-- TABELLA DATI CONFIGURAZIONE SERVER -->
<%
	Enumeration e = props.keys();
	while (e.hasMoreElements()) {
		String k = (String)e.nextElement();
		String v = (String)props.get(k);

%>
	<tr>
		<td class='DetailLabel' width='40%'><%=k%></td>
		<td class='DetailData' width='60%'><%=v%></td>
	</tr>
<%	} %>
<table class="Bordo" width="80%" align="center">
<!-- TABELLA DATI CONFIGURAZIONE APPLICAZIONE -->
	<tr>
		<td class='DetailLabel' width='40%'>Fonte Dati</td>
		<td class='DetailData' width='60%'><%=conf.getFonteDefault()%></td>
	</tr>
	<tr>
		<td class='DetailLabel'>Database</td>
		<td class='DetailData'><%=jSource.getUrl()%></td>
	</tr>
	<tr>
		<td class='DetailLabel'>Fonte Target</td>
		<td class='DetailData'><%=conf.getParametroApplicazione("FONTE-TARGET")%></td>
	</tr>
</table>
<form method='post' name='myForm' action=''>
	<input type='hidden' name='<%=jsp.BMA_JSP_CAMPO_FUNZIONE%>' value='ADM'>
<table class="Bordo" width="80%" align="center">
<!-- TABELLA LISTA SESSIONI -->
	<tr>
		<td class='DetailTitle' colspan='8'>Lista Sessioni</td>
	</tr>
	<tr>
		<td class='DetailLabel' width='3%'>&nbsp;</td>
		<td class='DetailLabel' width='17%'>Id</td>
		<td class='DetailLabel' width='10%'>Durata</td>
		<td class='DetailLabel' width='10%'>Utente</td>
		<td class='DetailLabel' width='30%'>Nominativo</td>
		<td class='DetailLabel' width='10%'>Applicazione</td>
		<td class='DetailLabel' width='10%'>Societa</td>
		<td class='DetailLabel' width='10%'>Profilo</td>
	</tr>
<%
	Enumeration eSessioni = listaSessioni.elements();
	while (eSessioni.hasMoreElements()) {
		BmaSessione unaSessione = (BmaSessione)eSessioni.nextElement();
		String idSessione = unaSessione.getChiave();
		BmaUtente utente = unaSessione.getUtente();
		if (utente==null) utente = new BmaUtente();

%>
	<tr>
		<td class='DetailLabel' width='3%'>
			<% if (!idSessione.equals(sessione.getChiave())) { %>
				<input class='DetailData' type='checkbox' name='idSessione' value='<%=idSessione%>'>
			<% } %>
		</td>
		<td class='DetailData'><%=idSessione%></td>
		<td class='DetailData'><%=unaSessione.getDurata()%></td>
		<td class='DetailData'><%=utente.getCodUtente()%></td>
		<td class='DetailData'><%=utente.getDesUtente()%></td>
		<td class='DetailData'><%=utente.getCodApplicazione()%></td>
		<td class='DetailData'><%=utente.getCodSocieta()%></td>
		<td class='DetailData'><%=utente.getCodProfilo()%></td>
	</tr>
<% } %>
</table>
<table class="Bordo" width="80%" align="center">
	<tr>
		<td class="DetailData" width='25%' align='center'>Ricarica Configurazione<br>
			<input class="DetailData" type='submit' name='reload' value='Ricarica'>
		</td>
		<td class="DetailData" width='25%' align='center'>Rimuovi Sessioni<br>
			<input class="DetailData" type='submit' name='remove' value='Selezione'>
			<input class="DetailData" type='submit' name='removeAll' value='Tutto'>
		</td>
		<td class="DetailData" width='25%' align='center'>&nbsp;</td>
		<td class="DetailData" width='25%' align='center'>&nbsp;</td>
	</tr>
</table>
</form>
</body>
</html>
