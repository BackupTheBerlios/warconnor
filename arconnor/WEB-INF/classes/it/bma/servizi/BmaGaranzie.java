package it.bma.servizi;

import it.bma.comuni.*;
import it.bma.web.*;
import it.bma.prodotti.*;
import java.util.*;
public class BmaGaranzie extends BmaServizio {
/**
 * BmaListaEntiEmittenti constructor comment.
 */
public BmaGaranzie() {
	super();
}
public String esegui(BmaJdbcTrx trx) throws BmaException {
	BmaGestoreProdotto gp = new BmaGestoreProdotto();
	gp.jModel = jModel;
	String nomeTabella = input.getInfoServizio("NomeTabella");
	if (nomeTabella.trim().length()==0) {
		throw new BmaException(BMA_ERR_WEB_PARAMETRO,"Manca il parametro: NomeTabella","",this);
	}
	BmaDataTable tabella = jModel.getDataTable(nomeTabella);
	if (tabella==null) {
		throw new BmaException(BMA_ERR_WEB_PARAMETRO,"Riferimento errato a tabella: " + nomeTabella,"",this);
	}
	applicaStandardDati(tabella);
	String tipoAzione = input.getInfoServizio("TipoAzione");
	if (tipoAzione.trim().length()==0) tipoAzione = BMA_SQL_SELECT;
	
	String datRiferimento = input.getInfoServizio("DatRiferimento");
	if (datRiferimento.trim().length()==0 && tipoAzione.equals(BMA_SQL_SELECT)) {
		throw new BmaException(BMA_ERR_WEB_PARAMETRO,"Manca il parametro: DatRiferimento","",this);
	}
	
	Vector dati;
	String sql = "";
	Hashtable filtri = input.getInfoServizio().getStringTable();
	filtri.put("COD_SOCIETA", input.getSocieta());
		
	if (tipoAzione.equals(BMA_SQL_SELECT)) {
		if (nomeTabella.equals("PD_GARANZIE")) {
			BmaDataColumn c = tabella.getColonna("COD_TIPOGARANZIA");
			c.setTipoControllo(BMA_CONTROLLO_LISTA);
			c.setValoriControllo(gp.valoriTipiGaranzia(trx, input.getSocieta()).getStringTable());
		}
		else if (nomeTabella.equals("PD_REGOLEGARANZIA") || nomeTabella.equals("PD_PRODREGOLEGAR")) {
			BmaDataColumn c = tabella.getColonna("COD_REGOLA");
			c.setTipoControllo(BMA_CONTROLLO_LISTA);
			c.setValoriControllo(gp.valoriRegoleStruttura(trx, input.getSocieta()).getStringTable());
			c = tabella.getColonna("COD_LIVELLO");
			c.setTipoControllo(BMA_CONTROLLO_LISTA);
			c.setValoriControllo(gp.valoriLivelliDeroga(trx, input.getSocieta()).getStringTable());
			c = tabella.getColonna("RIF_GARANZIA");
			c.setTipoControllo(BMA_CONTROLLO_LISTA);
			c.setValoriControllo(gp.valoriGaranzie(trx, input.getSocieta()).getStringTable());
		}
		else if (nomeTabella.equals("PD_PRODGAROGG")) {
			BmaDataColumn c = tabella.getColonna("COD_GARANZIA");
			c.setTipoControllo(BMA_CONTROLLO_LISTA);
			c.setValoriControllo(gp.valoriGaranzie(trx, input.getSocieta()).getStringTable());
		}
		else {
			BmaDataColumn c = tabella.getColonna("COD_GARANZIA");
			c.setTipoControllo(BMA_CONTROLLO_LISTA);
			c.setValoriControllo(gp.valoriGaranzie(trx, input.getSocieta()).getStringTable());
			tabella.setOrderBy("NUM_PROGRESSIVO");
		}
		sql = tabella.getSqlLista(filtri);
		dati = trx.eseguiSqlSelect(sql);
		BmaDataList elenco = new BmaDataList(tabella, dati);
		return elenco.toXml();
	}
	else {
		gp.eseguiAggiorna(trx, tabella, tipoAzione, filtri);
		return "";
	}
}
/**
 * getChiave method comment.
 */
public String getChiave() {
	return null;
}
/**
 * getXmlTag method comment.
 */
protected String getXmlTag() {
	return null;
}
}
