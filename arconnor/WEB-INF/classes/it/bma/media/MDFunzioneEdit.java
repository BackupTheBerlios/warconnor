package it.bma.media;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public abstract class MDFunzioneEdit extends it.bma.web.BmaFunzioneEdit {
	
	public MDFunzioneEdit() {
		super();
		jsp = new JspMd();
		driver = new MDDriver();
	}
	protected String getNomeTabella(String funzione) {
		if (funzione.equals("MD_TIPIVOLUME")) return "MD_TIPIVOLUME";
		else if (funzione.equals("MD_VOLUMI")) return "MD_VOLUMI";
		else if (funzione.equals("MD_CARTELLEVOLUME")) return "MD_CARTELLEVOLUME";
		else return "";
	}
}
