package it.bma.prodotti;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class PMNoteCollaudo extends PDFunzioneEdit {
	public PMNoteCollaudo() {
		super();
	}
	public java.lang.String[] getFunzioniEditDettaglio() {
		return new String[] {"PM_INTERVENTI"};
	}
	protected java.lang.String ricavaDesContesto(it.bma.comuni.BmaDataForm df) {
		return df.getValoreCampo("NUM_PROGRESSIVO");
	}
}
