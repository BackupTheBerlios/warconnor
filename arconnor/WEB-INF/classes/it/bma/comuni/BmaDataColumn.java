package it.bma.comuni;

import java.util.*;
import java.text.*;
public class BmaDataColumn extends BmaObject {
	protected String nome = "";
	protected String tipo = "";
	protected String lunghezza = "";
	protected String decimali = "";
	protected boolean annullabile = false;
	//
	protected String nomeFisico = "";
	protected String nomeUtente = "";
	protected String descrizione = "";
	protected boolean chiavePrimaria = false;
	protected boolean caseSensitive = false;
	protected boolean changeNumber = false;
	protected boolean datoAuditing = false;
	protected boolean dataInizio = false;
	protected boolean dataFine = false;
	protected String valore = "";
	protected String formato = "";
	protected String tipoEsterno = "";
	protected String regolaValidazione = "";
	protected String tipoControllo = BMA_CONTROLLO_LIBERO;
	protected Hashtable valoriControllo = new Hashtable();

	private final String FMT_DATE = "yyyy-MM-dd";
	private final String FMT_DATECHAR = "yyyyMMdd";
	private final String FMT_TIME = "HH:mm:ss";
	private final String FMT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";
public BmaDataColumn() {
	super();
}
public BmaDataColumn(String nome, String xml) throws BmaException {
	super();
	if (nome.trim().length()>0) this.nome = nome;
	if (xml==null) xml = "";
	if (xml.trim().length() >=0) fromXml(xml);
}
public java.lang.String getChiave() {
	return nome;
}
public String getCondizioneSql(String operatore, String valore) {
	if (operatore.equals(BMA_SQL_OPE_BETWEEN)) {
		return null;
	}
	else if (operatore.equals(BMA_SQL_OPE_IN)) {
		return nomeFisico + " " + operatore + " (" + valore + ") ";
	}
	else if (operatore.equals(BMA_SQL_OPE_LIKE)) {
		return nomeFisico + operatore + "'%" + valore + "%'";
	}
	else {
		return nomeFisico + operatore + getValoreSql(valore);
	}
}
public String getCondizioneSqlRange(String valMin, String valMax) {
	return nomeFisico + " " + BMA_SQL_OPE_BETWEEN + " " +
					getValoreSql(valMin) + " AND " + getValoreSql(valMax);	
}
public java.lang.String getDecimali() {
	return decimali;
}
public java.lang.String getDescrizione() {
	return descrizione;
}
public java.lang.String getFormato() {
	return formato;
}
public java.lang.String getLunghezza() {
	return lunghezza;
}
public java.lang.String getNome() {
	return nome;
}
public java.lang.String getTipoControllo() {
	return tipoControllo;
}
public String getTipoSemplice() {
	if (tipo.equals(BMA_SQL_TYP_CHAR)) return BMA_SQL_TYS_CHR;
	else if (tipo.equals(BMA_SQL_TYP_VARCHAR)) return BMA_SQL_TYS_CHR;
	else if (tipo.equals(BMA_SQL_TYP_DATE)) return BMA_SQL_TYS_DAT;
	else if (tipo.equals(BMA_SQL_TYP_TIME)) return BMA_SQL_TYS_DAT;
	else if (tipo.equals(BMA_SQL_TYP_TIMESTAMP)) return BMA_SQL_TYS_DAT;
	else if (tipo.equals(BMA_SQL_TYP_DATECHAR)) return BMA_SQL_TYS_DAT;
	else if (tipo.equals(BMA_SQL_TYP_DECIMAL)) return BMA_SQL_TYS_NUM;
	else return "";
}
public java.lang.String getValore() {
	return valore;
}
public String getValoreSql() {
	return getValoreSql(valore);
}
public String getValoreSql(String valoreTemp) {
	if (valoreTemp == null || valoreTemp.length() == 0) {
		if (isAnnullabile()) return BMA_NULL;
		if (getTipoSemplice().equals(BMA_SQL_TYS_NUM)) {
			return "0";
		}
		else {
			return "' '";
		}
	}
	if (getTipoSemplice().equals(BMA_SQL_TYS_NUM)) {
		return valoreTemp;
	}
	String dato = valoreTemp;
	if (!caseSensitive) {
		dato = dato.toUpperCase();
	}
	int pos = dato.indexOf("'");
	while (pos>=0) {
		dato = dato.substring(0, pos) + "'" + dato.substring(pos);
		pos = dato.indexOf("'", pos + 2);
	}
//	dato = dato.replace('\'','´');
	return "'" + dato + "'";
}
public Hashtable getValoriControllo() {
	return valoriControllo;
}
protected java.lang.String getXmlTag() {
	return getClassName();
}
public boolean isAnnullabile() {
	return annullabile;
}
public boolean isCaseSensitive() {
	return caseSensitive;
}
public boolean isChiave() {
	return chiavePrimaria;
}
private Date parseDateTime(String newValore, String dateFormat) {
	try {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.parse(newValore);
	} 
	catch (ParseException pe) {
		return null;
	}
}
public void setAnnullabile(boolean newAnnullabile) {
	annullabile = newAnnullabile;
}
public void setCaseSensitive(boolean newCaseSensitive) {
	caseSensitive = newCaseSensitive;
}
public void setDecimali(java.lang.String newDecimali) {
	decimali = newDecimali;
}
public void setDescrizione(java.lang.String newDescrizione) {
	descrizione = newDescrizione;
}
public void setFormato(java.lang.String newFormato) {
	formato = newFormato;
}
public void setLunghezza(java.lang.String newLunghezza) {
	lunghezza = newLunghezza;
}
public void setTipo(java.lang.String newTipo) {
	tipo = newTipo;
}
public void setTipoControllo(java.lang.String newTipoControllo) {
	tipoControllo = newTipoControllo;
}
public void setValore(java.lang.String newValore) {
	valore = newValore;
}
public void setValoreInterno(String valoreLetto) throws BmaException {
	if (valoreLetto==null || valoreLetto.trim().length()==0) valore = "";
	
	// Intercetta le date
	else if (tipo.equalsIgnoreCase(BMA_SQL_TYP_DATE)) {
		Date tmp = parseDateTime(valoreLetto, FMT_DATE);
		if (tmp==null) tmp = parseDateTime(valoreLetto, FMT_TIMESTAMP);
		if (tmp==null) tmp = parseDateTime(valoreLetto, FMT_DATECHAR);
		if (tmp==null) throw new BmaException(BMA_ERR_JDB_SQL, "Errore dati", "Errore nel formato dati: " + valoreLetto, this);
		valore = new SimpleDateFormat(FMT_DATE).format(tmp);
	}
	else if (tipo.equalsIgnoreCase(BMA_SQL_TYP_DATECHAR)) {
		Date tmp = parseDateTime(valoreLetto, FMT_DATECHAR);
		if (tmp==null) tmp = parseDateTime(valoreLetto, FMT_DATE);
		if (tmp==null) throw new BmaException(BMA_ERR_JDB_SQL, "Errore dati", "Errore nel formato dati: " + valoreLetto, this);
		valore = new SimpleDateFormat(FMT_DATECHAR).format(tmp);
	}
	else if (tipo.equalsIgnoreCase(BMA_SQL_TYP_TIME)) {
		Date tmp = parseDateTime(valoreLetto, FMT_TIME);
		if (tmp==null) tmp = parseDateTime(valoreLetto, FMT_TIMESTAMP);
		if (tmp==null) throw new BmaException(BMA_ERR_JDB_SQL, "Errore dati", "Errore nel formato dati: " + valoreLetto, this);
		valore = new SimpleDateFormat(FMT_TIME).format(tmp);
	}
	else if (tipo.equalsIgnoreCase(BMA_SQL_TYP_TIMESTAMP)) {
		Date tmp = parseDateTime(valoreLetto, FMT_TIMESTAMP);
		if (tmp==null) tmp = parseDateTime(valoreLetto, FMT_DATE);
		if (tmp==null) tmp = parseDateTime(valoreLetto, FMT_TIME);
		if (tmp==null) throw new BmaException(BMA_ERR_JDB_SQL, "Errore dati", "Errore nel formato dati: " + valoreLetto, this);
		valore = new SimpleDateFormat(FMT_TIMESTAMP).format(tmp);
	}

	// Intercetta i numeri
	else if (getTipoSemplice().equalsIgnoreCase(BMA_SQL_TYS_NUM)) {
			setValoreNumerico(valoreLetto);
	}
	else {
		valore = valoreLetto;
	}
}
public void setValoreNumerico(String valoreLetto) {
	//Tappo provvisorio per virgole
	int h = valoreLetto.indexOf(",");
	if (h>=0) {
		valoreLetto = valoreLetto.substring(0,h) + "." + valoreLetto.substring(h+1);
	}
	if (decimali.trim().length()==0 || Integer.parseInt(decimali)==0) {
		long n = Long.parseLong(valoreLetto);
		valore = Long.toString(n);
	}
	else {
		double n = Double.parseDouble(valoreLetto);
		valore = Double.toString(n);
	}
}
public void setValoriControllo(Hashtable valori) {
	valoriControllo = valori;
}
}
