package it.bma.prodotti;
import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class PDRemove extends it.bma.web.BmaFunzioneServizi {
	public PDRemove() {
		super();
	}
	
	public void completaInputServizio(it.bma.web.BmaInputServizio is) throws it.bma.comuni.BmaException {
		BmaUserConfig config = sessione.getUserConfig();
		String nSource = config.getParametroApplicazione(config.BMA_CFGCOD_APP_FONTE_TARGET); 
		is.setInfoServizio(BMA_JSP_CAMPO_FONTEDATI,  nSource);
		is.setInfoServizio("codAzione", "elimina");
	}
	
	public void gestisciOutputServizio(it.bma.web.BmaOutputServizio os) throws it.bma.comuni.BmaException {
	}
	
	public String getNomeServizio(String funzione) {
		return "ProdottoOperativo";
	}
	
	public void impostaAzioni() {
		azioniMenu.clear();
		new JspProdotti().impostaMenuPrincipale(sessione, azioniMenu);
		BmaFunzione f = sessione.getUtente().getFunzione(this.getCodFunzione());
		int liv = this.getLivelloMenu() + 1;
		if (f.isAzioneAmmessa(BMA_JSP_AZIONE_DETTAGLIO)) {
			azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_AZIONE_DETTAGLIO, BMA_JSP_MENU_AZIONE));
		}
	}
	
	public BmaDataForm impostaModulo() throws BmaException {
		BmaDataForm modulo = new BmaDataForm("SELEZIONE_PRODOTTO");
		BmaDataField campo = null;
		campo = new BmaDataField();
		campo.setNome("COD_PRODOTTO");
		campo.setDescrizione("Codice Prodotto");
		campo.setTipo(BMA_SQL_TYP_CHAR);
		campo.setLunghezza("20");
		campo.setTipoControllo(BMA_CONTROLLO_LIBERO);
		modulo.getDati().add(campo);
		//
		campo = new BmaDataField();
		campo.setNome("RIF_VERSIONE");
		campo.setDescrizione("Versione");
		campo.setTipo(BMA_SQL_TYP_CHAR);
		campo.setLunghezza("20");
		campo.setTipoControllo(BMA_CONTROLLO_LIBERO);
		modulo.getDati().add(campo);
		return modulo;
	}
	
}
