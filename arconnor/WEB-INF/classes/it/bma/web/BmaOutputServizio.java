package it.bma.web;

import it.bma.comuni.*;
import java.io.*;
import java.util.*;

public class BmaOutputServizio extends BmaObject {
	private String sessione = "";
	private String timeStamp = "";
	private String applicazione = "";
	private String codiceEsito = "0";
	private String messaggioErrore = "";
	private String infoErrore = "";
	private String xmlOutput = "";
public BmaOutputServizio() {
	super();
}
public String bmaObjectsOutput(BmaHashtable lista) {
	Enumeration e = lista.elements();
	BmaHashtable output = new BmaHashtable("Output");
	while (e.hasMoreElements()) {
		BmaObject o = (BmaObject)e.nextElement();
		output.add(new BmaOggettoServizio(o));
	}
	return output.toXml();
}
public void fromInputStream(java.io.InputStream is) throws it.bma.comuni.BmaException {
	String xml = "";
	InputStreamReader isr = new InputStreamReader(is);
	BufferedReader br = new BufferedReader(isr);
	try {
		String s;
		while ((s = br.readLine()) !=null) {
			xml = xml + s;
		}
		fromXml(xml);
	}
	catch (java.io.IOException e) {
		throw new BmaException("Errore I/O", e.getMessage());
	}
}
public java.lang.String getApplicazione() {
	return applicazione;
}
public Hashtable getBmaObjects() throws BmaException {
	BmaHashtable lista = new BmaHashtable("Output");
	lista.fromXml(xmlOutput);
	Hashtable objects = new Hashtable();
	Enumeration e = lista.elements();
	while (e.hasMoreElements()) {
		BmaOggettoServizio os = (BmaOggettoServizio)e.nextElement();
		objects.put(os.nome, os.getBmaObject());
	}
	return objects;
}
public java.lang.String getChiave() {
	return "OutputServizio";
}
public java.lang.String getCodiceEsito() {
	return codiceEsito;
}
public java.lang.String getInfoErrore() {
	return infoErrore;
}
public java.lang.String getMessaggioErrore() {
	return messaggioErrore;
}
public java.lang.String getSessione() {
	return sessione;
}
public java.lang.String getTimeStamp() {
	return timeStamp;
}
public java.lang.String getXmlOutput() {
	return xmlOutput;
}
protected java.lang.String getXmlTag() {
	return "OutputServizio";
}
public boolean isErrore() {
	return (!codiceEsito.equals("0"));
}
public void setApplicazione(java.lang.String newApplicazione) {
	applicazione = newApplicazione;
}
public void setCodiceEsito(java.lang.String newCodiceEsito) {
	codiceEsito = newCodiceEsito;
}
public void setInfoErrore(java.lang.String newInfoErrore) {
	infoErrore = newInfoErrore;
}
public void setMessaggioErrore(java.lang.String newMessaggioErrore) {
	messaggioErrore = newMessaggioErrore;
}
public void setSessione(java.lang.String newSessione) {
	sessione = newSessione;
}
public void setTimeStamp(java.lang.String newTimeStamp) {
	timeStamp = newTimeStamp;
}
public void setXmlOutput(java.lang.String newXmlOutput) {
	xmlOutput = newXmlOutput;
}
}
