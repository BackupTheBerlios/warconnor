package it.bma.db;
import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class BmaOrdineLoad extends BmaServizioDb {
	public BmaOrdineLoad() {
		super();
	}
	
	public void completaInputServizio(BmaInputServizio is) throws BmaException {
		is.setInfoServizio("flgLoad", BMA_TRUE);
	}
	
	public void gestisciOutputServizio(BmaOutputServizio os) throws BmaException {
		BmaVector vMain = new BmaVector("LoadOrder");
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
	}
	
	public String getFunzioneEditDettaglio() {
		return "";
	}
	
	public BmaDataForm impostaModulo() throws BmaException {
		BmaDataForm modulo = new BmaDataForm("ORDINE-LOAD");
		BmaDataField campo = null;
		campo = new BmaDataField();
		campo.setNome(BMA_JSP_CAMPO_FONTEDATI);
		campo.setDescrizione("Sorgente Dati");
		campo.setTipo(BMA_SQL_TYP_CHAR);
		campo.setLunghezza("20");
		campo.setTipoControllo(BMA_CONTROLLO_LISTA);
		campo.setValore(jsp.config.getFonteDefault());
		campo.setValoriControllo(sessione.getUserConfig().getListaUrlFonti());
		modulo.getDati().add(campo);
		return modulo;
	}
	
}
