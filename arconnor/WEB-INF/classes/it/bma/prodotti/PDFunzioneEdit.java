package it.bma.prodotti;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public abstract class PDFunzioneEdit extends it.bma.web.BmaFunzioneEdit {
	public PDFunzioneEdit() {
		super();
		jsp = new JspProdotti();
	}
	protected String getNomeTabella(String funzione) {
		if (funzione.equals("PDPROFILI")) return "PD_PROFILIUTENTE";
		else if (funzione.equals("PDPROFUTENTE")) return "PD_PROFILOFUNZIONI";
		else if (funzione.equals("PDFUNZIONI")) return "PD_FUNZIONI";
		else if (funzione.equals("PDCICLI")) return "PD_CICLOSTATI";
		else if (funzione.equals("PM_BLOCCHI")) return "PM_BLOCCHI";
		else if (funzione.equals("PM_FUNZIONI")) return "PM_FUNZIONI";
		else if (funzione.equals("PM_NOTECOLLAUDO")) return "PM_NOTECOLLAUDO";
		else if (funzione.equals("PM_INTERVENTI")) return "PM_INTERVENTI";
		else return "";
	}
}
