package it.bma.media;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class MDTracceAudio extends MDFunzioneEdit {
	public MDTracceAudio() {
		super();
	}
	public java.lang.String[] getFunzioniEditDettaglio() {
		return new String[] {"MD_FILETRACCE"};
	}	
	protected java.lang.String ricavaDesContesto(it.bma.comuni.BmaDataForm df) {
		return df.getValoreCampo("DES_BRANO");
	}
}
