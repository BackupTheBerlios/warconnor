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
		addMenuSinistra(user, "MD_TIPIVOLUME", 1, azioniMenu);
		addMenuSinistra(user, "MD_TIPICARTELLE", 1, azioniMenu);
		addMenuSinistra(user, "MD_VOLUMI", 1, azioniMenu);
		addMenuSinistra(user, "MD_CARTELLE", 1, azioniMenu);
		addMenuSinistra(user, "MD_ALBUMAUDIO", 1, azioniMenu);
		addMenuSinistra(user, "MD_AUTORI", 1, azioniMenu);
		addMenuSinistra(user, "MD_BRANI", 1, azioniMenu);
	}
}
