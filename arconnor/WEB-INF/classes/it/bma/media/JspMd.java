package it.bma.media;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class JspMd extends BmaJsp {
	public JspMd() {
		super();
	}
	public String getWebPath() {
		return config.getWebPath() + "media/";
	}
	public void impostaMenuPrincipale(BmaSessione sessione, BmaVector azioniMenu) {
		BmaUtente user = sessione.getUtente();
		BmaFunzione f;
		// Menu Principale
		int liv = 1;
		
		f = user.getFunzione("MD_TIPIVOLUME"); 			// Tipologie Volumi
		if (f!=null && f.isAzioneAmmessa(f.getAzioneDefault())) azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_MENU_SINISTRA));
		
		f = user.getFunzione("MD_VOLUMI"); 					// Catalogo Volumi
		if (f!=null && f.isAzioneAmmessa(f.getAzioneDefault())) azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_MENU_SINISTRA));
	}
}
