package it.bma.db;

import it.bma.comuni.*;
import it.bma.web.*;
public abstract class BmaServizioDb extends BmaFunzioneServizi {
	
	public BmaServizioDb() {
		super();
		jsp = new JspDb();
	}
	public String getNomeServizio(String funzione) {
		if (funzione.equals("DBEDIT")) return "BmaDataModel";
		if (funzione.equals("DBLOADORD")) return "BmaDataModel";
		else if (funzione.equals("DBREPLAY")) return "BmaDataReplay";
		else return "";
	}
	public void impostaAzioni() {
		azioniMenu.clear();
		jsp.impostaMenuPrincipale(sessione, azioniMenu);
		
		// Menu Azioni
		BmaFunzione f = sessione.getUtente().getFunzione(this.getCodFunzione());
		int liv = this.getLivelloMenu() + 1;
		if (f.isAzioneAmmessa(BMA_JSP_AZIONE_DETTAGLIO)) {
			azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_AZIONE_DETTAGLIO, BMA_JSP_MENU_AZIONE));
		}
	}
}
