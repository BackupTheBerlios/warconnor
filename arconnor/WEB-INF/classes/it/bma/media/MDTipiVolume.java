package it.bma.media;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class MDTipiVolume extends MDFunzioneEdit {
	public MDTipiVolume() {
		super();
	}
	protected java.lang.String ricavaDesContesto(it.bma.comuni.BmaDataForm df) {
		return df.getValoreCampo("DES_TIPOVOLUME");
	}
}
