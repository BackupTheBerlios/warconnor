package it.bma.web;
import java.util.*;
import it.bma.comuni.*;
public class BmaDataDriverGeneric extends BmaObject {
	private BmaUserConfig config = null;
	private BmaJdbcSource jSource = null;
	private JdbcModel jModel = new JdbcModel();
	public BmaDataDriverGeneric() {
		super();
	}
	public String getChiave() { return null; }
	protected String getXmlTag() { return getClassName(); }
	public BmaUserConfig getUserConfig() { return config; }
	public BmaJdbcSource getJdbcSource() { return jSource; }
	public JdbcModel getJModel() { return jModel; }
	public void setUserConfig(BmaUserConfig newUserConfig) { config = newUserConfig; }
	public void setJdbcSource(BmaJdbcSource newSource) { jSource = newSource; }
	public void setJModel(JdbcModel newModel) { jModel = newModel; }
	public BmaDataList getDataList(String tabella, Hashtable condizioni) throws BmaException {
		BmaDataList list = null;
		String sql = "";
		BmaJdbcTrx jTrx = new BmaJdbcTrx(jSource);
		try {
			jTrx.open("System");
			BmaDataTable table = jModel.getDataTable(jTrx, tabella);
			Vector dati = jTrx.eseguiSqlSelect(table.getSqlLista(condizioni));
			list = new BmaDataList(table, dati);
			jTrx.chiudi();
			return list;
		}
		catch (BmaException bma) {
			if (jTrx.isAperta()) jTrx.invalida();
			throw bma;
		}
	}
	public BmaDataList getDataList(String sql, String nomeQuery) throws BmaException {
		return getDataList(sql, nomeQuery, null);
	}
	public BmaDataList getDataList(String sql, String nomeQuery, String[] colonneChiave) throws BmaException {
		BmaDataList list = null;
		BmaJdbcTrx jTrx = new BmaJdbcTrx(jSource);
		try {
			jTrx.open("System");
			list = jTrx.eseguiQuery(sql, nomeQuery);
			jTrx.chiudi();
			if (colonneChiave!=null && colonneChiave.length>0) list.setColonneChiave(colonneChiave);
			return list;
		}
		catch (BmaException bma) {
			if (jTrx.isAperta()) jTrx.invalida();
			throw bma;
		}
	}
	public Hashtable getValoriControllo(String sql) throws BmaException {
		Hashtable valori = new Hashtable();
		BmaDataList list = getDataList(sql, "ValoriControllo", null);
		for (int i=0;i<list.getValori().size();i++) {
			Vector riga = (Vector)list.getValori().elementAt(i);
			valori.put((String)riga.elementAt(0), (String)riga.elementAt(1));
		}
		return valori;
	}
	public void aggiorna(String tabella, Hashtable valori, String azione) throws BmaException {
		BmaJdbcTrx jTrx = new BmaJdbcTrx(jSource);
		try {
			jTrx.open("System");
			BmaDataTable table = jModel.getDataTable(jTrx, tabella);
			String sql = "";
			if (azione.equals(BMA_SQL_INSERT)) sql = table.getSqlInsert(valori);
			else if (azione.equals(BMA_SQL_UPDATE)) sql = table.getSqlUpdate(valori);
			else if (azione.equals(BMA_SQL_DELETE)) sql = table.getSqlDelete(valori);
			jTrx.eseguiSqlUpdate(sql);
			jTrx.chiudi();
		}
		catch (BmaException bma) {
			if (jTrx.isAperta()) jTrx.invalida();
			throw bma;
		}
	}
}