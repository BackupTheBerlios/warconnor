package it.bma.comuni;

import java.util.*;
public class ParametroConfigurazione extends BmaObject {
	private String tipo = "";
	private String nome = "";
	private String valore = "";
public ParametroConfigurazione() {
	super();
}
public String getChiave() {
  return getTipo() + new Character(BMA_KEY_SEPARATOR).toString() + getNome();
}
public java.lang.String getNome() {
	return nome;
}
/**
 * Insert the method's description here.
 * Creation date: (12/12/01 16.23.00)
 * @return java.lang.String
 */
public java.lang.String getTipo() {
	return tipo;
}
public java.lang.String getValore() {
	return valore;
}
protected java.lang.String getXmlTag() {
	return "ParametroConfigurazione";
}
public void setNome(java.lang.String newNome) {
	nome = newNome;
}
/**
 * Insert the method's description here.
 * Creation date: (12/12/01 16.23.00)
 * @param newTipo java.lang.String
 */
public void setTipo(java.lang.String newTipo) {
	tipo = newTipo;
}
public void setValore(java.lang.String newValore) {
	valore = newValore;
}
}
