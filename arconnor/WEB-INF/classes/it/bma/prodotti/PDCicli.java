package it.bma.prodotti;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class PDCicli extends PDFunzioneEdit {
	public PDCicli() {
		super();
	}
	protected java.lang.String ricavaDesContesto(it.bma.comuni.BmaDataForm df) {
		return df.getValoreCampo("COD_TIPOSTATO") + "-" + df.getValoreCampo("COD_STATODA");
	}
}
