package it.bma.db;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class JspDb extends BmaJsp {
	public JspDb() {
		super();
	}
	public void impostaMenuPrincipale(BmaSessione sessione, BmaVector azioniMenu) {
		BmaUtente user = sessione.getUtente();
		BmaFunzione f;
		// Menu Principale
		int liv = 1;
		f = user.getFunzione("DBEDIT"); 					// Data Base editing
		if (f!=null && f.isAzioneAmmessa(f.getAzioneDefault())) azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_MENU_SINISTRA));

		f = user.getFunzione("DBLOADORD"); 				// Ordine di Load
		if (f!=null && f.isAzioneAmmessa(f.getAzioneDefault())) azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_MENU_SINISTRA));

		f = user.getFunzione("DBREPLAY"); 				// Replica Database
		if (f!=null && f.isAzioneAmmessa(f.getAzioneDefault())) azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_MENU_SINISTRA));
    
		f = user.getFunzione("AR_APPLICAZIONI"); 	// Applicazioni
		if (f!=null && f.isAzioneAmmessa(f.getAzioneDefault())) azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_MENU_SINISTRA));

		f = user.getFunzione("AR_PROFILI");       // Profili
		if (f!=null && f.isAzioneAmmessa(f.getAzioneDefault())) azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_MENU_SINISTRA));

	}
}
