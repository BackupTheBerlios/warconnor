package it.bma.servizi;

import it.bma.comuni.*;
import it.bma.web.*;
import java.util.*;
public class BmaCodifiche extends BmaServizio {
/**
 * BmaListaEntiEmittenti constructor comment.
 */
public BmaCodifiche() {
	super();
}
public String esegui(BmaJdbcTrx trx) throws BmaException {
	BmaGestoreProdotto gp = new BmaGestoreProdotto();
	gp.jModel = jModel;
	String nomeTabella = input.getInfoServizio("NomeTabella");
	String tipoAzione = input.getInfoServizio("TipoAzione");
	
	if (nomeTabella.trim().length()==0) {
		throw new BmaException(BMA_ERR_WEB_PARAMETRO,"Manca il parametro: NomeTabella","",this);
	}
	if (tipoAzione.trim().length()==0) tipoAzione = BMA_SQL_SELECT;
	
	BmaDataTable tabella = jModel.getDataTable(nomeTabella);
	if (tabella==null) {
		throw new BmaException(BMA_ERR_WEB_PARAMETRO,"Riferimento errato a tabella: " + nomeTabella,"",this);
	}
	applicaStandardDati(tabella);
	
	Vector dati;
	String sql = "";
	Hashtable filtri = input.getInfoServizio().getStringTable();
	filtri.put("COD_SOCIETA", input.getSocieta());
		
	if (tipoAzione.equals(BMA_SQL_SELECT)) {
		if (nomeTabella.equals("PD_TARIFFE")) {
			BmaDataColumn c = tabella.getColonna("COD_STATO");
			c.setTipoControllo(BMA_CONTROLLO_LISTA);
			c.setValoriControllo(gp.valoriStatiTariffa(trx, input.getSocieta()).getStringTable());
		}
		else if (nomeTabella.equals("PD_AUTORIZZATARIFFA")) {
			BmaDataColumn c = tabella.getColonna("COD_TIPOSTATO");
			c.setTipoControllo(BMA_CONTROLLO_LISTA);
			c.setValoriControllo(gp.valoreTipoStatoTariffa());
			c = tabella.getColonna("COD_STATO");
			c.setTipoControllo(BMA_CONTROLLO_LISTA);
			c.setValoriControllo(gp.valoriStatiTariffa(trx, input.getSocieta()).getStringTable());
			c = tabella.getColonna("COD_POSIZIONE");
			c.setTipoControllo(BMA_CONTROLLO_LISTA);
			c.setValoriControllo(gp.valoriPosizioniAziendali(trx, input.getSocieta()).getStringTable());
		}
		else if (nomeTabella.equals("PD_CONVENZIONIPUNTO")) {
			BmaDataColumn c = tabella.getColonna("COD_CONVENZIONE");
			c.setTipoControllo(BMA_CONTROLLO_LISTA);
			c.setValoriControllo(gp.valoriConvenzioni(trx, input.getSocieta()).getStringTable());
		}
		else if (nomeTabella.equals("PD_PRODCONVENZIONI")) {
			BmaDataColumn c = tabella.getColonna("COD_CONVENZIONE");
			c.setTipoControllo(BMA_CONTROLLO_LISTA);
			c.setValoriControllo(gp.valoriConvenzioni(trx, input.getSocieta()).getStringTable());
			c = tabella.getColonna("COD_LIVELLO");
			c.setTipoControllo(BMA_CONTROLLO_LISTA);
			c.setValoriControllo(gp.valoriLivelliDeroga(trx, input.getSocieta()).getStringTable());
		}
		else if (nomeTabella.equals("PD_PRODREGOLETAR")) {
			BmaDataColumn c = tabella.getColonna("COD_LIVELLO");
			c.setTipoControllo(BMA_CONTROLLO_LISTA);
			c.setValoriControllo(gp.valoriLivelliDeroga(trx, input.getSocieta()).getStringTable());
			
			c = tabella.getColonna("COD_TARIFFA");
			c.setTipoControllo(BMA_CONTROLLO_LISTA);
			c.setValoriControllo(gp.valoriTariffe(trx, input.getSocieta()).getStringTable());
			
			c = tabella.getColonna("COD_REGOLA");
			c.setTipoControllo(BMA_CONTROLLO_LISTA);
			c.setValoriControllo(gp.valoriRegoleTariffa(trx, input.getSocieta()).getStringTable());
			
			if (filtri.get("COD_GARANZIA")==null) {
				c = tabella.getColonna("COD_GARANZIA");
				c.setTipoControllo(BMA_CONTROLLO_LISTA);
				c.setValoriControllo(gp.valoriGaranzie(trx, input.getSocieta()).getStringTable());
			}	
			if (filtri.get("COD_CONDIZIONE")==null) {
				c = tabella.getColonna("COD_CONDIZIONE");
				c.setTipoControllo(BMA_CONTROLLO_LISTA);
				c.setValoriControllo(gp.valoriCondizioniGaranzia(trx, input.getSocieta()).getStringTable());
			}	
			if (filtri.get("COD_OGGETTO")==null) {
				c = tabella.getColonna("COD_OGGETTO");
				c.setTipoControllo(BMA_CONTROLLO_LISTA);
				c.setValoriControllo(gp.valoriOggetti(trx, input.getSocieta()).getStringTable());
			}
			tabella.setOrderBy("NUM_PROGRESSIVO");
		}
		else if (nomeTabella.equals("PD_PRODREGTARPAR")) {
			BmaDataColumn c = tabella.getColonna("COD_TABELLA");
			c.setTipoControllo(BMA_CONTROLLO_LISTA);
			c.setValoriControllo(gp.valoriTabelleTariffe(trx, input.getSocieta()).getStringTable());
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
