package it.bma.db;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class DBProfili extends FunzioneEdit {
	public DBProfili() {
		super();
	}
	public java.lang.String[] getFunzioniEditDettaglio() {
		return new String[] {"AR_UTENTI", "AR_PROFILIFUNZIONI"};
	}
	protected java.lang.String ricavaDesContesto(it.bma.comuni.BmaDataForm df) {
		return df.getValoreCampo("DES_PROFILO");
	}
}
