package it.bma.web;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import it.bma.comuni.*;
import it.bma.ptf.*;

public class BmaContrattoServlet extends HttpServlet implements it.bma.comuni.BmaLiterals {
	private final String DATA_RICEZIONE = "2003-02-13";
	private BmaUserConfig appConfig;
	private BmaHashtable listaSessioni = new BmaHashtable("Sessioni");
	private BmaJsp bJsp = new BmaJsp();
/**
 * BmaServlet constructor comment.
 */
public BmaContrattoServlet() {
	super();
}
private void aggiornaCaratteristicheOggetto(BmaHashtable caratteristiche, HttpServletRequest request) {
	Enumeration e = caratteristiche.elements();
	while (e.hasMoreElements()) {
		BmaCondizione c = (BmaCondizione)e.nextElement();
		String d = request.getParameter("DD_" + c.getCodCondizione());
		if (d==null) d = "";
		c.setValCondizione(d);
	}
}
private void aggiornaGaranzieOggetto(BmaHashtable garanzie, HttpServletRequest request) {
	Enumeration e = garanzie.elements();
	while (e.hasMoreElements()) {
		BmaGaranziaPrestata g = (BmaGaranziaPrestata)e.nextElement();
		String d = "";
		
		d = request.getParameter("DD_" + g.getCodGaranzia() + "codMassimale");
		if (d==null) d = "";
		g.setCodMassimale(d);
		
		d = request.getParameter("DD_" + g.getCodGaranzia() + "impAssicurato");
		if (d==null) d = "";
		g.setImpAssicurato(d);

		// Attenzione!!!
		g.getCondizioni().clear();
		
		d = request.getParameter("DD_" + g.getCodGaranzia() + "impPremio");
		if (d==null) d = "";
		g.impostaCondizione(g.BMA_PTF_CONDIZIONE_BASE, "", d);
		
		d = request.getParameter("DD_" + g.getCodGaranzia() + "desCondizioni");
		if (d==null) d = "";
		for (int i = 0; i < d.length(); i++){
			String k = d.substring(i, i+1);
			g.impostaCondizione(k, "", d);
		}
	}
}
private void aggiornaInfo(BmaContratto contratto, HttpServletRequest request) {
	BmaHashtable t = contratto.getInfo();
	Enumeration e = t.elements();
	while (e.hasMoreElements()) {
		BmaParametro p = (BmaParametro)e.nextElement();
		String d = request.getParameter("DD_" + p.getNome());
		if (d==null) d = "";
		p.setValore(d);
	}
}
private void aggiornaInfoContraente(BmaContratto contratto, HttpServletRequest request) {
	BmaHashtable t = contratto.getInfoContraente();
	Enumeration e = t.elements();
	while (e.hasMoreElements()) {
		BmaParametro p = (BmaParametro)e.nextElement();
		String d = request.getParameter("DD_" + p.getNome());
		if (d==null) d = "";
		p.setValore(d);
	}
}
private void aggiornaInfoPremiBil(BmaContratto contratto, HttpServletRequest request) {

	String ramo = request.getParameter("key");
	if (ramo==null || ramo.trim().length()==0) return;
	
	BmaPremiRamo premioRamo = (BmaPremiRamo)contratto.getInfoPremiRamoBil().getElement(ramo);
	if (premioRamo==null) return;
		
	BmaHashtable t = premioRamo.getPremi();
	Enumeration e = t.elements();
	while (e.hasMoreElements()) {
		BmaParametro p = (BmaParametro)e.nextElement();
		String d = request.getParameter("DD_" + p.getNome());
		if (d==null) d = "";
		p.setValore(d);
	}
}
private void aggiornaInfoPremiProt(BmaContratto contratto, HttpServletRequest request) {

	String ramo = request.getParameter("key");
	if (ramo==null || ramo.trim().length()==0) return;
	
	BmaPremiRamo premioRamo = (BmaPremiRamo)contratto.getInfoPremiRamoProt().getElement(ramo);
	if (premioRamo==null) return;
		
	BmaHashtable t = premioRamo.getPremi();
	Enumeration e = t.elements();
	while (e.hasMoreElements()) {
		BmaParametro p = (BmaParametro)e.nextElement();
		String d = request.getParameter("DD_" + p.getNome());
		if (d==null) d = "";
		p.setValore(d);
	}
}
private void aggiornaInfoPrimaRata(BmaContratto contratto, HttpServletRequest request) {
	BmaHashtable t = contratto.getInfoPrimaRata();
	Enumeration e = t.elements();
	while (e.hasMoreElements()) {
		BmaParametro p = (BmaParametro)e.nextElement();
		String d = request.getParameter("DD_" + p.getNome());
		if (d==null) d = "";
		p.setValore(d);
	}
}
private void aggiornaInfoProvvigioni(BmaContratto contratto, HttpServletRequest request) {
	BmaHashtable t = contratto.getInfoProvvigioni();
	Enumeration e = t.elements();
	while (e.hasMoreElements()) {
		BmaParametro p = (BmaParametro)e.nextElement();
		String d = request.getParameter("DD_" + p.getNome());
		if (d==null) d = "";
		p.setValore(d);
	}
}
private void aggiornaInfoRateSuccessive(BmaContratto contratto, HttpServletRequest request) {
	BmaHashtable t = contratto.getInfoRateSuccessive();
	Enumeration e = t.elements();
	while (e.hasMoreElements()) {
		BmaParametro p = (BmaParametro)e.nextElement();
		String d = request.getParameter("DD_" + p.getNome());
		if (d==null) d = "";
		p.setValore(d);
	}
}
private void aggiornaInfoSostituita(BmaContratto contratto, HttpServletRequest request) {
	BmaHashtable t = contratto.getInfoSostituita();
	Enumeration e = t.elements();
	while (e.hasMoreElements()) {
		BmaParametro p = (BmaParametro)e.nextElement();
		String d = request.getParameter("DD_" + p.getNome());
		if (d==null) d = "";
		p.setValore(d);
	}
}
private void aggiornaInfoVincolo(BmaContratto contratto, HttpServletRequest request) {
	BmaHashtable t = contratto.getInfoVincolo();
	Enumeration e = t.elements();
	while (e.hasMoreElements()) {
		BmaParametro p = (BmaParametro)e.nextElement();
		String d = request.getParameter("DD_" + p.getNome());
		if (d==null) d = "";
		p.setValore(d);
	}
}
private BmaContratto copiaContratto(HttpServletRequest request, BmaContratto contratto) throws BmaException {

	String codSocietaNew = request.getParameter("DD_codSocieta");
	String codAgenziaNew = request.getParameter("DD_codAgenzia");
	while (codAgenziaNew.trim().length()<6) {
		codAgenziaNew = "0" + codAgenziaNew;
	}
	String numPolizzaNew = request.getParameter("DD_numPolizza");
	while (numPolizzaNew.trim().length()<6) {
		numPolizzaNew = "0" + numPolizzaNew;
	}

	BmaContratto c2 = (BmaContratto)contratto.cloneXml(contratto);
	
	c2.setCodSocieta(codSocietaNew);
	c2.setCodAgenzia(codAgenziaNew);
	c2.setNumPolizza(numPolizzaNew);

	registraContratto(request, c2);
	
	return c2;	
}
 public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
	esegui(request, response);
}
public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
	esegui(request, response);
}
private void esegui(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
	BmaSessione sessione = null;
	try {
		// Imposta Configurazione
		if (appConfig==null) setAppConfiguration(request);

		// Recupera sessione
		String id = request.getSession().getId();
		sessione = (BmaSessione)listaSessioni.getElement(id);
		if (sessione==null || sessione.isScaduta()) sessione = nuovaSessione(request);

		// Recupera il contratto corrente
		BmaContratto contratto = leggiContratto(request, sessione);
		sessione.setBeanApplicativo("Contratto", contratto);
		sessione.setBeanApplicativo("Menu", getBeanMenu());

		// Determina la pagina da aprire e gli aggiornamenti da fare
		String pagina = "";
		String cmd = request.getParameter("cmd");
		if (cmd==null || cmd.trim().length()==0) cmd = "info";
		String act = request.getParameter("act");
		if (act==null || act.trim().length()==0) act = "open";
		
		if (cmd.equals("key")) pagina = "cpChiave.jsp";
		else if (cmd.equals("info")) {
			pagina = "cpInfo.jsp";
			if (act.equals("update")) aggiornaInfo(contratto, request);
		}
		else if (cmd.equals("infoContraente")) {
			pagina = "cpInfoContraente.jsp";
			if (act.equals("update")) aggiornaInfoContraente(contratto, request);
		}
		else if (cmd.equals("infoSostituita")) {
			pagina = "cpInfoSostituita.jsp";
			if (act.equals("update")) aggiornaInfoSostituita(contratto, request);
		}
		else if (cmd.equals("infoVincolo")) {
			pagina = "cpInfoVincolo.jsp";
			if (act.equals("update")) aggiornaInfoVincolo(contratto, request);
		}
		else if (cmd.equals("infoPrimaRata")) {
			pagina = "cpInfoPrimaRata.jsp";
			if (act.equals("update")) aggiornaInfoPrimaRata(contratto, request);
		}
		else if (cmd.equals("infoRateSuccessive")) {
			pagina = "cpInfoRateSuccessive.jsp";
			if (act.equals("update")) aggiornaInfoRateSuccessive(contratto, request);
		}
		else if (cmd.equals("infoProvvigioni")) {
			pagina = "cpInfoProvvigioni.jsp";
			if (act.equals("update")) aggiornaInfoProvvigioni(contratto, request);
		}
		else if (cmd.equals("infoPremiProtLista")) {
			pagina = "cpPremiProtLista.jsp";
		}
		else if (cmd.equals("infoPremiProtEdit")) {
			pagina = "cpPremiProtEdit.jsp";
			String ramo = request.getParameter("key");
			if (ramo==null || ramo.trim().length()==0) {
				pagina = "cpPremiProtLista.jsp";
			}
			else {
				request.setAttribute("key", ramo);
				if (act.equals("update")) aggiornaInfoPremiProt(contratto, request);
			}
		}
		else if (cmd.equals("infoPremiBilLista")) {
			pagina = "cpPremiBilLista.jsp";
		}
		else if (cmd.equals("infoPremiBilEdit")) {
			pagina = "cpPremiBilEdit.jsp";
			String ramo = request.getParameter("key");
			if (ramo==null || ramo.trim().length()==0) {
				pagina = "cpPremiBilLista.jsp";
			}
			else {
				request.setAttribute("key", ramo);
				if (act.equals("update")) aggiornaInfoPremiBil(contratto, request);
			}
		}
		else if (cmd.equals("attivitaLista")) {
			pagina = "cpAttivitaLista.jsp";
		}
		else if (cmd.equals("attivitaEditCaratteristiche")) {
			pagina = "cpAttivitaEditCaratteristiche.jsp";
			BmaOggettoAssicurato oggetto = getAttivita(request, contratto);
			if (oggetto==null) pagina = "cpAttivitaLista.jsp";
			else {
				if (act.equals("update")) aggiornaCaratteristicheOggetto(oggetto.getCaratteristiche(), request);
			}
		}
		else if (cmd.equals("attivitaEditGaranzie")) {
			pagina = "cpAttivitaEditGaranzie.jsp";
			BmaOggettoAssicurato oggetto = getAttivita(request, contratto);
			if (oggetto==null) pagina = "cpAttivitaLista.jsp";
			else {
				if (act.equals("update")) aggiornaGaranzieOggetto(oggetto.getGaranzie(), request);
			}
		}
		else if (cmd.equals("beniLista")) {
			pagina = "cpBeniLista.jsp";
		}
		else if (cmd.equals("beniEditCaratteristiche")) {
			pagina = "cpBeniEditCaratteristiche.jsp";
			BmaOggettoAssicurato oggetto = getBene(request, contratto);
			if (oggetto==null) pagina = "cpBeniLista.jsp";
			else {
				if (act.equals("update")) aggiornaCaratteristicheOggetto(oggetto.getCaratteristiche(), request);
			}
		}
		else if (cmd.equals("beniEditGaranzie")) {
			pagina = "cpBeniEditGaranzie.jsp";
			BmaOggettoAssicurato oggetto = getBene(request, contratto);
			if (oggetto==null) pagina = "cpBeniLista.jsp";
			else {
				if (act.equals("update")) aggiornaGaranzieOggetto(oggetto.getGaranzie(), request);
			}
		}
		else if (cmd.equals("personeLista")) {
			pagina = "cpPersoneLista.jsp";
		}
		else if (cmd.equals("personeEditCaratteristiche")) {
			pagina = "cpPersoneEditCaratteristiche.jsp";
			BmaOggettoAssicurato oggetto = getPersona(request, contratto);
			if (oggetto==null) pagina = "cpPersoneLista.jsp";
			else {
				if (act.equals("update")) aggiornaCaratteristicheOggetto(oggetto.getCaratteristiche(), request);
			}
		}
		else if (cmd.equals("personeEditGaranzie")) {
			pagina = "cpPersoneEditGaranzie.jsp";
			BmaOggettoAssicurato oggetto = getPersona(request, contratto);
			if (oggetto==null) pagina = "cpPersoneLista.jsp";
			else {
				if (act.equals("update")) aggiornaGaranzieOggetto(oggetto.getGaranzie(), request);
			}
		}
		else if (cmd.equals("salva")) {
			pagina = "cpInfo.jsp";
			registraContratto(request, contratto);
			sessione.getBeansApplicativi().remove("Contratto");
			contratto = leggiContratto(request, sessione);
			sessione.setBeanApplicativo("Contratto", contratto);
		}
		else if (cmd.equals("copia")) {
			pagina = "cpCopia.jsp";
			if (act.equals("update")) {
				BmaContratto c2 = copiaContratto(request, contratto);
				pagina = "cpInfo.jsp";
				sessione.getBeansApplicativi().remove("Contratto");
				contratto = c2;
				sessione.setBeanApplicativo("Contratto", contratto);
			}
		}
		
		request.setAttribute("Sessione", sessione);
		vaiPaginaJSP(pagina, request, response);


	}
	catch (BmaException e) {
		BmaErrore err = e.getErrore();
		response.getWriter().println(e.getMessage() + "<br>" + e.getInfo() + "<br>" + e.getInfoEstese());
//		scriviLog(err, sessione, request);
//		vaiPaginaErr(err, request, response);
	}
}
private BmaOggettoAssicurato getAttivita(HttpServletRequest request, BmaContratto contratto) {
	String numOggetto = request.getParameter("key");
	BmaOggettoAssicurato oggetto = (BmaOggettoAssicurato)contratto.getAttivita().getElement(numOggetto);
	
	if (oggetto==null) oggetto = new BmaOggettoAssicurato();
	request.setAttribute("oggetto", oggetto);
	return oggetto;
}
private BmaVector getBeanMenu() {
	BmaVector menu = new BmaVector("Menu");
	menu.add(new BmaParametro("key","Cambia Polizza"));
	menu.add(new BmaParametro("copia","Copia Contratto"));
	menu.add(new BmaParametro("info","Dati Generali"));
	menu.add(new BmaParametro("infoContraente","Dati Contraente"));
	menu.add(new BmaParametro("infoSostituita","Dati Sostituita"));
	menu.add(new BmaParametro("infoVincolo","Dati Vincolo"));
	menu.add(new BmaParametro("infoPrimaRata","Dati Prima Rata"));
	menu.add(new BmaParametro("infoRateSuccessive","Dati Rate Successive"));
	menu.add(new BmaParametro("infoProvvigioni","Dati Provvigioni"));
	menu.add(new BmaParametro("infoPremiProtLista","Premi per Ramo di Protocollo"));
	menu.add(new BmaParametro("infoPremiBilLista","Premi per Ramo di Bilancio"));
	menu.add(new BmaParametro("attivitaLista","Attività Assicurate"));
	menu.add(new BmaParametro("beniLista","Beni Assicurati"));
	menu.add(new BmaParametro("personeLista","Persone Assicurate"));
	menu.add(new BmaParametro("deroga","Deroghe"));
	menu.add(new BmaParametro("salva","Registra Contratto"));
	return menu;
}
private BmaOggettoAssicurato getBene(HttpServletRequest request, BmaContratto contratto) {
	String numOggetto = request.getParameter("key");
	BmaOggettoAssicurato oggetto = (BmaOggettoAssicurato)contratto.getBeni().getElement(numOggetto);
	
	if (oggetto==null) oggetto = new BmaOggettoAssicurato();
	request.setAttribute("oggetto", oggetto);
	return oggetto;
}
private BmaOggettoAssicurato getPersona(HttpServletRequest request, BmaContratto contratto) {
	String numOggetto = request.getParameter("key");
	BmaOggettoAssicurato oggetto = (BmaOggettoAssicurato)contratto.getPersone().getElement(numOggetto);
	
	if (oggetto==null) oggetto = new BmaOggettoAssicurato();
	request.setAttribute("oggetto", oggetto);
	return oggetto;
}
private BmaContratto leggiContratto(HttpServletRequest request, BmaSessione sessione) throws BmaException {

	String codSocieta = request.getParameter("codSocieta");
	String codAgenzia = request.getParameter("codAgenzia");
	while (codAgenzia.length()<6) {
		codAgenzia = "0" + codAgenzia;
	}
	String codProdotto = request.getParameter("codProdotto");
	String numPolizza = request.getParameter("numPolizza");
	while (numPolizza.length()<6) {
		numPolizza = "0" + numPolizza;
	}
	BmaContratto contratto = (BmaContratto)sessione.getBeanApplicativo("Contratto");
	if (contratto != null) {
		if (contratto.getCodSocieta().equals(codSocieta) &&
				contratto.getCodAgenzia().equals(codAgenzia) &&
				contratto.getCodProdotto().equals(codProdotto) &&
				contratto.getNumPolizza().equals(numPolizza)) {
					return contratto;
		}
	}
		
	// Verifica concorrenza sessioni	
	contratto = new BmaContratto();
	contratto.setCodSocieta(codSocieta);
	contratto.setCodAgenzia(codAgenzia);
	contratto.setCodProdotto(codProdotto);
	contratto.setNumPolizza(numPolizza);
	Enumeration e = listaSessioni.elements();
	while (e.hasMoreElements()) {
		BmaSessione sessione2 = (BmaSessione) e.nextElement();
		if (!sessione2.getChiave().equals(sessione.getChiave())) {
			BmaContratto contratto2 = (BmaContratto)sessione2.getBeanApplicativo("Contratto");
			if (contratto2!=null && contratto2.getChiave().equals(contratto.getChiave())) {
				throw new BmaException(
						contratto.BMA_ERR_WEB_PARAMETRO,
						"Contratto in uso",
						"Il contratto è in edizione da altro utente",
						contratto2);
			}
		}
	}

	// Richiama servizio di lettura
	BmaInputServizio is = new BmaInputServizio();
	if (codSocieta==null) codSocieta = "057";
	is.setSocieta(codSocieta);
	//
	is.setInfoServizio("codProcedura", codProdotto);
	//
	String datRicezione = request.getParameter("datRicezione");
	if (datRicezione==null) datRicezione = DATA_RICEZIONE;
	is.setInfoServizio("datRicezione", datRicezione);
	//
	is.setInfoServizio("codTipoMittente", "AG");
	//
	is.setInfoServizio("codMittente", codAgenzia);
	//
	is.setInfoServizio("numPolizza", numPolizza);
	//
	is.setSessione(request.getSession().getId());
	is.setTimeStamp(Long.toString(new Date().getTime()));
	is.setApplicazione("bma");
	is.setCanale("INTERNET");
	is.setOperatore("SYSTEM");
	is.setIdLocale(request.getRemoteAddr());
	is.setIdServizio("MessaggiEntrata");
	String param = "?xml=" + is.urlEncode(is.toXml());
	BmaOutputServizio os = new BmaOutputServizio();
	
	try {
		java.net.URL url = new java.net.URL(appConfig.getParametroApplicazione(appConfig.BMA_CFGCOD_URL_SERVIZI) + param);
		InputStream isXml = url.openStream();
		os.fromInputStream(isXml);
		isXml.close();
	} 
	catch (java.net.MalformedURLException mue) {
		throw new BmaException(is.BMA_ERR_WEB_PARAMETRO, "MalformedURLException", mue.getMessage(),null);
	}
	catch (java.io.IOException ioe) {
		throw new BmaException(is.BMA_ERR_WEB_PARAMETRO, "IOException", ioe.getMessage(),null);
	}

	
	// Genera e valorizza il contratto letto 
	contratto = (BmaContratto)is.objectInstance("it.bma.ptf.BmaContratto" + codProdotto);
	contratto.fromXml(os.getXmlOutput());
	if (contratto.getCodSocieta().trim().length()==0) contratto.setCodSocieta(codSocieta);
	if (contratto.getCodAgenzia().trim().length()==0)contratto.setCodAgenzia(codAgenzia);
	if (contratto.getCodProdotto().trim().length()==0)contratto.setCodProdotto(codProdotto);
	if (contratto.getNumPolizza().trim().length()==0)contratto.setNumPolizza(numPolizza);
	return contratto;	
}
private BmaSessione nuovaSessione(HttpServletRequest request) {
	String id = request.getSession().getId();
	BmaSessione sessione = new BmaSessione(id);
	sessione.setIndirizzoIp(request.getRemoteAddr());
	sessione.setOraInizio(new Date().getTime());
	sessione.setUserConfig(appConfig);
	sessione.setUtente(new BmaUtente());

	// Ripulisce la lista sessioni
	Vector v = new Vector();
	Enumeration e = listaSessioni.elements();
	while (e.hasMoreElements()) {
		BmaSessione old = (BmaSessione) e.nextElement();
		if (old.isScaduta() || !old.isValida()) v.add(old.getChiave());
	}
	for (int i = 0; i < v.size(); i++){
		String k = (String)v.elementAt(i);
		listaSessioni.remove(k);
	}

	// Aggiorna lista sessioni
	listaSessioni.add(sessione);
	return sessione;
}
private void registraContratto(HttpServletRequest request, BmaContratto contratto) throws BmaException {

	String datRicezione = request.getParameter("datRicezione");
	if (datRicezione==null) datRicezione = DATA_RICEZIONE;
	// Richiama servizio di scrittura
	BmaInputServizio is = new BmaInputServizio();
	is.setSocieta(contratto.getCodSocieta());
	//
	is.setInfoServizio("codProcedura", contratto.getCodProdotto());
	//
	is.setInfoServizio("datRicezione", datRicezione);
	//
	is.setInfoServizio("codTipoMittente", "AG");
	//
	is.setInfoServizio("codMittente", contratto.getCodAgenzia());
	//
	is.setInfoServizio("numPolizza", contratto.getNumPolizza());

	BmaVector records = contratto.saveGammaRecords().getBmaVector();
	records.setName("Records");
	String tmp = records.urlEncode(records.toXml());
//	is.setInfoServizio("Records", tmp);
	String idFile = request.getSession().getId() + ".xml";
	records.toXmlFile(appConfig.getConfigPath() + idFile);
//
	if (records.getSize()>0) {
		String idFile2 = request.getSession().getId() + ".txt";
		appConfig.writeFile(records.getString(0), appConfig.getConfigPath()+idFile2, false);
		for (int i = 1; i < records.getSize(); i++){
			appConfig.writeFile(records.getString(i), appConfig.getConfigPath()+idFile2, true);
		}
	}
	is.setInfoServizio("idFile", idFile);	
	
	//
	is.setSessione(request.getSession().getId());
	is.setTimeStamp(Long.toString(new Date().getTime()));
	is.setApplicazione("bma");
	is.setCanale("INTERNET");
	is.setOperatore("SYSTEM");
	is.setIdLocale(request.getRemoteAddr());
	is.setIdServizio("MessaggiUpdate");
	String param = "?xml=" + is.urlEncode(is.toXml());
	BmaOutputServizio os = new BmaOutputServizio();
	try {
		java.net.URL url = new java.net.URL(appConfig.getParametroApplicazione(appConfig.BMA_CFGCOD_URL_SERVIZI) + param);
/*
		java.net.URLConnection uc = url.openConnection();
		Object cnt = uc.getContent();
		String cntType = uc.getContentType();
		InputStream isXml = uc.getInputStream();
*/
		InputStream isXml = url.openStream();
		os.fromInputStream(isXml);
		isXml.close();
	} 
	catch (java.net.MalformedURLException mue) {
		throw new BmaException(is.BMA_ERR_WEB_PARAMETRO, "MalformedURLException", mue.getMessage(),null);
	}
	catch (java.io.IOException ioe) {
		throw new BmaException(is.BMA_ERR_WEB_PARAMETRO, "IOException", ioe.getMessage(), is);
	}
	if (os.isErrore()) throw new BmaException(os.BMA_ERR_XML_GENERICO,os.getInfoErrore(),os.getMessaggioErrore(),contratto);
}
private void setAppConfiguration(HttpServletRequest request) throws BmaException {
	if (appConfig==null || !appConfig.isCompleta()) {
	  appConfig = new BmaUserConfig();
		appConfig.setServerName(request.getServerName());
		appConfig.setServerPort(Integer.toString(request.getServerPort()));
		appConfig.setProtocol(request.getProtocol());
 		String path = getServletContext().getInitParameter("app.root");
		String file = getServletContext().getInitParameter("app.config");
		appConfig.carica(path, file, getServletContext().getInitParameter("app.errors"));
	}
}
private void vaiPaginaJSP(String pagina, HttpServletRequest req, HttpServletResponse res) {

	req.setAttribute("BmaJsp", bJsp);
	
	String jspPage = appConfig.getWebPath() + pagina;
	if (!jspPage.substring(0,1).equals("/")) jspPage = "/" + jspPage;
	try {
		getServletContext().getRequestDispatcher(jspPage).forward(req, res);
	}
	catch (Exception e) {
		System.out.println(e.getMessage());
	}
}
}
