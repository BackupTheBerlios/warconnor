<%@ page import="java.util.*"%>
<%@ page import="it.bma.web.*"%>
<%@ page import="it.bma.comuni.*"%>

<%

	BmaJsp jsp = new BmaJsp();
	BmaSessione sessione = (BmaSessione)request.getAttribute(jsp.BMA_JSP_BEAN_SESSIONE);
	if (sessione==null) {
		response.sendRedirect(jsp.BMA_JSP_PAGINA_HOME);
	} 
	BmaFunzioneAttiva funzione = (BmaFunzioneAttiva)sessione.getFunzioneCorrente();
	String codSocieta = sessione.getUtente().getCodSocieta();
	String codUnita   = sessione.getUtente().getCodUnita();
	String codUtente  = sessione.getUtente().getCodUtente();
	String desUtente  = sessione.getUtente().getDesUtente();
	jsp = funzione.getJsp();
	jsp.maxColLen = 50;
	jsp.maxColLen2 = 25;
	jsp.config = sessione.getUserConfig();
	String webPath = jsp.getWebPath();
	String imgPath = webPath + "images/";
	sessione.getAliasLabels().setString("ORDINE-LOAD", "Ordine di Load");
		sessione.getAliasLabels().setString("COD_FONTE", "Sorgente Dati");
%>

