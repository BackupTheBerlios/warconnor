package it.bma.web;

import it.bma.comuni.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;
public class BmaDatiServlet extends javax.servlet.http.HttpServlet {
	private String lastMessage = "";
	private JdbcModel jModel = new JdbcModel();
private void apriPagina(HttpServletRequest req, HttpServletResponse res) {
	String jspPage = "/db/start.jsp";
	try {
		getServletContext().getRequestDispatcher(jspPage).forward(req, res);
	}
	catch (Exception e) {
		System.out.println(e.getMessage());
	}
}
public void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
	esegui(request, response);
}
public void doPost(HttpServletRequest request,HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
	esegui(request, response);
}
private void esegui(HttpServletRequest request, HttpServletResponse response) {
	lastMessage = "";
	// Esamina i possibili parametri
	String azione = request.getParameter("azione");
	if (azione == null)
		azione = "";
	String comando = request.getParameter("comando");
	if (comando == null)
		comando = "";
	String driver = request.getParameter("driver");
	if (driver == null)
		driver = "";
	String nomeFonte = request.getParameter("nomeFonte");
	String url = request.getParameter("url");
	String user = request.getParameter("user");
	String pass = request.getParameter("pass");
	String schema = request.getParameter("schema");
	String prefix = request.getParameter("prefix");
	String tabella = request.getParameter("tabella");
	String selezione = request.getParameter("selezione");
	BmaJdbcSource fonte = null;
	try {
		if (azione.trim().length() > 0) {
			fonte = getFonte(nomeFonte, driver, url, user, pass);
			fonte.setSchema(schema);
			fonte.setPrefix(prefix);
			loadJdbcModel(fonte);
			request.setAttribute("fonte", fonte);
			request.setAttribute("schema", schema);
			request.setAttribute("prefix", prefix);
			request.setAttribute("catalogo", jModel.getTableNames());
		}
		if (azione.equals("catalogo") && comando.equals("apri")) {
			tabella = selezione;
			request.setAttribute("tabella", tabella);
			if (tabella.equals("SQL Query")) {
				azione = "SQL";
			}
			else {
				azione = impostaElenco(request, fonte, schema, tabella);
			}
		}

		else if (azione.equals("SQL") && comando.equals("esegui")) {
			String sqlQuery = request.getParameter("sqlQuery");
			request.setAttribute("sqlQuery", sqlQuery);
			request.setAttribute("tabella", tabella);
			azione = impostaQuerySql(request, fonte, sqlQuery);
		}
		else if (azione.equals("elenco") && comando.equals("nuovo")) {
			request.setAttribute("tabella", tabella);
			BmaDataForm dettaglio = getFormDati(tabella, "",fonte);
			request.setAttribute("dettaglio", dettaglio);
			azione = "inserimento";
		}

		else if (azione.equals("elenco") && comando.equals("modifica")) {
			request.setAttribute("tabella", tabella);
			BmaDataForm dettaglio = getFormDati(tabella, selezione, fonte);
			request.setAttribute("dettaglio", dettaglio);
			azione = "modifica";
		}

		else if (azione.equals("inserimento") && comando.equals("aggiorna")) {
			request.setAttribute("tabella", tabella);
			BmaDataTable t = jModel.getDataTable(tabella);
			eseguiUpdate(fonte, t.getSqlInsert(getRequestParameters(request)));
			azione = impostaElenco(request, fonte, schema, tabella);
		}

		else if (azione.equals("inserimento") && comando.equals("annulla")) {
			request.setAttribute("tabella", tabella);
			azione = impostaElenco(request, fonte, schema, tabella);
		}

		else if (azione.equals("modifica") && comando.equals("aggiorna")) {
			request.setAttribute("tabella", tabella);
			BmaDataTable t = jModel.getDataTable(tabella);
			eseguiUpdate(fonte, t.getSqlUpdate(getRequestParameters(request)));
			azione = impostaElenco(request, fonte, schema, tabella);
		}

		else if (azione.equals("modifica") && comando.equals("duplica")) {
			request.setAttribute("tabella", tabella);
			BmaDataTable t = jModel.getDataTable(tabella);
			t.setValori(getRequestParameters(request));
			BmaDataForm dettaglio = getFormDati(t);
			request.setAttribute("dettaglio", dettaglio);
			azione = "inserimento";
		}

		else if (azione.equals("modifica") && comando.equals("elimina")) {
			request.setAttribute("tabella", tabella);
			BmaDataTable t = jModel.getDataTable(tabella);
			eseguiUpdate(fonte, t.getSqlDelete(getRequestParameters(request)));
			azione = impostaElenco(request, fonte, schema, tabella);
		}

		else if (azione.equals("modifica") && comando.equals("annulla")) {
			request.setAttribute("tabella", tabella);
			azione = impostaElenco(request, fonte, schema, tabella);
		}

	}
	catch (BmaException ec) {
		lastMessage = ec.getMessage();
	}
	request.setAttribute("azione", azione);
	request.setAttribute("beanMessaggio", normalizzaTesto(lastMessage));
	apriPagina(request, response);
}
private Vector eseguiQuery(BmaJdbcSource fonte, String sql) throws BmaException {
	if (fonte == null) return null;
	BmaJdbcTrx trx = new BmaJdbcTrx(fonte);
	try {
		trx.open("SYSTEM");
		Vector dati = trx.eseguiSqlSelect(sql);
		trx.chiudi();
		return dati;
	}
	catch (BmaException ec) {
		trx.invalida();
		throw new BmaException(ec.getMessage() + "(" + sql + ")");
	}
}
private boolean eseguiUpdate(BmaJdbcSource fonte, String sql) throws BmaException {
	if (fonte == null) return false;
	BmaJdbcTrx trx = new BmaJdbcTrx(fonte);
	try {
		trx.open("SYSTEM");
		trx.eseguiSqlUpdate(sql);
		trx.chiudi();
		return true;
	}
	catch (BmaException e) {
		trx.invalida();
		throw e;
	}
}
private BmaJdbcSource getFonte(String nome, String driver, String url, String user, String pass) {
	BmaJdbcSource fj = new BmaJdbcSource(nome);
	fj.setDriver(driver);
	fj.setUrl(url);
	fj.setUser(user);
	fj.setPass(pass);
	return fj;
}
private BmaDataForm getFormDati(BmaDataTable dataTable) throws BmaException {
	BmaDataForm t = new BmaDataForm(dataTable);
	return t;
}
private BmaDataForm getFormDati(String tabella, String selezione, BmaJdbcSource fonte) throws BmaException {
	BmaDataTable t = jModel.getDataTable(tabella);
	if (selezione.trim().length()>0) {
		BmaDataKey k = t.getBmaDataKey();
		k.impostaDaQueryString(selezione);
		Vector dati = eseguiQuery(fonte, t.getSqlLista(k.getValori()));
		if (dati.size() != 1) throw new BmaException(k.BMA_ERR_JDB_SQL,"Selezione non univoca",selezione,t);
		t.setValoriDaQuery((Vector)dati.elementAt(0));
	}
	return new BmaDataForm(t);
}
private Hashtable getRequestParameters(HttpServletRequest request) {
	Hashtable h = new Hashtable();
	Enumeration e = request.getParameterNames();
	while (e.hasMoreElements()) {
		String n = (String)e.nextElement();
		String v = request.getParameter(n);
		h.put(n,v);
	}
	return h;
}
private String impostaElenco(HttpServletRequest request, BmaJdbcSource fonte, String schema, String tabella) throws BmaException {
	if (tabella.trim().length() > 0) {
		BmaDataTable tsg = jModel.getDataTable(tabella);
		tsg.setOrderByKeys();
		Vector dati = eseguiQuery(fonte, tsg.getSqlLista(""));
		BmaDataList elenco = new BmaDataList(tsg, dati);
		request.setAttribute("elenco", elenco);
		return "elenco";
	}
	else {
		return "catalogo";
	}
}
private String impostaQuerySql(HttpServletRequest request, BmaJdbcSource fonte, String sql) throws BmaException {
	if (fonte == null) return null;
	BmaDataList elenco = new BmaDataList("SqlQuery");
	BmaJdbcTrx trx = new BmaJdbcTrx(fonte);
	try {
		trx.open("SYSTEM");
		Connection cn = trx.getConnessione();
		Statement st = cn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if (rs==null) throw new BmaException("Result null: ");
		int colNo = rs.getMetaData().getColumnCount();
		Vector cols = new Vector();
		for (int i = 0; i < colNo; i++){
			String colName = rs.getMetaData().getColumnName(i + 1);
			if (colName==null) colName = "";
			String colType = rs.getMetaData().getColumnTypeName(i + 1);
			if (colType==null) colType = "";
			int len = rs.getMetaData().getPrecision(i + 1);
			String colLen  = Integer.toString(len);
			int dec = rs.getMetaData().getScale(i + 1);
			String colDec = Integer.toString(dec);
//			String colLabel = rs.getMetaData().getColumnLabel(i +1);
//			if (colLabel==null) colLabel = "";

			elenco.impostaDato(false, colName, colType, colLen, colDec);
		}
		Vector dati = new Vector();
		while (rs.next()) {
			Vector riga = new Vector();
			for (int i = 0; i < colNo; i++){
				String v = rs.getString(i + 1);
				if (v==null) v = "";
				riga.add(v.trim());
			}
			dati.add(riga);
		}
		rs.close();
		st.close();
		trx.chiudi();
		elenco.setValori(dati);
		request.setAttribute("elenco", elenco);
		return "elenco";
	}
	catch (BmaException ec) {
		trx.invalida();
		lastMessage = ec.getMessage() + "(" + sql + ")";
		return "SQL";
	}
	catch (SQLException e2) {
		trx.invalida();
		lastMessage = e2.getMessage() + "(" + sql + ")";
		return "SQL";
	}
}
public void init() {
}
private void loadJdbcModel(BmaJdbcSource fonte) throws BmaException {
	BmaJdbcTrx trx = new BmaJdbcTrx(fonte);
	if (jModel.getJSource().toXml().equals(fonte.toXml())) return;
	try {
		trx.open("SYSTEM");
		jModel = new JdbcModel();
		jModel.load(trx, fonte.getSchema(), fonte.getPrefix());
		trx.chiudi();
	}
	catch (BmaException e1) {
		trx.invalida();
		throw e1;
	}
	catch (Exception e2) {
		trx.invalida();
		throw new BmaException(trx.BMA_ERR_JDB_MODEL,e2.getClass().getName(),e2.getMessage(),fonte);
	}
}
private String normalizzaTesto(String in) {
	String t = in;
	t = t.replace('\'', '\u0060');
	t = t.replace('\n', '\t');
	t = t.replace('\r', '\t');
	t = t.replace('\"', '\u0060');
	return t;
}
}
