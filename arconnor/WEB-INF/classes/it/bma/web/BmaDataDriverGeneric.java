package it.bma.web;
import java.util.*;
import it.bma.comuni.*;
public class BmaDataDriverGeneric extends BmaObject {
	private BmaUserConfig config = null;
	private BmaJdbcSource jSource = null;
	public BmaDataDriverGeneric() {
		super();
	}
	public String getChiave() { return null; }
	protected String getXmlTag() { return getClassName(); }
	public BmaUserConfig getUserConfig() { return config; }
	public BmaJdbcSource getJdbcSource() { return jSource; }
	public void setUserConfig(BmaUserConfig newUserConfig) { config = newUserConfig; }
	public void setJdbcSource(BmaJdbcSource newSource) { jSource = newSource; }
	public BmaDataList getDataList(String tabella, Hashtable condizioni) throws BmaException {
		BmaDataList list = null;
		String sql = "";
		BmaJdbcTrx jTrx = new BmaJdbcTrx(jSource);
		try {
			jTrx.open("System");
			JdbcModel jModel = new JdbcModel();
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
	public void aggiorna(String tabella, Hashtable valori, String azione) throws BmaException {
		BmaJdbcTrx jTrx = new BmaJdbcTrx(jSource);
		try {
			jTrx.open("System");
			JdbcModel jModel = new JdbcModel();
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