package it.bma.comuni;

import java.util.*;
public class BmaDataTable extends BmaObject {
	protected String nome = "";
	protected String nomeFisico = "";
	protected String nomeUtente = "";
	protected boolean logApplicativo = false;
	private Vector colonne = new Vector();
	private String condizioniFiltro = "";
	private String orderBy = "";
public BmaDataTable() {
	super();
}
public BmaDataTable(String nome) {
	super();
	this.nome = nome;
	nomeFisico = nome;
	nomeUtente = nome;
}
public BmaDataTable(String nome, String xml) throws BmaException {
	super();
	if (nome.trim().length()>0) this.nome = nome;
	if (xml==null) xml = "";
	if (xml.trim().length() >=0) fromXml(xml);
}
public BmaDataKey getBmaDataKey() {
	BmaDataKey k = new BmaDataKey(nome);
	for (int i = 0; i < colonne.size(); i++){
		BmaDataColumn c = (BmaDataColumn)colonne.elementAt(i);
		if (c.chiavePrimaria) k.add(c);
	}
	return k;
}
public java.lang.String getChiave() {
	return nome;
}
public BmaDataColumn getColonna(int numeroColonna) {
	if (numeroColonna > colonne.size()-1) return null;
	else return (BmaDataColumn)colonne.elementAt(numeroColonna);
}
public BmaDataColumn getColonna(String nome) {
	for (int i = 0; i < colonne.size(); i++){
		BmaDataColumn dColumn = (BmaDataColumn)colonne.elementAt(i);
		if (dColumn.nome.equals(nome)) return dColumn;
	}
	return null;
}
/**
 * 
 * @return java.util.Vector
 */
public java.util.Vector getColonne() {
	return colonne;
}
public String getCondizioneColonna(String nome, String operatore, String valore) {
	BmaDataColumn col = getColonna(nome);
	if (col==null) return "";
	return col.getCondizioneSql(operatore, valore);
}
public String getCondizioniChiave() {
	String cond = "";
	for (int i = 0; i < colonne.size(); i++){
		BmaDataColumn c = (BmaDataColumn)colonne.elementAt(i);
		if (c.chiavePrimaria) {
			cond = cond + " AND " + c.nomeFisico + BMA_SQL_OPE_EQ + c.getValoreSql();
		}
	}
	return cond;
}
public String getCondizioniChiave(Hashtable valori) {

	String cond = "";
	for (int i = 0; i < colonne.size(); i++){
		BmaDataColumn c = (BmaDataColumn)colonne.elementAt(i);
		String v = (String)valori.get(c.nome);
		if (v==null) v = "";
		if (c.chiavePrimaria) {
			cond = cond + " AND " + c.nomeFisico + BMA_SQL_OPE_EQ + c.getValoreSql(v);
		}
	}
	return cond;
}
private String getCondizioniComplete(String condizioni) {
	String cond = condizioni.trim();
	int p = cond.indexOf("AND");
	// Se la AND non è nei primi due caratteri
	if (p < 0 || p > 1) {
		cond = " AND " + cond;
	}
	cond = getCondizioniFiltro() + cond;
	cond = cond.trim();
	p = cond.indexOf("AND");
	// Se cond inizia con AND
	if (p == 0) {
		if (cond.length()==3) {
			cond = "";
		}
		else {
			cond = " WHERE " + cond.substring(4);
		}
	}
	else {
		p = cond.indexOf("WHERE");
		// Se cond non inizia con WHERE
		if (p == 0) {
			cond = " " + cond;
		}
		else {
			cond = " WHERE " + cond;
		}	
	}
	return cond;
}
public java.lang.String getCondizioniFiltro() {
	return condizioniFiltro;
}
public Hashtable getFiltriDaQueryString(String queryString) {
	BmaDataKey dk = new BmaDataKey();
	return dk.parseQueryString(queryString);
}
public int getNumeroColonna(String nome) {
	for (int i = 0; i < colonne.size(); i++){
		BmaDataColumn dColumn = (BmaDataColumn)colonne.elementAt(i);
		if (dColumn.nome.equals(nome)) return i;
	}
	return -1;
}
public java.lang.String getOrderBy() {
	return orderBy;
}
public String getSqlDelete() {
	Hashtable valori = new Hashtable();
	for (int i = 0; i < colonne.size(); i++) {
		BmaDataColumn c = (BmaDataColumn)colonne.elementAt(i);
		valori.put(c.nome, c.getValore());
	}
	return getSqlDelete(valori);
}
public String getSqlDelete(Hashtable valori) {
	String cond = getCondizioniComplete(getCondizioniChiave(valori));
	return " DELETE FROM " + nomeFisico + cond;
}
public String getSqlInsert() {
	Hashtable valori = new Hashtable();
	for (int i = 0; i < colonne.size(); i++) {
		BmaDataColumn c = (BmaDataColumn)colonne.elementAt(i);
		valori.put(c.nome, c.getValore());
	}
	return getSqlInsert(valori);
}
public String getSqlInsert(Hashtable valori) {
	String into = "";
	String values = "";
	for (int i = 0; i < colonne.size(); i++){
		BmaDataColumn c = (BmaDataColumn)colonne.elementAt(i);
		String v = (String)valori.get(c.nome);
		into = into + ", " + c.nomeFisico;
		values = values + ", " + c.getValoreSql(v);
	}
	return " INSERT INTO " + nomeFisico + " ( " + into.substring(2) + ") VALUES ( " + values.substring(2) + ")";
}
public String getSqlLista(String condizioni) {
	String cond = getCondizioniComplete(condizioni);
	String sql = getSqlNomiColonne();
	if (sql.length() > 0) {
		sql = "SELECT " + sql + " FROM " + nomeFisico + " " + cond;
		if (orderBy.trim().length()>0) sql = sql + " ORDER BY " + orderBy;
		else if (getNaturalOrderBy().length()>0) sql = sql + " ORDER BY " + getNaturalOrderBy();
	}
	return sql;
}
public String getSqlLista(Hashtable condizioni) {
	String cond = "";
	Enumeration e = colonne.elements();
	while (e.hasMoreElements()) {
		BmaDataColumn dColumn = (BmaDataColumn)e.nextElement();
		String valore = (String)condizioni.get(dColumn.nome);
		if (valore!=null) {
			cond = cond + " AND " + dColumn.getCondizioneSql(BMA_SQL_OPE_EQ, valore);
		}
	}
	return getSqlLista(cond);
}
public String getSqlNomiColonne() {
	String sql = "";
	// Prepara elenco delle colonne
	for (int i = 0; i < colonne.size(); i++) {
		BmaDataColumn dColumn = (BmaDataColumn) colonne.elementAt(i);
		sql = sql + ", " + dColumn.nomeFisico;
	}
	if (sql.length() > 0) return sql.substring(2);
	else return "1";
}
public String getNaturalOrderBy() {
	String order = "";
	for (int i = 0; i < colonne.size(); i++) {
		BmaDataColumn dColumn = (BmaDataColumn) colonne.elementAt(i);
		if (dColumn.isChiave()) {
			order = order + ", " + dColumn.nomeFisico;
		}
	}
	if (order.length() > 0) return order.substring(2);
	else return "";
}
public String getSqlReadKey() {
	String cond = getCondizioniChiave();
	return getSqlLista(cond);
}
public String getSqlReadKey(String dataKeyString) {
	BmaDataKey dk = new BmaDataKey();
	Hashtable cond = dk.parseQueryString(dataKeyString);
	return getSqlLista(cond);
}
public String getSqlReadKey(Hashtable valori) {

	String cond = getCondizioniChiave(valori);
	return getSqlLista(cond);
}
public String getSqlUpdate() {
	Hashtable valori = new Hashtable();
	for (int i = 0; i < colonne.size(); i++) {
		BmaDataColumn c = (BmaDataColumn)colonne.elementAt(i);
		valori.put(c.nome, c.getValore());
	}
	return getSqlUpdate(valori);
}
public String getSqlUpdate(Hashtable valori) {
	String sql = "";
	for (int i = 0; i < colonne.size(); i++){
		BmaDataColumn c = (BmaDataColumn)colonne.elementAt(i);
		String v = (String)valori.get(c.nome);
		sql = sql + ", " + c.nomeFisico + BMA_SQL_OPE_EQ + c.getValoreSql(v);
	}
	String cond = getCondizioniChiave(valori).trim();
	cond = getCondizioniComplete(cond);
	sql = " UPDATE " + nomeFisico + " SET " + sql.substring(2) + cond;
	return sql;
}
public String getValoreChiave() {

	String v = "";
	for (int i = 0; i < colonne.size(); i++){
		BmaDataColumn c = (BmaDataColumn)colonne.elementAt(i);
		if (c.chiavePrimaria) {
			if (i==0) v = c.getValore();
			else v = v + BMA_KEY_SEPARATOR + c.getValore();
		}
	}
	return v;
}
public String getValoreColonna(String nome) {
	BmaDataColumn dColumn = getColonna(nome);
	if (dColumn==null) return "";
	return dColumn.getValore();
}
public Vector getValori() {
	Vector valori = new Vector();
	for (int i = 0; i < colonne.size(); i++){
		BmaDataColumn c = (BmaDataColumn)colonne.elementAt(i);
		valori.add(c.getValore());
	}
	return valori;
}
public Hashtable getHashtableValori() {
	Hashtable valori = new Hashtable();
	for (int i = 0; i < colonne.size(); i++){
		BmaDataColumn c = (BmaDataColumn)colonne.elementAt(i);
		valori.put(c.nomeFisico, c.getValore());
	}
	return valori;
}
protected java.lang.String getXmlTag() {
	return getClassName();
}
/**
 * 
 * @param newColonne java.util.Vector
 */
public void setColonne(java.util.Vector newColonne) {
	colonne = newColonne;
}
public void setCondizioniFiltro(java.lang.String newCondizioniFiltro) {
	String cond = newCondizioniFiltro.trim();
	if (!cond.substring(0, 3).equals("AND"))
		cond = "AND " + cond;
	condizioniFiltro = cond;
}
public void setNomeUtente(String nome) {
	nomeUtente = nome;
}
public void setOrderBy(java.lang.String newOrderBy) {
	orderBy = newOrderBy;
}
public void setOrderByKeys() {
	String s = "";
	for (int i=0;i<this.colonne.size();i++) {
		BmaDataColumn c = (BmaDataColumn)this.colonne.elementAt(i);
		if (c.isChiave()) s = s + "," + c.nome;
	}
	if (s.length()>0) setOrderBy(s.substring(1));
}
public void setValori(Hashtable valori) {
	for (int i = 0; i < colonne.size(); i++){
		BmaDataColumn c = (BmaDataColumn)colonne.elementAt(i);
		String valore = (String)valori.get(c.nome);
		if (valore==null) valore = "";
		if (c.getTipoSemplice().equals(BMA_SQL_TYS_NUM)) {
			if (valore.trim().length()==0) valore = "0";
			double vd = Double.parseDouble(valore);
			valore = Double.toString(vd);
		}
		c.valore = valore;
	}
}
public void setValori(Vector rigaValori) {
	if (rigaValori.size()!=colonne.size()) return;
	Hashtable valori = new Hashtable();
	for (int i = 0; i < colonne.size(); i++){
		BmaDataColumn c = (BmaDataColumn)colonne.elementAt(i);
		String valore = (String)rigaValori.elementAt(i);
		if (valore==null) valore = "";
		valori.put(c.nome, valore);
	}
	setValori(valori);
}
public void setValoriDaQuery(Hashtable valori) throws BmaException {
	for (int i = 0; i < colonne.size(); i++){
		BmaDataColumn c = (BmaDataColumn)colonne.elementAt(i);
		String valore = (String)valori.get(c.nome);
		if (valore==null) valore = "";
		c.setValoreInterno(valore);
	}
}
public void setValoriDaQuery(Vector rigaValori) throws BmaException {
	if (rigaValori.size()!=colonne.size()) return;
	Hashtable valori = new Hashtable();
	for (int i = 0; i < colonne.size(); i++){
		BmaDataColumn c = (BmaDataColumn)colonne.elementAt(i);
		String valore = (String)rigaValori.elementAt(i);
		if (valore==null) valore = "";
		valori.put(c.nome, valore);
	}
	setValoriDaQuery(valori);
}
}
