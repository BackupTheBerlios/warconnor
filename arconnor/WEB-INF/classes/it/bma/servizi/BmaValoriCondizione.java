package it.bma.servizi;

import it.bma.comuni.*;
import it.bma.web.*;
import it.bma.prodotti.*;
import java.util.*;
public class BmaValoriCondizione extends BmaServizio {
/**
 * BmaListaEntiEmittenti constructor comment.
 */
public BmaValoriCondizione() {
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
	Hashtable filtri = new Hashtable();
	
	if (tipoAzione.equals(BMA_SQL_SELECT)) {
		BmaDataColumn c = tabella.getColonna("COD_LIVELLO");
		c.setTipoControllo(BMA_CONTROLLO_LISTA);
		c.setValoriControllo(gp.valoriLivelliDeroga(trx, input.getSocieta()).getStringTable());
		sql = tabella.getSqlLista(input.getInfoServizio().getStringTable());
		dati = trx.eseguiSqlSelect(sql);
		BmaDataList elenco = new BmaDataList(tabella, dati);
		return elenco.toXml();
	}
	else if (tipoAzione.equals(BMA_SQL_INSERT)) {
		sql = tabella.getSqlInsert(input.getInfoServizio().getStringTable());
		trx.eseguiSqlUpdate(sql);
		return "";
	}		
	else if (tipoAzione.equals(BMA_SQL_UPDATE)) {
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
