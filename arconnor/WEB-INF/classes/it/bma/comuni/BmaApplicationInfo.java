package it.bma.comuni;

public class BmaApplicationInfo extends BmaObject {
	private String nome = "";
	private String descrizione = "";
	private String autore = "";
	private String versione = "";
	private String dataVersione = "";
public BmaApplicationInfo() {
	super();
}
public java.lang.String getAutore() {
	return autore;
}
public String getChiave() {
	return nome;
}
public java.lang.String getDataVersione() {
	return dataVersione;
}
public java.lang.String getDescrizione() {
	return descrizione;
}
public java.lang.String getNome() {
	return nome;
}
public java.lang.String getVersione() {
	return versione;
}
public void setAutore(java.lang.String newAutore) {
	autore = newAutore;
}
public void setDataVersione(java.lang.String newDataVersione) {
	dataVersione = newDataVersione;
}
public void setDescrizione(java.lang.String newDescrizione) {
	descrizione = newDescrizione;
}
public void setNome(java.lang.String newNome) {
	nome = newNome;
}
public void setVersione(java.lang.String newVersione) {
	versione = newVersione;
}

protected java.lang.String getXmlTag() {
	return "InfoApplicazione";
}
}
