<table width="578" align="center">
<!-- ELENCHI PARTICOLARI -->
<!-- START -->
<!-- ELENCO REGOLE DI TARIFFA -->
<% if (funzione.getCodFunzione().equals("PDPD28") || 
			 funzione.getCodFunzione().equals("PDPD29") ||
			 funzione.getCodFunzione().equals("PDPD30")) {
%>
	<tr>
		<td class="DetailLabel" width="10">&nbsp;</td>		
		<td class="DetailLabel" width="30">Progr.</td>		
		<td class="DetailLabel" width="100">Regola</td>		
		<td class="DetailLabel" width="250">Nota</td>		
		<td class="DetailLabel" width="50">Valore</td>		
		<td class="DetailLabel" width="130">Tabella</td>		
	</tr>
<%
		for (int r=0;r<elenco.getNumeroRighe();r++) {
%> 
	<tr> 
		<td class="DetailData">
			<a href='javascript:invia("<%=codFunzioneEdit%>","<%=jsp.BMA_JSP_AZIONE_MODIFICA%>","<%=jsp.BMA_JSP_COMANDO_PREPARA%>","<%=elenco.getChiaveSelezione(r)%>")'>
				<img src="../images/destra.gif" border="0">
			</a>
		</td>
		<td class="DetailData"><%=jsp.getHtmlOutputList(elenco.getTabella().getColonna("NUM_PROGRESSIVO"), "DetailData")%></td>
		<td class="DetailData"><%=jsp.getHtmlOutputList(elenco.getTabella().getColonna("COD_REGOLA"), "DetailData")%></td>
		<td class="DetailData"><%=jsp.getHtmlOutputList(elenco.getTabella().getColonna("NOT_REGOLA"), "DetailData")%></td>
		<td class="DetailData"><%=jsp.getHtmlOutputList(elenco.getTabella().getColonna("VAL_REGOLA"), "DetailData")%></td>
		<td class="DetailData"><%=jsp.getHtmlOutputList(elenco.getTabella().getColonna("COD_TABELLA"), "DetailData")%></td>
	</tr>
<%	} %>	
<% }
	else { 
%>
<!-- ELENCO REGOLE DI TARIFFA -->
<!-- END -->

<!-- ELENCHI PARTICOLARI -->
<!-- END -->


<!-- ELENCO STANDARD -->
<!-- START -->
	<tr>
		<td class="DetailLabel" width="10">&nbsp;</td>		
<%
		for (int i=0;i<elenco.getSize();i++) {
			BmaDataColumn c = elenco.getTabella().getColonna(i);
			String n = sessione.getAlias(c.getNome());
			if (jsp.isVisibileList(c, funzione)) {
%>
		<td class="DetailLabel" width="<%=elenco.getSpazioColonna(570, i, n)%>"><%=n%></td>
<% 		} 
		}
		for (int r=0;r<elenco.getNumeroRighe();r++) {
			String chiaveRiga = elenco.getChiaveSelezione(r); 
%> 
	</tr><tr> 
		<td class="DetailData">
<% if (funzione.isAmmessa(jsp.BMA_JSP_AZIONE_MODIFICA)) { %>
			<a href='javascript:invia("<%=codFunzioneEdit%>","<%=jsp.BMA_JSP_AZIONE_MODIFICA%>","<%=jsp.BMA_JSP_COMANDO_PREPARA%>","<%=chiaveRiga%>")'>
				<img src="images/destra.gif" border="0">
			</a>
<% } else { %>&nbsp;<% } %>
		</td>
<%
			for (int i=0;i<elenco.getSize();i++) {
				BmaDataColumn c = elenco.getTabella().getColonna(i);
				if (jsp.isVisibileList(c, funzione)) {
%>	
		<td class="DetailData"><%=jsp.getHtmlOutputList(c, "DetailData")%></td>
<% 			} 
			} 
 		}
%>
	</tr>
<!-- ELENCO STANDARD -->
<!-- END -->
<% } %>
</table>

