package it.bma.web;

import java.util.*;
import it.bma.comuni.*;
public class BmaMenu extends it.bma.comuni.BmaObject implements it.bma.web.BmaJspLiterals {
	private String label = "";
	private String funzione = "";
	private String azione = "";
	private String comando = "";
	private int livello = 0;
	private String tipo = "";
	private boolean attivo = true;
public BmaMenu() {
	super();
}
public BmaMenu(int l, BmaFunzione f, String t) {
	super();
	livello = l;
	tipo = t;
	label = f.getDesFunzione();
	funzione = f.getCodFunzione();
	azione = f.getAzioneDefault();
	comando = BMA_JSP_COMANDO_PREPARA;
	attivo = true;
}
public BmaMenu(int l, BmaFunzione f, String a, String t) {
	super();
	livello = l;
	tipo = t;
	label = f.getDesFunzione();
	funzione = f.getCodFunzione();
	azione = a;
	comando = BMA_JSP_COMANDO_PREPARA;
	attivo = true;
}
public java.lang.String getAzione() {
	return azione;
}
public String getChiave() {
	return funzione + BMA_KEY_SEPARATOR + azione;
}
public java.lang.String getComando() {
	return comando;
}
public java.lang.String getFunzione() {
	return funzione;
}
public java.lang.String getLabel() {
	return label;
}
public String getLink(String style) {
	if (!attivo) return label;
	String p1 = '\"' + funzione + '\"';
	String p2 = '\"' + azione + '\"';
	String p3 = '\"' + comando + '\"';
	String p4 = '\"' + "" + '\"';
	String link = "<a";
	if (style.trim().length()>0) link = link + " class=" + '\"' + style + '\"';
	link = link + " href='javascript:invia(" +
								p1 + "," + p2 + "," + p3 + "," + p4 + ")'>" +
								label + 
								"</a>";
	return link;
}
public int getLivello() {
	return livello;
}
public java.lang.String getTipo() {
	return tipo;
}
protected String getXmlTag() {
	return "ElementoMenu";
}
public boolean isAttivo() {
	return attivo;
}
public void setAttivo(boolean newAttivo) {
	attivo = newAttivo;
}
public void setAzione(java.lang.String newAzione) {
	azione = newAzione;
}
public void setComando(java.lang.String newComando) {
	comando = newComando;
}
public void setFunzione(java.lang.String newFunzione) {
	funzione = newFunzione;
}
public void setLabel(java.lang.String newLabel) {
	label = newLabel;
}
public void setLivello(int newLivello) {
	livello = newLivello;
}
public void setTipo(java.lang.String newTipo) {
	tipo = newTipo;
}
}
