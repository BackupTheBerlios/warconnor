package it.bma.comuni;

import java.util.*;
public class BmaAzione extends BmaObject {
	private String codAzione = "";
	private String desAzione = "";
	private String progressivo = "";
	private boolean visibile = false;
	public BmaAzione() {
		super();
	}
	public String getChiave() {
		return codAzione;
	}
	public java.lang.String getCodAzione() {
		return codAzione;
	}
	public java.lang.String getDesAzione() {
		return desAzione;
	}
	public java.lang.String getProgressivo() {
		return progressivo;
	}
	public boolean isVisibile() {
		return visibile;
	}
	public void setCodAzione(String newCodAzione) {
		codAzione = newCodAzione;
	}
	public void setDesAzione(String newDesAzione) {
		desAzione = newDesAzione;
	}
	public void setProgressivo(java.lang.String valore) {
		progressivo = valore;
	}
	public void setVisibile(boolean flag) {
		visibile = flag;
	}

	protected java.lang.String getXmlTag() {
		return "Azione";
	}
}
