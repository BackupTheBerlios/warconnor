<div class="Bordo" style="position: absolute; 
													top: 5; left: 5; 
													width: 199; height: 58;
													overflow: hidden;">
	<img src="<%=imgPath + codSocieta%>.jpg">
</div>
<div class="Bordo" style="position: absolute; 
													top: 5; left: 205; 
													width: 600; height: 58;
													overflow: hidden;">
			<table width="100%">
				<tr><td class="MasterLabel" colspan="3" align="right"><%=funzione.getDesFunzione()%></td></tr>
				<tr>
					<td class="DetailLabel" width="15%">Ufficio</td>
					<td class="DetailLabel" width="70%">Utente</td>
					<td class="DetailLabel" width="15%" align="center">Data</td>
				</tr>
				<tr>
					<td class="DetailData"><%=codUnita%></td>
					<td class="DetailData"><%=desUtente%> (<%=codUtente%>)</td>
					<td class="DetailData" align="center"><%=jsp.getData()%></td>
				</tr>
			</table> 
</div>
<div class="Bordo" style="position: absolute; 
													top: 68; left: 5; 
													width: 800; height: 28;
													overflow: auto;
													background-color: #E5E5E5;">
	<table width="100%"><tr><td width="100%">
<%
	BmaVector percorso = sessione.getListaFunzioni();
	for (int i=0;i<percorso.getSize();i++) {
		BmaFunzioneAttiva f = (BmaFunzioneAttiva)percorso.getElement(i);
		String sep = " - ";
		if (i==0) sep = ""; 
%>
	<%=sep%>
	<a href='javascript:invia("<%=f.getCodFunzione()%>","<%=f.getCodAzione()%>","<%=jsp.BMA_JSP_COMANDO_RIPRISTINA%>","")'>
	<%=f.getDesFunzione()%>(<%=f.getCodAzione()%>)
	</a>
<% }%>
	</td></tr></table> 
</div>
<div class="Bordo" style="position: absolute; 
													top: 100; left: 5; 
													width: 199; height: 480;
													overflow: auto;
													background-color: #BBBBFF;">
<table width="193" align="center">
	<tr>
		<td class="DetailTitle" width="95%">Opzioni Principali</td>
		<td class="DetailTitle" width="5%">
			<a href='index.html'><img src='images/su.gif' border='0'></a>
		</td>
	</tr><tr>
		<td class="DetailLabel">
<%
	BmaVector azioniMenu = (BmaVector)funzione.getAzioniMenu();
	if (azioniMenu==null) azioniMenu = new BmaVector("Menu");
	for (int i=0;i<azioniMenu.getSize();i++) {
		BmaMenu m = (BmaMenu)azioniMenu.getElement(i);
		if (m.getTipo().equals(jsp.BMA_JSP_MENU_SINISTRA)) {
			String classeLink = "Menu";
			BmaVector lf = sessione.getListaFunzioni();
			for (int kk=0;kk<lf.getSize();kk++) {
				BmaFunzioneAttiva ff = (BmaFunzioneAttiva)lf.getElement(kk);
				if (ff.getCodFunzione().equals(m.getFunzione())) classeLink = "MenuCorrente";
			}
%>
		<br><img src="images/pallino_arancio.gif" align="center"><%=m.getLink(classeLink)%>
<% 
		} 
	}
%>
		</td>
	</tr>
</table>
</div>

