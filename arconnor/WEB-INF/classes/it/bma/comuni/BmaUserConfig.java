package it.bma.comuni;

/**
 * Configurazione Utente.
 * Creation date: (30/11/2003 09.19.59)
 * Contiene le proprietà di configurazione per l'utente corrente
 */
public class BmaUserConfig extends BmaConfigurazione {
	private BmaHashtable profiliFunzioni = new BmaHashtable("ProfiliFunzioni");
	public BmaUserConfig() {
		super();
	}
	public BmaHashtable getProfiliFunzioni() {
		return profiliFunzioni;
	}
	public void setProfiliFunzioni(BmaHashtable newProfiliFunzioni) {
		profiliFunzioni = newProfiliFunzioni;
	}
}
