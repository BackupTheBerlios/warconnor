<%@ include file="../incInit.jsp"%>
<%
	sessione.getAliasLabels().setString("PD_FUNZIONI", "Funzioni");
		sessione.getAliasLabels().setString("COD_SOCIETA", "Società");
		sessione.getAliasLabels().setString("COD_FUNZIONE", "Funzione");
		sessione.getAliasLabels().setString("COD_ENTITA", "Entita");
		sessione.getAliasLabels().setString("LABEL", "Label");
		sessione.getAliasLabels().setString("LABEL_BREVE", "Label Breve");
		sessione.getAliasLabels().setString("URI", "URI");
		sessione.getAliasLabels().setString("NOTE", "Note");
	sessione.getAliasLabels().setString("PD_PROFILIUTENTE", "Profili Utente");
		sessione.getAliasLabels().setString("COD_PROFILO", "Profilo");
		sessione.getAliasLabels().setString("DES_PROFILO", "Descrizione");
	sessione.getAliasLabels().setString("PD_PROFILOFUNZIONI", "Profilo Funzioni");
		sessione.getAliasLabels().setString("FLAG_RW", "Flag R/W");
	sessione.getAliasLabels().setString("PD_CICLOSTATI", "Passaggi Stato");
		sessione.getAliasLabels().setString("COD_TIPOSTATO", "Tipo Stato");
		sessione.getAliasLabels().setString("COD_STATODA", "Stato da");
		sessione.getAliasLabels().setString("COD_STATOA", "Stato a");
		sessione.getAliasLabels().setString("FLG_APPRMULTIPLA", "Appr.Multipla");
	
%>
<html>
<head>
<title>BMA-PRODOTTI - HOME</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<link rel="stylesheet" href="<%=webPath%>style.css" type="text/css">
</head>
<%@ include file="../bmaScript.js"%>
<body onload=<%=jsp.getOnLoadScript(sessione)%>>

<%@ include file="../incSchema.jsp"%>
<div class="Bordo" style="position: absolute; 
													top: 100; left: 205; 
													width: 600; height: 480;
													overflow: auto;">
<form method="post" action="<%=jsp.config.getServletPath()%>BmaServlet">
	<input type="hidden" name="BmaFunzione" value="">
	<input type="hidden" name="BmaAzione" value="">
	<input type="hidden" name="BmaComando" value="">
	<input type="hidden" name="BmaSelezione" value="">
<h3>Benvenuto nelle utility di amministrazione Prodotti...</h3>
</form>
</div>
</body>
</html>

