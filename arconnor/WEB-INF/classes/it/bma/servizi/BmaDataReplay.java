package it.bma.servizi;

import it.bma.comuni.*;
import it.bma.web.*;
import java.util.*;
public class BmaDataReplay extends BmaServizio {
	private final String REPLACE_MAIN = "ReplaceMain";
	private final String REPLACE_TARGET = "ReplaceTarget";
	private final String MERGE_MAIN = "MergeMain";
	private final String MERGE_TARGET = "MergeTarget";
	private String replayMode = "";
	private String checkOnly = BMA_TRUE;
	private Vector tableNames = new Vector();
	private BmaVector logInfo = new BmaVector("LogInfo");
	private BmaVector sqlDeleteMain = new BmaVector();
	private BmaVector sqlUpdateMain = new BmaVector();
	private BmaVector sqlInsertMain = new BmaVector();
	private BmaVector sqlDeleteTarg = new BmaVector();
	private BmaVector sqlUpdateTarg = new BmaVector();
	private BmaVector sqlInsertTarg = new BmaVector();
	public BmaDataReplay() {
		super();
	}
	private void compareTable(BmaJdbcTrx trxMain, BmaJdbcTrx trxTarget, BmaDataTable table) throws BmaException {
		String msg = "";
		String sql = "";
		BmaLogInfo info = null;
		applicaStandardDati(table);
		sql = table.getSqlLista("");
	// 
		Vector dati = trxMain.eseguiSqlSelect(sql);
		Hashtable mRighe = new Hashtable(dati.size());
		for (int i = 0; i < dati.size(); i++){
			Vector v = (Vector)dati.elementAt(i);
			table.setValoriDaQuery(v);
			mRighe.put(table.getValoreChiave(), table.getValori());
		}
	//
		dati = trxTarget.eseguiSqlSelect(sql);
		Hashtable tRighe = new Hashtable(dati.size());
		for (int i = 0; i < dati.size(); i++){
			Vector v = (Vector)dati.elementAt(i);
			table.setValoriDaQuery(v);
			tRighe.put(table.getValoreChiave(), table.getValori());
		}
	// Esamina da Main
		Enumeration eM = mRighe.keys();
		while (eM.hasMoreElements()) {
			String k = (String)eM.nextElement();
			Vector vMain = (Vector)mRighe.get(k);
			Vector vTarg = (Vector)tRighe.get(k);
			if (vTarg==null) {
				info = new BmaLogInfo("", table.getChiave(), k, "", "");
				if (replayMode.equals(REPLACE_MAIN)) {
					table.setValoriDaQuery(vMain);
					sql = table.getSqlDelete();
					sqlDeleteMain.setString(table.getChiave(), sql);
					info.setAction("DELETE-MAIN");
				}
				else {
					table.setValoriDaQuery(vMain);
					sql = table.getSqlInsert();
					sqlInsertTarg.setString(table.getChiave(), sql);
					info.setAction("INSERT-TARGET");
				}
				logInfo.add(info);
			}
			else {
				boolean bUpdate = false;
				for (int i = 0; i < vMain.size() && !bUpdate; i++){
					String vM = (String)vMain.elementAt(i);
					String vT = (String)vTarg.elementAt(i);
					if (!vM.equals(vT)) {
						bUpdate = true;
						info = new BmaLogInfo("", table.getChiave(), k, "", "(MV=" + vM + ") notEqualTo (TV=" + vT + ")");
					}
				}
				if (bUpdate && (replayMode.equals(REPLACE_TARGET) || replayMode.equals(MERGE_TARGET))) {
					info.setAction("UPDATE-TARGET");
					table.setValoriDaQuery(vMain);
					sql = table.getSqlUpdate();
					sqlUpdateTarg.setString(table.getChiave(), sql);
				}
				else if (bUpdate && (replayMode.equals(REPLACE_MAIN) || replayMode.equals(MERGE_MAIN)))  {
					info.setAction("UPDATE-MAIN");
					table.setValoriDaQuery(vTarg);
					sql = table.getSqlUpdate();
					sqlUpdateMain.setString(table.getChiave(), sql);
				}
				if (bUpdate) logInfo.add(info);
			}
		}
	// Esamina da Target
		Enumeration eT = tRighe.keys();
		while (eT.hasMoreElements()) {
			String k = (String)eT.nextElement();
			Vector vTarg = (Vector)tRighe.get(k);
			Vector vMain = (Vector)mRighe.get(k);
			if (vMain==null) {
				info = new BmaLogInfo("", table.getChiave(), k, "", "");
				if (replayMode.equals(REPLACE_TARGET)) {
					table.setValoriDaQuery(vTarg);
					sql = table.getSqlDelete();
					sqlDeleteTarg.setString(table.getChiave(), sql);
					info.setAction("DELETE-TARGET");
				}
				else {
					table.setValoriDaQuery(vTarg);
					sql = table.getSqlInsert();
					sqlInsertMain.setString(table.getChiave(), sql);
					info.setAction("INSERT-MAIN");
				}
				logInfo.add(info);
			}
		}
	}
	public String esegui(BmaJdbcTrx trx) throws BmaException {
		// Parametri
		String loadTables = input.getInfoServizio("loadTables");
		if (loadTables!=null && loadTables.equals(BMA_TRUE)) return tableList();
		//
		String targetSource = input.getInfoServizio("COD_TARGET");
		replayMode = input.getInfoServizio("IND_REPLAYMODE");
		checkOnly = input.getInfoServizio("FLG_CHECKONLY");
		if (checkOnly==null || !checkOnly.equals(BMA_FALSE)) checkOnly=BMA_TRUE;
		// Controlli
		if (targetSource==null) throw new BmaException(BMA_ERR_WEB_PARAMETRO,"Target Source","",this);
		BmaJdbcSource jTarget = userConfig.getJdbcSource(targetSource);
		if (jTarget==null) throw new BmaException(BMA_ERR_WEB_PARAMETRO,"Target Source not found",targetSource,this);
		// Determina statements da eseguire
		loadNames();
		BmaJdbcTrx trxTarget = new BmaJdbcTrx(jTarget);
		try {
			trxTarget.open("system");
			for (int i = 0; i < tableNames.size(); i++){
				String tableName = (String)tableNames.elementAt(i);
				if (input.getInfoServizio(tableName)!=null && input.getInfoServizio(tableName).equals(tableName)) {
					BmaDataTable table = jModel.getDataTable(tableName);
					compareTable(trx, trxTarget, table);
				}
			}
			if (checkOnly.equals(BMA_FALSE)) {
				execDelete(trx, sqlDeleteMain);
				execUpdate(trx, sqlInsertMain);
				execUpdate(trx, sqlUpdateMain);
				execDelete(trxTarget, sqlDeleteTarg);
				execUpdate(trxTarget, sqlInsertTarg);
				execUpdate(trxTarget, sqlUpdateTarg);
			}
			trxTarget.chiudi();
			return logInfo.toXml();
		} 
		catch (BmaException e) {
			if (trxTarget.isAperta() || trxTarget.isValida()) {
				trxTarget.invalida();
			}
			throw e;
		}
	}
	private void execDelete(BmaJdbcTrx trx, BmaVector commands) throws BmaException {
		if (commands.getSize()==0) return;
		for (int i = tableNames.size() - 1; i>=0; i--){
			String tbName = (String)tableNames.elementAt(i);
			for (int j = 0; j < commands.getSize(); j++){
				BmaParametro p = (BmaParametro)commands.getElement(j);
				if (p.getNome().equals(tbName)) {
					trx.eseguiSqlUpdate(p.getValore());
				}
			}
		}
	}
	private void execUpdate(BmaJdbcTrx trx, BmaVector commands) throws BmaException {
		if (commands.getSize()==0) return;
		for (int i = 0; i < tableNames.size(); i++){
			String tbName = (String)tableNames.elementAt(i);
			for (int j = 0; j < commands.getSize(); j++){
				BmaParametro p = (BmaParametro)commands.getElement(j);
				if (p.getNome().equals(tbName)) {
					trx.eseguiSqlUpdate(p.getValore());
				}
			}
		}
	}
	private void loadNames() {
		BmaVector loadOrder = jModel.getLoadOrder();
		for (int i = 0; i < loadOrder.getSize(); i++){
			BmaValuesList bvl = (BmaValuesList)loadOrder.getElement(i);
			String[] s = bvl.getValues();
			for (int j = 0; j < s.length; j++){
				tableNames.add(s[j]);
			}
		}
	}
	private String tableList() {
		BmaVector table = new BmaVector("tablesNames");
		loadNames();
		Object list[] = tableNames.toArray();
		Arrays.sort(list);
		for (int i = 0; i < list.length; i++){
			String s = (String)list[i];
			table.setString(s);
		}
		return table.toXml();
	}
}
