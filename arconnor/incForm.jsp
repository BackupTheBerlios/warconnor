<table width="578" align="center">
<% 
	BmaVector campi = form.getDati();
	for (int i=0;i<campi.getSize();i++) { 
		BmaDataColumn f = (BmaDataColumn)campi.getElement(i);
		if (jsp.isVisibileForm(f, funzione)) {
%>
	<tr>
		<td class="DetailLabel" width="40%"><%=sessione.getAlias(f.getNome())%></td>		
		<td class="DetailData" width="60%"><%=jsp.getHtmlInput(f, funzione.getCodAzione(), "DetailData")%></td>		
	</tr>
<% 
		} 
	}
%>
</table>
