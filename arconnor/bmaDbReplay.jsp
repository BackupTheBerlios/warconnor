<%@ include file="incInit.jsp"%>

<%

	BmaDataList elenco = null;
	BmaDataList elencoMaster = (BmaDataList)sessione.getBeanApplicativo(jsp.BMA_JSP_BEAN_LISTA);
	BmaDataForm form = (BmaDataForm)sessione.getBeanApplicativo(jsp.BMA_JSP_BEAN_FORM);
	BmaVector tables = (BmaVector)sessione.getBeanApplicativo("tablesNames");
	String campoSelezionate = jsp.BMA_JSP_PREFISSO_MULTI + "TabelleSelezionate"; 
	BmaValuesList selTabObj = (BmaValuesList)sessione.getBeanApplicativo(campoSelezionate);
	if (selTabObj==null) selTabObj = new BmaValuesList(campoSelezionate);
	String[] selTables = selTabObj.getValues();
	String codFunzioneEdit = funzione.getCodFunzione();
%>
<html>
<head>
<title>BMA-SERVIZI - <%=funzione.getDesFunzione()%></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<link rel="stylesheet" href="<%=webPath%>style.css" type="text/css">
</head>
<%@ include file="bmaScript.js"%>
<script language='JavaScript'>
	function addSectedOptions(list1, list2) {
		for (var i=0; i<list1.options.length; i++) {
			if (list1.options[i].selected) {
				var idx = seekOption(list2, list1.options[i].value);
				if (idx>=0) {
					nOption = new Option(list1.options[i].text, list1.options[i].value, false, false);
					list2.add(nOption, idx);
				}
			}
		}
	}
	function addAllOptions(list1, list2) {
		for (var i=0; i<list1.options.length; i++) {
			nOption = new Option(list1.options[i].text, list1.options[i].value, false, false);
			list2.options[i]=nOption;
		}
		for (var i=list1.options.length; i <list2.options.length; i++) {
			list2.options[i]=null;
		}
	}
	function removeOptions(list, onlySelected) {
		for (var i=list.options.length - 1; i>=0; i--) {
			if (!onlySelected || list.options[i].selected) {
				list.options[i]=null;
			}
		}
	}
	function seekOption(list, v) {
		for (var i=0; i<list.length; i++) {
			if (list.options[i].value==v) return -1;
			else if(list.options[i].value > v) return i;
		}
		return list.length;
	}
	function selectAllOptions(list) {
		for (var i=0; i<list.length; i++) {
			list.options[i].selected = true;
		}
	}
	function selezionaInvia(f,a,c,s) {
		frm = document.forms[0];
		if (frm.<%=campoSelezionate%>!=null) {
			selectAllOptions(frm.<%=campoSelezionate%>);
		}
		invia(f,a,c,s);
	}
</script>

<body onload=<%=jsp.getOnLoadScript(sessione)%>>

<%@ include file="incSchema.jsp"%>

<div class="Bordo" style="position: absolute; 
													top: 100; left: 205; 
													width: 600; height: 480;
													overflow: scroll;">
								
<form method="post" action="<%=jsp.config.getServletPath()%>BmaServlet">
	<input type="hidden" name="BmaFunzione" value="">
	<input type="hidden" name="BmaAzione" value="">
	<input type="hidden" name="BmaComando" value="">
	<input type="hidden" name="BmaSelezione" value="">

<!-- TABELLA PER IL FORM PRINCIPALE -->
<!-- START -->
<% if (form!=null) { %> 

<table width="578" align="center">
	<tr>
		<td class="DataLevel-1" width="40%"><%=sessione.getAlias(form.getChiave())%></td>
		<td class="Action" align="right" width="60%">
<%
	for (int i=0;i<azioniMenu.getSize();i++) {
		BmaMenu m = (BmaMenu)azioniMenu.getElement(i);
		if (m.getTipo().equals(jsp.BMA_JSP_MENU_BARRA)) { %>
			<%=jsp.getHtmlBottone(funzione, m.getFunzione(), m.getAzione(), jsp.BMA_JSP_COMANDO_PREPARA, m.getLabel())%>
<%	}	
		else if (m.getTipo().equals(jsp.BMA_JSP_MENU_AZIONE)) { %>
			<%=jsp.getHtmlBottone(funzione, "", "", jsp.BMA_JSP_COMANDO_ANNULLA, "Annulla")%>		
<%	} 	
	}
%>		
		</td>
	</tr>
</table>
<%@ include file="incForm.jsp"%> 
<% } %>
<!-- TABELLA PER IL FORM PRINCIPALE -->
<!-- END -->


