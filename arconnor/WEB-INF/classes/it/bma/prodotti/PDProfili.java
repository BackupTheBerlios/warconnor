package it.bma.prodotti;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class PDProfili extends PDFunzioneEdit {
	public PDProfili() {
		super();
	}
	public java.lang.String[] getFunzioniEditDettaglio() {
		return new String[] {"PDPROFUTENTE"};
	}
	protected java.lang.String ricavaDesContesto(it.bma.comuni.BmaDataForm df) {
		return df.getValoreCampo("DES_PROFILO");
	}
}
