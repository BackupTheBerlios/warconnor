package it.bma.comuni;

import java.util.*;

public class BmaFunzione extends BmaObject {
  private String codFunzione = "";
  private String desFunzione = "";
  private String codClasse = "";
  private String rifLayout = "";
  private String azioneDefault = "";
  private BmaHashtable azioni = new BmaHashtable("Azioni");
	public BmaFunzione() {
		super();
	}
	public String getRifLayout() {
		return rifLayout;
	}
	public String getChiave() {
		return codFunzione;
	}
	public java.lang.String getCodClasse() {
		return codClasse;
	}
	public java.lang.String getCodFunzione() {
		return codFunzione;
	}
	public java.lang.String getAzioneDefault() {
		return azioneDefault;
	}
	public java.lang.String getDesFunzione() {
		return desFunzione;
	}
	public boolean isAzioneAmmessa(String azione) {
		BmaAzione a = (BmaAzione)azioni.getElement(azione);
		if (a==null) return false;
		return a.isVisibile();
	}
	public void setRifLayout(String newRifLayout) {
		rifLayout = newRifLayout;
	}
	public void setCodClasse(java.lang.String valore) {
		codClasse = valore;
	}
	public void setCodFunzione(String newCodFunzione) {
		codFunzione = newCodFunzione;
	}
	public void setDesFunzione(String newDesFunzione) {
		desFunzione = newDesFunzione;
	}
	public void setAzioneDefault(String newAzione) {
		azioneDefault = newAzione;
	}
	public void addAzione(BmaAzione azione) {
		azioni.add(azione);
	}
	public BmaHashtable getAzioni() {
		return azioni;
	}
	protected java.lang.String getXmlTag() {
		return "Funzione";
	}
	public boolean hasAzione(String codAzione) {
		BmaAzione azione = (BmaAzione)azioni.getElement(codAzione);
		return (azione!=null);
	}
	public java.lang.String getDesAzione(String codAzione) {
		BmaAzione azione = (BmaAzione)azioni.getElement(codAzione);
		if (azione==null) return "";
		else return azione.getDesAzione();
	}
}