package it.bma.servizi;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class ProdottoOperativo extends BmaServizio {
	public ProdottoOperativo() {
		super();
	}
	public String esegui(BmaJdbcTrx trx) throws it.bma.comuni.BmaException {
		// Lettura dei parametri di attivazione generali
		String codAzione = input.getInfoServizio("codAzione");
		if (codAzione.equals("elimina")) {
			return eliminaProdotto(trx);
		}
		else if (codAzione.equals("ricodifica")) {
			return ricodifica(trx);
		}
		return "";
	}
	private String eliminaProdotto(BmaJdbcTrx jTrx) throws BmaException {
		String codProdotto = input.getInfoServizio("COD_PRODOTTO");
		if (codProdotto==null || codProdotto.trim().length()==0) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Manca parametro prodotto","",  this);
		String rifVersione = input.getInfoServizio("RIF_VERSIONE");
		if (rifVersione==null || rifVersione.trim().length()==0) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Manca parametro versione","",  this);
		String sql="";
		sql = "SELECT * FROM PD_PRODOTTI " +
					" WHERE COD_SOCIETA='" + input.getSocieta() + "' " +
					" AND		COD_PRODOTTO='" + codProdotto + "' " +
					" AND		RIF_VERSIONE='" + rifVersione + "'" +
					" AND		COD_STATO='X'";
		Vector dati = jTrx.eseguiSqlSelect(sql);
		if (dati.size()!=1) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Prodotto inesistente o non estinto (Stato=X)", "", this);
		Hashtable condizioni = new Hashtable();
		condizioni.put("COD_SOCIETA", input.getSocieta());
		condizioni.put("COD_PRODOTTO", codProdotto);
		condizioni.put("RIF_VERSIONE", rifVersione);
		BmaVector vLoad = jModel.getLoadOrder();
		Vector vSql = new Vector();
		for (int i=vLoad.getSize()-1;i>=0;i--) {
			BmaValuesList vl = (BmaValuesList)vLoad.getElement(i);
			String[] n = vl.getValues();
			for (int j=0;j<n.length;j++) {
				BmaDataTable table = jModel.getDataTable(n[j]);
				if (table.getColonna("COD_SOCIETA")!= null &&
						table.getColonna("COD_PRODOTTO") != null &&
						table.getColonna("RIF_VERSIONE") != null) {
					dati = jTrx.eseguiSqlSelect(table.getSqlLista(condizioni));
					if (dati.size()>0) {
						sql = "DELETE FROM " + table.getChiave() +
									" WHERE COD_SOCIETA='" + input.getSocieta() + "' " +
									" AND		COD_PRODOTTO='" + codProdotto + "' " +
									" AND		RIF_VERSIONE='" + rifVersione + "'";
						vSql.add(sql);
					}
				}
			}
		}
		for (int i=0;i<vSql.size();i++) {
			sql = (String)vSql.elementAt(i);
			jTrx.eseguiSqlUpdate(sql);
		}
		return "";
	}
	private String ricodifica(BmaJdbcTrx jTrx) throws BmaException {
		String codColonna = input.getInfoServizio("COD_COLONNA");
		if (codColonna==null || codColonna.trim().length()==0) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Manca parametro codice colonna","",  this);
		String desOld = input.getInfoServizio("DES_OLD");
		if (desOld==null || desOld.trim().length()==0) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Manca parametro valore attuale","",  this);
		String desNew = input.getInfoServizio("DES_NEW");
		if (desNew==null || desNew.trim().length()==0) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Manca parametro nuovo valore","",  this);
		
		Vector vTabelle = jModel.getTableNames();
		Hashtable cmdInsert = new Hashtable();
		Hashtable cmdDelete = new Hashtable();
		Hashtable cmdUpdate = new Hashtable();
		for(int i=0;i<vTabelle.size();i++) {
			String sTabella = (String)vTabelle.elementAt(i);
			BmaDataTable tabella = jModel.getDataTable(sTabella);
			String nTabella = tabella.getChiave();
			Vector colonneValide = new Vector();
			BmaDataColumn colonna = tabella.getColonna(codColonna);
			if (colonna!=null) colonneValide.add(colonna);
			Vector alias = jModel.getAlias(codColonna, sTabella);
			for(int j=0;j<alias.size();j++) {
				String aliasName = (String)alias.elementAt(j);
				BmaDataColumn c = tabella.getColonna(aliasName);
				if (c!=null) colonneValide.add(c);
			}
			if (colonneValide.size()>0) {
				String cond = "";
				for(int j=0;j<colonneValide.size();j++) {
					colonna = (BmaDataColumn)colonneValide.elementAt(j);
					cond = cond + "OR " + colonna.getCondizioneSql(BMA_SQL_OPE_EQ, desOld);
				}
				if (cond.length()>2) cond = cond.substring(2);
				cond = "(" + cond + ")";
				colonna = tabella.getColonna("COD_SOCIETA");
				cond = cond + " AND " + colonna.getCondizioneSql(BMA_SQL_OPE_EQ, input.getSocieta());
				
				Vector dati = jTrx.eseguiSqlSelect(tabella.getSqlLista(cond));
				Vector vInsert = new Vector();
				Vector vDelete = new Vector();
				Vector vUpdate = new Vector();
				for(int j=0;j<dati.size();j++) {
					Vector riga = (Vector)dati.elementAt(j);
					tabella.setValori(riga);
					String sqlDelete = tabella.getSqlDelete();
					boolean bUpdate = true;
					for(int t=0;t<colonneValide.size();t++) {
						colonna = (BmaDataColumn)colonneValide.elementAt(t);
						String v = colonna.getValore();
						if (v.equals(desOld)) {
							colonna.setValore(desNew);
							if (colonna.isChiave()) bUpdate = false;
						}
					}
					if (bUpdate) {
						String sqlUpdate = tabella.getSqlUpdate();
						vUpdate.add(sqlUpdate);
					}
					else {
						String sqlInsert = tabella.getSqlInsert();
						vDelete.add(sqlDelete);
						vInsert.add(sqlInsert);
					}
				}
				if (vUpdate.size()>0) cmdUpdate.put(nTabella, vUpdate);
				if (vDelete.size()>0) cmdDelete.put(nTabella, vDelete);
				if (vInsert.size()>0) cmdInsert.put(nTabella, vInsert);
			}
		}
		BmaVector vLoad = jModel.getLoadOrder();
		for(int i=0;i<vLoad.getSize();i++) {
			BmaValuesList vl = (BmaValuesList)vLoad.getElement(i);
			String[] n = vl.getValues();
			for (int j=0;j<n.length;j++) {
				Vector vSql = (Vector)cmdInsert.get(n[j]);
				if (vSql!=null) {
					for(int t=0;t<vSql.size();t++) {
						String sql = (String)vSql.elementAt(t);
						jTrx.eseguiSqlUpdate(sql);
					}
				}
			}
		}
		for (int i=vLoad.getSize()-1;i>=0;i--) {
			BmaValuesList vl = (BmaValuesList)vLoad.getElement(i);
			String[] n = vl.getValues();
			for (int j=0;j<n.length;j++) {
				Vector vSql = (Vector)cmdUpdate.get(n[j]);
				if (vSql!=null) {
					for(int t=0;t<vSql.size();t++) {
						String sql = (String)vSql.elementAt(t);
						jTrx.eseguiSqlUpdate(sql);
					}
				}
			}
		}
		for (int i=vLoad.getSize()-1;i>=0;i--) {
			BmaValuesList vl = (BmaValuesList)vLoad.getElement(i);
			String[] n = vl.getValues();
			for (int j=0;j<n.length;j++) {
				Vector vSql = (Vector)cmdDelete.get(n[j]);
				if (vSql!=null) {
					for(int t=0;t<vSql.size();t++) {
						String sql = (String)vSql.elementAt(t);
						jTrx.eseguiSqlUpdate(sql);
					}
				}
			}
		}
		return "";
	}
}
