package it.bma.db;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public abstract class FunzioneEdit extends it.bma.web.BmaFunzioneEdit {
	public FunzioneEdit() {
		super();
		jsp = new JspDb();
	}
	protected String getNomeTabella(String funzione) {
		if (funzione.equals("AR_APPLICAZIONI")) return "AR_APPLICAZIONI";
		else if (funzione.equals("AR_FUNZIONI")) return "AR_FUNZIONI";
		else if (funzione.equals("AR_PROFILI")) return "AR_PROFILI";
		else if (funzione.equals("AR_UTENTI")) return "AR_UTENTI";
		else if (funzione.equals("AR_PROFILIFUNZIONI")) return "AR_PROFILIFUNZIONI";
		else return "";
	}
}
