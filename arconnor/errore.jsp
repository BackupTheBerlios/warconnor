<%@ page import="java.util.*"%>
<%@ page import="it.bma.comuni.*"%>
<%@ page import="it.bma.web.*"%>

<%
	BmaJsp jsp = new BmaJsp();
	BmaErrore err = (BmaErrore)request.getSession().getAttribute(jsp.BMA_JSP_BEAN_ERRORE);
	if (err==null) err = new BmaErrore();
  else request.getSession().removeAttribute("Errore");
%>

<html>
<head>
<title>Pagina Errore</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
<link rel="stylesheet" href="style.css" type="text/css">
</head>
<body>
<table width="800" class="Bordo">
  <tr> 
    <td class="DetailTitle" colspan="2">MESSAGGIO DI ERRORE</td>
	</tr>
	<tr>
    <td class="DetailLabel" width="33%"> Codice dell'errore: </td>
		<td class="DetailData" width="67%"><%= err.getCodErrore()%></td>
	</tr>
  <tr> 
    <td class="DetailLabel">Messaggio utente:</td>
		<td class="DetailData"><%= err.getMsgUtente()%></td>
	</tr>
  <tr> 
    <td class="DetailLabel">Informazioni: </td>
		<td class="DetailData"><%= err.getInfo()%></td>
	</tr>
  <tr> 
    <td class="DetailLabel">Informazioni estese:</td>
		<td class="DetailData"><%= err.getInfoEstese()%></td>
	</tr>
  <tr> 
    <td class="DetailLabel">Oggetto: </td>
		<td class="DetailData"><%= err.getNomeOggetto()%></td>
	</tr>
  <tr> 
    <td class="DetailLabel">Messaggio sistema:</td>
		<td class="DetailData"><%= err.getMsgSistema()%></td>
	</tr>
  <tr> 
    <td class="DetailLabel">Tipo di Errore:</td>
		<td class="DetailData"><%= err.getTipo()%></td>
	</tr>
	<tr>
		<td class="MasterData" colspan="2" height="25" valign="middle" align="center">
			<a href='javascript:history.back()'>Riprendi</a>
		</td>
	</tr>
</table>

</body>
</html>