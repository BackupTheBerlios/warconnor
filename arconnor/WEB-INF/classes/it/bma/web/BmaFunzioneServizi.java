package it.bma.web;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;

public abstract class BmaFunzioneServizi extends BmaFunzioneAttiva {
	public BmaFunzioneServizi() {
		super();
	}
	public abstract void impostaAzioni();
	public abstract BmaDataForm impostaModulo() throws BmaException;
	public abstract void completaInputServizio(BmaInputServizio is) throws BmaException;
	public abstract void gestisciOutputServizio(BmaOutputServizio os) throws BmaException;
	public abstract String getNomeServizio(String funzione);
	public boolean eseguiComando() throws BmaException {
		String codComando = getParametri().getString(BMA_JSP_CAMPO_COMANDO);
		if (codComando.trim().length()==0) codComando = BMA_JSP_COMANDO_PREPARA;
		
		if (codComando.equals(BMA_JSP_COMANDO_PREPARA)) {
			sessione.removeBeanApplicativo(BMA_JSP_BEAN_LISTA);
			impostaAzioni();
			BmaDataForm modulo = impostaModulo();
			sessione.setBeanApplicativo(BMA_JSP_BEAN_FORM, modulo);
		}
		else if (codComando.equals(BMA_JSP_COMANDO_AGGIORNA)) {
			BmaDataForm modulo = (BmaDataForm)sessione.getBeanApplicativo(BMA_JSP_BEAN_FORM);
			if (modulo==null) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Modulo Assente","",this);
			aggiornaModulo(modulo);
			BmaInputServizio is = new BmaInputServizio();
			impostaParametriServizio(modulo, is);
			completaInputServizio(is);
			String codServizio = getNomeServizio(getCodFunzione());
			BmaOutputServizio os = eseguiServizio(codServizio, is);
			gestisciOutputServizio(os);
		}
		return false;
	}
	protected void impostaParametriServizio(BmaDataForm modulo, BmaInputServizio is) {
		Hashtable valori = new Hashtable();
		mergeValoriModuloContesto(modulo, valori);
		Enumeration e = valori.keys();
		while (e.hasMoreElements()) {
			String k = (String)e.nextElement();
			String v = (String)valori.get(k);
			is.setInfoServizio(k, v);
		}			
	}
	protected BmaOutputServizio eseguiServizio(String codServizio, BmaInputServizio is) throws BmaException {
		is.setSessione(sessione.getChiave());
		is.setTimeStamp(Long.toString(new Date().getTime()));
		is.setCanale("INTERNET");
		is.setSocieta(sessione.getUtente().getCodSocieta());
		is.setApplicazione(sessione.getUtente().getCodApplicazione());
		is.setOperatore(sessione.getUtente().getCodUtente());
		is.setIdLocale(sessione.getIndirizzoIp());
		is.setIdServizio(codServizio);
		String classeServizio = BMA_JSP_PACKAGE_SERVIZI + codServizio;
		BmaServizioInterno servizio = (BmaServizioInterno)is.objectInstance(classeServizio);
		BmaOutputServizio os = servizio.esegui(sessione, is);
		return os;
	}
}
