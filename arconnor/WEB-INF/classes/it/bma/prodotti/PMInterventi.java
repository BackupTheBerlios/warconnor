package it.bma.prodotti;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class PMInterventi extends PDFunzioneEdit {
	public PMInterventi() {
		super();
	}
	public java.lang.String[] getFunzioniEditDettaglio() {
		return new String[] {""};
	}
	protected java.lang.String ricavaDesContesto(it.bma.comuni.BmaDataForm df) {
		return df.getValoreCampo("");
	}
}
