package it.bma.db;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class DBApplicazioni extends FunzioneEdit {
	public DBApplicazioni() {
		super();
	}
	public java.lang.String[] getFunzioniEditDettaglio() {
		return new String[] {"AR_FUNZIONI"};
	}
	protected java.lang.String ricavaDesContesto(it.bma.comuni.BmaDataForm df) {
		return df.getValoreCampo("DES_APPLICAZIONE");
	}
}
