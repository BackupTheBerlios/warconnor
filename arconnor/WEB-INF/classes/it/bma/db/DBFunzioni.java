package it.bma.db;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class DBFunzioni extends FunzioneEdit {
	public DBFunzioni() {
		super();
	}
	public java.lang.String getFunzioneEditDettaglio() {
		return "AR_FUNZIONI";
	}
	protected void impostaAzioni() {
		azioniMenu.clear();
		jsp.impostaMenuPrincipale(sessione, azioniMenu);
		impostaMenuEdit();
		BmaFunzione f = sessione.getUtente().getFunzione(getFunzioneEditDettaglio());
		if (f==null) return;
		
		int liv = this.getLivelloMenu() + 1;

		if (f.isAzioneAmmessa(jsp.BMA_JSP_AZIONE_NUOVO)) azioniMenu.add(new BmaMenu(liv, f, jsp.BMA_JSP_AZIONE_NUOVO, jsp.BMA_JSP_MENU_AZIONE));
		if (f.isAzioneAmmessa(jsp.BMA_JSP_AZIONE_MODIFICA)) {
			azioniMenu.add(new BmaMenu(liv, f, jsp.BMA_JSP_AZIONE_MODIFICA, jsp.BMA_JSP_MENU_AZIONE));
		}
		else if (f.isAzioneAmmessa(jsp.BMA_JSP_AZIONE_DETTAGLIO)) {
			azioniMenu.add(new BmaMenu(liv, f, jsp.BMA_JSP_AZIONE_DETTAGLIO, jsp.BMA_JSP_MENU_AZIONE));
		}
	}
	protected java.lang.String ricavaDesContesto(it.bma.comuni.BmaDataForm df) {
		return df.getValoreCampo("DES_APPLICAZIONE");
	}
}
