package it.bma.servizi;

import it.bma.comuni.*;
import it.bma.web.*;
import it.bma.prodotti.*;
import java.util.*;
public class BmaProdotti extends BmaServizio {
/**
 * BmaListaEntiEmittenti constructor comment.
 */
public BmaProdotti() {
	super();
}
public String esegui(BmaJdbcTrx trx) throws BmaException {
	BmaGestoreProdotto gp = new BmaGestoreProdotto();
	gp.jModel = jModel;
	String nomeTabella = "PD_PRODOTTI";
	BmaDataTable tabella = jModel.getDataTable(nomeTabella);
	applicaStandardDati(tabella);
	if (tabella==null) {
		throw new BmaException(BMA_ERR_WEB_PARAMETRO,"Riferimento errato a tabella: " + nomeTabella,"",this);
	}
	String tipoAzione = input.getInfoServizio("TipoAzione");
	if (tipoAzione.trim().length()==0) tipoAzione = BMA_SQL_SELECT;
	String datRiferimento = input.getInfoServizio("DatRiferimento");
	if (datRiferimento.trim().length()==0 && tipoAzione.equals(BMA_SQL_SELECT)) {
		throw new BmaException(BMA_ERR_WEB_PARAMETRO,"Manca il parametro: DatRiferimento","",this);
	}
	String tipRicerca = input.getInfoServizio("TipoRicerca");
	if (tipRicerca.trim().length()==0) tipRicerca = "Codice";
	String valRicerca = input.getInfoServizio("ValoreRicerca");
	
	Vector dati;
	String sql = "";
	Hashtable filtri = new Hashtable();
		
	if (tipoAzione.equals(BMA_SQL_SELECT)) {
		tabella.getColonna("FLG_STRUTTURA").setTipoControllo(BMA_CONTROLLO_HIDDEN);
		tabella.getColonna("FLG_PROPRIETA").setTipoControllo(BMA_CONTROLLO_HIDDEN);
		BmaDataColumn c = tabella.getColonna("COD_EMITTENTE");
		c.setTipoControllo(BMA_CONTROLLO_LISTA);
		c.setValoriControllo(gp.valoriEntiEmittenti(trx, input.getSocieta()).getStringTable());
		c = tabella.getColonna("COD_STATO");
		c.setTipoControllo(BMA_CONTROLLO_LISTA);
		c.setValoriControllo(gp.valoriStatiPrincipali(trx, input.getSocieta()).getStringTable());
		filtri.put("COD_SOCIETA", input.getSocieta());
		String cond = "";
		cond = cond + tabella.getCondizioneColonna("COD_SOCIETA", BMA_SQL_OPE_EQ, input.getSocieta());
		if (tipRicerca.equals("Codice")) {
			cond = cond + " AND " + tabella.getCondizioneColonna("COD_PRODOTTO", BMA_SQL_OPE_LIKE, valRicerca);
		}
		else {
			cond = cond + " AND " +  tabella.getCondizioneColonna("DES_PRODOTTO", BMA_SQL_OPE_LIKE, valRicerca);
		}
		sql = tabella.getSqlLista(cond);
		dati = trx.eseguiSqlSelect(sql);
		BmaDataList elenco = new BmaDataList(tabella, dati);
		return elenco.toXml();
	}
	else if (tipoAzione.equals(BMA_SQL_INSERT)) {
		input.getInfoServizio().setString("FLG_STRUTTURA", BMA_FALSE);
		input.getInfoServizio().setString("FLG_PROPRIETA", BMA_TRUE);
		sql = tabella.getSqlInsert(input.getInfoServizio().getStringTable());
		trx.eseguiSqlUpdate(sql);
		return "";
	}		
	else if (tipoAzione.equals(BMA_SQL_UPDATE)) {
		input.getInfoServizio().setString("FLG_STRUTTURA", BMA_FALSE);
		input.getInfoServizio().setString("FLG_PROPRIETA", BMA_TRUE);
		sql = tabella.getSqlUpdate(input.getInfoServizio().getStringTable());
		trx.eseguiSqlUpdate(sql);
		return "";
	}
	else if (tipoAzione.equals(BMA_SQL_DELETE)) {
		sql = tabella.getSqlDelete(input.getInfoServizio().getStringTable());
		trx.eseguiSqlUpdate(sql);
		return "";
	}
	else {
		throw new BmaException(BMA_ERR_WEB_PARAMETRO,"Tipo di Azione non previsto: " + tipoAzione,"",this);
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
