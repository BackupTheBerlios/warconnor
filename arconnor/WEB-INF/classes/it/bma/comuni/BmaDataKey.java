package it.bma.comuni;

import java.util.*;
public class BmaDataKey extends BmaVector {
	private final String BMA_QST_KEY_SEPARATOR = "&";
	private final String BMA_QST_VAL_SEPARATOR = "=";
public BmaDataKey() {
  super();
}
public BmaDataKey(String name) {
  super(name);
}
public String getKey() {
	String s = "";
	if (getSize() == 0) return "";
	for (int i = 0; i < getSize(); i++) {
		BmaDataColumn c = (BmaDataColumn)getElement(i);
		s = s + BMA_KEY_SEPARATOR + c.valore;
	}
	return s.substring(1);
}
public String getKey(Hashtable valori) {
  impostaValori(valori);
  return getKey();
}
public String getQueryString() {
	String s = "";
	for (int i = 0; i < getSize(); i++) {
		BmaDataColumn c = (BmaDataColumn)getElement(i);
		s = s + BMA_QST_KEY_SEPARATOR + c.nome + BMA_QST_VAL_SEPARATOR + c.valore;
	}
	if (s.length() > 1) return s.substring(1);
	else return "";
}
public String getQueryString(Hashtable valori) {
  impostaValori(valori);
  return getQueryString();
}
public String getValore(int pos) {
	BmaDataColumn c = (BmaDataColumn)getElement(pos);
  return c.valore;
}
public String getValore(String nome) {
  for (int i = 0; i < getSize(); i++) {
	  BmaDataColumn c = (BmaDataColumn)getElement(i);
	  if (c.nome.equals(nome)) return c.valore;
	}
	return "";
}
public Hashtable getValori() {
	Hashtable a = new Hashtable();
  for (int i = 0; i < getSize(); i++) {
	  BmaDataColumn c = (BmaDataColumn)getElement(i);
		a.put(c.nome, c.valore);
  }
  return a;
}
public void impostaDaChiave(String chiave) throws BmaException {
	int e = 0;
	for (int i = 0; i < getSize(); i++){
		BmaDataColumn c = (BmaDataColumn)getElement(i);
		int p = chiave.indexOf(BMA_KEY_SEPARATOR, e);
		if (p < 0)  {
			if (i==getSize()) c.valore = chiave.substring(e).trim();
			else throw new BmaException(BMA_ERR_SYS_INTERNO, "Chiave non valida per: " + c.nome, chiave, this);
		}
		else {
			c.valore = chiave.substring(e, p).trim();
			e = p + 1;
		}	
	}
}
public void impostaDaQueryString(String chiave) throws BmaException {
	for (int i = 0; i < getSize(); i++){
		BmaDataColumn c = (BmaDataColumn)getElement(i);
		int p = chiave.indexOf(c.nome);
		if (p < 0) throw new BmaException(BMA_ERR_SYS_INTERNO, "Query String non valida per: " + c.nome, chiave, this);
		p = p + c.nome.length();
		p = chiave.indexOf(BMA_QST_VAL_SEPARATOR, p);
		if (p < 0) throw new BmaException(BMA_ERR_SYS_INTERNO, "Query String non valida per: " + c.nome, chiave, this);
		p = p + 1;
		int e = chiave.indexOf(BMA_QST_KEY_SEPARATOR, p);
		if (e < 0) c.valore = chiave.substring(p).trim();
		else c.valore = chiave.substring(p, e);
	}
}
public void impostaValori(Hashtable valori) {
	for (int i = 0; i < getSize(); i++) {
		BmaDataColumn c = (BmaDataColumn)getElement(i);
		String v = (String)valori.get(c.nome);
		if (v!=null) c.valore = v;
	}
}
public void setValore(int pos, String valore) {
	if (pos < 0 || pos > getSize()) return;
	BmaDataColumn c = (BmaDataColumn)getElement(pos);
	c.valore = valore;
}
public void setValore(String nome, String valore) {
	for (int i = 0; i < getSize(); i++) {
		BmaDataColumn c = (BmaDataColumn)getElement(i);
		if (c.nome.equals(nome)) {
			c.valore = valore;
			return;
		}
	}
}

public Hashtable parseQueryString(String chiave) {
	Hashtable valori = new Hashtable();
	String k = chiave;
	int p = k.indexOf(BMA_QST_KEY_SEPARATOR);
	int p2;
	while (p >= 0) {
		String v = k.substring(0, p);
		p2 = v.indexOf(BMA_QST_VAL_SEPARATOR);
		if (p2 > 0) valori.put(v.substring(0, p2).trim(), v.substring(p2 + 1).trim());
		k = k.substring(p + 1);
		p = k.indexOf(BMA_QST_KEY_SEPARATOR);
	}
	p2 = k.indexOf(BMA_QST_VAL_SEPARATOR);
	if (p2 > 0) valori.put(k.substring(0, p2).trim(), k.substring(p2 + 1).trim());
	return valori;	
}
}
