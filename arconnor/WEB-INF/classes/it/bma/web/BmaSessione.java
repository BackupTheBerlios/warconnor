package it.bma.web;

import java.io.*;
import java.util.*;
import java.text.*;
import javax.servlet.http.*;
import it.bma.comuni.*;

public class BmaSessione extends it.bma.comuni.BmaObject implements BmaJspLiterals {
	private String chiave = "";
	private String indirizzoIp = "";
	private long oraInizio = 0;
	private long ultimoAccesso = 0;
	private String userRemoto = "";
	private BmaUserConfig userConfig;
	private BmaUtente utente; 
	private BmaHashtable aliasLabels = new BmaHashtable("AlialsLabels");
	private BmaHashtable beansTemporanei = new BmaHashtable("BeansTemporanei");
	private BmaHashtable beansApplicativi = new BmaHashtable("BeansApplicativi");
	private BmaVector funzioni = new BmaVector("Funzioni");
	public BmaSessione(String httpSessionId) {
		super();
		oraInizio = new Date().getTime();
		ultimoAccesso = oraInizio;
		chiave = httpSessionId;
	}
	public void aggiorna() {
		ultimoAccesso = new Date().getTime();
		beansTemporanei.clear();
	}
	public void fromXml(String xml) throws BmaException {
		throw new BmaException(BMA_ERR_XML_GENERICO, BMA_MSG_XML_NONPREVISTO, "", this);
	}
	public BmaHashtable getAliasLabels() {
		return aliasLabels;
	}
	public String getAlias(String id) {
		String s = aliasLabels.getString(id);
		if (s==null || s.trim().length()==0) s = id;
		return s;
	}
	public BmaObject getBeanApplicativo(String nome) {
		return (BmaObject)beansApplicativi.getElement(nome);
	}
	public BmaHashtable getBeansApplicativi() {
		return beansApplicativi;
	}
	public BmaHashtable getBeansTemporanei() {
		return beansTemporanei;
	}
	public BmaObject getBeanTemporaneo(String nome) {
		return (BmaObject)beansTemporanei.getElement(nome);
	}
	public String getChiave() {
		return chiave;
	}
	public String getDurata() {
		Date d = new Date();
		d.setTime(d.getTime() - oraInizio);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
		sdf.setTimeZone(new SimpleTimeZone(0, "GMT"));
		return sdf.format(d);
	}
	public BmaFunzioneAttiva getFunzione(String codFunzione, String codAzione) throws BmaException {
		BmaFunzioneAttiva funzione = null;
		BmaFunzioneAttiva corrente = getFunzioneCorrente();
		for (int i = 0; i < funzioni.getSize(); i++){
			funzione = (BmaFunzioneAttiva)funzioni.getElement(i);
			if (funzione.getCodFunzione().equals(codFunzione) &&
					funzione.getCodAzione().equals(codAzione)) {
				funzioni.setSize(i+1);
				return funzione;
			}
		}
		// Nuova istanza di funzione
		BmaFunzione fUtente = utente.getFunzione(codFunzione);
		codAzione = utente.verificaFunzione(codFunzione, codAzione);
		if (fUtente==null || codAzione.trim().length()==0) {
			throw new BmaException(BMA_ERR_WEB_PARAMETRO,"Funzione non abilitata",codFunzione + "- " + codAzione, this);
		}
		funzione = (BmaFunzioneAttiva)objectInstance(fUtente.getCodClasse());
		funzione.sessione = this;
		funzione.setCodFunzione(codFunzione);
		funzione.setCodAzione(codAzione);
		funzione.setDesFunzione(fUtente.getDesFunzione());
		funzione.setDesAzione(fUtente.getDesAzione(codAzione));
		funzione.initContesto(utente);
		if (corrente==null) {
			funzioni.add(funzione);
			return funzione;
		}
		BmaMenu menu = (BmaMenu)corrente.getAzioniMenu().getElement(funzione.getChiave());
		if (menu==null) {
			throw new BmaException(BMA_ERR_WEB_PARAMETRO,"Funzione non prevista",codFunzione + "- " + codAzione, this);
		}
		funzione.setLivelloMenu(menu.getLivello());
		if (funzione.getLivelloMenu()==corrente.getLivelloMenu()) {
			if (funzione.getLivelloMenu()>funzioni.getSize()-1) {
				throw new BmaException(BMA_ERR_WEB_PARAMETRO,"Livello Menu Errato",codFunzione + "- " + codAzione, this);
			}
			funzioni.getVector().setElementAt(funzione, funzione.getLivelloMenu());
		}	
		else if (funzione.getLivelloMenu()>corrente.getLivelloMenu()) {
			if (funzione.getLivelloMenu()>funzioni.getSize()) {
				throw new BmaException(BMA_ERR_WEB_PARAMETRO,"Livello Menu Errato",codFunzione + "- " + codAzione, this);
			}
			funzioni.add(funzione);
		}	
		else {
			funzioni.setSize(funzione.getLivelloMenu());
			funzioni.add(funzione);
		}
		return funzione;
	}
	public BmaFunzione getFunzioneHome() {
		Enumeration e = utente.getListaFunzioni().keys();
		while (e.hasMoreElements()) {
			String k = (String)e.nextElement();
			BmaFunzione f = (BmaFunzione)utente.getListaFunzioni().getElement(k);
			if (f.hasAzione(BMA_JSP_AZIONE_HOME)) return f;
		}
		return null;
	}
	public BmaFunzioneAttiva getFunzioneAttivante() {
		BmaFunzioneAttiva funzione = null;
		if (funzioni.getSize()>1) funzione = (BmaFunzioneAttiva)funzioni.getElement(funzioni.getSize()-2);
		return funzione;
	}
	public BmaFunzioneAttiva getFunzioneCorrente() {
		BmaFunzioneAttiva funzione = null;
		if (funzioni.getSize()>0) funzione = (BmaFunzioneAttiva)funzioni.getElement(funzioni.getSize()-1);
		return funzione;
	}
	public BmaFunzioneAttiva getFunzionePrecedente() throws BmaException {
		if (funzioni.getSize()>0) funzioni.setSize(funzioni.getSize()-1);
		return getFunzioneCorrente();
	}
	public String getInattiva() {
		Date d = new Date();
		d.setTime(d.getTime() - ultimoAccesso);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
		sdf.setTimeZone(new SimpleTimeZone(0, "GMT"));
		return sdf.format(d);
	}
	public java.lang.String getIndirizzoIp() {
		return indirizzoIp;
	}
	public String getInizio() {
		Date d = new Date();
		d.setTime(oraInizio);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return sdf.format(d);
	}
	public BmaVector getListaFunzioni() {
		return funzioni;
	}
	public int getMinutiTimeout() {
		return Integer.parseInt(userConfig.getMinutiTimeout());
	}
	public int getMinutiValidita() {
		return Integer.parseInt(userConfig.getMinutiValidita());
	}
	public long getOraInizio() {
		return oraInizio;
	}
	public long getUltimoAccesso() {
		return ultimoAccesso;
	}
	public BmaUserConfig getUserConfig() {
		return userConfig;
	}
	public java.lang.String getUserRemoto() {
		return userRemoto;
	}
	public BmaUtente getUtente() {
		return utente;
	}
	protected java.lang.String getXmlTag() {
		return "Sessione";
	}
	public boolean isScaduta() {
		long i = oraInizio + (getMinutiTimeout() * 60000);
		return (new Date().getTime() > i);
	}
	public boolean isValida() {
		long i = ultimoAccesso + (getMinutiValidita() * 60000);
		return (new Date().getTime() <= i);
	}
	public void removeBeanApplicativo(String nome) {
		beansApplicativi.remove(nome);
	}
	public void setBeanApplicativo(String nome, BmaObject newBean) {
		beansApplicativi.setElement(nome, newBean);
	}
	public void setBeansApplicativi(BmaHashtable newBeansApplicativi) {
		beansApplicativi = newBeansApplicativi;
	}
	public void setBeansTemporanei(BmaHashtable newBeansTemporanei) {
		beansTemporanei = newBeansTemporanei;
	}
	public void setBeanTemporaneo(String nome, BmaObject newBean) {
		beansTemporanei.setElement(nome, newBean);
	}
	public void setFunzioni(BmaVector newFunzioni) {
		funzioni = newFunzioni;
	}
	public void setIndirizzoIp(java.lang.String newIndirizzoIp) {
		indirizzoIp = newIndirizzoIp;
	}
	public void setOraInizio(long newOraInizio) {
		oraInizio = newOraInizio;
	}
	public void setUltimoAccesso(long newUltimoAccesso) {
		ultimoAccesso = newUltimoAccesso;
	}
	public void setUserConfig(BmaUserConfig newConfig) {
		userConfig = newConfig;
	}
	public void setUserRemoto(java.lang.String newUserRemoto) {
		userRemoto = newUserRemoto;
	}
	public void setUtente(BmaUtente newUtente) {
		utente = newUtente;
	}
}
