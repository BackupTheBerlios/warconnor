package it.bma.prodotti;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public abstract class PDFunzioneEdit extends it.bma.web.BmaFunzioneEdit {
	public PDFunzioneEdit() {
		super();
		jsp = new JspProdotti();
	}
}
