package it.bma.db;
import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class BmaDbEdit extends BmaServizioDb {
	private final String SQL_COMMAND = "Command";
	private final String SQL_QUERY = "Query";
	public BmaDbEdit() {
		super();
	}
	public void completaInputServizio(BmaInputServizio is) throws BmaException {
		String sql = is.getInfoServizio("DES_SQL");
		if (sql.trim().length()>0) {
			is.setInfoServizio("execSql", getTipoSql(sql));
		}
	}
	private String getTipoSql(String sql) {
		int i = sql.indexOf("SELECT");
		if (i>=0 && i<10) return SQL_QUERY;
		else return SQL_COMMAND;
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
	public BmaDataForm impostaModulo() throws BmaException {
		BmaDataForm modulo = new BmaDataForm("DATA-MODEL");
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
		campo.setNome("DES_SQL");
		campo.setDescrizione("Comando o Query Sql");
		campo.setTipo(BMA_SQL_TYP_VARCHAR);
		campo.setLunghezza("1000");
		campo.setTipoControllo(BMA_CONTROLLO_TEXT);
		modulo.getDati().add(campo);
		return modulo;
	}
}
