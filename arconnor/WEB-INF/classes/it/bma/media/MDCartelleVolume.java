package it.bma.media;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class MDCartelleVolume extends MDFunzioneEdit {
	public MDCartelleVolume() {
		super();
	}
	protected java.lang.String ricavaDesContesto(it.bma.comuni.BmaDataForm df) {
		return df.getValoreCampo("DES_CARTELLA");
	}
	public java.lang.String[] getFunzioniEditDettaglio() {
		return new String[] {"MD_FILETRACCE", "MD_BRANITEMP", "MD_RELOADBRANI"};
	}
}
