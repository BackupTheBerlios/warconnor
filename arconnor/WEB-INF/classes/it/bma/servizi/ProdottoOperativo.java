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
					sql = "DELETE FROM " + table.getChiave() + 
								" WHERE COD_SOCIETA='" + input.getSocieta() + "' " +
								" AND		COD_PRODOTTO='" + codProdotto + "' " +
								" AND		RIF_VERSIONE='" + rifVersione + "'";
					vSql.add(sql);
				}
			}
		}
		for (int i=0;i<vSql.size();i++) {
			sql = (String)vSql.elementAt(i);
			jTrx.eseguiSqlUpdate(sql);
		}
		return "";
	}
}
