package it.bma.servizi;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.util.*;
import it.bma.comuni.*;
import it.bma.prodotti.*;
public class BmaSchedaPdfHelper extends com.lowagie.text.pdf.PdfPageEventHelper {
	private BmaSchedaPdf scheda;
public BmaSchedaPdfHelper(BmaSchedaPdf schedaCorrente) {
	super();
	this.scheda = schedaCorrente;
}
public void onEndPage(PdfWriter writer, Document document) {
}
public void onStartPage(PdfWriter writer, Document document) {
	scheda.addHeader(writer, document);
	scheda.addFooter(writer, document);
}
}
