package it.bma.comuni;

import java.util.*;
import java.sql.*;
import java.text.*;

public class BmaJdbcTrx extends BmaObject {
	private String nome = "";
	private BmaJdbcSource fonte = null;
	private Connection connessione;
	private boolean valida = false;
	private boolean aperta = false;
	private String user = "";
	private int sqlStep = 0;
	private String db;
	private final String JDBC_DB_SQLBASE = "SQLBase";
	private final String JDBC_DB_ORACLE = "oracle";
	private final String JDBC_DB_DB2NT = "db2/nt";
	private final String JDBC_DB_SQLSERVER = "Microsoft SQL Server";
	private boolean commitSupported = true;
	private String pkErrorState = "23000";
	private int pkErrorCode = 1;
	private String fkSqlStateMin = "23000";
	private String fkSqlStateMax = "24000";
public BmaJdbcTrx() {
	super();
}
public BmaJdbcTrx(BmaJdbcSource fonte) {
	super();
	this.fonte = fonte;
}
public void addSqlStep() {
	sqlStep++;
}
public void chiudi() throws BmaException {
	try {
		if (!connessione.getAutoCommit()) {
			if (valida) commit();
			else rollback();
			sqlStep = 0;
			user = "";
			nome = "";
			connessione.close();
			connessione = null;
			aperta = false;
		}
	} 
	catch (SQLException esql) {
			valida = false;
			aperta = false;
			connessione = null;
			throw new BmaException(new BmaErrore().BMA_ERR_JDB_CONNESSIONE, "", esql.getMessage(), this);
	}
}
private void commit() throws SQLException, BmaException {

	if (commitSupported) connessione.commit();
	else {
		Statement st = getStatement();
		st.executeUpdate("COMMIT");
		st.close();
	}
}
public BmaDataList eseguiQuery(String sql, String nomeVista) throws BmaException {
	if (!isAperta() || !isValida()) {
		throw new BmaException(BMA_ERR_JDB_CONNESSIONE, BMA_MSG_JDB_TRXINVALIDA, sql, this);
	}
	BmaDataList table = new BmaDataList(new BmaDataTable(nomeVista));
	try {
		Statement st = getStatement();
		ResultSet rs = st.executeQuery(sql);
		if (rs == null) return null;
		int numCols = rs.getMetaData().getColumnCount();
		for (int i = 1; i <= numCols; i++){
			BmaDataColumn c = new BmaDataColumn();
			c.nome = rs.getMetaData().getColumnName(i);
			c.nomeFisico = c.nome;
			c.nomeUtente = c.nome;
			c.tipo = rs.getMetaData().getColumnTypeName(i).toUpperCase();
			c.decimali = Integer.toString(rs.getMetaData().getScale(i));
			c.lunghezza = Integer.toString(rs.getMetaData().getPrecision(i));
			c.annullabile = rs.getMetaData().isNullable(i)==1;
			table.getTabella().getColonne().add(c);
		}
		Vector matrice = new Vector();
		Vector riga;
		String dato;
		while (rs.next()) {
			riga = new Vector();
			for (int i = 0; i < numCols; i++) {
				dato = rs.getString(i + 1);
				if (dato == null) {
					dato = "";
				}
				riga.addElement(dato.trim());
			}
			matrice.addElement(riga);
		}
		rs.close();
		st.close();
		table.setValori(matrice);
		return table;
	}
	catch (SQLException esql) {
		invalida();
		throw new BmaException(BMA_ERR_JDB_SQL, esql.getMessage(), sql, this);
	}
	catch (Exception e) {
		invalida();
		throw new BmaException(BMA_ERR_NON_PREVISTO, e.getMessage(), sql, this);
	}
}
/* Controlla i valori relativi ad un'occorrenza. Restituisce:<br/>
 * 0  se la riga è inesistente
 * -1 se la riga esiste con valori diversi
 * 1  se la riga esiste con valori uguali
 */
public int controllaRiga(BmaDataTable table, Hashtable valori) throws BmaException {
	Vector dati = eseguiSqlSelect(table.getSqlReadKey(valori));
	if (dati.size()==0) return 0;
	else if (dati.size()>1) throw new BmaException(BMA_ERR_JDB_SQL, "Errore in controllo riga", table.getSqlReadKey(valori), table);
	dati = (Vector)dati.elementAt(0);
	table.setValori(dati);
	for (int i=0;i<table.getColonne().size();i++) {
		BmaDataColumn c = (BmaDataColumn)table.getColonne().elementAt(i);
		if (!c.valore.equals((String)valori.get(c.nome))) return -1;
	}
	return 1;
}
public synchronized Vector eseguiSqlSelect(String sql) throws BmaException {
	if (!isAperta() || !isValida()) {
		throw new BmaException(BMA_ERR_JDB_CONNESSIONE, BMA_MSG_JDB_TRXINVALIDA, sql, this);
	}
	try {
		Statement st = getStatement();
		ResultSet rs = st.executeQuery(sql);
		if (rs == null)
			return null;
		int numCols = rs.getMetaData().getColumnCount();
		Vector matrice = new Vector();
		Vector riga;
		String dato;
		while (rs.next()) {
			riga = new Vector();
			for (int i = 0; i < numCols; i++) {
				
/*
				ResultSetMetaData rsmd = rs.getMetaData();
				String name = rsmd.getColumnName(i + 1);
				String colName = rsmd.getColumnTypeName(i + 1);
				if (rs.getMetaData().getColumnType(i + 1)==Types.TIMESTAMP) {
					Timestamp tt = rs.getTimestamp(i + 1);
					dato = tt.toString();
				}
				else {
					dato = rs.getString(i + 1);
				}
*/
				dato = rs.getString(i + 1);
				if (dato == null) {
					dato = "";
				}
				riga.addElement(dato.trim());
			}
			matrice.addElement(riga);
		}
		rs.close();
		st.close();
		return matrice;
	}
	catch (SQLException esql) {
		invalida();
		throw new BmaException(BMA_ERR_JDB_SQL, esql.getMessage(), sql, this);
	}
	catch (Exception e) {
		invalida();
		throw new BmaException(BMA_ERR_NON_PREVISTO, e.getMessage(), sql, this);
	}
}
public synchronized void eseguiSqlUpdate(String sql) throws BmaException {
	if (!isAperta() || !isValida()) {
		throw new BmaException(BMA_ERR_JDB_CONNESSIONE, BMA_MSG_JDB_TRXINVALIDA, sql, this);
	}
	try {
		Statement st = getStatement();
		st.executeUpdate(sql);
		st.close();
		addSqlStep();
	}
	catch (SQLException esql) {
		invalida();
		if (isChiaveDoppiaException(esql)){
			throw new BmaException(BMA_ERR_JDB_PKEY, esql.getMessage(), sql, this);
		}
		else if (isConstraintException(esql)){
			throw new BmaException(BMA_ERR_JDB_FKEY, esql.getMessage(), sql, this);
		}
		else {
			throw new BmaException(BMA_ERR_JDB_SQL, esql.getMessage(), sql, this);				
		}
	}
	catch (Exception e) {
		invalida();
		throw new BmaException(BMA_ERR_NON_PREVISTO, e.getMessage(), sql, this);
	}
}
public java.lang.String getChiave() {
	return nome;
}
public java.sql.Connection getConnessione() {
	return connessione;
}
public BmaJdbcSource getFonte() {
	return fonte;
}
public String getNome() {
	if (isAperta() && isValida())
		return nome;
	else
		return "";
}
public String getSqlStep() {
	return Integer.toString(sqlStep);
}
public Statement getStatement() throws java.sql.SQLException {
	return connessione.createStatement();
}
public String getUser() {
	if (isAperta() && isValida())
		return user;
	else
		return "";
}
protected java.lang.String getXmlTag() {
	return "TransazioneJdbc";
}
public void invalida() {
	valida = false;
	if (aperta) {
		try {
			chiudi();
		}
		catch (BmaException e) {
			user = e.getMessage();
		}
	}
}
public boolean isAperta() {
	return aperta;
}
private boolean isChiaveDoppiaException(SQLException esql) {
	if (esql == null) return false;
	int errCode = esql.getErrorCode();
	String sqlState = esql.getSQLState();
	if (sqlState==null) return errCode == pkErrorCode;  
	else return (sqlState.equals(pkErrorState) && errCode == pkErrorCode);
}
private boolean isConstraintException(SQLException esql) {
	if (esql == null) return false;
	return (esql.getSQLState().compareTo(fkSqlStateMin) >= 0 && esql.getSQLState().compareTo(fkSqlStateMax) < 0);
}
public boolean isValida() {
	return valida;
}
public boolean open(String user) throws BmaException {
	return open(
				user, 
				fonte.getDriver(), 
				fonte.getUrl(), 
				fonte.getUser(), 
				fonte.getPass()
			);
}
protected boolean open(String user, 
										String jdbcClass,
										String jdbcUrl,
										String jdbcUser,
										String jdbcPass) throws BmaException {
	try {
		Class.forName(jdbcClass);
		connessione = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);
		db = connessione.getMetaData().getDatabaseProductName();
	}
	catch (ClassNotFoundException e) {
		aperta = false;
		valida = false;
		throw new BmaException(BMA_ERR_JDB_DRIVER, BMA_MSG_JDB_ERROREDATI, e.getMessage(), this);
	}
	catch (SQLException esql) {
		aperta = false;
		valida = false;
		throw new BmaException(BMA_ERR_JDB_URL, BMA_MSG_JDB_ERROREDATI, esql.getMessage(), this);
	}

