package it.bma.servizi;

import it.bma.comuni.*;
import it.bma.ptf.*;
import java.util.*;

public class ListaMessaggi extends BmaServizio {
/**
 * MessaggiEntrata constructor comment.
 */
public ListaMessaggi() {
	super();
}
public String esegui(BmaJdbcTrx trx) throws BmaException {
	// Lettura dei parametri di attivazione
	String codSocieta = input.getInfoServizio("codSocieta");
	String codProdotto = input.getInfoServizio("codProdotto");
	String datRicezione = input.getInfoServizio("datRicezione");
	String codTipoMittente = input.getInfoServizio("codTipoMittente");
	String codAgenzia = input.getInfoServizio("codAgenzia");
	String numPolizza = input.getInfoServizio("numPolizza");

	// Imposta i parametri di ricerca
	Hashtable condizioni = new Hashtable();
	if (codTipoMittente==null || codTipoMittente.trim().length()==0) codTipoMittente = "AG";
	condizioni.put("CD_TIPO_MITTENTE", codTipoMittente);
/*
	condizioni.put("CD_PROCEDURA", codProcedura);
	condizioni.put("DT_RICEZIONE", datRicezione);
	condizioni.put("CD_MITTENTE", codAgenzia);
*/
	// Imposta tabella per query

	
	BmaDataTable dTable = jModel.getDataTable(jModel.getSchema() + ".TATR0030");
	dTable.setOrderBy("CD_PROCEDURA, CD_MITTENTE, NM_MESSAGGIO");

	// Esegue Query e filtra i dati
	Vector dati = trx.eseguiSqlSelect(dTable.getSqlLista(condizioni));
	int posPrd = dTable.getNumeroColonna("CD_PROCEDURA");
	int posAge = dTable.getNumeroColonna("CD_MITTENTE");
	int posMsg = dTable.getNumeroColonna("MESSAGGIO");
	String kCtr = "";
	BmaVector righe = new BmaVector("Righe");
	BmaElementoListaContratti elemento;
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		String cPrd = (String)riga.elementAt(posPrd);
		String cAge = (String)riga.elementAt(posAge);
		String msg = (String)riga.elementAt(posMsg);
		String nPol = msg.substring(10,16);
		String kCtr2 = cPrd+cAge+nPol;
		// Controlla rottura Prodotto + Agenzia + Polizza
		if (!kCtr2.equals(kCtr)) {
			kCtr = kCtr2;
			elemento = new BmaElementoListaContratti();
			elemento.setCodSocieta(codSocieta);
			elemento.setDatRicezione((String)riga.elementAt(dTable.getNumeroColonna("DT_RICEZIONE")));
			elemento.setCodProdotto(cPrd);
			elemento.setCodAgenzia(cAge);
			elemento.setNumPolizza(nPol);
			righe.add(elemento);
		}
	}
	return righe.toXml();
}
}
