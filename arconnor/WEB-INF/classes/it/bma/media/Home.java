package it.bma.media;
import it.bma.comuni.*;
import it.bma.web.*;
public class Home extends BmaFunzioneAttiva {
	public Home() {
		super();
		jsp = new JspMd();
	}
	public boolean eseguiComando() throws BmaException {
		azioniMenu.clear();
		initContesto(sessione.getUtente());
		jsp.impostaMenuPrincipale(sessione, azioniMenu);
		return false;
	}
	public String getFunzioneEditDettaglio() {
		return "";
	}
}
