<html>
<head><title>JSP Page</title></head>
<body>
<%
	String ra = request.getRemoteAddr();
	String rh = request.getRemoteHost();
%>
<h1>Ciao</h1>
<p>Il tuo indirizzo è: <%=ra%> per host=<%=rh%></p>
</body>
</html>
