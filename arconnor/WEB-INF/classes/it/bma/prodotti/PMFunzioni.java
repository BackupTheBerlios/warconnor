package it.bma.prodotti;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class PMFunzioni extends PDFunzioneEdit {
	public PMFunzioni() {
		super();
	}
	public java.lang.String[] getFunzioniEditDettaglio() {
		return new String[] {"PM_NOTECOLLAUDO"};
	}
	protected java.lang.String ricavaDesContesto(it.bma.comuni.BmaDataForm df) {
		return df.getValoreCampo("DES_FUNZIONE");
	}
}
