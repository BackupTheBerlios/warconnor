package it.bma.comuni;

import java.util.*;
public class BmaUtente extends BmaObject {
  private String codSocieta = "";
  private String codUtente = "";
  private String desUtente = "";
  private String codProfilo = "";
  private String codUnita = "";
  private String codApplicazione = "";
  private BmaHashtable listaFunzioni = new BmaHashtable("FunzioniAbilitate");
	public BmaUtente() {
		super();
	}
	public void addFunzione(BmaFunzione funzione) {
		listaFunzioni.add(funzione);
	}
	public String getChiave() {
		return codUtente;
	}
	public String getCodSocieta() {
		return codSocieta;
	}
	public String getDesUtente() {
		return desUtente;
	}
	public String getCodProfilo() {
		return codProfilo;
	}
	public String getCodUnita() {
		return codUnita;
	}
	public String getCodApplicazione() {
		return codApplicazione;
	}
	public BmaFunzione getFunzione(String codFunzione) {
		return (BmaFunzione) listaFunzioni.getElement(codFunzione);
	}
	public BmaHashtable getListaFunzioni() {
		return listaFunzioni;
	}
	public String getCodUtente() {
		return codUtente;
	}
	public void setCodSocieta(String newCodSocieta) {
		codSocieta = newCodSocieta;
	}
	public String verificaFunzione(String funzione, String azione) {
		String azionePossibile = "";
		BmaFunzione funzioneUtente = (BmaFunzione) listaFunzioni.getElement(funzione);
		if (funzioneUtente == null) return "";
		if (funzioneUtente.isAzioneAmmessa(azione)) {
			azionePossibile = azione;
		}
		else if (funzioneUtente.isAzioneAmmessa(funzioneUtente.getAzioneDefault())) {
			azionePossibile = funzioneUtente.getAzioneDefault();
		}
		return azionePossibile;
	}

	protected String getXmlTag() {
		return "Utente";
	}
	public void setCodUtente(String newCodUtente) {
		codUtente = newCodUtente;
	}
	public void setCodProfilo(String newCodProfilo) {
		codProfilo = newCodProfilo;
	}
	public void setDesUtente(String newDesUtente) {
		desUtente = newDesUtente;
	}
	public void setCodUnita(String newCodUnita) {
		codUnita = newCodUnita;
	}
	public void setCodApplicazione(String newCodApplicazione) {
		codApplicazione = newCodApplicazione;
	}
	public void setListaFunzioni(BmaHashtable newListaFunzioni) {
		listaFunzioni = newListaFunzioni;
	}
}
