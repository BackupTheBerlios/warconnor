package it.bma.prodotti;
import it.bma.comuni.*;
import it.bma.web.*;
public class PDHOME extends BmaFunzioneAttiva {
	public PDHOME() {
		super();
		jsp = new JspProdotti();
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
