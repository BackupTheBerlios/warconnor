package it.bma.prodotti;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class PDProfiloFunzioni extends PDFunzioneEdit {
	public PDProfiloFunzioni() {
		super();
	}
	protected java.lang.String ricavaDesContesto(it.bma.comuni.BmaDataForm df) {
		return df.getValoreCampo("COD_FUNZIONE");
	}
}
