package it.bma.db;
import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class BmaDbReplay extends BmaServizioDb {
	public BmaDbReplay() {
		super();
	}
	public void completaInputServizio(BmaInputServizio is) throws BmaException {
		is.setInfoServizio("flgLoad", BMA_TRUE);
	}
	public void gestisciOutputServizio(BmaOutputServizio os) throws BmaException {
		/*
		BmaVector vMain = new BmaVector("DBReplay");
		String xml = os.getXmlOutput();
		vMain.fromXml(xml);
		BmaParametro pXml = new BmaParametro("xml", xml);
		sessione.setBeanTemporaneo(BMA_JSP_BEAN_XML, pXml);
		BmaDataList lista = new BmaDataList("LoadOrder");
		BmaDataField campo = null;
		campo = new BmaDataField();
		campo.setNome("COD_LIVELLO");
		campo.setDescrizione("Livello di Load");
		campo.setTipo(BMA_SQL_TYP_CHAR);
		campo.setLunghezza("30");
		lista.getTabella().getColonne().add(campo);
		campo = new BmaDataField();
		campo.setNome("NUM_PROGRESSIVO");
		campo.setDescrizione("Progressivo");
		campo.setTipo(BMA_SQL_TYP_CHAR);
		campo.setLunghezza("5");
		lista.getTabella().getColonne().add(campo);
		campo = new BmaDataField();
		campo.setNome("COD_TABELLA");
		campo.setDescrizione("Tabella");
		campo.setTipo(BMA_SQL_TYP_CHAR);
		campo.setLunghezza("30");
		lista.getTabella().getColonne().add(campo);
		Vector dati = lista.getValori();
		for (int i=0;i<vMain.getSize();i++) {
			JdbcTableList v = (JdbcTableList)vMain.getElement(i);
			BmaVector vTables = v.getTableList().getBmaVector();
			String codLivello = v.getChiave();
			for (int j=0;j<vTables.getSize();j++) {
				BmaParametro p = (BmaParametro)vTables.getElement(j);
				Vector riga = new Vector();
				riga.add(codLivello);
				riga.add(p.getNome());
				riga.add(p.getValore());
				dati.add(riga);
			}
		}
		sessione.setBeanApplicativo(BMA_JSP_BEAN_LISTA, lista);
		 */
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
		/* Verifica se prepare la lista tabelle */
		String fonteCtr = getParametri().getString(BMA_JSP_CAMPO_FONTEDATI);
		if (fonteCtr!=null && fonteCtr.trim().length()>0) {
		}
		
		return modulo;
	}
	private Hashtable listaTabelle(String nomeSorgente) throws BmaException {
		BmaInputServizio is = new BmaInputServizio();
		is.setInfoServizio("loadTables", BMA_TRUE);
		String codServizio = getNomeServizio(getCodFunzione());
		BmaOutputServizio os = eseguiServizio(codServizio, is);
		BmaVector v = new BmaVector("tablesNames");
		v.fromXml(os.getXmlOutput());
		for (int i=0;i<v.getSize();i++) {
			
		}
		return new Hashtable();
	}
}
