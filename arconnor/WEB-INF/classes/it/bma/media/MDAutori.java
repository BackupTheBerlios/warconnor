package it.bma.media;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class MDAutori extends MDFunzioneEdit {
	public MDAutori() {
		super();
	}
	protected java.lang.String ricavaDesContesto(it.bma.comuni.BmaDataForm df) {
		return df.getValoreCampo("DES_AUTORE");
	}
	public java.lang.String[] getFunzioniEditDettaglio() {
		return new String[] {"MD_TRACCEAUDIO"};
	}
}