<!-- BOX DI SELEZIONE DELLE TABELLE -->
<!-- START -->
<% if (tables!=null) { %>
<table class="Bordo" width="100%" class="Input" align="center">
	<tr>
		<td class="DetailLabel" width="40%">Elenco Tabelle</td>
		<td class="DetailLabel" width="20%">&nbsp;</td>
		<td class="DetailLabel" width="40%">Da Replicare</td>
	</tr><tr>
		<td class="DetailData" align="center">
			<select class="DetailData" multiple size="10" name="Tutte">
	<% for (int i=0;i<tables.getSize();i++) { 
			String sTable = tables.getString(i);
	%>
				<option class="DetailData" value="<%=sTable%>"><%=sTable%></option>
	<% } %>
			</select>
		</td>
		<td class="DetailData">
			<p align="center"><input class="DetailData" type="button" name="add" value="Includi ----->" onclick='javascript:addSectedOptions(Tutte, <%=campoSelezionate%>)'>
			<p align="center"><input class="DetailData" type="button" name="addAll" value="Includi Tutte" onclick='javascript:addAllOptions(Tutte, <%=campoSelezionate%>)'>
			<p align="center"><input class="DetailData" type="button" name="del" value="<----- Escludi" onclick='javascript:removeOptions(<%=campoSelezionate%>, true)'>
			<p align="center"><input class="DetailData" type="button" name="del" value="Escludi Tutte" onclick='javascript:removeOptions(<%=campoSelezionate%>, false)'>
		</td>
		<td class="DetailData" align="center">
			<select class="DetailData" multiple size="10" name="<%=campoSelezionate%>">
	<% for (int i=0;i<selTables.length;i++) { %>
				<option class="DetailData" value="<%=selTables[i]%>"><%=selTables[i]%></option>
	<% } %>
			</select>
		</td>
	</tr>
</table>
<% } %>
<!-- BOX DI SELEZIONE DELLE TABELLE -->
<!-- END -->

<!-- BOX DEI PULSANTI -->
<!-- START -->
<table width="100%" class="Input" align="center">
	<tr>
		<td class='DetailLabel' align='center' width='40%'>
			<input class='DetailData' type='button' name='ListaTabelle' value='Lista Tabelle' 
							onclick='javascript:selezionaInvia("","","<%=jsp.BMA_JSP_COMANDO_PREPARA%>","ListaTabelle")'>
		</td>
		<td class='DetailLabel' align='center' width='60%'>
<% if (tables!=null) { %>
			<input class='DetailData' type='button' name='EseguiServizio' value='Esegui Servizio' 
							onclick='javascript:selezionaInvia("","","<%=jsp.BMA_JSP_COMANDO_PREPARA%>","EseguiReplay")'>
<% } %>
		</td>
	</tr>
</table>
<!-- BOX DEI PULSANTI -->
<!-- END -->

<!-- TABELLA PER LA LISTA PRINCIPALE -->
<!-- START -->
<% if (elencoMaster!=null) { 
			elenco = elencoMaster;
%> 
<table width="578" align="center">
	<tr>
		<td class="DataLevel-1" width="40%"><%=sessione.getAlias(elenco.getChiave())%></td>
		<td class="Action" align="right" width="60%">
			<a class='Button' href='xmlOutput.jsp' target='_blank'>XML</a>
		</td>
	</tr>
</table>
<%@ include file="incElenco.jsp"%> 
<% } %>
<!-- TABELLA PER LA LISTA PRINCIPALE -->
<!-- END -->


</form>
</div>
</body>
</html>