	if (db.equalsIgnoreCase(JDBC_DB_ORACLE)) {
		commitSupported = true;
		pkErrorState = "23000";
		pkErrorCode = 1;
		fkSqlStateMin = "23000";
		fkSqlStateMax = "24000";
	}
	else if (db.equalsIgnoreCase(JDBC_DB_DB2NT)) {
		commitSupported = true;
		pkErrorState = "23505";
		pkErrorCode = -803;
		fkSqlStateMin = "23000";
		fkSqlStateMax = "24000";
	}
	else if (db.equalsIgnoreCase(JDBC_DB_SQLSERVER)) {
		commitSupported = true;
		pkErrorState = "23000";
		pkErrorCode = 2627;
		fkSqlStateMin = "23000";
		fkSqlStateMax = "24000";
	}
	else if (db.equalsIgnoreCase(JDBC_DB_SQLBASE)) {
		commitSupported = false;
		pkErrorState = "23000";
		pkErrorCode = 1;
		fkSqlStateMin = "23000";
		fkSqlStateMax = "24000";
	}
	
	try {
		connessione.setAutoCommit(false);
		nome = Long.toString(new java.util.Date().getTime());
		this.user = user;
		sqlStep = 0;
		aperta = true;
		valida = true;
		return true;
	}
	catch (SQLException esql2) {
		aperta = false;
		valida = false;
		throw new BmaException(BMA_ERR_JDB_CONNESSIONE, BMA_MSG_JDB_ERROREDATI, esql2.getMessage(), this);
	}
}
private void rollback() throws SQLException, BmaException {
	
	if (commitSupported) connessione.rollback();
	else {
		Statement st = getStatement();
		st.executeUpdate("ROLLBACK");
		st.close();
	}
}
public void setFonte(BmaJdbcSource fonte) {
	this.fonte = fonte;
}
}
