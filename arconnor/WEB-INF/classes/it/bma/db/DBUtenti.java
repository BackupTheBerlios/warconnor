package it.bma.db;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class DBUtenti extends FunzioneEdit {
	public DBUtenti() {
		super();
	}
	protected java.lang.String ricavaDesContesto(it.bma.comuni.BmaDataForm df) {
		return df.getValoreCampo("DES_UTENTE");
	}
}
