<%@ include file="../incInit.jsp"%>

<html>
<head>
<title>BMA-MEDIA UTILITY - HOME</title>
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
													overflow: auto;">
<form method="post" action="<%=jsp.config.getServletPath()%>BmaServlet">
	<input type="hidden" name="BmaFunzione" value="">
	<input type="hidden" name="BmaAzione" value="">
	<input type="hidden" name="BmaComando" value="">
	<input type="hidden" name="BmaSelezione" value="">
<h3>Benvenuto nelle utility di amministrazione Media...</h3>
</form>
</div>
</body>
</html>

