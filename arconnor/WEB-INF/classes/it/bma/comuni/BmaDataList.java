package it.bma.comuni;

import java.util.*;
import java.text.*;
public class BmaDataList extends BmaObject {
	private final String BMA_LST_VALUES = "BmaDataValues";
	private final String BMA_LST_ROW = "BmaDataRow";
	private String nome = "";
	private BmaDataTable tabella = new BmaDataTable();
	private Vector valori= new Vector();
public BmaDataList() {
	super();
}
public BmaDataList(BmaDataTable newDataTable) {
	super();
	tabella = newDataTable;
	nome = tabella.nome;
}
public BmaDataList(BmaDataTable newDataTable, Vector newValori) {
	super();
	tabella = newDataTable;
	nome = tabella.nome;
	valori = newValori;
}
public BmaDataList(String nome) {
	super();
	this.nome = nome;
}
public void clear() {
	valori.clear();
}
public void setColonneChiave(String[] nomi) {
	for (int i=0;i<nomi.length;i++) {
		BmaDataColumn c = tabella.getColonna(nomi[i]);
		c.chiavePrimaria = true;
	}
}
public void fromXml(String text) throws BmaException {
	String xml = parseXmlTag(text, getXmlTag(), 0);
	tabella.fromXml(xml);
	fromXmlValori(xml);
}
public void fromXmlSoloValori(String text) throws BmaException {
	String xml = parseXmlTag(text, getXmlTag(), 0);
	fromXmlValori(xml);
}
private void fromXmlValori(String text) throws BmaException {
	String xml = parseXmlTag(text, BMA_LST_VALUES, 0);
	Vector righe = parseXmlList(xml, BMA_LST_ROW);
	for (int i = 0; i < righe.size(); i++){
		xml = (String)righe.elementAt(i);
		Vector datiRiga = new Vector();
		for (int j = 0; j < tabella.getColonne().size(); j++){
			BmaDataColumn c = (BmaDataColumn)tabella.getColonne().elementAt(j);
			String v = parseXmlTag(xml, c.nome, 0);
			datiRiga.add(v);
		}
		valori.add(datiRiga);
	}
}
public java.lang.String getChiave() {
	return nome;
}
public java.lang.String getChiaveSelezione(int numRiga) {
	if (numRiga >= valori.size()) return "";
	Vector riga = (Vector) valori.elementAt(numRiga);
	try {
		tabella.setValoriDaQuery(riga);	
	} 
	catch (BmaException e) {
		return e.getMessage();
	}
	BmaDataKey k = tabella.getBmaDataKey();
	return k.getQueryString();
}
public java.util.Vector getNomi() {
	Vector v = new Vector();
	for (int i = 0; i < tabella.getColonne().size(); i++){
		BmaDataColumn c = (BmaDataColumn)tabella.getColonne().elementAt(i);
		v.add(c.nomeUtente);
	}
	return v;
}
public int getNumeroRighe() {
	return valori.size();
}
public int getRigaChiave(String chiave) {
	for (int i = 0; i < valori.size(); i++) {
		String k = getChiaveSelezione(i);
		if (k.equals(chiave)) {
			return i;
		}
	}
	return -1;
}
public BmaDataTable getTabella() {
	return tabella;
}
public String getValore(int riga, int colonna) {
	if (riga >= valori.size()) return "";
	Vector r = (Vector) valori.elementAt(riga);
	if (colonna >= tabella.getColonne().size() || colonna >= r.size()) return "";
	return (String) r.elementAt(colonna);
}
public String getValore(int riga, String colonna) {
	int i = tabella.getNumeroColonna(colonna);
	if (i < 0) return "";
	return getValore(riga, i);
}
public String getValore(String chiave, String nome) {
	BmaHashtable t = getValoriRiga(chiave);
	BmaParametro p = (BmaParametro)t.getElement(nome);
	if (p==null) return "";
	return p.getValore();
}
public String getValoreDescrittivo(int riga, String nomeColonna) {
	Hashtable vc = getValoriControllo(nomeColonna);
	if (vc.size()==0) return "";
	
	int i = tabella.getNumeroColonna(nomeColonna);
	if (i < 0) return "";
	
	String valore = (String)vc.get(getValore(riga, i));
	if (valore == null) valore = "";
	return valore;
}
public String getValoreInput(int riga, int colonna) {
	if (riga >= valori.size()) return "";
	Vector r = (Vector) valori.elementAt(riga);
	
	if (colonna >= tabella.getColonne().size() || colonna >= r.size()) return "";

	BmaDataColumn dd = (BmaDataColumn)tabella.getColonne().elementAt(colonna);
	String v = (String) r.elementAt(colonna);
	if (v == null || v.trim().length() == 0) return "";

	if (dd.getTipoSemplice().equals(BMA_SQL_TYS_NUM)) {
		double vd = Double.parseDouble(v);
		NumberFormat nf = NumberFormat.getNumberInstance();
		if (dd.decimali.equals("0")) nf.setParseIntegerOnly(true);
		nf.setGroupingUsed(false);
		return nf.format(vd);
	}
	else if (dd.getTipoSemplice().equals(BMA_SQL_TYS_DAT)) {
		SimpleDateFormat sdfIn = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date d = sdfIn.parse(v);
			return sdfOut.format(d);
		}
		catch (ParseException e) {
			return "";
		}
	}
	else {
		return v.trim();
	}
}
public String getValoreInput(int riga, String colonna) {
	int i = tabella.getNumeroColonna(colonna);
	if (i < 0) return "";
	return getValoreInput(riga, i);
}
public String getValoreOutput(int riga, int colonna) {
	if (riga >= valori.size()) return "";
	Vector r = (Vector) valori.elementAt(riga);
	
	if (colonna >= tabella.getColonne().size() || colonna >= r.size()) return "";
	
	BmaDataColumn dd = (BmaDataColumn)tabella.getColonne().elementAt(colonna);
	String v = (String) r.elementAt(colonna);
	if (v==null || v.trim().length()==0) return "";

	if (dd.getTipoSemplice().equals(BMA_SQL_TYS_NUM)) {
		double vD = Double.parseDouble(v);
		NumberFormat nf = NumberFormat.getNumberInstance();
		if (dd.decimali.equals("0")) {
			nf.setParseIntegerOnly(true);
		}
		return nf.format(vD);
	}
	else if (dd.tipo.equals(BMA_SQL_TYP_TIMESTAMP)) {
		long vL = Long.parseLong(v);
		SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS");
		return sdfOut.format(new Date(vL));
	}
	else if (dd.getTipoSemplice().equals(BMA_SQL_TYS_DAT)) {
		SimpleDateFormat sdfIn = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date d = sdfIn.parse(v);
			return sdfOut.format(d);
		}
		catch (ParseException e) {
			return "";
		}
	}
	else {
		String s = v.trim();
		if (s.length()>50) s = s.substring(0,50);
		return s;
	}
}
public String getValoreOutput(int riga, String colonna) {
	int i = tabella.getNumeroColonna(colonna);
	if (i < 0) return "";
	return getValoreOutput(riga, i);
}
public java.util.Vector getValori() {
	return valori;
}
public Hashtable getValoriControllo(String nomeColonna) {
	BmaDataColumn c = tabella.getColonna(nomeColonna);
	if (c!=null) return c.valoriControllo;
	else return new Hashtable();
}
public BmaHashtable getValoriRiga(String chiave) {
	BmaHashtable dati = new BmaHashtable(nome);
	int i = getRigaChiave(chiave);
	if (i >= 0) {
		Vector riga = (Vector) valori.elementAt(i);
		for (int j = 0; j < tabella.getColonne().size(); j++) {
			BmaDataColumn dd = (BmaDataColumn)tabella.getColonne().elementAt(j);
			BmaParametro p = new BmaParametro(dd.nome, (String) riga.elementAt(j));
			dati.add(p);
		}
	}
	return dati;
}
protected java.lang.String getXmlTag() {
	if (nome.trim().length()==0) return getClassName();
	else return nome;
}
public void impostaDato(boolean chiave, String nome, String tipo, String lunghezza, String decimali) {
	BmaDataColumn c;
	c = tabella.getColonna(nome);
	if (c==null) {
		c = new BmaDataColumn();
		c.nome = nome;
		tabella.getColonne().add(c);
	}
	c.chiavePrimaria = chiave;
	if (tipo.equals(BMA_SQL_TYS_CHR)) c.tipo = BMA_SQL_TYP_CHAR;
	else if (tipo.equals(BMA_SQL_TYS_DAT)) c.tipo = BMA_SQL_TYP_DATE;
	else if (tipo.equals(BMA_SQL_TYS_NUM)) c.tipo = BMA_SQL_TYP_DECIMAL;
	c.lunghezza = lunghezza;
	c.decimali = decimali;
}
public boolean isPari(int i) {
	int k = (i + 1) / 2;
	k = k * 2 - 1;
	return (k==i);
}
public void setValori(java.util.Vector newValori) {
	valori = newValori;
}
public java.lang.String toXml() {
	String xml = ""; 
	xml = xml + tabella.toXml();
	xml = xml + toXmlValori();
	return getXml(getXmlTag(), xml);
}
public java.lang.String toXmlSoloValori() {
	String xml = ""; 
	xml = xml + toXmlValori();
	return getXml(getXmlTag(), xml);
}
private String toXmlValori() {
	String xml = "";
	for (int i = 0; i < valori.size(); i++){
		Vector riga = (Vector)valori.elementAt(i);
		String s = "";
		for (int j = 0; j < tabella.getColonne().size(); j++){
			BmaDataColumn c = (BmaDataColumn)tabella.getColonne().elementAt(j);
			s = s + getXml(c.nome, (String)riga.elementAt(j));
		}
		xml = xml + getXml(BMA_LST_ROW, s);
	}
	xml = getXml(BMA_XML_ELEMENTS_NUMBER, Integer.toString(valori.size())) + xml;
	return getXml(BMA_LST_VALUES, xml);
}

