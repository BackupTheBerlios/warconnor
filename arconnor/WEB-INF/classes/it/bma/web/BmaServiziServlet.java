package it.bma.web;

import java.io.*;
import java.util.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import it.bma.comuni.*;
import it.bma.servizi.*;

public class BmaServiziServlet extends javax.servlet.http.HttpServlet {
	private final String BMA_PACKAGE_SERVIZI = "it.bma.servizi.";
	private BmaUserConfig appConfig;
	private JdbcModel jModel = null;
/**
 * ServiziProfiloServlet constructor comment.
 */
public BmaServiziServlet() {
	super();
}
 public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
	esegui(request, response);
}
public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
	esegui(request, response);
}
private void esegui(HttpServletRequest req, HttpServletResponse res) throws ServletException, java.io.IOException {
	String input = req.getParameter(appConfig.BMA_XML_INPUTSERVIZI);
	try {
		setAppConfiguration(req);
		eseguiServizio(input, res);
	} 
	catch (BmaException e) {
		res.getWriter().println(e.getMessage() + "<br>" +
		e.getErrore().getInfo() + "<br>" + 
		e.getErrore().getInfoEstese());
	}
	catch (Throwable t) {
		// Imposta output di Errore
		BmaOutputServizio outputServizio = new BmaOutputServizio();
		outputServizio.setCodiceEsito("-1");
		outputServizio.setMessaggioErrore(t.getClass().getName());
		outputServizio.setInfoErrore(t.getMessage());
		outputServizio.setXmlOutput("");
		res.getWriter().println(outputServizio.getXmlTop("", outputServizio.toXml()));
	}
}
private void eseguiServizio(String input, HttpServletResponse res) throws IOException {
	BmaOutputServizio outputObject = new BmaOutputServizio();
	BmaInputServizio is = null;
	try {
		if (input == null || input.trim().length() == 0) {
			throw new BmaException(appConfig.BMA_ERR_WEB_PARAMETRO, "Richiesta di servizio priva di input");
		}
		is = new BmaInputServizio(input);
		String societa = is.getSocieta();
		if (societa == null || societa.trim().length() == 0) {
			throw new BmaException(appConfig.BMA_ERR_XML_GENERICO, "Richiesta di servizio priva di tag Societa");
		}

		
/*
		PfgConfigurazione userConfig = (PfgConfigurazione)istitutoProperties.get(di.getIstituto());
		if (userConfig==null) {
			userConfig = (PfgConfigurazione)appConfig.getConfigurazioneUtente();
			impostaParametriUtente(userConfig, di.getIstituto());
		}
*/		
		outputObject.setSessione(is.getSessione());
		outputObject.setTimeStamp(is.getTimeStamp());
		outputObject.setApplicazione(is.getApplicazione());
		String nomeClasse = "";

		
		BmaServizioInterno servizio = (BmaServizioInterno)is.getServizio(BMA_PACKAGE_SERVIZI + is.getIdServizio());
		servizio.setUserConfig(appConfig);
		servizio.setJModel(jModel);
		
		if (is.getIdServizio().equals("BmaSchedaPdf")) {
			res.setContentType("application/pdf");
			servizio.setOutputStream(res.getOutputStream());
		}
			
		servizio.init(input, outputObject);
		String esito = servizio.esegui();
		jModel = servizio.getJModel();
		if (esito!=null) {
			outputObject.setXmlOutput(esito);
			outputObject.setCodiceEsito("0");
			outputObject.setMessaggioErrore("");
			outputObject.setInfoErrore("");
			res.getWriter().println(outputObject.getXmlTop("", outputObject.toXml()));
		}

	}
	catch (BmaException e) {
		// Imposta output di Errore
		outputObject.setCodiceEsito("-1");
		outputObject.setMessaggioErrore(e.getErrore().getMsgUtente());
		outputObject.setInfoErrore(e.getErrore().toXml());
		outputObject.setXmlOutput("");
		res.getWriter().println(outputObject.getXmlTop("", outputObject.toXml()));
		tracciaErrore(is, e.getErrore());
	}
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
private void tracciaErrore(BmaInputServizio is, BmaErrore err) {
/*
	String idSessione = "***********************";
	String ip = "***.***.***.***";
	Utente utente = new Utente();
	utente.setUserID("*****");
	utente.setCodIstituto("*****");
	if (is!=null) {
		idSessione = is.getSessione();
		ip = is.getIdLocale();
		utente.setUserID(is.getUtenza());
		utente.setCodIstituto(is.getIstituto());
	}
	Sessione sessione = new Sessione(idSessione);
	sessione.setIndirizzoIp(ip);
	sessione.setUtente(utente);
	sessione.setUserConfig(appConfig);
	sessione.traceError(err);
*/
}
}
