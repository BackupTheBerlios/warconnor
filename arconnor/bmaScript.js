<script language="JavaScript">
function invia(f,a,c,s) {
	frm = document.forms[0];
	initForm(f,a,c,s);
	frm.submit();	
}
function initForm(f,a,c,s) {
	frm = document.forms[0];
	if (f.length>0) frm.BmaFunzione.value = f;
	if (a.length>0) frm.BmaAzione.value = a;
	if (c.length>0) frm.BmaComando.value = c;
	if (s.length>0) frm.BmaSelezione.value = s;
}
function init(f,a,c,s, msg) {
	frm = document.forms[0];
	if (frm.BmaFunzione.value.length>0) {
		alert("E' possibile utilizzare solo le opzioni di navigazione previste dalla pagina");
		invia("<%=funzione.getCodFunzione()%>","<%=funzione.getCodAzione()%>","<%=jsp.BMA_JSP_COMANDO_RIPRISTINA%>","");
	}
	if (msg.length>0) alert(msg);
	initForm(f,a,c,s);
}
function confermaElimina() {
	var msg = "La cancellazione potrebbe non essere possibile a causa di altri riferimenti a questo elemento." + '\n' +
						"Confermi la cancellazione?";
	if (confirm(msg)) invia("","","Elimina","");
}
</script>
