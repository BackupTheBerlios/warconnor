package it.bma.web;

import it.bma.comuni.*;
import java.io.*;
import java.net.*;
import java.util.*;
public abstract class BmaFunzioneEdit extends BmaFunzioneAttiva {
/**
 * BmaFunzioneEdit constructor comment.
 */
	public BmaFunzioneEdit() {
		super();
	}
	protected abstract String getNomeTabella(String funzione);
	public boolean eseguiComando() throws BmaException {
		String codComando = getParametri().getString(BMA_JSP_CAMPO_COMANDO);
		if (codComando.trim().length()==0) codComando = BMA_JSP_COMANDO_PREPARA;
		if (codComando.equals(BMA_JSP_COMANDO_PREPARA)) {
			impostaAzioni();
			eseguiPrepara(codComando);
			return false;
		}
		else if (codComando.equals(BMA_JSP_COMANDO_RIPRISTINA)) {
			impostaAzioni();
			eseguiPrepara(codComando);
			return false;
		}
		else if (codComando.equals(BMA_JSP_COMANDO_AGGIORNA)) {
			eseguiAggiona(codComando);
			sessione.getFunzioneAttivante().statoBeanLista = false;
			return true;
		}
		else if (codComando.equals(BMA_JSP_COMANDO_ELIMINA)) {
			eseguiAggiona(codComando);
			sessione.getFunzioneAttivante().statoBeanLista = false;
			return true;
		}
		else {
			throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Comando non previsto: " + codComando, "", this); 
		}
	}
	protected abstract void impostaAzioni();
	protected void impostaMenuAnnulla() {
		BmaUtente user = sessione.getUtente();
		BmaFunzioneAttiva fa = sessione.getFunzioneAttivante();
		if (fa==null) return;
		BmaFunzione f = user.getFunzione(fa.getCodFunzione());
		if (f==null) return;
		azioniMenu.add(new BmaMenu(fa.getLivelloMenu(), f, fa.getCodAzione(), jsp.BMA_JSP_MENU_AZIONE));
	}
	protected void impostaMenuDettaglio(String funzioneDettaglio) {
		BmaUtente user = sessione.getUtente();
		BmaFunzione f = user.getFunzione(funzioneDettaglio);
		if (f==null) return;
		// Compone il Menu con le funzioni di editing della funzione di dettaglio
		int liv = this.getLivelloMenu() + 1;
		if (f.isAzioneAmmessa(BMA_JSP_AZIONE_NUOVO)) {
			azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_AZIONE_NUOVO, jsp.BMA_JSP_MENU_AZIONE));
		}
		//  if (f.isAzioneAmmessa(BMA_JSP_AZIONE_ELIMINA)) azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_AZIONE_ELIMINA, jsp.BMA_JSP_MENU_AZIONE));
		if (f.isAzioneAmmessa(BMA_JSP_AZIONE_MODIFICA)) {
			azioniMenu.add(new BmaMenu(liv, f, jsp.BMA_JSP_AZIONE_MODIFICA, BMA_JSP_MENU_AZIONE));
		}
		else if (f.isAzioneAmmessa(BMA_JSP_AZIONE_DETTAGLIO)) {
			azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_AZIONE_DETTAGLIO, BMA_JSP_MENU_AZIONE));
		}
	}
	protected void impostaMenuEdit() {
		BmaUtente user = sessione.getUtente();
		BmaFunzione f = user.getFunzione(this.getCodFunzione());
		if (f==null) return;
		int liv = this.getLivelloMenu() + 1;
		if (f.isAzioneAmmessa(BMA_JSP_AZIONE_NUOVO)) {
			azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_AZIONE_NUOVO, BMA_JSP_MENU_AZIONE));
		}
		if (f.isAzioneAmmessa(BMA_JSP_AZIONE_MODELLO_NEW)) {
			azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_AZIONE_MODELLO_NEW, BMA_JSP_MENU_AZIONE));
		}
		if (f.isAzioneAmmessa(BMA_JSP_AZIONE_MODELLO_RES)) {
			azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_AZIONE_MODELLO_RES, BMA_JSP_MENU_AZIONE));
		}
		if (f.isAzioneAmmessa(jsp.BMA_JSP_AZIONE_ELIMINA)) {
			azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_AZIONE_ELIMINA, BMA_JSP_MENU_AZIONE));
		}
		if (f.isAzioneAmmessa(BMA_JSP_AZIONE_MODIFICA)) {
			azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_AZIONE_MODIFICA, BMA_JSP_MENU_AZIONE));
		}
		else if (f.isAzioneAmmessa(BMA_JSP_AZIONE_DETTAGLIO)) {
			azioniMenu.add(new BmaMenu(liv, f, BMA_JSP_AZIONE_DETTAGLIO, BMA_JSP_MENU_AZIONE));
		}
	}
	protected BmaDataDriverGeneric getDriver() throws BmaException {
		BmaDataDriverGeneric driver = new BmaDataDriverGeneric();
		BmaUserConfig config = sessione.getUserConfig();
		String nSource = config.getParametroApplicazione(config.BMA_CFGCOD_APP_FONTE_TARGET); 
		if (nSource==null || nSource.trim().length()==0) nSource = config.getFonteDefault(); 
		BmaJdbcSource jSource = (BmaJdbcSource)config.getFontiJdbc().getElement(nSource);
		driver.setUserConfig(config);
		driver.setJdbcSource(jSource);	
		return driver;
	}
	public final void eseguiAggiona(String codComando) throws BmaException {
		BmaDataDriverGeneric driver = getDriver();
		Hashtable valori = new Hashtable();
		String nomeTabella = getNomeTabella(this.getCodFunzione());
		if (this.getCodAzione().equals(BMA_JSP_AZIONE_NUOVO)) {
			if (codComando.equals(BMA_JSP_COMANDO_AGGIORNA)) {
				BmaDataForm df = (BmaDataForm)sessione.getBeanApplicativo(BMA_JSP_BEAN_FORM);
				if (df==null) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Modulo Assente","",this);
				verificaDatiInput(df);
				if (verificaVariazioni(df)) {	// Chiamata per valorizzare i dati del modulo
					impostaValoriAggiornamento(df, valori);
					driver.aggiorna(nomeTabella, valori, BMA_SQL_INSERT);
				}
			}
		}
		else if (this.getCodAzione().equals(BMA_JSP_AZIONE_MODIFICA)) {
			if (codComando.equals(BMA_JSP_COMANDO_AGGIORNA)) {
				BmaDataForm df = (BmaDataForm)sessione.getBeanApplicativo(BMA_JSP_BEAN_FORM);
				if (df==null) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Modulo Assente","",this);
				verificaDatiInput(df);
				if (verificaVariazioni(df)) {
					impostaValoriAggiornamento(df, valori);
					driver.aggiorna(nomeTabella, valori, BMA_SQL_UPDATE);
				}
			}
			if (codComando.equals(BMA_JSP_COMANDO_ELIMINA)) {
				BmaDataForm df = (BmaDataForm)sessione.getBeanApplicativo(BMA_JSP_BEAN_FORM);
				if (df==null) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Modulo Assente","",this);
				impostaValoriAggiornamento(df, valori);
				driver.aggiorna(nomeTabella, valori, BMA_SQL_DELETE);
			}
		}
	}
	public final void eseguiPrepara(String codComando) throws it.bma.comuni.BmaException {

		/* Controlla lo stato delle liste */
		if (!statoBeanLista) {
			if (getBean(BMA_JSP_BEAN_LISTA_DETAIL)==null) {
				removeBean(BMA_JSP_BEAN_LISTA);
			}
			else {
				removeBean(BMA_JSP_BEAN_LISTA_DETAIL);
			}
			statoBeanLista = true;
		}
		
		/* Imposta il tipo funzione, i valori del bean Lista e del contesto in relazione 
		 * alla modalità di chiamata 
		 */
		BmaDataList dl = null;
		BmaFunzioneAttiva attivante = sessione.getFunzioneAttivante();	
		if (attivante!=null) {
			aggiornaContesto(attivante.getChiaviContesto());
			if (attivante.getCodFunzione().equals(this.getCodFunzione())) {
				tipoDettaglio = attivante.tipoDettaglio;
				dl = (BmaDataList)attivante.getBean(BMA_JSP_BEAN_LISTA);
				this.setBean(BMA_JSP_BEAN_LISTA, dl);
			}
			else if (attivante.getFunzioneEditDettaglio().equals(this.getCodFunzione())) {
				tipoDettaglio = true;
				dl = (BmaDataList)attivante.getBean(BMA_JSP_BEAN_LISTA_DETAIL);
				this.setBean(BMA_JSP_BEAN_LISTA, dl);
			}
		}
		if (!tipoDettaglio) {
			initContesto(sessione.getUtente());
			this.setDesContesto("");
		}
		// Comando Prepara
		if (codComando.equals(BMA_JSP_COMANDO_PREPARA)) {

			// Azione Lista
			if (this.getCodAzione().equals(BMA_JSP_AZIONE_LISTA)) {
				sessione.getBeansApplicativi().clear();
				String nomeTabella = getNomeTabella(this.getCodFunzione());
				dl = (BmaDataList)this.getBean(BMA_JSP_BEAN_LISTA);
				if (dl==null) {
					BmaDataDriverGeneric driver = getDriver();
					Hashtable condizioni = new Hashtable();
					Enumeration e = getChiaviContesto().keys();
					while (e.hasMoreElements()) {
						String k = (String)e.nextElement();
						condizioni.put(k, getChiaveContesto(k));
					}
					dl = driver.getDataList(nomeTabella, condizioni);
					jsp.applicaStandard(dl.getTabella().getColonne());
					this.setBean(BMA_JSP_BEAN_LISTA, dl);
				}	
				impostaContestoElenco(dl);
				sessione.setBeanApplicativo(BMA_JSP_BEAN_LISTA, dl);
			}
			// Azione Nuovo
			else if (this.getCodAzione().equals(BMA_JSP_AZIONE_NUOVO)) {
				sessione.getBeansApplicativi().clear();
				if (dl==null) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Lista Assente","",this);
				BmaDataTable tb = dl.getTabella();
				tb.setValori(new Hashtable());
				BmaDataForm df = new BmaDataForm(dl.getTabella());
				impostaContestoForm(df);
				jsp.applicaStandard(df.getDati().getVector());
				sessione.setBeanApplicativo(BMA_JSP_BEAN_FORM, df);
			}
			// Azione Nuovo da Modello
			else if (this.getCodAzione().equals(BMA_JSP_AZIONE_MODELLO_NEW)) {
				sessione.getBeansApplicativi().clear();
				if (dl==null) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Lista Assente","",this);
				BmaDataTable tb = dl.getTabella();
				tb.setValori(new Hashtable());
				BmaDataForm df = new BmaDataForm(dl.getTabella());
				impostaContestoForm(df);
				jsp.applicaStandard(df.getDati().getVector());
				sessione.setBeanApplicativo(BMA_JSP_BEAN_FORM, df);
			}
			// Azione Modifica
			else if (this.getCodAzione().equals(BMA_JSP_AZIONE_MODIFICA) ||
								this.getCodAzione().equals(BMA_JSP_AZIONE_DETTAGLIO)) {
				sessione.getBeansApplicativi().clear();
				if (dl==null) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Lista Assente","",this);
				BmaDataForm df = new BmaDataForm(dl.getTabella());
				String kSel = this.getParametri().getString(BMA_JSP_CAMPO_SELEZIONE);
				if (kSel==null) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Campo Selezione Assente","",this);
				aggiornaContesto(dl.getChiaviContesto(kSel));
				impostaContestoForm(df);
				jsp.applicaStandard(df.getDati().getVector());
				df.setValori(dl.getValori(kSel));
				this.setDesContesto(ricavaDesContesto(df));
				sessione.setBeanApplicativo(BMA_JSP_BEAN_FORM, df);
				setDetailList(kSel);
			}
			else {
				throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Comando non previsto: " + codComando, "", this); 
			}
		}
	}
	protected abstract String ricavaDesContesto(BmaDataForm df);
	private void setDetailList(String kSel) throws it.bma.comuni.BmaException {
		String fDettaglio = getFunzioneEditDettaglio();
		if (fDettaglio==null || fDettaglio.trim().length()==0) return;
		BmaDataList dlDetail = (BmaDataList)this.getBean(BMA_JSP_BEAN_LISTA_DETAIL);
		if (dlDetail==null) {
			String nomeTabella = getNomeTabella(fDettaglio);
			dlDetail = new BmaDataList(nomeTabella);
			Hashtable condizioni = new Hashtable();
			Enumeration e = getChiaviContesto().keys();
			while (e.hasMoreElements()) {
				String k = (String)e.nextElement();
				condizioni.put(k, getChiaveContesto(k));
			}
			BmaDataDriverGeneric driver = getDriver();
			dlDetail = driver.getDataList(nomeTabella, condizioni);
			jsp.applicaStandard(dlDetail.getTabella().getColonne());
			this.setBean(BMA_JSP_BEAN_LISTA_DETAIL, dlDetail);
		}	
		sessione.setBeanApplicativo(BMA_JSP_BEAN_LISTA_DETAIL, dlDetail);
	}
}
