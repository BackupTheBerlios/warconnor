package it.bma.comuni;

public class BmaParametro extends BmaObject {
	private String nome = "";
	private String valore = "";
public BmaParametro() {
	super();
}
public BmaParametro(String nome, String valore) {
	super();
	this.nome = nome;
	this.valore = valore;
}
public String getChiave() {
	return nome;
}
public java.lang.String getNome() {
	return nome;
}
public java.lang.String getValore() {
	return valore;
}
protected java.lang.String getXmlTag() {
	return "Parametro";
}
public void setNome(java.lang.String newNome) {
	nome = newNome;
}
public void setValore(java.lang.String newValore) {
	valore = newValore;
}
}
