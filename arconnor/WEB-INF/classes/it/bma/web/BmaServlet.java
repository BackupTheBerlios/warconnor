package it.bma.web;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import it.bma.comuni.*;

public class BmaServlet extends HttpServlet implements it.bma.comuni.BmaLiterals, it.bma.web.BmaJspLiterals {
	private BmaUserConfig appConfig;
	private BmaHashtable listaSessioni = new BmaHashtable("Sessioni");
	
	public BmaServlet() {
		super();
	}
	 public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
		esegui(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
		esegui(request, response);
	}
	private void amministra(BmaSessione sessione, HttpServletRequest request, HttpServletResponse response) throws BmaException {
		sessione.setBeanApplicativo(BMA_JSP_BEAN_LISTA, listaSessioni);
		String codFunzione = request.getParameter(BMA_JSP_CAMPO_FUNZIONE);
		if (codFunzione!= null && codFunzione.trim().length()>0) {
			if (request.getParameter("reload")!=null) {
				setAppConfiguration(request, true);
				sessione.setUserConfig(appConfig);
				listaSessioni.clear();
				listaSessioni.add(sessione);
			}
			else if (request.getParameter("remove")!=null) {
				String[] ids = request.getParameterValues("idSessione");
				for (int i=0;i<ids.length;i++) {
					listaSessioni.remove(ids[i]);
				}
			}
			else if (request.getParameter("removeAll")!=null) {
				listaSessioni.clear();
				vaiPaginaJSP(appConfig.getUrlUscita(), request, response);
			}
		}
	}
	private void eseguiFunzione(BmaSessione sessione, HttpServletRequest request, HttpServletResponse response) throws BmaException {
		/* Inizializza i principali parametri di innesco della funzione */
		String codFunzione = "";
		String codAzione = BMA_JSP_AZIONE_DETTAGLIO;
		String codComando = BMA_JSP_COMANDO_PREPARA;
		String codSelezione = "";
		
		/* Verifica se è la prima attivazione */
		if (sessione.getListaFunzioni().getSize()==0) {
			/* E' la prima attivazione: determina funzione Home */
			BmaFunzione f = sessione.getFunzioneHome();
			if (f==null) {
				throw new BmaException(appConfig.BMA_ERR_WEB_PARAMETRO, "Funzione Home non definita", "",  appConfig);
			}
			codFunzione = f.getCodFunzione();
			codAzione = BMA_JSP_AZIONE_HOME;
		}
		else {
			/* Non è la prima attivazione: prende valori da request */
			codFunzione = request.getParameter(BMA_JSP_CAMPO_FUNZIONE);
			codAzione = request.getParameter(BMA_JSP_CAMPO_AZIONE);
			codComando = request.getParameter(BMA_JSP_CAMPO_COMANDO);
			if (codFunzione==null || codFunzione.trim().length()==0) {
				/* Potrebbe essere la riattivazione da errore o da login */
				/* ... quindi riattiva l'ultima funzione per l'azione di default */
				codFunzione = sessione.getFunzioneCorrente().getCodFunzione();
				codAzione = sessione.getFunzioneCorrente().getCodAzione();
				codComando = BMA_JSP_COMANDO_RIPRISTINA;
			}
			if (codComando==null || codComando.trim().length()==0) codComando = BMA_JSP_COMANDO_PREPARA;
			codSelezione = request.getParameter(BMA_JSP_CAMPO_SELEZIONE);
		}
		
		/* Instanzia o recupera la funzione esecutiva */
		BmaFunzioneAttiva funzione;
		if (codComando.equals(BMA_JSP_COMANDO_PRECEDENTE)) {
			funzione = (BmaFunzioneAttiva)sessione.getFunzioneCorrente();
			codFunzione = funzione.getCodFunzione();
			codAzione = funzione.getCodAzione();
			codComando = BMA_JSP_COMANDO_RIPRISTINA;
		}
		else {
			funzione =  sessione.getFunzione(codFunzione, codAzione);
			funzione.sessione = sessione;
		}
		
		/* Trasferisce alla funzione i dati della request */
		if (codComando.equals(BMA_JSP_COMANDO_PREPARA)) {
			funzione.getDatiIput().clear();
			funzione.aggiornaParametri(request);
		}		
		else if (codComando.equals(BMA_JSP_COMANDO_RIPRISTINA)) {
			funzione.getDatiIput().clear();
			codComando = BMA_JSP_COMANDO_PREPARA;
		}
		else {
			funzione.aggiornaParametri(request);
			funzione.aggiornaDatiInput(request);
		}
		/* Esegue la funzione in un ciclo che risale la History di chiamata */
		boolean exitFunction = true;
		while (exitFunction && sessione.getListaFunzioni().getSize()>0) {
			exitFunction = funzione.eseguiComando();
			if (exitFunction) funzione = sessione.getFunzionePrecedente();
		}
		BmaObject xmlBean = sessione.getBeanTemporaneo(BMA_JSP_BEAN_XML);
		if (xmlBean!=null) {
			request.getSession().setAttribute(BMA_JSP_BEAN_XML, xmlBean);
		}
		request.setAttribute(BMA_JSP_BEAN_SESSIONE, sessione);
		BmaFunzione f = sessione.getUtente().getFunzione(funzione.getCodFunzione());
		vaiPaginaJSP(f.getRifLayout(), request, response);
	}
	private void esegui(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
		/* Rimuove sempre il beanXml */
		request.getSession().removeAttribute(BMA_JSP_BEAN_XML);
		BmaSessione sessione = null;
		try {
			setAppConfiguration(request, false);
			String id = request.getSession().getId();
			/* Se proviene da login:
			 * - Se è una nuova sessione verifica utente
			 * - Se c'è una sessione aperta per l'utente la riutilizza
			 * - Se c'è una sessione aperta per un altro utente la chiude e ne apre una nuova
			*/
			sessione = (BmaSessione)listaSessioni.getElement(id);
			String form = request.getParameter("formName");
			if (form!=null && form.equalsIgnoreCase("login")) {
				if (sessione!=null) {
					BmaUtente utente = sessione.getUtente();
					String user = request.getParameter(BMA_JSP_USER);
					if (user!=null && user.trim().length()>0 && !utente.getCodUtente().equals(user)) {
						listaSessioni.remove(id);
						sessione = nuovaSessione(request);
					}
				}
				else {
					sessione = nuovaSessione(request);
				}
			}
			else {
				if (sessione==null) response.sendRedirect("index.html");
			}
			//
			sessione.aggiorna();
			if (sessione.isScaduta() || !sessione.isValida()) {
				listaSessioni.remove(sessione.getChiave());
				throw new BmaException(appConfig.BMA_ERR_WEB_PARAMETRO, "Sessione Scaduta", "Sessione Scaduta", appConfig);
			}
			// Attivazione ambiente di amministrazione
			if (sessione.getUtente().getCodApplicazione().equalsIgnoreCase(BMA_JSP_APP_ADMIN)) {
				amministra(sessione, request, response);
				request.setAttribute(BMA_JSP_BEAN_SESSIONE, sessione);
				vaiPaginaJSP(BMA_JSP_PAGINA_ADMIN, request, response);
			}
			// Attivazione applicazione DBA
			else if (sessione.getUtente().getCodApplicazione().equalsIgnoreCase(BMA_JSP_APP_DBA)) {
				eseguiFunzione(sessione, request, response);
			}
			// Attivazione applicazione MDA
			else if (sessione.getUtente().getCodApplicazione().equalsIgnoreCase(BMA_JSP_APP_MDA)) {
				eseguiFunzione(sessione, request, response);
			}
			// Attivazione applicazione PDAS
			else if (sessione.getUtente().getCodApplicazione().equalsIgnoreCase(BMA_JSP_APP_PDAS)) {
				eseguiFunzione(sessione, request, response);
			}
			else {
				throw new BmaException(appConfig.BMA_ERR_WEB_PARAMETRO, "Applicazione non attiva", "Applicazione non attiva", appConfig);
			}
		}
		catch (BmaException e) {
			BmaErrore err = e.getErrore();
			vaiPaginaErrore(err, request, response);
		}
	}
	private void setAppConfiguration(HttpServletRequest request, boolean reload) throws BmaException {
		if (appConfig==null || !appConfig.isCompleta() || reload) {
			appConfig = new BmaUserConfig();
			appConfig.setServerName(request.getServerName());
			appConfig.setServerPort(Integer.toString(request.getServerPort()));
			appConfig.setProtocol(request.getProtocol());
			String path = request.getSession().getServletContext().getRealPath("") + BMA_JSP_CONF_PATH;
			appConfig.carica(path, BMA_JSP_CONF_FILE, path + BMA_JSP_ERRS_FILE);
		}
	}
	private void vaiPaginaErrore(BmaErrore err, HttpServletRequest request, HttpServletResponse response) {
		request.getSession().setAttribute(BMA_JSP_BEAN_ERRORE, err);
		String errPage = BMA_JSP_PAGINA_ERRORE;
		if (appConfig!=null) errPage = appConfig.getUrlErrore();
		vaiPaginaJSP(errPage, request, response);
	}
	private void vaiPaginaJSP(String pagina, HttpServletRequest request, HttpServletResponse response) {
		String jspPage = appConfig.getWebPath() + pagina;
		if (!jspPage.substring(0,1).equals("/")) jspPage = "/" + jspPage;
		try {
			getServletContext().getRequestDispatcher(jspPage).forward(request, response);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	private BmaSessione nuovaSessione(HttpServletRequest request) throws BmaException {
		String id = request.getSession().getId();
		BmaSessione sessione = new BmaSessione(id);
		sessione.setIndirizzoIp(request.getRemoteAddr());
		sessione.setOraInizio(new Date().getTime());
		sessione.setUserConfig(appConfig);
		BmaUtente utente = caricaUtente(request);
		sessione.setUtente(utente);
		listaSessioni.add(sessione);
		return sessione;
	}
	private BmaUtente caricaUtente(HttpServletRequest request) throws BmaException {
		String user = request.getParameter(BMA_JSP_USER);
		String pswd = request.getParameter(BMA_JSP_PSWD);
		if (user==null || user.trim().length()==0) {
			throw new BmaException(appConfig.BMA_ERR_WEB_PARAMETRO, "Accesso Protetto", "Utente inesistente", appConfig);
		}
		if (pswd==null || pswd.trim().length()==0) {
			throw new BmaException(appConfig.BMA_ERR_WEB_PARAMETRO, "Accesso Protetto", "Password inesistente", appConfig);
		}
		user = user.toUpperCase();
		pswd = pswd.toLowerCase();
		String nSource = appConfig.getFonteDefault();
		BmaJdbcSource jSource = appConfig.getJdbcSource(nSource);
		BmaJdbcTrx jTrx = new BmaJdbcTrx(jSource);
		try {
			jTrx.open("system");
			String sql = "";
			sql = "SELECT COD_SOCIETA, COD_APPLICAZIONE, COD_PROFILO, DES_UTENTE, COD_UNITA " +
						"	FROM	AR_UTENTI " +
						" WHERE	COD_UTENTE='" + user + "' " +
						" AND		DES_PASSWD='" + pswd + "'";
			Vector dati = jTrx.eseguiSqlSelect(sql);
			if (dati.size()!=1) {
				throw new BmaException(appConfig.BMA_ERR_WEB_PARAMETRO, "Accesso Protetto", "Utente inesistente o password errata", appConfig);
			}
			dati = (Vector)dati.elementAt(0);
			BmaUtente utente = new BmaUtente();
			utente.setCodUtente(user);
			utente.setCodSocieta(					(String)dati.elementAt(0));
			utente.setCodApplicazione(		(String)dati.elementAt(1));
			utente.setCodProfilo(					(String)dati.elementAt(2));
			utente.setDesUtente(					(String)dati.elementAt(3));
			utente.setCodUnita(						(String)dati.elementAt(4));
			//
			sql = "SELECT A.COD_FUNZIONE, B.DES_FUNZIONE, B.RIF_ROUTINE, A.DES_AZIONI, B.RIF_LAYOUT " +
						" FROM	AR_PROFILIFUNZIONI A, AR_FUNZIONI B " +
						" WHERE	A.COD_FUNZIONE = B.COD_FUNZIONE " +
						" AND		A.COD_SOCIETA='" + utente.getCodSocieta() + "' " +
						" AND		A.COD_APPLICAZIONE='" + utente.getCodApplicazione() + "' " +
						" AND		A.COD_PROFILO='" + utente.getCodProfilo() + "' " +
						" ORDER BY A.COD_FUNZIONE ";
			dati = jTrx.eseguiSqlSelect(sql);
			for (int i=0;i<dati.size();i++) {
				Vector riga = (Vector)dati.elementAt(i);
				BmaFunzione funzione = new BmaFunzione();
				funzione.setCodFunzione(	(String)riga.elementAt(0));
				funzione.setDesFunzione(	(String)riga.elementAt(1));
				funzione.setCodClasse(		(String)riga.elementAt(2));
				funzione.setRifLayout(		(String)riga.elementAt(4));
				String listaAzioni = (String)riga.elementAt(3);
				for (int j=0;j<listaAzioni.trim().length();j++) {
					String codAzione = listaAzioni.substring(j, j+1);
					if (j==0) funzione.setAzioneDefault(codAzione);
					BmaAzione azione = new BmaAzione();
					azione.setCodAzione(codAzione);
					if (codAzione.equals("L")) azione.setDesAzione("Lista");
					else if (codAzione.equals("D")) azione.setDesAzione("Dettaglio");
					else if (codAzione.equals("N")) azione.setDesAzione("Nuovo");
					else if (codAzione.equals("M")) azione.setDesAzione("Modifica");
					else if (codAzione.equals("E")) azione.setDesAzione("Elimina");
					azione.setVisibile(true);
					funzione.addAzione(azione);
				}
				utente.addFunzione(funzione);
			}
			jTrx.chiudi();
			return utente;
		}
		catch (BmaException bma) {
			if (jTrx.isAperta()) jTrx.invalida();
			throw bma;
		}
	}
}
