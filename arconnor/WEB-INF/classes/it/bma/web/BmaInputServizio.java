package it.bma.web;

import it.bma.comuni.*;
import java.util.*;

public class BmaInputServizio extends BmaObject {
	private String sessione = "";
	private String timeStamp = "";
	private String applicazione = "";
	private String societa = "";
	private String canale = "";
	private String operatore = "";
	private String idLocale = "";
	private String idServizio = "";
	private BmaHashtable infoServizio = new BmaHashtable("InfoServizio");
public BmaInputServizio() {
	super();
}
public BmaInputServizio(String xml) throws BmaException {
	super();
	fromXml(xml);
}
public void clearInfo() {
	infoServizio.clear();
}
public java.lang.String getApplicazione() {
	return applicazione;
}
public java.lang.String getCanale() {
	return canale;
}
public String getChiave() {
	return sessione + timeStamp;
}
public java.lang.String getIdLocale() {
	return idLocale;
}
public java.lang.String getIdServizio() {
	return idServizio;
}
public BmaHashtable getInfoServizio() {
	return infoServizio;
}
public java.lang.String getInfoServizio(String nome) {
	BmaParametro p = (BmaParametro) infoServizio.getElement(nome);
	if (p == null) {
		return "";
	}
	else {
		return p.getValore();
	}
}
public java.lang.String getSocieta() {
	return societa;
}
public java.lang.String getOperatore() {
	return operatore;
}
public java.lang.String getSessione() {
	return sessione;
}
public java.lang.String getTimeStamp() {
	return timeStamp;
}
public void setApplicazione(java.lang.String newApplicazione) {
	applicazione = newApplicazione;
}
public void setCanale(java.lang.String newCanale) {
	canale = newCanale;
}
public void setIdLocale(java.lang.String newIdLocale) {
	idLocale = newIdLocale;
}
public void setIdServizio(java.lang.String newIdServizio) {
	idServizio = newIdServizio;
}
public void setInfoServizio(String nome, String valore) {
	BmaParametro p = new BmaParametro();
	p.setNome(nome);
	p.setValore(valore);
	infoServizio.add(p);
}
public void setSocieta(java.lang.String newSocieta) {
	societa = newSocieta;
}
public void setOperatore(java.lang.String newOperatore) {
	operatore = newOperatore;
}
public void setSessione(java.lang.String newSessione) {
	sessione = newSessione;
}
public void setTimeStamp(java.lang.String newTimeStamp) {
	timeStamp = newTimeStamp;
}

public BmaObject getServizio(String nome) throws BmaException {
	return objectInstance(nome);
}

protected java.lang.String getXmlTag() {
	return "InputServizio";
}

public void setInfoServizio(BmaHashtable info) {
	infoServizio.setTable(info.getTable());
}
}
