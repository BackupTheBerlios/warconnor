package it.bma.servizi;

import it.bma.comuni.*;
import it.bma.web.*;
import java.util.*;
public class BmaAllegati extends BmaServizio {
public BmaAllegati() {
	super();
}
public String esegui(BmaJdbcTrx trx) throws BmaException {
	BmaGestoreProdotto gp = new BmaGestoreProdotto();
	gp.jModel = jModel;
	String nomeTabella = "PD_PRODALLEGATI";
	String tipoAzione = input.getInfoServizio("TipoAzione");
	if (tipoAzione.trim().length()==0) tipoAzione = BMA_SQL_SELECT;
	BmaDataTable tabella = jModel.getDataTable(nomeTabella);
	if (tabella==null) throw new BmaException(BMA_ERR_WEB_PARAMETRO,"Riferimento errato a tabella: " + nomeTabella,"",this);
	applicaStandardDati(tabella);
	Vector dati;
	String sql = "";
	Hashtable filtri = input.getInfoServizio().getStringTable();
	filtri.put("COD_SOCIETA", input.getSocieta());
		
	if (tipoAzione.equals(BMA_SQL_SELECT)) {
		BmaDataColumn c = tabella.getColonna("COD_TIPOALLEGATO");
		c.setTipoControllo(BMA_CONTROLLO_LISTA);
		c.setValoriControllo(gp.valoriTipiAllegato(trx, input.getSocieta()).getStringTable());
		sql = tabella.getSqlLista(input.getInfoServizio().getStringTable());
		dati = trx.eseguiSqlSelect(sql);
		BmaDataList elenco = new BmaDataList(tabella, dati);
		return elenco.toXml();
	}
	else {
		gp.eseguiAggiorna(trx, tabella, tipoAzione, filtri);
		return "";
	}
}
public String getChiave() {
	return null;
}
protected String getXmlTag() {
	return null;
}
}
