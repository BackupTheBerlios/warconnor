package it.bma.servizi;

import it.bma.comuni.*;
import it.bma.ptf.*;
import java.util.*;

public class MessaggiEntrata extends BmaServizio {
/**
 * MessaggiEntrata constructor comment.
 */
public MessaggiEntrata() {
	super();
}
public String esegui(BmaJdbcTrx trx) throws BmaException {

	// Lettura dei parametri di attivazione
	String codSocieta = input.getInfoServizio("codSocieta");
	String codProcedura = input.getInfoServizio("codProcedura");
	String datRicezione = input.getInfoServizio("datRicezione");
	String codTipoMittente = input.getInfoServizio("codTipoMittente");
	String codMittente = input.getInfoServizio("codMittente");
	String numPolizza = input.getInfoServizio("numPolizza");

	// Controllo dei parametri di attivazione
	if (codSocieta==null) codSocieta = "";
	if (codProcedura==null) codProcedura = "";
	if (datRicezione==null) datRicezione = "";
	if (codTipoMittente==null) codTipoMittente = "";
	if (codMittente==null) codMittente = "";
	if (numPolizza==null) numPolizza = "";

	// Imposta tabella per query
	BmaDataTable dTable = jModel.getDataTable(jModel.getSchema() + ".TATR0030");
	Hashtable condizioni = new Hashtable();
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
//	contratto.toXmlFile("c:\\contratto.xml");
	return contratto.toXml();
}
}
