package it.bma.media;
import java.util.*;
import java.io.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class MdLoad extends MdServizio {
	public MdLoad() {
		super();
	}
	public void completaInputServizio(BmaInputServizio is) throws BmaException {
		
	}
	private String caricaVolume(BmaInputServizio is) throws BmaException {
		String nomeVolume = is.getInfoServizio("DES_FILE");
		return "";
	}
	public void gestisciOutputServizio(BmaOutputServizio os) throws BmaException {
		BmaDataForm modulo = (BmaDataForm)sessione.getBeanApplicativo(BMA_JSP_BEAN_FORM);
		String sql = modulo.getValoreCampo("DES_SQL");
		if (sql.trim().length()>0) {
			BmaDataList listaQuery = new BmaDataList("SqlSelect");
			String xml = os.getXmlOutput();
			listaQuery.fromXml(xml);
	 		sessione.setBeanApplicativo(BMA_JSP_BEAN_LISTA, listaQuery);
		}
/*		
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
			BmaValuesList listaTabelle = (BmaValuesList)vMain.getElement(i);
			String[] vTables = listaTabelle.getValues();
			String codLivello = listaTabelle.getName();
			for (int j=0;j<vTables.length;j++) {
				Vector riga = new Vector();
				riga.add(codLivello);
				riga.add(getNumeroXChiave(j, 3));
				riga.add(vTables[j]);
				dati.add(riga);
			}
		}
 		sessione.setBeanApplicativo(BMA_JSP_BEAN_LISTA, lista);
*/
	}
	public String getFunzioneEditDettaglio() {
		return "";
	}
	private Hashtable getTipiVolume() {
		// Da sviluppare il richiamo al servizio
		Hashtable result = new Hashtable();
		result.put("CD-BACKUP","CD Back Up");
		result.put("CD-ALBUM","CD Album Immagini");
		result.put("CD-SHOW","CD Show");
		result.put("CD-PROG","CD Programmi");
		return result;
	}
	public BmaDataForm impostaModulo() throws BmaException {
		BmaDataForm modulo = new BmaDataForm("MEDIA-LOAD");
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
		
		campo = new BmaDataField();
		campo.setNome("COD_TIPOVOLUME");
		campo.setDescrizione("Tipo di Volume");
		campo.setTipo(BMA_SQL_TYP_CHAR);
		campo.setLunghezza("20");
		campo.setTipoControllo(BMA_CONTROLLO_LISTA);
		campo.setValoriControllo(getTipiVolume());
		modulo.getDati().add(campo);
		
		campo = new BmaDataField();
		campo.setNome("COD_VOLUME");
		campo.setDescrizione("Codice Volume");
		campo.setTipo(BMA_SQL_TYP_CHAR);
		campo.setLunghezza("20");
		campo.setTipoControllo(BMA_CONTROLLO_LIBERO);
		modulo.getDati().add(campo);
		
		campo = new BmaDataField();
		campo.setNome("DES_VOLUME");
		campo.setDescrizione("Descrizione Volume");
		campo.setTipo(BMA_SQL_TYP_CHAR);
		campo.setLunghezza("50");
		campo.setTipoControllo(BMA_CONTROLLO_LIBERO);
		modulo.getDati().add(campo);
		
		campo = new BmaDataField();
		campo.setNome("DES_FILE");
		campo.setDescrizione("Nome File");
		campo.setTipo(BMA_SQL_TYP_CHAR);
		campo.setLunghezza("50");
		campo.setTipoControllo(BMA_CONTROLLO_FILE);
		modulo.getDati().add(campo);
		
		return modulo;
	}
}
