package it.bma.web;

import it.bma.comuni.*;
import java.util.*;
import javax.servlet.http.*;

public abstract class BmaFunzioneAttiva extends BmaObject implements BmaJspLiterals {
	private int livelloMenu = 0;
	private String codFunzione = "";
	private String codAzione = "";
	private String desFunzione = "";
	private String desAzione = "";
	private String desContesto = "";
	private BmaHashtable parametri = new BmaHashtable("Parametri");
	private BmaHashtable paramMulti = new BmaHashtable("ParametriMultipli");
	private BmaHashtable datiInput = new BmaHashtable("DatiInput");
	private BmaHashtable contesto = new BmaHashtable("Contesto");
	private BmaHashtable beans = new BmaHashtable("Beans");
	protected BmaVector azioniMenu = new BmaVector("Azioni");
	protected BmaSessione sessione;
	protected BmaJsp jsp = new BmaJsp();
	public boolean statoBeanLista = true;
	public BmaFunzioneAttiva() {
		super();
	}
	public BmaFunzioneAttiva(int livello, String codFunzione, String codAzione) {
		super();
		this.livelloMenu = livello;
		this.codFunzione = codFunzione;
		this.codAzione = codAzione;
	}
	public BmaJsp getJsp() { return jsp; }
	protected void aggiornaContesto(Hashtable valori) {
		Enumeration e = valori.keys();
		while (e.hasMoreElements()) {
			String k = (String)e.nextElement();
			String v = (String)valori.get(k);
			contesto.setString(k, v);
		}
	}
	public void aggiornaDatiInput(HttpServletRequest request) {
		datiInput.clear();
		Enumeration e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String n = (String)e.nextElement();
			datiInput.setString(n, request.getParameter(n));
		}
	}
	public void aggiornaParametri(HttpServletRequest request) {
		parametri.clear();
		paramMulti.clear();
		Enumeration e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String n = (String)e.nextElement();
			int p = n.indexOf(BMA_JSP_PREFISSO_MULTI);
			if (p>=0) {
				String[] multi = request.getParameterValues(n);
				BmaValuesList list = new BmaValuesList(n);
				list.setValues(multi);
				paramMulti.add(list);
			}
			else {
				parametri.setString(n, request.getParameter(n));
			}
		}
	}
	public void clearBeans() {
		beans.clear();
	}
	public boolean eseguiComando() throws BmaException {
		boolean exitFunction = false;
		return exitFunction;
	}
	public BmaMenu getAzione(String azione) {
		String k = codFunzione + BMA_KEY_SEPARATOR + azione;
		BmaMenu menu = (BmaMenu)azioniMenu.getElement(k);
		return menu;
	}
	public BmaMenu getAzione(String funzione, String azione) {
		String k = funzione + BMA_KEY_SEPARATOR + azione;
		BmaMenu menu = (BmaMenu)azioniMenu.getElement(k);
		return menu;
	}
	public it.bma.comuni.BmaVector getAzioniMenu() {
		return azioniMenu;
	}
	public BmaObject getBean(String nome) {
		return beans.getElement(nome);
	}
	public String getChiave() {
		return codFunzione + BMA_KEY_SEPARATOR + codAzione;
	}
	public String getChiaveContesto(String nome) {
		String v = contesto.getString(nome);
		if (v==null) v = "";
		return v;
	}
	public Hashtable getChiaviContesto() {
		return contesto.getStringTable();
	}
	public java.lang.String getCodAzione() {
		return codAzione;
	}
	public java.lang.String getCodFunzione() {
		return codFunzione;
	}
	public BmaHashtable getDatiIput() {
		return datiInput;
	}
	public java.lang.String getDesAzione() {
		return desAzione;
	}
	public java.lang.String getDesContesto() {
		return desContesto;
	}
	/**
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDesFunzione() {
		return desFunzione;
	}
	public int getLivelloMenu() {
		return livelloMenu;
	}
	public BmaHashtable getParametri() {
		return parametri;
	}
	public BmaHashtable getParamMulti() {
		return paramMulti;
	}
	protected String getXmlTag() {
		return "StatoFunzione";
	}
	protected void impostaContestoElenco(BmaDataList elenco) {
		Vector dati = elenco.getTabella().getColonne();
		for (int i = 0; i < dati.size(); i++){
			BmaDataColumn f = (BmaDataColumn)dati.elementAt(i);
			String v = getChiaveContesto(f.getNome());
			if (v!=null && v.trim().length()>0) {
				f.setValore(v);
				f.setTipoControllo(BMA_CONTROLLO_HIDDEN);
			}
		}
	}
	protected void impostaContestoForm(BmaDataForm modulo) {
		BmaVector dati = modulo.getDati();
		for (int i = 0; i < dati.getSize(); i++){
			BmaDataField f = (BmaDataField)dati.getElement(i);
			String v = getChiaveContesto(f.getNome());
			if (v!=null && v.trim().length()>0) {
				f.setValore(v);
				f.setTipoControllo(BMA_CONTROLLO_HIDDEN);
			}
		}
	}
	protected void mergeValoriModuloContesto(BmaDataForm modulo, Hashtable valori) {
		BmaVector dati = modulo.getDati();
		for (int i = 0; i < dati.getSize(); i++){
			BmaDataField f = (BmaDataField)dati.getElement(i);
			valori.put(f.getNome(), f.getValore());
		}
		Enumeration e = contesto.getHashtable().keys();
		while (e.hasMoreElements()) {
			String k = (String)e.nextElement();
			String v = contesto.getString(k);
			valori.put(k, v);
		}
	}
	public void initContesto(BmaUtente utente) {
		contesto.clear();
		contesto.setString("COD_SOCIETA", utente.getCodSocieta());
	}
	public boolean isAmmessa(String azione) {
		String k = codFunzione + BMA_KEY_SEPARATOR + azione;
		BmaMenu menu = (BmaMenu)azioniMenu.getElement(k);
		return (menu!=null);
	}
	public void removeBean(String nome) {
		beans.remove(nome);
	}
	public void removeChiaveContesto(String nome) {
		contesto.remove(nome);
	}
	public void setAzioniMenu(it.bma.comuni.BmaVector newAzioniMenu) {
		azioniMenu = newAzioniMenu;
	}
	public void setBean(String nome, BmaObject oggetto) {
		beans.setElement(nome, oggetto);
	}
	public void setChiaveContesto(String nome, String valore) {
		contesto.setString(nome, valore);
	}
	/**
	 * 
	 * @param newCodAzione java.lang.String
	 */
	public void setCodAzione(java.lang.String newCodAzione) {
		codAzione = newCodAzione;
	}
	public void setCodFunzione(java.lang.String newCodFunzione) {
		codFunzione = newCodFunzione;
	}
	public void setDesAzione(java.lang.String newDesAzione) {
		desAzione = newDesAzione;
	}
	public void setDesContesto(java.lang.String newDesContesto) {
		desContesto = newDesContesto;
	}
	public void setDesFunzione(java.lang.String newDesFunzione) {
		desFunzione = newDesFunzione;
	}
	public void setLivelloMenu(int newLivelloMenu) {
		livelloMenu = newLivelloMenu;
	}
	protected void verificaDatiInput(BmaDataForm modulo) throws BmaException {
		/* Controlla e formatta i dati di input alla funzione */
		Hashtable tmpContesto = contesto.getHashtable();
		Hashtable tmpDati = datiInput.getHashtable();
		for (int i = 0; i < modulo.getDati().getSize(); i++) {
			BmaDataField f = (BmaDataField)modulo.getDati().getElement(i);
			String valoreContesto = (String)tmpContesto.get(f.getNome());
			if (valoreContesto==null) {
				String valore = (String)tmpDati.get(f.getNome());
				if (valore==null) {
					if (f.getTipoControllo().equals(BMA_CONTROLLO_BOOLEAN)) datiInput.setString(f.getNome(), BMA_FALSE);
				}
				else {
					if (f.getTipo().equals(BMA_SQL_TYS_DAT)) datiInput.setString(f.getNome(), jsp.getDataInterna(valore));
					if (f.getTipo().equals(BMA_SQL_TYS_NUM)) datiInput.setString(f.getNome(), jsp.getNumeroInterno(valore));
					if (f.getTipoControllo().equals(BMA_CONTROLLO_BOOLEAN)) datiInput.setString(f.getNome(), BMA_TRUE);
					if (f.getTipoControllo().equals(BMA_CONTROLLO_LINK)) datiInput.setString(f.getNome(), jsp.getLinkInterno(valore));
				}
			}
			else {
				datiInput.setString(f.getNome(), valoreContesto);
			}
		}
	}
	protected boolean verificaVariazioni(BmaDataForm modulo) throws BmaException {
		/* Verifica se ci sono differenze e aggiorna il modulo */
		boolean differenze = false;
		for (int i = 0; i < modulo.getDati().getSize(); i++) {
			BmaDataField f = (BmaDataField)modulo.getDati().getElement(i);
			String nuovoValore = datiInput.getString(f.getNome());
			if (nuovoValore==null) differenze = true;
			else if (!nuovoValore.equals(f.getValore())) {
				differenze = true;
				f.setValore(nuovoValore);
			}
		}	
		return differenze;
	}
	protected void aggiornaModulo(BmaDataForm modulo) throws BmaException {
		/* Aggiorna il modulo con i valori di input della funzione */
		if (datiInput.size()==0) datiInput = parametri;
		for (int i = 0; i < modulo.getDati().getSize(); i++) {
			BmaDataField f = (BmaDataField)modulo.getDati().getElement(i);
			String nuovoValore = (String)parametri.getHashtable().get(f.getNome());
			if (nuovoValore==null) {
				if (f.getTipoControllo().equals(BMA_CONTROLLO_BOOLEAN)) f.setValore(BMA_FALSE);
			}
			else {
				if (f.getTipo().equals(BMA_SQL_TYS_DAT)) f.setValore(jsp.getDataInterna(nuovoValore));
				else if (f.getTipo().equals(BMA_SQL_TYS_NUM)) f.setValore(jsp.getNumeroInterno(nuovoValore));
				else if (f.getTipoControllo().equals(BMA_CONTROLLO_BOOLEAN)) {
					if (nuovoValore.equals(BMA_TRUE)) f.setValore(BMA_TRUE);
					else f.setValore(BMA_FALSE);
				}
				else if (f.getTipoControllo().equals(BMA_CONTROLLO_LINK)) f.setValore(jsp.getLinkInterno(nuovoValore));
				else f.setValore(nuovoValore);
			}
		}
	}
}
