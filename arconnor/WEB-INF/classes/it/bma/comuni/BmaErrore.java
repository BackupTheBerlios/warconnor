package it.bma.comuni;

public class BmaErrore extends BmaObject {
	private String codErrore = "";
	private String tipo = "";
	private String nomeOggetto = "";
	private String msgUtente = "";
	private String msgSistema = "";
	private String info = "";
	private String infoEstese = "";
public BmaErrore() {
	super();
}
public java.lang.String getChiave() {
	return codErrore;
}
public java.lang.String getCodErrore() {
	return codErrore;
}
public java.lang.String getInfo() {
	return info;
}
public java.lang.String getInfoEstese() {
	return infoEstese;
}
public java.lang.String getMsgSistema() {
	return msgSistema;
}
public java.lang.String getMsgUtente() {
	return msgUtente;
}
public String getNomeOggetto() {
	return nomeOggetto;
}
public java.lang.String getTipo() {
	return tipo;
}
protected java.lang.String getXmlTag() {
	return "Errore";
}
public void setCodErrore(java.lang.String newCodErrore) {
	codErrore = newCodErrore;
}
public void setInfo(java.lang.String newInfo) {
	info = newInfo;
}
public void setInfoEstese(java.lang.String newInfo) {
	infoEstese = newInfo;
}
public void setMsgSistema(java.lang.String msg) {
	msgSistema = msg;
}
public void setMsgUtente(java.lang.String msg) {
	msgUtente = msg;
}
public void setNomeOggetto(String newOggetto) {
	nomeOggetto = newOggetto;
}
public void setTipo(java.lang.String newTipo) {
	tipo = newTipo;
}
}
