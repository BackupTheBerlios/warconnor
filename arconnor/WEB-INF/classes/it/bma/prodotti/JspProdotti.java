package it.bma.prodotti;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class JspProdotti extends BmaJsp {
	public JspProdotti() {
		super();
	}
	public String getWebPath() {
		return config.getWebPath() + "bma/";
	}
	public void impostaMenuPrincipale(BmaSessione sessione, BmaVector azioniMenu) {
		BmaUtente user = sessione.getUtente();
		BmaFunzione f;
		int liv = 1;
		f = user.getFunzione("PDFUNZIONI"); 			// Funzioni
		if (f!=null && f.isAzioneAmmessa(f.getAzioneDefault())) azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_MENU_SINISTRA));
		f = user.getFunzione("PDPROFILI"); 				// Profili
		if (f!=null && f.isAzioneAmmessa(f.getAzioneDefault())) azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_MENU_SINISTRA));
		f = user.getFunzione("PDCICLI");					// Cicli
		if (f!=null && f.isAzioneAmmessa(f.getAzioneDefault())) azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_MENU_SINISTRA));
		f = user.getFunzione("PDREMOVE"); 				// Remove Prodotto
		if (f!=null && f.isAzioneAmmessa(f.getAzioneDefault())) azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_MENU_SINISTRA));
		f = user.getFunzione("PM_BLOCCHI"); 				// Blocchi
		if (f!=null && f.isAzioneAmmessa(f.getAzioneDefault())) azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_MENU_SINISTRA));
		f = user.getFunzione("PM_FUNZIONI"); 				// PM_FUNZIONI
		if (f!=null && f.isAzioneAmmessa(f.getAzioneDefault())) azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_MENU_SINISTRA));
	}
}
