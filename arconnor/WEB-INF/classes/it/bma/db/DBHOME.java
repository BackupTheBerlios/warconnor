package it.bma.db;
import it.bma.comuni.*;
import it.bma.web.*;
public class DBHOME extends BmaFunzioneAttiva {
	public DBHOME() {
		super();
		jsp = new JspDb();
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
