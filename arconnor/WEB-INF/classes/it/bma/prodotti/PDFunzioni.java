package it.bma.prodotti;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class PDFunzioni extends PDFunzioneEdit {
	public PDFunzioni() {
		super();
	}
	public java.lang.String getFunzioneEditDettaglio() {
		return "";
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
		return df.getValoreCampo("LABEL");
	}
}
