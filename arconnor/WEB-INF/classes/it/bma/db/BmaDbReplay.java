package it.bma.db;
import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class BmaDbReplay extends BmaServizioDb {
	public BmaDbReplay() {
		super();
	}
	public boolean eseguiComando() throws BmaException {
		String beanListaTabelle = "loadTables";
		String beanSelezionate = BMA_JSP_PREFISSO_MULTI + "TabelleSelezionate"; 
		//
		String codSelezione = getParametri().getString(BMA_JSP_CAMPO_SELEZIONE);
		if (codSelezione==null || codSelezione.trim().length()==0) {
			sessione.removeBeanApplicativo(BMA_JSP_BEAN_LISTA);
			sessione.removeBeanApplicativo(beanListaTabelle);
			sessione.removeBeanApplicativo(beanSelezionate);
			impostaAzioni();
			BmaDataForm modulo = impostaModulo();
			sessione.setBeanApplicativo(BMA_JSP_BEAN_FORM, modulo);
		}
		else if (codSelezione.equals("ListaTabelle")) {
			BmaValuesList selezionate = (BmaValuesList)getParamMulti().getElement(beanSelezionate);
			if (selezionate!=null) sessione.setBeanApplicativo(beanSelezionate, selezionate);
			sessione.removeBeanApplicativo(beanListaTabelle);
			BmaDataForm modulo = (BmaDataForm)sessione.getBeanApplicativo(BMA_JSP_BEAN_FORM);
			if (modulo==null) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Modulo Assente", "",  this);
			aggiornaModulo(modulo);
			BmaInputServizio is = new BmaInputServizio();
			impostaParametriServizio(modulo, is);
			is.setInfoServizio("loadTables", BMA_TRUE);
			String codServizio = getNomeServizio(getCodFunzione());
			BmaOutputServizio os = eseguiServizio(codServizio, is);
			BmaValuesList list = new BmaValuesList("loadTables");
			list.fromXml(os.getXmlOutput());
			sessione.setBeanApplicativo(beanListaTabelle, list);
			sessione.setBeanApplicativo(BMA_JSP_BEAN_FORM, modulo);
		}
		else if (codSelezione.equals("EseguiReplay")) {
			BmaValuesList selezionate = (BmaValuesList)getParamMulti().getElement(beanSelezionate);
			if (selezionate!=null) sessione.setBeanApplicativo(beanSelezionate, selezionate);
			BmaDataForm modulo = (BmaDataForm)sessione.getBeanApplicativo(BMA_JSP_BEAN_FORM);
			if (modulo==null) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Modulo Assente","",this);
			aggiornaModulo(modulo);
			BmaInputServizio is = new BmaInputServizio();
			impostaParametriServizio(modulo, is);
			completaInputServizio(is);
			String codServizio = getNomeServizio(getCodFunzione());
			BmaOutputServizio os = eseguiServizio(codServizio, is);
			gestisciOutputServizio(os);
		}
		return false;
	}
	public void completaInputServizio(BmaInputServizio is) throws BmaException {
		is.setInfoServizio("loadTables", BMA_FALSE);
		String beanSelezionate = BMA_JSP_PREFISSO_MULTI + "TabelleSelezionate"; 
		BmaValuesList selezionate = (BmaValuesList)sessione.getBeanApplicativo(beanSelezionate);
		if (selezionate==null) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Nessuna Tabelle Selezionata", "", this);
		String[] tabelle = selezionate.getValues();
		for (int i=0;i<tabelle.length;i++) {
			is.setInfoServizio(tabelle[i], tabelle[i]);
		}
	}
	public void gestisciOutputServizio(BmaOutputServizio os) throws BmaException {
		BmaVector vMain = new BmaVector("LogInfo");
		String xml = os.getXmlOutput();
		vMain.fromXml(xml);
		BmaParametro pXml = new BmaParametro("xml", xml);
		sessione.setBeanTemporaneo(BMA_JSP_BEAN_XML, pXml);
		BmaDataList lista = new BmaDataList("LogInfo");
		BmaDataField campo = null;
		//
		campo = new BmaDataField();
		campo.setNome("COD_TABELLA");
		campo.setDescrizione("Tabella");
		campo.setTipo(BMA_SQL_TYP_CHAR);
		campo.setLunghezza("20");
		lista.getTabella().getColonne().add(campo);
		//
		campo = new BmaDataField();
		campo.setNome("COD_CHIAVE");
		campo.setDescrizione("COD_CHIAVE");
		campo.setTipo(BMA_SQL_TYP_CHAR);
		campo.setLunghezza("10");
		lista.getTabella().getColonne().add(campo);
		//
		campo = new BmaDataField();
		campo.setNome("COD_AZIONE");
		campo.setDescrizione("Azione");
		campo.setTipo(BMA_SQL_TYP_CHAR);
		campo.setLunghezza("15");
		lista.getTabella().getColonne().add(campo);
		//
		campo = new BmaDataField();
		campo.setNome("COD_INFO");
		campo.setDescrizione("Info");
		campo.setTipo(BMA_SQL_TYP_CHAR);
		campo.setLunghezza("50");
		lista.getTabella().getColonne().add(campo);
		//
		Vector dati = lista.getValori();
		for (int i=0;i<vMain.getSize();i++) {
			BmaLogInfo info = (BmaLogInfo)vMain.getElement(i);
			Vector riga = new Vector();
			riga.add(info.getTable());
			riga.add(info.getKey());
			riga.add(info.getAction());
			riga.add(info.getInfo());
			dati.add(riga);
		}
		sessione.setBeanApplicativo(BMA_JSP_BEAN_LISTA, lista);
	}
	public String getFunzioneEditDettaglio() {
		return "";
	}
	public BmaDataForm impostaModulo() throws BmaException {
		BmaDataForm modulo = new BmaDataForm("DB-REPLAY");
		BmaDataField campo = null;
		/* Sorgente Dati Main */
		campo = new BmaDataField();
		campo.setNome(BMA_JSP_CAMPO_FONTEDATI);
		campo.setDescrizione("Sorgente Dati Principale");
		campo.setTipo(BMA_SQL_TYP_CHAR);
		campo.setLunghezza("20");
		campo.setTipoControllo(BMA_CONTROLLO_LISTA);
		campo.setValore(jsp.config.getFonteDefault());
		campo.setValoriControllo(sessione.getUserConfig().getListaUrlFonti());
		modulo.getDati().add(campo);
		/* Sorgente Dati Target */
		campo = new BmaDataField();
		campo.setNome("COD_TARGET");
		campo.setDescrizione("Sorgente Dati Target");
		campo.setTipo(BMA_SQL_TYP_CHAR);
		campo.setLunghezza("20");
		campo.setTipoControllo(BMA_CONTROLLO_LISTA);
		campo.setValoriControllo(sessione.getUserConfig().getListaUrlFonti());
		modulo.getDati().add(campo);
		/* Ordine di caricamento */
		campo = new BmaDataField();
		campo.setNome("IND_LOADORDER");
		campo.setDescrizione("Ordine di Caricamento");
		campo.setTipo(BMA_SQL_TYP_CHAR);
		campo.setLunghezza("1");
		campo.setTipoControllo(BMA_CONTROLLO_LISTA);
		Hashtable numLivello = new Hashtable();
		numLivello.put("0","Tutti i Livelli");
		for (int i=1;i<10;i++) {
			String s = Integer.toString(i);
			numLivello.put(s,"Livello - " + s);
		}
		campo.setValoriControllo(numLivello);
		campo.setValore("0");
		modulo.getDati().add(campo);
		/* Sorgente Replay Mode */
		campo = new BmaDataField();
		campo.setNome("IND_REPLAYMODE");
		campo.setDescrizione("Modo Replay");
		campo.setTipo(BMA_SQL_TYP_CHAR);
		campo.setLunghezza("1");
		campo.setTipoControllo(BMA_CONTROLLO_LISTA);
		Hashtable valoriReplay = new Hashtable();
		valoriReplay.put("ReplaceMain","Replace Main");
		valoriReplay.put("ReplaceTarget","Replace Target");
		valoriReplay.put("MergeMain","Merge Main");
		valoriReplay.put("MergeTarget","Merge Target");
		campo.setValoriControllo(valoriReplay);
		campo.setValore("MergeTarget");
		modulo.getDati().add(campo);
		/* Flag Solo Check */
		campo = new BmaDataField();
		campo.setNome("FLG_CHECKONLY");
		campo.setDescrizione("Solo Check (S/N)");
		campo.setTipo(BMA_SQL_TYP_CHAR);
		campo.setLunghezza("1");
		campo.setTipoControllo(BMA_CONTROLLO_BOOLEAN);
		campo.setValore(BMA_TRUE);
		modulo.getDati().add(campo);
		return modulo;
	}
}
