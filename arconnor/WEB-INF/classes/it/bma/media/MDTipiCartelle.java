package it.bma.media;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class MDTipiCartelle extends MDFunzioneEdit {
	public MDTipiCartelle() {
		super();
	}
	protected java.lang.String ricavaDesContesto(it.bma.comuni.BmaDataForm df) {
		return df.getValoreCampo("DES_TIPOCARTELLA");
	}
}
