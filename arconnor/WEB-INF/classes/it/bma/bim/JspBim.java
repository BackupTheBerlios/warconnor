package it.bma.bim;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class JspBim extends BmaJsp {
	public JspBim() {
		super();
	}
	public void impostaMenuPrincipale(BmaSessione sessione, BmaVector azioniMenu) {
		BmaUtente user = sessione.getUtente();
		BmaFunzione f;
		// Menu Principale
		int liv = 1;
		f = user.getFunzione("BM_LISTAOPE");
		if (f!=null && f.isAzioneAmmessa(f.getAzioneDefault())) azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_MENU_SINISTRA));

	}
}
