package it.bma.servizi;

import it.bma.comuni.*;
import it.bma.ptf.*;
import java.util.*;

public class MessaggiUpdate extends BmaServizio {
/**
 * MessaggiEntrata constructor comment.
 */
public MessaggiUpdate() {
	super();
}
public String esegui(BmaJdbcTrx trx) throws BmaException {

	// Lettura dei parametri di attivazione
	String codSocieta = input.getSocieta();
	String codProcedura = input.getInfoServizio("codProcedura");
	String datRicezione = input.getInfoServizio("datRicezione");
	String codTipoMittente = input.getInfoServizio("codTipoMittente");
	String codMittente = input.getInfoServizio("codMittente");
	String numPolizza = input.getInfoServizio("numPolizza");
//	String xmlRecords = input.getInfoServizio("Records");
	String idFile = input.getInfoServizio("idFile");
	
	// Controllo dei parametri di attivazione
	if (codSocieta==null) codSocieta = "";
	if (codProcedura==null) codProcedura = "";
	if (datRicezione==null) datRicezione = "";
	if (codTipoMittente==null) codTipoMittente = "";
	if (codMittente==null) codMittente = "";
	if (numPolizza==null) numPolizza = "";
	if (idFile==null) idFile = "";
//	if (xmlRecords==null) xmlRecords = "";
	// Carica il vettore dei record
	BmaVector records = new BmaVector("Records");
//	xmlRecords = input.urlDecode(xmlRecords);
//	records.fromXml(xmlRecords);

	records.fromXmlFile(userConfig.getConfigPath() + idFile);
	
	BmaDataTable dTable = jModel.getDataTable(jModel.getSchema() + ".TATR0030");
	Hashtable condizioni = new Hashtable();


	// Elimina dalla tabella i valori precedenti
	String sql = 	" DELETE FROM " + jModel.getSchema() + ".TATR0030 " +
								" WHERE	CD_PROCEDURA = '" + codProcedura + "' " +
								"	AND		DT_RICEZIONE = '" + datRicezione + "' " +
								" AND		CD_TIPO_MITTENTE = '" + codTipoMittente + "' " +
								" AND   CD_MITTENTE = '" + codMittente + "' " +
								" AND		NM_MESSAGGIO BETWEEN " + numPolizza + "000 AND " + numPolizza + "999";
	trx.eseguiSqlUpdate(sql);
	// Inserisce i nuovi record

	for (int i = 0; i < records.getSize(); i++){
		BmaParametro p = (BmaParametro)records.getElement(i);
		condizioni.clear();
		condizioni.put("CD_PROCEDURA", codProcedura);
		condizioni.put("DT_RICEZIONE", datRicezione);
		condizioni.put("TI_RICEZIONE", datRicezione + "-12.00.00.000000");
		condizioni.put("CD_TIPO_MITTENTE", codTipoMittente);
		condizioni.put("CD_MITTENTE", codMittente);
		String n = Integer.toString(i);
		while (n.length()<3) { 
			n = "0" + n;
		}
		condizioni.put("NM_MESSAGGIO", numPolizza + n);
		condizioni.put("MESSAGGIO", p.getValore());
		condizioni.put("CD_STATO", "E");
		condizioni.put("DT_STATO", datRicezione);
		condizioni.put("NM_SESSIONE", "0");
		sql = dTable.getSqlInsert(condizioni);			
		trx.eseguiSqlUpdate(sql);
	}
//Inutile Rileggere
/*
	condizioni.put("CD_PROCEDURA", codProcedura);
	condizioni.put("DT_RICEZIONE", datRicezione);
	condizioni.put("CD_TIPO_MITTENTE", codTipoMittente);
	condizioni.put("CD_MITTENTE", codMittente);

	dTable.setOrderBy("NM_MESSAGGIO");

	// Esegue Query e filtra i dati
	Vector dati = trx.eseguiSqlSelect(dTable.getSqlLista(condizioni));
	int posMsg = dTable.getNumeroColonna("MESSAGGIO");
	BmaContratto contratto = (BmaContratto)objectInstance(BMA_SRV_PKG_PORTAFOGLIO + "BmaContratto" + codProcedura);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		String msg = (String)riga.elementAt(posMsg);
		// Controlla se appartiene alla polizza selezionata
		String numPolizza2 = msg.substring(10,16);
		if (numPolizza2.equals(numPolizza)) contratto.eventoGamma(msg);
	}
	// Test per prova upload
	contratto.getInfo().add(new BmaParametro("PDRecordGammaA02", contratto.saveGammaRecords().getString("A02")));
	// Fine
	
	return contratto.toXml();
*/
	return "";
}
}