public java.util.Hashtable getChiaviContesto(String chiave) {
	BmaDataKey dk = tabella.getBmaDataKey();
	return dk.parseQueryString(chiave);
}

public int getSize() {
	return tabella.getColonne().size();
}

public int getSpazioColonna(int ampiezza, int numeroColonna) {
	int totale = 0;
	int corrente = 0;
	for (int i = 0; i < tabella.getColonne().size(); i++){
		BmaDataColumn c = (BmaDataColumn)tabella.getColonne().elementAt(i);
		int l=5;
		if (c.getLunghezza().trim().length()>0) l = Integer.parseInt(c.getLunghezza());
		totale = totale + l;
		if (i==numeroColonna) corrente = l;
	}
	if (totale==0) totale = 100;
	return corrente * ampiezza / totale;
}

public int getSpazioColonna(int ampiezza, int numeroColonna, BmaHashtable alias) {
	int totale = 0;
	int corrente = 0;
	for (int i = 0; i < tabella.getColonne().size(); i++){
		BmaDataColumn c = (BmaDataColumn)tabella.getColonne().elementAt(i);
		int l=3;
		if (c.getLunghezza().trim().length()>0) l = Integer.parseInt(c.getLunghezza());
		String na = alias.getString(c.getNome()).trim();
		if (na==null) na = "";
		if (na.length()>l) l = na.length();
		if (i==numeroColonna) corrente = l;
		totale = totale + l;
	}
	if (totale==0) totale = 100;
	return corrente * ampiezza / totale;
}

public int getSpazioColonna(int ampiezza, int numeroColonna, String alias) {
	int totale = 0;
	if (alias==null) alias = "";
	int corrente = alias.length();
	for (int i = 0; i < tabella.getColonne().size(); i++){
		BmaDataColumn c = (BmaDataColumn)tabella.getColonne().elementAt(i);
		int l=5;
		if (c.getLunghezza().trim().length()>0) l = Integer.parseInt(c.getLunghezza());
		if (i==numeroColonna) {
			if (l > corrente) corrente = l;
			totale = totale + corrente;			
		}
		else totale = totale + l;
	}
	if (totale==0) totale = 100;
	return corrente * ampiezza / totale;
}

public java.util.Vector getValori(String chiave) {
	int i = getRigaChiave(chiave);
	if (i<0) return null;
	return (Vector)valori.elementAt(i);
}
}
