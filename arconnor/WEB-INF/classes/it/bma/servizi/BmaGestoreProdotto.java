package it.bma.servizi;

import java.util.*;
import it.bma.comuni.*;
import it.bma.prodotti.*;

public class BmaGestoreProdotto extends it.bma.comuni.BmaObject {
	private String codSocieta = "";
	private Prodotto pdo = null;
	protected JdbcModel jModel;
public BmaGestoreProdotto() {
	super();
}
private void caricaAllegati(BmaJdbcTrx trx) throws it.bma.comuni.BmaException {
	String sql;
	
	BmaDataTable table;
	BmaDataList list;
	// Lettura di PD_ALLEGATI
	sql = " SELECT	A.NUM_ALLEGATO, A.COD_TIPOALLEGATO, " +
				"					B.DES_TIPOALLEGATO, A.DAT_ALLEGATO, " +
				"					A.DES_ALLEGATO, A.NOT_ALLEGATO, " +
				"					A.OBJ_ALLEGATO " +
				" FROM 		PD_PRODALLEGATI A, PD_TIPIALLEGATO B " +
				" WHERE		A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" AND			A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_TIPOALLEGATO = B.COD_TIPOALLEGATO " +
				" ORDER BY A.NUM_ALLEGATO";
	list = trx.eseguiQuery(sql, "PD_PRODALLEGATI");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		Allegato a = new Allegato();
		a.setNumAllegato(				Integer.parseInt(table.getValoreColonna("NUM_ALLEGATO")));
		a.setCodTipoAllegato(		table.getValoreColonna("COD_TIPOALLEGATO"));
		a.setDesTipoAllegato(		table.getValoreColonna("DES_TIPOALLEGATO"));
		a.setDatAllegato(				table.getValoreColonna("DAT_ALLEGATO"));
		a.setDesAllegato(				table.getValoreColonna("DES_ALLEGATO"));
		a.setNotAllegato(				table.getValoreColonna("NOT_ALLEGATO"));
		a.setObjAllegato(				table.getValoreColonna("OBJ_ALLEGATO"));
		pdo.getAllegati().add(a);
	}
}
private void caricaAllegatiNorma(BmaJdbcTrx trx, Normativa norma) throws it.bma.comuni.BmaException {
	String sql;
	BmaDataTable table;
	BmaDataList list;
	// Lettura di PD_ALLEGATINORME
	sql = " SELECT	A.NUM_ALLEGATO, A.DAT_ALLEGATO, " +
				"					A.DES_ALLEGATO, A.NOT_ALLEGATO, " +
				"					A.OBJ_ALLEGATO " +
				" FROM 		PD_ALLEGATINORME A " +
				" WHERE		A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_NORMATIVA = '" + norma.getCodNormativa() + "' " +
				" ORDER BY A.NUM_ALLEGATO ";
	list = trx.eseguiQuery(sql, "PD_ALLEGATINORME");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		AllegatoNorma obj = new AllegatoNorma();
		obj.setNumAllegato(Integer.parseInt(table.getValoreColonna("NUM_ALLEGATO")));
		obj.setDatAllegato(			table.getValoreColonna("DAT_ALLEGATO"));
		obj.setDesAllegato(			table.getValoreColonna("DES_ALLEGATO"));
		obj.setNotAllegato(			table.getValoreColonna("NOT_ALLEGATO"));
		obj.setObjAllegato(			table.getValoreColonna("OBJ_ALLEGATO"));
		norma.getAllegati().add(obj);
	}
}
private void caricaApprovazioni(BmaJdbcTrx trx, Responsabile responsabile) throws it.bma.comuni.BmaException {
	String sql;
	BmaDataTable table;
	BmaDataList list;
	// Lettura di PD_APPROVAZIONI
	sql = " SELECT	A.COD_TIPOSTATO, C.DES_TIPOSTATO, " +
				"					A.COD_STATO, B.DES_STATO, " +
				"					A.DES_MAILPOSIZIONE, A.NOT_APPROVAZIONE " +
				" FROM 		PD_APPROVAZIONI A, PD_STATI B, " +
				"					PD_TIPISTATO C " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				"	AND			A.COD_TIPOSTATO = B.COD_TIPOSTATO " +
				" AND			A.COD_STATO = B.COD_STATO " +
				" AND			B.COD_SOCIETA = C.COD_SOCIETA " +
				" AND			B.COD_TIPOSTATO = C.COD_TIPOSTATO " +	
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_POSIZIONE = '" + responsabile.getCodPosizione() + "' " +
				" ORDER BY A.COD_TIPOSTATO, A.COD_STATO ";
	list = trx.eseguiQuery(sql, "PD_APPROVAZIONI");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		Approvazione obj = new Approvazione();
		obj.setCodTipoStato(			table.getValoreColonna("COD_TIPOSTATO"));
		obj.setDesTipoStato(			table.getValoreColonna("DES_TIPOSTATO"));
		obj.setCodStato(					table.getValoreColonna("COD_STATO"));
		obj.setDesStato(					table.getValoreColonna("DES_STATO"));
		obj.setDesMailPosizione(	table.getValoreColonna("DES_MAILPOSIZIONE"));
		obj.setNotApprovazione(		table.getValoreColonna("NOT_APPROVAZIONE"));
		responsabile.getApprovazioni().add(obj);
	}
}
private void caricaArgomenti(BmaJdbcTrx trx) throws it.bma.comuni.BmaException {
	String sql;
	BmaDataTable table;
	BmaDataList list;
	sql = " SELECT	A.COD_ARGOMENTO, B.DES_ARGOMENTO, " +
				"					A.NOT_ARGOMENTO " +
				" FROM 		PD_PRODARGOMENTI A, PD_ARGOMENTI B " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_ARGOMENTO = B.COD_ARGOMENTO " +
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" ORDER BY A.COD_ARGOMENTO";
	list = trx.eseguiQuery(sql, "PD_PRODARGOMENTI");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		Argomento obj = new Argomento();
		obj.setCodArgomento(	table.getValoreColonna("COD_ARGOMENTO"));
		obj.setDesArgomento(	table.getValoreColonna("DES_ARGOMENTO"));
		obj.setNotArgomento(	table.getValoreColonna("NOT_ARGOMENTO"));
		pdo.getArgomenti().add(obj);
	}
}
private void caricaCanali(BmaJdbcTrx trx) throws it.bma.comuni.BmaException {
	String sql;
	BmaDataTable table;
	BmaDataList list;
	// Lettura di PD_PRODCANALI
	sql = " SELECT	A.COD_CANALE, B.DES_CANALE, " +
				"					A.COD_USOCANALE, C.DES_USOCANALE, " +
				"					A.NOT_CANALE " +
				" FROM 		PD_PRODCANALI A, PD_CANALI B, PD_USICANALE C " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_CANALE = B.COD_CANALE " +				
				" AND			A.COD_SOCIETA = C.COD_SOCIETA " +
				" AND			A.COD_USOCANALE = C.COD_USOCANALE " +				
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" ORDER BY A.COD_CANALE";
	list = trx.eseguiQuery(sql, "PD_PRODCANALI");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		Canale obj = new Canale();
		obj.setCodCanale(			table.getValoreColonna("COD_CANALE"));
		obj.setDesCanale(			table.getValoreColonna("DES_CANALE"));
		obj.setCodUsoCanale(	table.getValoreColonna("COD_USOCANALE"));
		obj.setDesUsoCanale(	table.getValoreColonna("DES_USOCANALE"));
		obj.setNotCanale(			table.getValoreColonna("NOT_CANALE"));
		pdo.getCanali().add(obj);
	}
}
private void caricaConcorrenti(BmaJdbcTrx trx) throws it.bma.comuni.BmaException {
	String sql;
	BmaDataTable table;
	BmaDataList list;
	sql = " SELECT	A.COD_CONCORRENTE, A.NOT_CONCORRENTE " +
				" FROM 		PD_PRODCONCORRENTI A " +
				" WHERE		A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" ORDER BY A.COD_CONCORRENTE";
	list = trx.eseguiQuery(sql, "PD_PRODCONCORRENTI");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		Concorrente obj = new Concorrente();
		obj.setCodConcorrente(	table.getValoreColonna("COD_CONCORRENTE"));
		obj.setNotConcorrente(	table.getValoreColonna("NOT_CONCORRENTE"));
		pdo.getConcorrenti().add(obj);
	}

	// Integra con argomenti
	Enumeration eCorr = pdo.getConcorrenti().elements();
	while (eCorr.hasMoreElements()) {
		Concorrente concorrente = (Concorrente)eCorr.nextElement();
		sql = " SELECT	A.COD_ARGOMENTO, B.DES_ARGOMENTO, " +
					"					A.NOT_ARGOMENTO, A.IND_PESO " +
					" FROM 		PD_ARGOMENTICONFRONTO A, PD_ARGOMENTI B " +
					" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
					" AND			A.COD_ARGOMENTO = B.COD_ARGOMENTO " +
					" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
					" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
					" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
					" AND			A.COD_CONCORRENTE = '" + concorrente.getCodConcorrente() + "' " +
					" ORDER BY A.IND_PESO DESC ";
		list = trx.eseguiQuery(sql, "PD_ARGOMENTICONFRONTO");
		table = list.getTabella();
		for (int i = 0; i < list.getValori().size(); i++){
			table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
			Argomento obj = new Argomento();
			obj.setCodArgomento(	table.getValoreColonna("COD_ARGOMENTO"));
			obj.setDesArgomento(	table.getValoreColonna("DES_ARGOMENTO"));
			obj.setNotArgomento(	table.getValoreColonna("NOT_ARGOMENTO"));
			obj.setIndPeso(				table.getValoreColonna("IND_PESO"));
			concorrente.getArgomenti().add(obj);
		}
	}	
}
private Vector caricaCondizioni(BmaJdbcTrx trx, String tabella, OggettoAssicurazione oggetto, Garanzia garanzia) throws it.bma.comuni.BmaException {
	
	String sql;
	BmaDataTable table;
	BmaDataList list;
	Vector valori = new Vector();

	String tabValori = "PD_PRODVALCOND";
	String filtro = "";
	if (tabella.equals("PD_PRODCONDGAR")) {
		filtro = " AND A.COD_GARANZIA = '" + garanzia.getCodGaranzia() + "' ";
		tabValori = "PD_PRODVALCONDGAR";
	}
	else if (tabella.equals("PD_PRODCONDGAROGG")) {
		filtro = " AND A.COD_GARANZIA = '" + garanzia.getCodGaranzia() + "' ";
		filtro = filtro + " AND A.COD_OGGETTO = '" + oggetto.getCodOggetto() + "' ";
		tabValori = "PD_PRODOGGVCGAR";
	}
	else if (tabella.equals("PD_PRODIDONEITA")) {
		tabValori = "PD_PRODVALIDONEITA";
	}
	if (tabella.equals("PD_PRODIDONEITA")) {
		sql = " SELECT	A.COD_CONDIZIONE, B.DES_CONDIZIONE, " +
					"					'IDONEITA', 'IDONEITA', " +
					"					A.VAL_STANDARD, A.NOT_CONDIZIONE " +
					" FROM		PD_PRODIDONEITA A, PD_IDONEITA B " +
					" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
					" AND			A.COD_CONDIZIONE = B.COD_CONDIZIONE " +				
					" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
					" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
					" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
					"	ORDER BY A.COD_CONDIZIONE ";
	}
	else {
		sql = " SELECT	A.COD_CONDIZIONE, B.DES_CONDIZIONE, " +
					"					B.COD_TIPOCONDIZIONE, C.DES_TIPOCONDIZIONE, " +
					"					A.VAL_STANDARD, A.NOT_CONDIZIONE " +
					" FROM	" + tabella + " A, PD_CONDIZIONI B, PD_TIPICONDIZIONE C " +
					" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
					" AND			A.COD_CONDIZIONE = B.COD_CONDIZIONE " +				
					" AND			B.COD_SOCIETA = C.COD_SOCIETA " +
					" AND			B.COD_TIPOCONDIZIONE = C.COD_TIPOCONDIZIONE " +				
					" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
					" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
					" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
					filtro +
					"	ORDER BY A.COD_CONDIZIONE ";
	}
	list = trx.eseguiQuery(sql, tabella);
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		Condizione condizione = new Condizione();
		condizione.setCodCondizione(table.getValoreColonna("COD_CONDIZIONE"));
		condizione.setDesCondizione(table.getValoreColonna("DES_CONDIZIONE"));
		condizione.setCodTipo(			table.getValoreColonna("COD_TIPOCONDIZIONE"));
		condizione.setDesTipo(			table.getValoreColonna("DES_TIPOCONDIZIONE"));
		condizione.setValStandard(	table.getValoreColonna("VAL_STANDARD"));
		condizione.setNotCondizione(table.getValoreColonna("NOT_CONDIZIONE"));

		if (tabValori.equals("PD_PRODVALIDONEITA")) {
			sql = " SELECT	'0', 'Libero', " +
					"					A.VAL_MIN, A.VAL_MAX, " +
					"					A.DES_VALORE, A.NOT_VALORE " +
					" FROM		PD_PRODVALIDONEITA A " +
					" WHERE		A.COD_SOCIETA = '" + codSocieta + "' " +
					" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
					" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
					" AND			A.COD_CONDIZIONE = '" + condizione.getCodCondizione() + "' " +
					"	ORDER BY A.VAL_MIN ";
		}
		else {
			completaDatiRegoleTariffa(oggetto, garanzia, condizione);

			sql = " SELECT	A.COD_LIVELLO, B.DES_LIVELLO, " +
					"					A.VAL_MIN, A.VAL_MAX, " +
					"					A.DES_VALORE, A.NOT_VALORE " +
					" FROM		" + tabValori + " A, PD_LIVELLIDEROGA B " +
					" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
					" AND			A.COD_LIVELLO = B.COD_LIVELLO " +
					" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
					" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
					" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
					" AND			A.COD_CONDIZIONE = '" + condizione.getCodCondizione() + "' " +
					filtro +
					"	ORDER BY A.VAL_MIN ";
		}
		BmaDataList list2 = trx.eseguiQuery(sql, tabValori);
		BmaDataTable tab2 = list2.getTabella();
		for (int j = 0; j < list2.getValori().size(); j++){
			tab2.setValoriDaQuery((Vector)list2.getValori().elementAt(j));
			Valore val = new Valore();
			val.setCodLivelloDeroga(tab2.getValoreColonna("COD_LIVELLO"));
			val.setDesLivelloDeroga(tab2.getValoreColonna("DES_LIVELLO"));
			val.setValMin(					tab2.getValoreColonna("VAL_MIN"));
			val.setValMax(					tab2.getValoreColonna("VAL_MAX"));
			val.setDesValore(				tab2.getValoreColonna("DES_VALORE"));
			val.setNotValore(				tab2.getValoreColonna("NOT_VALORE"));
			condizione.getValori().add(val);
		}
		valori.add(condizione);
	}
	return valori;
}
private void caricaConvenzioni(BmaJdbcTrx trx) throws it.bma.comuni.BmaException {
	String sql;
	BmaDataTable table;
	BmaDataList list;
	sql = " SELECT	A.COD_CONVENZIONE, B.DES_CONVENZIONE, " +
				"					A.NOT_CONVENZIONE, A.COD_LIVELLO " +
				" FROM 		PD_PRODCONVENZIONI A, PD_CONVENZIONI B " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_CONVENZIONE = B.COD_CONVENZIONE " +
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" ORDER BY A.COD_CONVENZIONE";
	list = trx.eseguiQuery(sql, "PD_PRODCONVENZIONI");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		Convenzione obj = new Convenzione();
		obj.setCodConvenzione(	table.getValoreColonna("COD_CONVENZIONE"));
		obj.setDesConvenzione(	table.getValoreColonna("DES_CONVENZIONE"));
		obj.setNotConvenzione(	table.getValoreColonna("NOT_CONVENZIONE"));
		obj.setCodLivelloDeroga(table.getValoreColonna("COD_LIVELLO"));
		// Integra con regole Tariffa
		Enumeration eReg = pdo.getRegoleTariffa().elements();
		while (eReg.hasMoreElements()) {
			RegolaTariffa regola = (RegolaTariffa)eReg.nextElement();
			if (regola.getCodLivello().equals(obj.getCodLivelloDeroga())) obj.getRegoleTariffa().add(regola);
		}
		pdo.getConvenzioni().add(obj);
	}

	// Integra con Punti Vendita
	Enumeration eCon = pdo.getConvenzioni().elements();
	while (eCon.hasMoreElements()) {
		Convenzione convenzione = (Convenzione)eCon.nextElement();
		sql = " SELECT	A.COD_PUNTOVENDITA, B.DES_PUNTOVENDITA, " +
				"					B.COD_CANALE, A.NOT_CONVENZIONE, " +
				"					A.DAT_INIZIO, A.DAT_FINE " +
				" FROM 		PD_CONVENZIONIPUNTO A, PD_PUNTIVENDITA B " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_PUNTOVENDITA = B.COD_PUNTOVENDITA " +
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_CONVENZIONE = '" + convenzione.getCodConvenzione() + "' " +
				" ORDER BY A.COD_PUNTOVENDITA";
		list = trx.eseguiQuery(sql, "PD_CONVENZIONIPUNTO");
		table = list.getTabella();
		for (int i = 0; i < list.getValori().size(); i++){
			table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
			PuntoVendita obj = new PuntoVendita();
			obj.setCodPuntoVendita(	table.getValoreColonna("COD_PUNTOVENDITA"));
			obj.setDesPuntoVendita(	table.getValoreColonna("DES_PUNTOVENDITA"));
			obj.setNotPuntoVendita(	table.getValoreColonna("NOT_CONVENZIONE"));
			obj.setCodCanale(				table.getValoreColonna("COD_CANALE"));
			obj.setDatInizio(				table.getValoreColonna("DAT_INIZIO"));
			obj.setDatFine(				table.getValoreColonna("DAT_FINE"));
			convenzione.getPuntiVendita().add(obj);
		}
	}
}
private void caricaDescrizioni(BmaJdbcTrx trx) throws it.bma.comuni.BmaException {
	String sql;
	BmaDataTable table;
	BmaDataList list;
	sql = " SELECT	A.COD_TIPODESCRIZIONE, B.DES_TIPODESCRIZIONE, " +
				"					A.DES_PRODOTTO, A.NOT_PRODOTTO " +
				" FROM 		PD_PRODDESCRIZIONI A, PD_TIPIDESCRIZIONE B " +
				" WHERE		A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" AND			A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_TIPODESCRIZIONE = B.COD_TIPODESCRIZIONE " +
				" ORDER BY A.COD_TIPODESCRIZIONE";
	list = trx.eseguiQuery(sql, "PD_PRODDESCRIZIONI");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		Descrizione obj = new Descrizione();
		obj.setCodTipoDescrizione(	table.getValoreColonna("COD_TIPODESCRIZIONE"));
		obj.setDesTipoDescrizione(	table.getValoreColonna("DES_TIPODESCRIZIONE"));
		obj.setDesProdotto(					table.getValoreColonna("DES_PRODOTTO"));
		obj.setNotProdotto(					table.getValoreColonna("NOT_PRODOTTO"));
		pdo.getDescrizioni().add(obj);
	}
}
private void caricaDocumentiRuoli(BmaJdbcTrx trx) throws it.bma.comuni.BmaException {
	String sql;
	BmaDataTable table;
	BmaDataList list;
	// Carica i documenti
	sql = " SELECT	A.COD_TIPODOCUMENTO, B.DES_TIPODOCUMENTO, " +
				"					A.NOT_DOCUMENTO, B.IND_TIPOCLIENTE, " +
				"					C.COD_DOCUMENTO, C.DES_DOCUMENTO " +
				" FROM 		PD_PRODDOCUMENTI A, PD_TIPIDOCUMENTO B, PD_DOCUMENTI C " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_TIPODOCUMENTO = B.COD_TIPODOCUMENTO " +
				" AND			B.COD_SOCIETA = C.COD_SOCIETA " +
				" AND			B.COD_TIPODOCUMENTO = C.COD_TIPODOCUMENTO " +
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" ORDER BY A.COD_TIPODOCUMENTO";
	list = trx.eseguiQuery(sql, "PD_PRODDOCUMENTI");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		Documento obj = new Documento();
		obj.setCodTipoDocumento(	table.getValoreColonna("COD_TIPODOCUMENTO"));
		obj.setDesTipoDocumento(	table.getValoreColonna("DES_TIPODOCUMENTO"));
		obj.setNotDocumento(			table.getValoreColonna("NOT_DOCUMENTO"));
		obj.setIndTipoCliente(		table.getValoreColonna("IND_TIPOCLIENTE"));
		obj.setCodDocumento(			table.getValoreColonna("COD_DOCUMENTO"));
		obj.setDesDocumento(			table.getValoreColonna("DES_DOCUMENTO"));
		pdo.getDocumenti().add(obj);
	}
	// Carica i ruoli
	sql = " SELECT	A.COD_RUOLO, B.DES_RUOLO, " +
				"					A.IND_TIPOCLIENTE " +
				" FROM 		PD_PRODRUOLOCLIENTE A, PD_RUOLOCLIENTE B " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_RUOLO = B.COD_RUOLO " +
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" ORDER BY A.COD_RUOLO";
	list = trx.eseguiQuery(sql, "PD_PRODRUOLOCLIENTE");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		RuoloCliente obj = new RuoloCliente();
		obj.setCodRuolo(					table.getValoreColonna("COD_RUOLO"));
		obj.setDesRuolo(					table.getValoreColonna("DES_RUOLO"));
		obj.setIndTipoCliente(		table.getValoreColonna("IND_TIPOCLIENTE"));
		pdo.getRuoli().add(obj);
	}
	// Carica i documenti per il ruolo
	sql = " SELECT	A.COD_RUOLO, A.COD_TIPODOCUMENTO " +
				" FROM 		PD_PRODDOCRUOLO A " +
				" WHERE		A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" ORDER BY A.COD_RUOLO";
	list = trx.eseguiQuery(sql, "PD_PRODDOCRUOLO");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		String codRuolo = table.getValoreColonna("COD_RUOLO");
		String codTipo = table.getValoreColonna("COD_TIPODOCUMENTO");
		RuoloCliente obj = (RuoloCliente)pdo.getRuoli().getElement(codRuolo);
		Enumeration eDoc = pdo.getDocumenti().elements();
		while (eDoc.hasMoreElements()) {
			Documento doc = (Documento)eDoc.nextElement();
			if (doc.getCodTipoDocumento().equals(codTipo)) obj.getDocumenti().add(doc);
		}
	}
}
private Vector caricaGaranzie(BmaJdbcTrx trx, OggettoAssicurazione oggetto) throws it.bma.comuni.BmaException {
	Vector garanzie = new Vector();
	String sql;
	BmaDataTable table;
	BmaDataList list;

	String tabBase = "PD_PRODGARANZIA";
	String filtro = "";
	String flgFacoltativa = " A.FLG_FACOLTATIVA, ";
	String numProgressivo = " A.NUM_PROGRESSIVO, ";
	if (oggetto!=null) {
		filtro = " AND A.COD_OGGETTO = '" + oggetto.getCodOggetto() + "' ";
		tabBase = "PD_PRODGAROGG";
		flgFacoltativa = "";
		numProgressivo = "";
	}

	sql = " SELECT	" + numProgressivo + " A.COD_GARANZIA, " +
				"					B.DES_GARANZIA," + flgFacoltativa +
				"					A.NOT_GARANZIA " +
				" FROM 	" + tabBase + " A, PD_GARANZIE B " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_GARANZIA = B.COD_GARANZIA " +
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				filtro +
				" ORDER BY" + numProgressivo + " A.COD_GARANZIA";
	list = trx.eseguiQuery(sql, tabBase);
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		Garanzia garanzia = new Garanzia();
		garanzia.setCodGaranzia(table.getValoreColonna("COD_GARANZIA"));
		garanzia.setDesGaranzia(table.getValoreColonna("DES_GARANZIA"));
		garanzia.setFacoltativa(table.getValoreColonna("FLG_FACOLTATIVA")!=null && table.getValoreColonna("FLG_FACOLTATIVA").equals(BMA_TRUE));
		garanzia.setNotGaranzia(table.getValoreColonna("NOT_GARANZIA"));
		garanzie.add(garanzia);
	}

	// Personalizza le Regole di Vendita e le Condizioni per il prodotto
	Enumeration eGar = garanzie.elements();
	while (eGar.hasMoreElements()) {
		Garanzia garanzia = (Garanzia)eGar.nextElement();
		if (tabBase.equals("PD_PRODGARANZIA")) {
			garanzia.getCondizioni().setVector(caricaCondizioni(trx, "PD_PRODCONDGAR", null, garanzia));
			// Personalizza le regole con PD_PRODREGGAR	
			sql = " SELECT	A.COD_REGOLA, B.DES_REGOLA, " +
				"					A.COD_LIVELLO, C.DES_LIVELLO, " +
				"					A.NOT_REGOLA, " +
				"					A.RIF_GARANZIA, D.DES_GARANZIA " +
				" FROM 		PD_PRODREGOLEGAR A, PD_REGOLEVENDITA B, " +
				"					PD_LIVELLIDEROGA C, PD_GARANZIE D " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				"	AND			A.COD_REGOLA = B.COD_REGOLA " +
				" AND			A.COD_SOCIETA = C.COD_SOCIETA " +
				" AND			A.COD_LIVELLO = C.COD_LIVELLO " +
				"	AND		(	A.RIF_GARANZIA IS NULL " +
				"					OR (A.COD_SOCIETA = D.COD_SOCIETA " +
				"					AND A.RIF_GARANZIA = D.COD_GARANZIA )) " +
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" AND			A.COD_GARANZIA = '" + garanzia.getCodGaranzia() + "' " +
				" ORDER BY A.COD_REGOLA";
			list = trx.eseguiQuery(sql, "PD_PRODREGOLEGAR");
			table = list.getTabella();
			for (int j = 0; j < list.getValori().size(); j++){
				table.setValoriDaQuery((Vector)list.getValori().elementAt(j));
				RegolaGaranzia regola = new RegolaGaranzia();
				regola.setCodRegola(	table.getValoreColonna("COD_REGOLA"));
				regola.setDesRegola(	table.getValoreColonna("DES_REGOLA"));
				regola.setCodLivello(	table.getValoreColonna("COD_LIVELLO"));
				regola.setDesLivello(	table.getValoreColonna("DES_LIVELLO"));
				regola.setNotRegola(	table.getValoreColonna("NOT_REGOLA"));
				String tmp = table.getValoreColonna("RIF_GARANZIA");
				if (tmp.trim().length()>0) {
					regola.setRifCodGaranzia(tmp);
					regola.setRifDesGaranzia(table.getValoreColonna("DES_GARANZIA"));
				}
				garanzia.getRegole().add(regola);
			}
		}
		else {
			garanzia.getCondizioni().setVector(caricaCondizioni(trx, "PD_PRODCONDGAROGG", oggetto, garanzia));
		}	
	}
	return garanzie;
}
private void caricaIdentificativi(BmaJdbcTrx trx) throws it.bma.comuni.BmaException {
	String sql;
	BmaDataTable table;
	BmaDataList list;
	// Lettura di PD_PRODIDENTIFICA
	sql = " SELECT	A.COD_TIPOIDENTIFICA, B.DES_TIPOIDENTIFICA, " +
				"					A.COD_IDENTIFICA " +
				" FROM 		PD_PRODIDENTIFICA A, PD_TIPIIDENTIFICA B " +
				" WHERE		A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" AND			A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_TIPOIDENTIFICA = B.COD_TIPOIDENTIFICA " +
				" ORDER BY A.COD_IDENTIFICA";
	list = trx.eseguiQuery(sql, "PD_PRODIDENTIFICA");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		Identificativo obj = new Identificativo();
		obj.setCodTipoIdentificativo(	table.getValoreColonna("COD_TIPOIDENTIFICA"));
		obj.setDesTipoIdentificativo(	table.getValoreColonna("DES_TIPOIDENTIFICA"));
		obj.setCodIdentificativo(			table.getValoreColonna("COD_IDENTIFICA"));
		pdo.getIdentificativi().add(obj);
	}
}
private void caricaModuli(BmaJdbcTrx trx) throws it.bma.comuni.BmaException {
	String sql;
	BmaDataTable table;
	BmaDataList list;
	sql = " SELECT	A.COD_MODULO, B.DES_MODULO, " +
				"					A.NOT_MODULO, " +
				"					B.NOT_MODULO AS NOT_MODULOBASE, " +
				"					B.COD_TIPOMODULO, C.DES_TIPOMODULO, " +
				"					B.COD_APPLICAZIONE, B.COD_ESTERNO, " +
				"					B.DES_NOMEMODULO " +
				" FROM 		PD_PRODMODULI A, PD_MODULI B, " +
				"					PD_TIPIMODULO C " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_MODULO = B.COD_MODULO " +
				" AND			B.COD_SOCIETA = C.COD_SOCIETA " +
				" AND			B.COD_TIPOMODULO = C.COD_TIPOMODULO " +
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" ORDER BY A.COD_MODULO";
	list = trx.eseguiQuery(sql, "PD_PRODMODULI");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		Modulo obj = new Modulo();
		obj.setCodModulo(				table.getValoreColonna("COD_MODULO"));
		obj.setDesModulo(				table.getValoreColonna("DES_MODULO"));
		obj.setDatValidita(			table.getValoreColonna("DAT_INIZIO"));
		obj.setNotModulo(				table.getValoreColonna("NOT_MODULOBASE"));
		if (table.getValoreColonna("NOT_MODULO").trim().length()>0) {
			obj.setNotModulo(			table.getValoreColonna("NOT_MODULO"));
		}
		obj.setCodTipoModulo(		table.getValoreColonna("COD_TIPOMODULO"));
		obj.setDesTipoModulo(		table.getValoreColonna("DES_TIPOMODULO"));
		obj.setRifApplicazione(	table.getValoreColonna("COD_APPLICAZIONE"));
		obj.setRifEsterno(			table.getValoreColonna("COD_ESTERNO"));
		obj.setRifNome(					table.getValoreColonna("DES_NOMEMODULO"));
	
		pdo.getModuli().add(obj);
	}
	Enumeration eMod = pdo.getModuli().elements();
	while (eMod.hasMoreElements()) {
		Modulo obj = (Modulo)eMod.nextElement();
		// Carica Dati
		sql = " SELECT	A.COD_DATO, B.DES_DATO, " +
				"						B.IND_TIPODATO, B.NOT_DATO, " +
				"						A.NOT_DATO AS NOT_DATOMODULO " +
				" FROM 		PD_DATIMODULO A, PD_DATI B " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_DATO = B.COD_DATO " +
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_MODULO = '" + obj.getCodModulo() + "' " +
				" ORDER BY A.NUM_PROGRESSIVO";
		list = trx.eseguiQuery(sql, "PD_DATIMODULO");
		table = list.getTabella();
		for (int i = 0; i < list.getValori().size(); i++){
			table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
			DatoModulo dato = new DatoModulo();
			dato.setCodDato(				table.getValoreColonna("COD_DATO"));
			dato.setDesDato(				table.getValoreColonna("DES_DATO"));
			dato.setIndTipoDato(		table.getValoreColonna("IND_TIPODATO"));
			String tmp = 						table.getValoreColonna("NOT_DATOMODULO");
			if (tmp.trim().length()==0) tmp = table.getValoreColonna("NOT_DATO");
			dato.setNotDato(tmp);
			obj.getDati().add(dato);
		}		
		// Carica Regole
		sql = " SELECT	A.COD_REGOLA, B.DES_REGOLA, " +
				"						A.COD_DATO, B.NOT_REGOLA, " +
				"						A.NOT_REGOLA AS NOT_REGOLAPRODOTTO " +
				" FROM 		PD_PRODREGOLEMOD A, PD_REGOLEMODULI B " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_REGOLA = B.COD_REGOLA " +
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" AND			A.COD_MODULO = '" + obj.getCodModulo() + "' " +
				" ORDER BY A.NUM_PROGRESSIVO";
		list = trx.eseguiQuery(sql, "PD_PRODREGOLEMOD");
		table = list.getTabella();
		for (int i = 0; i < list.getValori().size(); i++){
			table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
			RegolaModulo reg = new RegolaModulo();
			reg.setCodRegola(	table.getValoreColonna("COD_REGOLA"));
			reg.setDesRegola(	table.getValoreColonna("DES_REGOLA"));
			reg.setCodDato(		table.getValoreColonna("COD_DATO"));
			String tmp = 			table.getValoreColonna("NOT_REGOLAPRODOTTO");
			if (tmp.trim().length()==0) tmp = table.getValoreColonna("NOT_REGOLA");
			reg.setNotRegola(tmp);
			if (reg.getCodDato().trim().length()==0) {
				obj.getRegole().add(reg);
			}
			else {
				DatoModulo dato = (DatoModulo)obj.getDati().getElement(reg.getCodDato());
				if (dato==null) obj.getRegole().add(reg);
				else dato.getRegole().add(reg);
			}
		}
	}
}
private void caricaNormative(BmaJdbcTrx trx) throws it.bma.comuni.BmaException {
	String sql;
	BmaDataTable table;
	BmaDataList list;
	// Lettura di PD_PRODNORMATIVE
	sql = " SELECT	A.COD_NORMATIVA, B.DES_NORMATIVA, " +
				"					A.NOT_NORMATIVA, B.FLG_INTERNA, " +
				"					B.COD_EMITTENTE, C.DES_EMITTENTE, " +
				"					B.DAT_INIZIO, B.DAT_FINE, " +
				"					B.COD_ESTERNO, B.NOT_NORMATIVA AS NOT_NORMATIVABASE" +
				" FROM 		PD_PRODNORMATIVE A, PD_NORMATIVE B, " +
				"					PD_EMITTENTI C " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_NORMATIVA = B.COD_NORMATIVA " +
				" AND			B.COD_SOCIETA = C.COD_SOCIETA " +
				" AND			B.COD_EMITTENTE = C.COD_EMITTENTE " +	
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" ORDER BY A.COD_NORMATIVA";
	list = trx.eseguiQuery(sql, "PD_PRODNORMATIVE");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		Normativa obj = new Normativa();
		obj.setCodNormativa(		table.getValoreColonna("COD_NORMATIVA"));
		obj.setDesNormativa(		table.getValoreColonna("DES_NORMATIVA"));
		obj.setCodEmittente(		table.getValoreColonna("COD_EMITTENTE"));
		obj.setDesEmittente(		table.getValoreColonna("DES_EMITTENTE"));
		obj.setDatValidita(			table.getValoreColonna("DAT_INIZIO"));
		obj.setDatFineValidita(	table.getValoreColonna("DAT_FINE"));
		obj.setRifEsterno(			table.getValoreColonna("COD_ESTERNO"));
		obj.setNotNormativa(		table.getValoreColonna("NOT_NORMATIVABASE"));
		if (table.getValoreColonna("NOT_NORMATIVA").trim().length()>0) {
			obj.setNotNormativa(	table.getValoreColonna("NOT_NORMATIVA"));
		}
		obj.setInterna(table.getValoreColonna("FLG_INTERNA").equals(BMA_TRUE));
		caricaAllegatiNorma(trx, obj);
		pdo.getNormative().add(obj);
	}
}
private void caricaObiettivi(BmaJdbcTrx trx) throws it.bma.comuni.BmaException {
	String sql;
	BmaDataTable table;
	BmaDataList list;
	sql = " SELECT	A.COD_OBIETTIVO, B.DES_OBIETTIVO, " +
				"					A.NOT_OBIETTIVO, A.IND_PESO " +
				" FROM 		PD_PRODOBIETTIVI A, PD_OBIETTIVI B " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_OBIETTIVO = B.COD_OBIETTIVO " +
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" ORDER BY A.COD_OBIETTIVO";
	list = trx.eseguiQuery(sql, "PD_PRODOBIETTIVI");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		Obiettivo obj = new Obiettivo();
		obj.setCodObiettivo(	table.getValoreColonna("COD_OBIETTIVO"));
		obj.setDesObiettivo(	table.getValoreColonna("DES_OBIETTIVO"));
		obj.setNotObiettivo(	table.getValoreColonna("NOT_OBIETTIVO"));
		obj.setIndPeso(				table.getValoreColonna("IND_PESO"));
		pdo.getObiettivi().add(obj);
	}
}
private void caricaOggetti(BmaJdbcTrx trx) throws it.bma.comuni.BmaException {
	String sql;
	BmaDataList list;
	BmaDataTable table;

	// Lettura di PD_PRODOGGETTI
	sql = " SELECT  A.COD_OGGETTO, B.DES_OGGETTO, " +
				"					B.COD_TIPOOGGETTO, C.DES_TIPOOGGETTO, " +
				"					A.NUM_PROGRESSIVO, A.NOT_OGGETTO " +
				" FROM		PD_PRODOGGETTI A, PD_OGGETTI B, PD_TIPIOGGETTO C " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_OGGETTO = B.COD_OGGETTO " +				
				" AND			B.COD_SOCIETA = C.COD_SOCIETA " +
				" AND			B.COD_TIPOOGGETTO = C.COD_TIPOOGGETTO " +				
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" ORDER BY A.NUM_PROGRESSIVO ";
	list = trx.eseguiQuery(sql, "PD_PRODOGGETTI");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		OggettoAssicurazione oggetto = new OggettoAssicurazione();
		oggetto.setCodOggetto(			table.getValoreColonna("COD_OGGETTO"));
		oggetto.setDesOggetto(			table.getValoreColonna("DES_OGGETTO"));
		oggetto.setCodTipoOggetto(	table.getValoreColonna("COD_TIPOOGGETTO"));
		oggetto.setDesTipoOggetto(	table.getValoreColonna("DES_TIPOOGGETTO"));
		oggetto.setNotOggetto(			table.getValoreColonna("NOT_OGGETTO"));
		pdo.getOggetti().add(oggetto);
	}
	Enumeration eOgg;
	// Personalizza le caratteristiche con PD_PRODCAROGGETTI
	eOgg = pdo.getOggetti().elements();
	while (eOgg.hasMoreElements()) {
		OggettoAssicurazione oggetto = (OggettoAssicurazione)eOgg.nextElement();
		// Determina le garanzie con PD_PRODGAROGG
		oggetto.getGaranzie().setVector(caricaGaranzie(trx, oggetto));	
		sql = " SELECT  A.COD_CARATTER,	B.DES_CARATTER, " +
					"					B.COD_TIPOCARATTER, C.DES_TIPOCARATTER, " +
					"					A.NUM_PROGRESSIVO, A.VAL_STANDARD " +
					" FROM		PD_PRODCAROGGETTI A, PD_CARATTER B, PD_TIPICARATTER C " +
					" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
					" AND			A.COD_CARATTER = B.COD_CARATTER " +				
					" AND			B.COD_SOCIETA = C.COD_SOCIETA " +
					" AND			B.COD_TIPOCARATTER = C.COD_TIPOCARATTER " +				
					" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
					" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
					" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
					" AND			A.COD_OGGETTO = '" + oggetto.getCodOggetto() + "' " +
					" ORDER BY A.NUM_PROGRESSIVO ";
		list = trx.eseguiQuery(sql, "PD_PRODCAROGGETTI");
		table = list.getTabella();	
		for (int i = 0; i < list.getValori().size(); i++){
			table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
			Caratteristica caratter = new Caratteristica();
			caratter.setCodCaratteristica(	table.getValoreColonna("COD_CARATTER"));
			caratter.setDesCaratteristica(	table.getValoreColonna("DES_CARATTER"));
			caratter.setCodTipo(						table.getValoreColonna("COD_TIPOCARATTER"));
			caratter.setDesTipo(						table.getValoreColonna("DES_TIPOCARATTER"));
			caratter.setValStandard(				table.getValoreColonna("VAL_STANDARD"));
			sql = " SELECT	A.COD_LIVELLO, B.DES_LIVELLO, " +
					"					A.VAL_MIN, A.VAL_MAX, " +
					"					A.DES_VALORE, A.NOT_VALORE " +
					" FROM		PD_PRODVALCAROGG A, PD_LIVELLIDEROGA B " +
					" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
					" AND			A.COD_LIVELLO = B.COD_LIVELLO " +
					" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
					" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
					" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
					" AND			A.COD_OGGETTO = '" + oggetto.getCodOggetto() + "' " +
					" AND			A.COD_CARATTER = '" + caratter.getCodCaratteristica() + "' " +
					"	ORDER BY A.VAL_MIN ";
			BmaDataList list2 = trx.eseguiQuery(sql, "PD_PRODVALCAROGG");
			BmaDataTable tab2 = list2.getTabella();
			for (int j = 0; j < list2.getValori().size(); j++){
				tab2.setValoriDaQuery((Vector)list2.getValori().elementAt(j));
				Valore val = new Valore();
				val.setCodLivelloDeroga(tab2.getValoreColonna("COD_LIVELLO"));
				val.setDesLivelloDeroga(tab2.getValoreColonna("DES_LIVELLO"));
				val.setValMin(					tab2.getValoreColonna("VAL_MIN"));
				val.setValMax(					tab2.getValoreColonna("VAL_MAX"));
				val.setDesValore(				tab2.getValoreColonna("DES_VALORE"));
				val.setNotValore(				tab2.getValoreColonna("NOT_VALORE"));
				caratter.getValori().add(val);
			}
			oggetto.getCaratteristiche().add(caratter);
		}
	}
}
private void caricaOpzioni(BmaJdbcTrx trx) throws it.bma.comuni.BmaException {
	String sql;
	BmaDataTable table;
	BmaDataList list;
	sql = " SELECT	A.COD_OPZIONE, B.DES_OPZIONE, " +
				"					A.NOT_OPZIONE, A.FLG_FACOLTATIVA, " +
				"					A.NUM_PROD_MIN, A.NUM_PROD_MAX, " +
				"					B.COD_REGOLA, C.DES_REGOLA, " +
				"					B.COD_TIPOOPZIONE, D.DES_TIPOOPZIONE " +
				" FROM 		PD_PRODOPZIONI A, PD_OPZIONE B, PD_REGOLEVENDITA C, PD_TIPIOPZIONE D " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_OPZIONE = B.COD_OPZIONE " +
				" AND			B.COD_SOCIETA = C.COD_SOCIETA " +
				" AND			B.COD_REGOLA = C.COD_REGOLA " +
				" AND			B.COD_SOCIETA = D.COD_SOCIETA " +
				" AND			B.COD_TIPOOPZIONE = D.COD_TIPOOPZIONE " +
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" ORDER BY A.COD_OPZIONE";
	list = trx.eseguiQuery(sql, "PD_PRODOPZIONI");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		Opzione obj = new Opzione();
		obj.setCodOpzione(			table.getValoreColonna("COD_OPZIONE"));
		obj.setDesOpzione(			table.getValoreColonna("DES_OPZIONE"));
		obj.setNotOpzione(			table.getValoreColonna("NOT_OPZIONE"));
		obj.setFlgFacoltativa(	table.getValoreColonna("FLG_FACOLTATIVA"));
		obj.setNumProdMin(			table.getValoreColonna("NUM_PROD_MIN"));
		obj.setNumProdMax(			table.getValoreColonna("NUM_PROD_MAX"));
		obj.setCodRegola(				table.getValoreColonna("COD_REGOLA"));
		obj.setDesRegola(				table.getValoreColonna("DES_REGOLA"));
		obj.setCodTipoOpzione(	table.getValoreColonna("COD_TIPOOPZIONE"));
		obj.setDesTipoOpzione(	table.getValoreColonna("DES_TIPOOPZIONE"));
		pdo.getOpzioni().add(obj);
	}

	// Integra con prodotti in opzione
	Enumeration eOpz = pdo.getOpzioni().elements();
	while (eOpz.hasMoreElements()) {
		Opzione opzione = (Opzione)eOpz.nextElement();
		sql = " SELECT	A.COD_PDINOPZIONE, B.DES_PRODOTTO, " +
				"					A.NOT_PRODOTTO, A.NUM_PROGRESSIVO, " +
				"					A.IND_GRUPPO " +
				" FROM 		PD_PDINOPZIONE A, PD_PRODOTTI B " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_PDINOPZIONE = B.COD_PRODOTTO " +
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" AND			A.COD_OPZIONE = '" + opzione.getCodOpzione() + "' " +
				" ORDER BY A.NUM_PROGRESSIVO";
		list = trx.eseguiQuery(sql, "PD_PDINOPZIONE");
		table = list.getTabella();
		for (int i = 0; i < list.getValori().size(); i++){
			table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
			ProdottoInOpzione obj = new ProdottoInOpzione();
			obj.setCodProdotto(	table.getValoreColonna("COD_PDINOPZIONE"));
			obj.setDesProdotto(	table.getValoreColonna("DES_PRODOTTO"));
			obj.setNotProdotto(	table.getValoreColonna("NOT_PRODOTTO"));
			obj.setIndGruppo(		table.getValoreColonna("IND_GRUPPO"));
			opzione.getProdottiOpzione().add(obj);
		}
	}
}
private void caricaParametriRegola(BmaJdbcTrx trx, RegolaTariffa regola) throws it.bma.comuni.BmaException {
	
	String sql;
	BmaDataTable table;
	BmaDataList list;
	sql = " SELECT	A.RIF_PARAMETRO,	A.VAL_PARAMETRO, " +
				"					A.COD_TABELLA, B.DES_TABELLA, " +
				"					A.OBJ_TABELLA " +
				" FROM 		PD_PRODREGTARPAR A LEFT JOIN PD_TABELLETARIFFE B " +
				" ON 			A.COD_SOCIETA = B.COD_SOCIETA " +
				"	AND			A.COD_TABELLA = B.COD_TABELLA " +
				" WHERE 	A.COD_SOCIETA='" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO='" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" AND			A.NUM_PROGRESSIVO = " + regola.getNumProgressivo() + 
				" AND			A.COD_LIVELLO = '" + regola.getCodLivello() + "' " +
				" ORDER BY A.RIF_PARAMETRO ";
	list = trx.eseguiQuery(sql, "PD_PRODREGTARPAR");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		Vector riga = (Vector)list.getValori().elementAt(i);
		table.setValoriDaQuery(riga);
		ParametroRegolaTariffa obj = new ParametroRegolaTariffa();
		obj.setRifParametro(	table.getValoreColonna("RIF_PARAMETRO"));
		obj.setValParametro(	table.getValoreColonna("VAL_PARAMETRO"));
		obj.setCodTabella(		table.getValoreColonna("COD_TABELLA"));
		obj.setDesTabella(		table.getValoreColonna("DES_TABELLA"));
		obj.setObjTabella(		table.getValoreColonna("OBJ_TABELLA"));
		regola.getParametri().add(obj);
	}
}
private void caricaProdotto(BmaJdbcTrx trx) throws it.bma.comuni.BmaException {
	String sql;
	Vector dati;
	this.codSocieta = codSocieta;
	
	BmaDataTable table;
	BmaDataList list;
	sql = " SELECT	A.DES_PRODOTTO, A.DAT_VERSIONE, " +
				"					A.DAT_FINEVERSIONE, A.FLG_APERTA, " +
				"					A.COD_EMITTENTE, B.DES_EMITTENTE, " +
				"					A.DAT_STATO, A.COD_STATO, " +
				"					C.DES_STATO, A.FLG_STRUTTURA, " +
				"					A.FLG_PROPRIETA, A.NOT_PRODOTTO " +
				" FROM PD_PRODOTTI A, PD_EMITTENTI B, PD_STATI C " +
				" WHERE		A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" AND			A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_EMITTENTE = B.COD_EMITTENTE " +
				" AND			A.COD_SOCIETA = C.COD_SOCIETA " +
				" AND			A.COD_STATO = C.COD_STATO " +
				" AND			C.COD_TIPOSTATO='P'";
				
	list = trx.eseguiQuery(sql, "PD_PRODOTTI");
	if (list.getNumeroRighe()==0) throw new BmaException(BMA_ERR_APP_NOTFOUND,pdo.getCodProdotto(),sql,this);		
	if (list.getNumeroRighe()> 1) throw new BmaException(BMA_ERR_APP_NOTUNIQUE,pdo.getCodProdotto(),sql,this);
	table = list.getTabella();
	table.setValoriDaQuery((Vector)list.getValori().elementAt(0));
	pdo.setDesProdotto(			table.getValoreColonna("DES_PRODOTTO"));
	pdo.setDatVersione(			table.getValoreColonna("DAT_VERSIONE"));
	pdo.setDatFineVersione(	table.getValoreColonna("DAT_FINEVERSIONE"));
	pdo.setFlgAperta(				table.getValoreColonna("FLG_APERTA"));
	pdo.setCodEmittente(		table.getValoreColonna("COD_EMITTENTE"));
	pdo.setDatStato(				table.getValoreColonna("DAT_STATO"));
	pdo.setCodStato(				table.getValoreColonna("COD_STATO"));
	pdo.setFlgStruttura(		table.getValoreColonna("FLG_STRUTTURA"));
	pdo.setFlgProprieta(		table.getValoreColonna("FLG_PROPRIETA"));
	pdo.setNotProdotto(			table.getValoreColonna("NOT_PRODOTTO"));
	pdo.setDesEmittente(		table.getValoreColonna("DES_EMITTENTE"));
	pdo.setDesStato(				table.getValoreColonna("DES_STATO"));
	
	// Lettura di Info
	sql = " SELECT	A.COD_INFO,	A.VAL_INFO, B.DES_INFO " +
				" FROM 		PD_PRODINFO A, PD_INFO B " +
				" WHERE 	A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_INFO = B.COD_INFO " +
				" AND			A.COD_SOCIETA='" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" ORDER BY A.COD_INFO ";
	list = trx.eseguiQuery(sql, "PD_PRODINFO");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		Vector riga = (Vector)list.getValori().elementAt(i);
		table.setValoriDaQuery(riga);
		InfoProdotto info = new InfoProdotto();
		info.setCodInfo(			table.getValoreColonna("COD_INFO"));
		info.setValInfo(			table.getValoreColonna("VAL_INFO"));
		info.setDesInfo(			table.getValoreColonna("DES_INFO"));
		pdo.getInfo().add(info);
	}
	
	// Lettura Regole Tariffa
	sql = " SELECT	A.NUM_PROGRESSIVO, " +
				"					A.COD_LIVELLO, B.DES_LIVELLO, " +
				"					A.COD_TARIFFA, C.DES_TARIFFA, " +
				"					A.COD_REGOLA, D.DES_REGOLA, " +
				"					A.DES_SCOPO,	A.VAL_REGOLA, " +
				"					A.NOT_REGOLA, A.COD_OGGETTO, " +
				"					A.COD_GARANZIA, A.COD_CONDIZIONE " +
				" FROM 		PD_PRODREGOLETAR A, PD_LIVELLIDEROGA B, " +
				"					PD_TARIFFE C, PD_REGOLETARIFFE D " +
				" WHERE 	A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_LIVELLO = B.COD_LIVELLO " +
				" AND 		A.COD_SOCIETA = C.COD_SOCIETA " +
				" AND			A.COD_TARIFFA = C.COD_TARIFFA " +
				" AND 		A.COD_SOCIETA = D.COD_SOCIETA " +
				" AND			A.COD_REGOLA = D.COD_REGOLA " +
				" AND			A.COD_SOCIETA='" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" ORDER BY A.NUM_PROGRESSIVO ";
	list = trx.eseguiQuery(sql, "PD_PRODREGOLETAR");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		Vector riga = (Vector)list.getValori().elementAt(i);
		table.setValoriDaQuery(riga);
		RegolaTariffa obj = new RegolaTariffa();
		obj.setNumProgressivo(	table.getValoreColonna("NUM_PROGRESSIVO"));
		obj.setCodLivello(			table.getValoreColonna("COD_LIVELLO"));
		obj.setDesLivello(			table.getValoreColonna("DES_LIVELLO"));
		obj.setCodTariffa(			table.getValoreColonna("COD_TARIFFA"));
		obj.setDesTariffa(			table.getValoreColonna("DES_TARIFFA"));
		obj.setCodRegola(				table.getValoreColonna("COD_REGOLA"));
		obj.setDesRegola(				table.getValoreColonna("DES_REGOLA"));
		obj.setDesScopo(				table.getValoreColonna("DES_SCOPO"));
		obj.setValRegola(				table.getValoreColonna("VAL_REGOLA"));
		obj.setNotRegola(				table.getValoreColonna("NOT_REGOLA"));
		
		obj.setCodOggetto(				table.getValoreColonna("COD_OGGETTO"));
		obj.setCodGaranzia(				table.getValoreColonna("COD_GARANZIA"));
		obj.setCodCondizione(			table.getValoreColonna("COD_CONDIZIONE"));
		
		caricaParametriRegola(trx, obj);
		
		pdo.getRegoleTariffa().add(obj);
		
	}
	// Lettura Regole Aggregazione
	sql = " SELECT	A.COD_AGGREGATO,	B.DES_AGGREGATO, " +
				"					B.COD_TIPOAGGREGATO, C.DES_TIPOAGGREGATO, "  +
				"					A.COD_GARANZIA, D.DES_GARANZIA, " +
				"					A.COD_CONDIZIONE, E.DES_CONDIZIONE, " +
				"					A.PER_PREMIO " +
				" FROM 		PD_REGOLEAGGREGATO A, PD_AGGREGATIPREMI B, " +
				"					PD_TIPIAGGREGATO C, PD_GARANZIE D, " +
				"					PD_CONDIZIONI E " +
				" WHERE 	A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_AGGREGATO = B.COD_AGGREGATO " +
				"	AND			B.COD_SOCIETA = C.COD_SOCIETA " +
				" AND			B.COD_TIPOAGGREGATO = C.COD_TIPOAGGREGATO " +
				" AND 		A.COD_SOCIETA = D.COD_SOCIETA " +
				" AND			A.COD_GARANZIA = D.COD_GARANZIA " +
				" AND 		A.COD_SOCIETA = E.COD_SOCIETA " +
				" AND			A.COD_CONDIZIONE = E.COD_CONDIZIONE " +
				" AND			A.COD_SOCIETA='" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" ORDER BY 	B.COD_TIPOAGGREGATO, A.COD_AGGREGATO, " +
				"						A.COD_GARANZIA, A.COD_CONDIZIONE ";
	list = trx.eseguiQuery(sql, "PD_REGOLEAGGREGATO");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		Vector riga = (Vector)list.getValori().elementAt(i);
		table.setValoriDaQuery(riga);
		RegolaAggregato obj = new RegolaAggregato();
		obj.setCodTipoAggregato(	table.getValoreColonna("COD_TIPOAGGREGATO"));
		obj.setDesTipoAggregato(	table.getValoreColonna("DES_TIPOAGGREGATO"));
		obj.setCodAggregato(			table.getValoreColonna("COD_AGGREGATO"));
		obj.setDesAggregato(			table.getValoreColonna("DES_AGGREGATO"));
		obj.setCodGaranzia(				table.getValoreColonna("COD_GARANZIA"));
		obj.setDesGaranzia(				table.getValoreColonna("DES_GARANZIA"));
		obj.setCodCondizione(			table.getValoreColonna("COD_CONDIZIONE"));
		obj.setDesCondizione(			table.getValoreColonna("DES_CONDIZIONE"));
		obj.setPerPremio(					table.getValoreColonna("PER_PREMIO"));
		pdo.getRegoleAggregati().add(obj);
	}
}
protected Prodotto caricaProdottoCompleto(BmaJdbcTrx trx, String codSocieta, String codProdotto, String rifVersione, String datRiferimento) throws it.bma.comuni.BmaException {
	String sql;
	Vector dati;
	this.codSocieta = codSocieta;
	pdo = new Prodotto();
	pdo.setCodProdotto(codProdotto);
	pdo.setRifVersione(rifVersione);
	caricaProdotto(trx);
	pdo.getIdoneita().setVector(caricaCondizioni(trx, "PD_PRODIDONEITA", null, null));
	caricaOpzioni(trx);
	caricaArgomenti(trx);
	caricaConcorrenti(trx);
	caricaConvenzioni(trx);
	caricaSegmenti(trx);
	caricaDocumentiRuoli(trx);
	caricaObiettivi(trx);
	caricaCanali(trx);
	caricaNormative(trx);
	caricaResponsabili(trx);
	caricaStati(trx);
	caricaModuli(trx);
	pdo.getCondizioni().setVector(caricaCondizioni(trx, "PD_PRODCOND", null, null));
	pdo.getGaranzie().setVector(caricaGaranzie(trx, null));
	caricaOggetti(trx);
	caricaAllegati(trx);
	caricaDescrizioni(trx);
	caricaIdentificativi(trx);
	caricaRaggruppamenti(trx);
	return pdo;
}
private void caricaRaggruppamenti(BmaJdbcTrx trx) throws it.bma.comuni.BmaException {
	String sql;
	BmaDataTable table;
	BmaDataList list;
	sql = " SELECT	A.COD_SCHEMA, B.DES_SCHEMA, " +
				"					A.COD_NODO, " +
				"					C.DES_NODO, A.NUM_PROGRESSIVO, " +
				"					A.DES_PRODOTTONODO " +
				" FROM 		PD_PRODNODI A, PD_SCHEMANODI B, PD_NODI C " +
				" WHERE		A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_SCHEMA = B.COD_SCHEMA " +
				" AND			A.COD_SOCIETA = C.COD_SOCIETA " +
				" AND			A.COD_SCHEMA = C.COD_SCHEMA " +
				" AND			A.COD_NODO = C.COD_NODO " +
				" ORDER BY A.COD_SCHEMA";
	list = trx.eseguiQuery(sql, "PD_PRODNODI");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		Raggruppamento obj = new Raggruppamento();
		obj.setCodSchema(				table.getValoreColonna("COD_SCHEMA"));
		obj.setDesSchema(				table.getValoreColonna("DES_SCHEMA"));
		obj.setCodNodo(					table.getValoreColonna("COD_NODO"));
		obj.setDesNodo(					table.getValoreColonna("DES_NODO"));
		obj.setDesProdotto(			table.getValoreColonna("DES_PRODOTTONODO"));
		obj.setNumProgressivo(	table.getValoreColonna("NUM_PROGRESSIVO"));
		pdo.getRagguppamenti().add(obj);
	}
}
private void caricaResponsabili(BmaJdbcTrx trx) throws it.bma.comuni.BmaException {
	String sql;
	BmaDataTable table;
	BmaDataList list;
	// Lettura di PD_PRODRESPONSABILI
	sql = " SELECT	A.COD_TIPORESPONSABILE, B.DES_TIPORESPONSABILE, " +
				"					A.COD_POSIZIONE, C.DES_POSIZIONE, " +
				"					A.NOT_RESPONSABILE " +
				" FROM 		PD_PRODRESPONSABILI A, PD_TIPIRESPONSABILE B, " +
				"					PD_POSIZIONI C " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_TIPORESPONSABILE = B.COD_TIPORESPONSABILE " +
				" AND			A.COD_SOCIETA = C.COD_SOCIETA " +
				" AND			A.COD_POSIZIONE = C.COD_POSIZIONE " +	
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" ORDER BY A.COD_TIPORESPONSABILE, A.COD_POSIZIONE ";
	list = trx.eseguiQuery(sql, "PD_PRODRESPONSABILI");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		Responsabile obj = new Responsabile();
		obj.setCodTipoResponsabile(		table.getValoreColonna("COD_TIPORESPONSABILE"));
		obj.setDesTipoResponsabile(		table.getValoreColonna("DES_TIPORESPONSABILE"));
		obj.setCodPosizione(					table.getValoreColonna("COD_POSIZIONE"));
		obj.setDesPosizione(					table.getValoreColonna("DES_POSIZIONE"));
		obj.setNotResponsabile(				table.getValoreColonna("NOT_RESPONSABILE"));
		caricaApprovazioni(trx, obj);
		pdo.getResponsabili().add(obj);
	}
}
private void caricaSegmenti(BmaJdbcTrx trx) throws it.bma.comuni.BmaException {
	String sql;
	BmaDataTable table;
	BmaDataList list;
	sql = " SELECT	A.COD_SEGMENTO, B.DES_SEGMENTO, " +
				"					A.NOT_SEGMENTO " +
				" FROM 		PD_PRODSEGMENTI A, PD_SEGMENTI B " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_SEGMENTO = B.COD_SEGMENTO " +
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" ORDER BY A.COD_SEGMENTO";
	list = trx.eseguiQuery(sql, "PD_PRODOBIETTIVI");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		Segmento obj = new Segmento();
		obj.setCodSegmento(	table.getValoreColonna("COD_SEGMENTO"));
		obj.setDesSegmento(	table.getValoreColonna("DES_SEGMENTO"));
		obj.setNotSegmento(	table.getValoreColonna("NOT_SEGMENTO"));
		pdo.getSegmenti().add(obj);
	}

	// Integra con obiettivi
	Enumeration eSeg = pdo.getSegmenti().elements();
	while (eSeg.hasMoreElements()) {
		Segmento segmento = (Segmento)eSeg.nextElement();
		sql = " SELECT	A.COD_OBIETTIVO, B.DES_OBIETTIVO, " +
				"					A.NOT_OBIETTIVO, A.IND_PESO " +
				" FROM 		PD_OBIETTIVOSEGMENTO A, PD_OBIETTIVI B " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_OBIETTIVO = B.COD_OBIETTIVO " +
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_SEGMENTO = '" + segmento.getCodSegmento() + "' " +
				" ORDER BY A.COD_OBIETTIVO";
		list = trx.eseguiQuery(sql, "PD_OBIETTIVOSEGMENTO");
		table = list.getTabella();
		for (int i = 0; i < list.getValori().size(); i++){
			table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
			Obiettivo obj = new Obiettivo();
			obj.setCodObiettivo(	table.getValoreColonna("COD_OBIETTIVO"));
			obj.setDesObiettivo(	table.getValoreColonna("DES_OBIETTIVO"));
			obj.setNotObiettivo(	table.getValoreColonna("NOT_OBIETTIVO"));
			obj.setIndPeso(				table.getValoreColonna("IND_PESO"));
			segmento.getObiettivi().add(obj);
		}
	}
}
private void caricaStati(BmaJdbcTrx trx) throws it.bma.comuni.BmaException {
	String sql;
	BmaDataTable table;
	BmaDataList list;
	sql = " SELECT	A.COD_TIPOSTATO, C.DES_TIPOSTATO, " +
				"					A.DAT_INIZIO, A.COD_STATO, " +
				"					B.DES_STATO, A.COD_UTENTE " +
				" FROM 		PD_PRODSTATI A, PD_STATI B, PD_TIPISTATO C " +
				" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
				" AND			A.COD_TIPOSTATO = B.COD_TIPOSTATO " +
				" AND			A.COD_STATO = B.COD_STATO " +
				" AND			B.COD_SOCIETA = C.COD_SOCIETA " +
				" AND			B.COD_TIPOSTATO = C.COD_TIPOSTATO " +
				" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
				" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
				" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
				" ORDER BY A.COD_TIPOSTATO, A.DAT_INIZIO";
	list = trx.eseguiQuery(sql, "PD_PRODSTATI");
	table = list.getTabella();
	for (int i = 0; i < list.getValori().size(); i++){
		table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
		Stato obj = new Stato();
		obj.setCodTipoStato(	table.getValoreColonna("COD_TIPOSTATO"));
		obj.setDesTipoStato(	table.getValoreColonna("DES_TIPOSTATO"));
		obj.setCodStato(			table.getValoreColonna("COD_STATO"));
		obj.setDesStato(			table.getValoreColonna("DES_STATO"));
		obj.setCodUtente(			table.getValoreColonna("COD_UTENTE"));
		obj.setDatValidita(		table.getValoreColonna("DAT_INIZIO"));
		pdo.getStati().add(obj);
	}
	Enumeration e = pdo.getStati().elements();
	while (e.hasMoreElements()) {
		Stato stato = (Stato)e.nextElement();
		sql = " SELECT	A.COD_POSIZIONE, B.DES_POSIZIONE, " +
					"					A.COD_UTENTE, A.DAT_AUTORIZZAZIONE, " +
					"					A.COD_AUTORIZZAZIONE, A.NOT_AUTORIZZAZIONE " +
					" FROM 		PD_AUTORIZZAZIONI A, PD_POSIZIONI B " +
					" WHERE		A.COD_SOCIETA = B.COD_SOCIETA " +
					" AND			A.COD_POSIZIONE = B.COD_POSIZIONE " +
					" AND			A.COD_SOCIETA = '" + codSocieta + "' " +
					" AND			A.COD_PRODOTTO = '" + pdo.getCodProdotto() + "' " +
					" AND			A.RIF_VERSIONE = '" + pdo.getRifVersione() + "' " +
					" AND			A.COD_TIPOSTATO = '" + stato.getCodTipoStato() + "' " +
					" AND			A.COD_STATO = '" + stato.getCodStato() + "' " +
					" AND			A.DAT_INIZIO = '" + stato.getDatValidita() + "' " +
					" ORDER BY A.COD_POSIZIONE";
		list = trx.eseguiQuery(sql, "PD_PRODSTATI");
		table = list.getTabella();
		for (int i = 0; i < list.getValori().size(); i++){
			table.setValoriDaQuery((Vector)list.getValori().elementAt(i));
			Autorizzazione obj = new Autorizzazione();
			obj.setCodTipoStato(			stato.getCodTipoStato());
			obj.setDesTipoStato(			stato.getDesTipoStato());
			obj.setCodStato(					stato.getCodStato());
			obj.setDesStato(					stato.getDesStato());
			obj.setDatValidita(				stato.getDatValidita());
			obj.setCodPosizione(			table.getValoreColonna("COD_POSIZIONE"));
			obj.setDesPosizione(			table.getValoreColonna("DES_POSIZIONE"));
			obj.setCodUtente(					table.getValoreColonna("COD_UTENTE"));
			obj.setCodAutorizzazione(	table.getValoreColonna("COD_AUTORIZZAZIONE"));
			obj.setDatAutorizzazione(	table.getValoreColonna("DAT_AUTORIZZAZIONE"));
			obj.setNotAutorizzazione(	table.getValoreColonna("NOT_AUTORIZZAZIONE"));
			pdo.getAutorizzazioni().add(obj);
		}
	}
}
private void completaDatiRegoleTariffa(OggettoAssicurazione oggetto, Garanzia garanzia, Condizione condizione) {
	Enumeration e = pdo.getRegoleTariffa().elements();
	while (e.hasMoreElements()) {
		RegolaTariffa r = (RegolaTariffa)e.nextElement();
		if (oggetto!=null && oggetto.getCodOggetto().equals(r.getCodOggetto())) {
			r.setDesOggetto(oggetto.getDesOggetto());
		}
		if (garanzia!=null && garanzia.getCodGaranzia().equals(r.getCodGaranzia())) {
			r.setDesGaranzia(garanzia.getDesGaranzia());
		}
		if (condizione!=null && condizione.getCodCondizione().equals(r.getCodCondizione())) {
			r.setDesCondizione(condizione.getDesCondizione());
		}
	}	
}
protected void eseguiAggiorna(BmaJdbcTrx trx, BmaDataTable tabella, String tipoAzione, Hashtable valori) throws it.bma.comuni.BmaException {
	String sql = "";
	if (tipoAzione.equals(BMA_SQL_INSERT)) {
		sql = tabella.getSqlInsert(valori);
		trx.eseguiSqlUpdate(sql);
	}		
	else if (tipoAzione.equals(BMA_SQL_UPDATE)) {
		sql = tabella.getSqlUpdate(valori);
		trx.eseguiSqlUpdate(sql);
	}
	else if (tipoAzione.equals(BMA_SQL_DELETE)) {
		sql = tabella.getSqlDelete(valori);
		trx.eseguiSqlUpdate(sql);
	}
	else {
		throw new BmaException(BMA_ERR_WEB_PARAMETRO,"Tipo di Azione non previsto: " + tipoAzione,"",this);
	}
}
public String getChiave() {
	return getClassName();
}
protected String getXmlTag() {
	return getClassName();
}
protected Hashtable valoreTipoStatoPrincipale() throws it.bma.comuni.BmaException {
	Hashtable lista = new Hashtable(1);	
	lista.put("P", "PRINCIPALE");
	return lista;
}
protected Hashtable valoreTipoStatoTariffa() throws it.bma.comuni.BmaException {
	Hashtable lista = new Hashtable(1);	
	lista.put("T", "TARIFFA");
	return lista;
}
protected BmaHashtable valoriAggregati(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaAggregati");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_AGGREGATIPREMI");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_AGGREGATO");
		String des = table.getValoreColonna("DES_AGGREGATO");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriCanali(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaCanale");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_CANALI");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_CANALE");
		String des = table.getValoreColonna("DES_CANALE");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriCaratteristiche(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaCaratteristiche");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_CARATTER");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_CARATTER");
		String des = table.getValoreColonna("DES_CARATTER");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriCondizioniGaranzia(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaCondizioni");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_CONDIZIONI");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	filtri.put("COD_TIPOCONDIZIONE", "CPGAR");
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_CONDIZIONE");
		String des = table.getValoreColonna("DES_CONDIZIONE");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriCondizioniGenerali(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaCondizioni");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_CONDIZIONI");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	filtri.put("COD_TIPOCONDIZIONE", "CGPOL");
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_CONDIZIONE");
		String des = table.getValoreColonna("DES_CONDIZIONE");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriConvenzioni(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaConvenzioni");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_CONVENZIONI");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_CONVENZIONE");
		String des = table.getValoreColonna("DES_CONVENZIONE");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriEntiEmittenti(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaEntiEmittenti");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_EMITTENTI");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_EMITTENTE");
		String des = table.getValoreColonna("DES_EMITTENTE");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriGaranzie(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaGaranzie");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_GARANZIE");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_GARANZIA");
		String des = table.getValoreColonna("DES_GARANZIA");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriInfo(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaInfo");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_INFO");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_INFO");
		String des = table.getValoreColonna("DES_INFO");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriLivelliDeroga(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaLivelliDeroga");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_LIVELLIDEROGA");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_LIVELLO");
		String des = table.getValoreColonna("DES_LIVELLO");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriNormative(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaNormative");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_NORMATIVE");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_NORMATIVA");
		String des = table.getValoreColonna("DES_NORMATIVA");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriOggetti(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaOggetti");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_OGGETTI");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_OGGETTO");
		String des = table.getValoreColonna("DES_OGGETTO");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriPosizioniAziendali(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaPosizioniAziendali");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_POSIZIONI");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_POSIZIONE");
		String des = table.getValoreColonna("DES_POSIZIONE");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriRegoleStruttura(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("RegoleStruttura");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_REGOLEVENDITA");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	filtri.put("FLG_STRUTTURA", BMA_TRUE);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_REGOLA");
		String des = table.getValoreColonna("DES_REGOLA");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriRegoleTariffa(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("RegoleTariffa");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_REGOLETARIFFE");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_REGOLA");
		String des = table.getValoreColonna("DES_REGOLA");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriStatiPrincipali(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaStati");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_STATI");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	filtri.put("COD_TIPOSTATO", "P");
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_STATO");
		String des = table.getValoreColonna("DES_STATO");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriStatiTariffa(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaStati");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_STATI");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	filtri.put("COD_TIPOSTATO", "T");
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_STATO");
		String des = table.getValoreColonna("DES_STATO");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriTabelleTariffe(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaTabelle");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_TABELLETARIFFE");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_TABELLA");
		String des = table.getValoreColonna("DES_TABELLA");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriTariffe(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaTariffe");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_TARIFFE");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_TARIFFA");
		String des = table.getValoreColonna("DES_TARIFFA");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriTipiAggregato(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaTipiAggregato");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_TIPIAGGREGATO");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_TIPOAGGREGATO");
		String des = table.getValoreColonna("DES_TIPOAGGREGATO");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriTipiAllegato(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaTipiAllegato");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_TIPIALLEGATO");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_TIPOALLEGATO");
		String des = table.getValoreColonna("DES_TIPOALLEGATO");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriTipiCaratteristica(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaTipiCaratterisitica");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_TIPICARATTER");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_TIPOCARATTER");
		String des = table.getValoreColonna("DES_TIPOCARATTER");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriTipiCondizione(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaTipiCondizione");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_TIPICONDIZIONE");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_TIPOCONDIZIONE");
		String des = table.getValoreColonna("DES_TIPOCONDIZIONE");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriTipiDescrizione(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaTipiDescrizione");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_TIPIDESCRIZIONE");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_TIPODESCRIZIONE");
		String des = table.getValoreColonna("DES_TIPODESCRIZIONE");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriTipiGaranzia(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaTipiGaranzia");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_TIPIGARANZIA");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_TIPOGARANZIA");
		String des = table.getValoreColonna("DES_TIPOGARANZIA");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriTipiIdentifica(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaTipiIdentifica");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_TIPIIDENTIFICA");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_TIPOIDENTIFICA");
		String des = table.getValoreColonna("DES_TIPOIDENTIFICA");
		lista.setString(cod, des);
	}
	return lista;
}
protected BmaHashtable valoriTipiOggetto(BmaJdbcTrx trx, String codSocieta) throws it.bma.comuni.BmaException {
	BmaHashtable lista = new BmaHashtable("ListaTipiOggetto");	
	String sql;
	Vector dati;
	
	BmaDataTable table = jModel.getDataTable("PD_TIPIOGGETTO");
	Hashtable filtri = new Hashtable();
	filtri.put("COD_SOCIETA", codSocieta);
	sql = table.getSqlLista(filtri);
	dati = trx.eseguiSqlSelect(sql);
	for (int i = 0; i < dati.size(); i++){
		Vector riga = (Vector)dati.elementAt(i);
		table.setValoriDaQuery(riga);
		String cod = table.getValoreColonna("COD_TIPOOGGETTO");
		String des = table.getValoreColonna("DES_TIPOOGGETTO");
		lista.setString(cod, des);
	}
	return lista;
}
}
