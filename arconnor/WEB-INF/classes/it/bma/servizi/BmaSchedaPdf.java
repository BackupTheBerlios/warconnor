package it.bma.servizi;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.awt.Color;
import it.bma.comuni.*;
import it.bma.web.*;
import it.bma.prodotti.*;
import java.util.*;
import java.text.*;
import java.net.*;

public class BmaSchedaPdf extends BmaServizio {
	protected Prodotto pd = new Prodotto();
	private Document docPdf = new Document(PageSize.A4, 60, 35, 140, 82);
	protected Hashtable fontSet = new Hashtable();
	protected Hashtable datiSommario = new Hashtable();
	protected BmaHashtable testi = new BmaHashtable("testischeda");
	private String[] keyContesto = new String[] {"","","","",""};
	private String[] valContesto = new String[] {"","","","",""};
	private BmaVector listaCompletaGaranzie = new BmaVector("ListaCompletaGaranzie");
	private BmaVector opzPdSost = new BmaVector("ProdottiSostituibili");
	private BmaVector opzPdElim = new BmaVector("ProdottiEliminati");
	private BmaVector opzAltre = new BmaVector("AltreOpzioni");
	private final String OPZPD_SOSTITUIBILI = "SOSTITUIBILI";
	private final String OPZPD_ELIMINATI = "ELIMINATI";
	private final String ARGOMENTO_PD_ELIMINATI = "Prodotti_Eliminati";
	private final String ARGOMENTO_PD_SOSTITUIBILI = "Prodotti_Sostituibili";
public BmaSchedaPdf() {
	super();
	// Inizializza il fontSet
	
	// Normal 8 Black
	Font f = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, Color.black);
	fontSet.put("Normal08", f);
	// Italic 8 Black
	f = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.ITALIC, Color.black);
	fontSet.put("Italic08", f);
	// ItalicBold 8 Black
	f = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC, Color.black);
	fontSet.put("ItalicBold08", f);
	// Bold 8 Black
	f = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.black);
	fontSet.put("Bold08", f);
	
	// Normal 10 Black
	f = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.black);
	fontSet.put("Normal10", f);
	// Italic 10 Black
	f = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.ITALIC, Color.black);
	fontSet.put("Italic10", f);
	// ItalicBold 10 Black
	f = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLDITALIC, Color.black);
	fontSet.put("ItalicBold10", f);
	// Bold 10 Black
	f = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, Color.black);
	fontSet.put("Bold10", f);
	
	// Bold 12 Black
	f = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, Color.black);
	fontSet.put("Bold12", f);
	
	// Bold 14 Black
	f = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, Color.black);
	fontSet.put("Bold14", f);
	
	// Normal 8 Blue
	f = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, Color.blue);
	fontSet.put("NormalBlue08", f);
}
private void addAllegati(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getAllegati().getSize()==0) {
		addTitolo("Non sono previsti allegati", "Italic10", 0);
		return;
	}
	// Raggruppa gli allegati per tipologia
	Hashtable tipiAllegato = new Hashtable();
	for (int i = 0; i < pd.getAllegati().getSize(); i++){
		Allegato allegato = (Allegato)pd.getAllegati().getElement(i);
		Vector v = (Vector)tipiAllegato.get(allegato.getCodTipoAllegato());
		if (v==null) {
			v = new Vector();
			tipiAllegato.put(allegato.getCodTipoAllegato(), v);
		}
		v.add(allegato);
	}
	Enumeration e = tipiAllegato.elements();
	while (e.hasMoreElements()) {
		Vector lista = (Vector)e.nextElement();
		Allegato allegato = (Allegato)lista.elementAt(0);
		Table tab = new Table(4);
		tab.setWidth(100);
		tab.setWidths(new int[] {20, 10, 65, 5});
		tab.setTableFitsPage(true);
		tab.setPadding(2);
		tab.setSpacing(0);
		tab.setOffset(0);
		Cell cell;
		int leading = 8;
		
		cell = new Cell(new Phrase(allegato.getDesTipoAllegato(), font("Bold10")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setColspan(4);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Descrizione", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Data", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setHorizontalAlignment(cell.ALIGN_CENTER);
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Note", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase("N°", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setHorizontalAlignment(cell.ALIGN_CENTER);
		cell.setLeading(leading);
		tab.addCell(cell);

		for (int i = 0; i < lista.size(); i++){
			allegato = (Allegato)lista.elementAt(i);
			
			cell = new Cell(new Phrase(allegato.getDesAllegato(), font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
			
			cell = new Cell(new Phrase(jsp.getDataEsterna(allegato.getDatAllegato()), font("Normal08")));
			cell.setHorizontalAlignment(cell.ALIGN_CENTER);
			cell.setLeading(leading);
			tab.addCell(cell);
			
			Anchor link = new Anchor(allegato.getObjAllegato(), font("NormalBlue08"));
			link.setReference(jsp.config.getWebPath() + "schede/" + allegato.getObjAllegato());
			cell = new Cell(link);
			cell.add(new Phrase('\n' + allegato.getNotAllegato(), font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
			
			cell = new Cell(new Phrase(Integer.toString(allegato.getNumAllegato()), font("Normal08")));
			cell.setHorizontalAlignment(cell.ALIGN_CENTER);
			cell.setLeading(leading);
			tab.addCell(cell);
		}
		docPdf.add(tab);		
	}
}
private void addArgomenti(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getArgomenti().getSize() == 0) {
		addTitolo("Non sono previsti argomenti", "Italic10", 0);
		return;
	}
	Table tab = new Table(3);
	tab.setWidth(100);
	tab.setWidths(new int[] {20, 30, 50});
	tab.setPadding(2);
	tab.setSpacing(0);
	tab.setOffset(0);
	Cell cell;
	int leading = 8;
	
	cell = new Cell(new Phrase("Peso", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Argomento", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Note", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	
	for (int i = 0; i < pd.getArgomenti().getSize(); i++){
		Argomento obj = (Argomento)pd.getArgomenti().getElement(i);
		cell = new Cell(new Phrase(obj.getIndPeso(), font("Normal08")));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesArgomento(), font("Normal08")));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNotArgomento(), font("Normal08")));
		cell.setLeading(leading);
		tab.addCell(cell);
	}
	docPdf.add(tab);
}
private void addAutorizzazioni(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getAutorizzazioni().getSize() == 0) {
		addTitolo("Non si sono registrate autorizzazioni", "Italic10", 0);
		return;
	}
	Table tab = new Table(7);
	tab.setWidth(100);
	tab.setWidths(new int[] {15, 15, 10, 25, 15, 10, 10});
	tab.setPadding(2);
	tab.setSpacing(0);
	tab.setOffset(0);

	Cell cell;
	
	cell = new Cell(new Phrase("Tipo", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Stato", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Inizio", font("Italic08")));
	cell.setHorizontalAlignment(cell.ALIGN_CENTER);
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Posizione", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Riferimenti Autorizzazione", font("Italic08")));
	cell.setColspan(3);
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	int leading = 8;
	for (int i = 0; i < pd.getAutorizzazioni().getSize(); i++){
		Autorizzazione obj = (Autorizzazione)pd.getAutorizzazioni().getElement(i);
		cell = new Cell(new Phrase(obj.getDesTipoStato(), font("Normal08")));
		cell.setRowspan(2);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesStato(), font("Normal08")));
		cell.setRowspan(2);
		tab.addCell(cell);
		cell = new Cell(new Phrase(jsp.getDataEsterna(obj.getDatValidita()), font("Normal08")));
		cell.setHorizontalAlignment(cell.ALIGN_CENTER);
		cell.setRowspan(2);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesPosizione(), font("Normal08")));
		cell.setRowspan(2);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getCodAutorizzazione(), font("Normal08")));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getCodUtente(), font("Normal08")));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase(jsp.getDataEsterna(obj.getDatAutorizzazione()), font("Normal08")));
		cell.setHorizontalAlignment(cell.ALIGN_CENTER);
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNotAutorizzazione(), font("Normal08")));
		cell.setColspan(3);
		cell.setLeading(leading);
		tab.addCell(cell);
	}
	
	docPdf.add(tab);
	
}
private void addCanali(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getCanali().getSize() == 0) {
		addTitolo("Non sono previsti canali", "Italic10", 0);
		return;
	}
	Table tab = new Table(3);
	tab.setWidth(100);
	tab.setWidths(new int[] {20, 30, 50});
	tab.setPadding(2);
	tab.setSpacing(0);
	tab.setOffset(0);
	Cell cell;
	int leading = 8;
	
	cell = new Cell(new Phrase("Canale", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Utilizzo", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Note", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	
	for (int i = 0; i < pd.getCanali().getSize(); i++){
		Canale obj = (Canale)pd.getCanali().getElement(i);
		cell = new Cell(new Phrase(obj.getDesCanale(), font("Normal08")));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesUsoCanale(), font("Normal08")));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNotCanale(), font("Normal08")));
		cell.setLeading(leading);
		tab.addCell(cell);
	}
	docPdf.add(tab);
}
private void addConcorrenti(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getConcorrenti().getSize() == 0) {
		addTitolo("Non sono previsti concorrenti", "Italic10", 0);
		return;
	}
	Table tab = new Table(3);
	tab.setWidth(100);
	tab.setWidths(new int[] {20, 30, 50});
	tab.setPadding(2);
	tab.setSpacing(0);
	tab.setOffset(0);
	Cell cell;
	int leading = 8;
	
	for (int i = 0; i < pd.getConcorrenti().getSize(); i++){
		Concorrente obj = (Concorrente)pd.getConcorrenti().getElement(i);
		cell = new Cell(new Phrase("Rif. Prodotto", font("Bold10")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getCodConcorrente(), font("Bold10")));
		cell.setColspan(2);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Note", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNotConcorrente(), font("Normal08")));
		cell.setColspan(2);
		tab.addCell(cell);
		if (obj.getArgomenti().getSize()>0) {
			cell = new Cell(new Phrase("Peso Argomenti", font("Italic08")));
			cell.setLeading(leading);
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			tab.addCell(cell);
			cell = new Cell(new Phrase("Argomento", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Note", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);

			for (int j = 0; j < obj.getArgomenti().getSize(); j++){
				Argomento obj2 = (Argomento)obj.getArgomenti().getElement(j);
				cell = new Cell(new Phrase(obj2.getIndPeso(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(obj2.getDesArgomento(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(obj2.getNotArgomento(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
			}
		}
	}
	docPdf.add(tab);
}
private void addConvenzioni(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getConvenzioni().getSize() == 0) {
		addTitolo("Non sono previste convenzioni", "Italic10", 0);
		return;
	}
	for (int i = 0; i < pd.getConvenzioni().getSize(); i++){
		Table tab = new Table(5);
		tab.setWidth(100);
		tab.setWidths(new int[] {20, 10, 10, 20, 40});
		tab.setPadding(2);
		tab.setSpacing(0);
		
		if (i>0) docPdf.newPage();
		
		Cell cell;
		int leading = 8;
	
		Convenzione obj = (Convenzione)pd.getConvenzioni().getElement(i);
		setArgomentoContesto(2, "Convenzione", obj.getDesConvenzione());
		cell = new Cell(new Phrase("Convenzione", font("Bold12")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesConvenzione(), font("Bold12")));
		cell.setColspan(4);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Rif. Deroghe", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getCodLivelloDeroga(), font("Normal08")));
		cell.setColspan(4);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Note", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNotConvenzione(), font("Normal08")));
		cell.setColspan(4);
		tab.addCell(cell);
		if (obj.getPuntiVendita().getSize()>0) {
			cell = new Cell(new Phrase("Punti Vendita", font("Italic08")));
			cell.setLeading(leading);
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			tab.addCell(cell);
			cell = new Cell(new Phrase("Validità", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setColspan(2);
			cell.setLeading(leading);
			tab.addCell(cell);
	 		cell = new Cell(new Phrase("Canale", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Note", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);

			for (int j = 0; j < obj.getPuntiVendita().getSize(); j++){
				PuntoVendita obj2 = (PuntoVendita)obj.getPuntiVendita().getElement(j);
				cell = new Cell(new Phrase(obj2.getDesPuntoVendita(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(jsp.getDataEsterna(obj2.getDatInizio()), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(jsp.getDataEsterna(obj2.getDatFine()), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(obj2.getDesCanale(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(obj2.getNotPuntoVendita(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
			}
		}
		docPdf.add(tab);
		
		if (obj.getRegoleTariffa().getSize()>0) {
			addTitolo("Regole Tariffarie per " + obj.getDesConvenzione(), "Bold10", 0);
			addRegoleTariffa(obj, writer);
		}
	}
}
private void addDescrizioni(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getDescrizioni().getSize()==0) {
		addTitolo("Non sono previste altre descrizioni", "Italic10", 0);
		return;
	}
	for (int i = 0; i < pd.getDescrizioni().getSize(); i++){
		Descrizione obj = (Descrizione)pd.getDescrizioni().getElement(i);
		
		Table tab = new Table(2);
		tab.setWidth(100);
		tab.setWidths(new int[] {20, 80});
		tab.setTableFitsPage(true);
		tab.setPadding(2);
		tab.setSpacing(0);
		tab.setOffset(0);
		Cell cell;
		
		cell = new Cell(new Phrase("Tipo", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesTipoDescrizione(), font("Normal08")));
		tab.addCell(cell);
		
		cell = new Cell(new Phrase("Descrizione", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesProdotto(), font("Normal08")));
		tab.addCell(cell);
		
		cell = new Cell(new Phrase("Note", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNotProdotto(), font("Normal08")));
		tab.addCell(cell);
		docPdf.add(tab);
	}
}
private void addDocumenti(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getDocumenti().getSize() == 0) {
		addTitolo("Non sono previste documenti", "Italic10", 0);
		return;
	}
	Table tab = new Table(4);
	tab.setWidth(100);
	tab.setWidths(new int[] {20, 10, 30, 40});
	tab.setPadding(2);
	tab.setSpacing(0);
	tab.setOffset(0);
	Cell cell;
	int leading = 8;
	
	cell = new Cell(new Phrase("Tipo", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Cliente", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Descrizione", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Note", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	
	for (int i = 0; i < pd.getDocumenti().getSize(); i++){
		Documento obj = (Documento)pd.getDocumenti().getElement(i);
		cell = new Cell(new Phrase(obj.getDesTipoDocumento(), font("Normal08")));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase(getDesTipoCliente(obj.getIndTipoCliente()), font("Normal08")));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesDocumento(), font("Normal08")));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNotDocumento(), font("Normal08")));
		cell.setLeading(leading);
		tab.addCell(cell);
	}
	docPdf.add(tab);
}
private void addEliminati(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (opzPdElim.getSize() == 0) {
		addTitolo("Non sono previsti prodotti da eliminare", "Italic10", 0);
		return;
	}
	Table tab = new Table(3);
	tab.setWidth(100);
	tab.setWidths(new int[] {20, 30, 50});
	tab.setPadding(2);
	tab.setSpacing(0);
	tab.setOffset(0);
	Cell cell;
	int leading = 8;
	
	for (int i = 0; i < opzPdElim.getSize(); i++){
		Opzione obj = (Opzione)opzPdElim.getElement(i);
		cell = new Cell(new Phrase("Opzione", font("Bold10")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesOpzione(), font("Bold10")));
		cell.setColspan(2);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Tipo", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesTipoOpzione(), font("Normal08")));
		cell.setColspan(2);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Facoltativa", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getFlgFacoltativa(), font("Normal08")));
		cell.setColspan(2);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Regola di Vendita", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesRegola(), font("Normal08")));
		cell.setColspan(2);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Note", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNotOpzione(), font("Normal08")));
		cell.setColspan(2);
		tab.addCell(cell);
		if (obj.getProdottiOpzione().getSize()>0) {
			cell = new Cell(new Phrase("Prodotti in Opzione", font("Italic08")));
			cell.setLeading(leading);
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			tab.addCell(cell);
			cell = new Cell(new Phrase("Prodotto", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Note", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);

			for (int j = 0; j < obj.getProdottiOpzione().getSize(); j++){
				ProdottoInOpzione obj2 = (ProdottoInOpzione)obj.getProdottiOpzione().getElement(j);
				cell = new Cell(new Phrase(obj2.getIndGruppo(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(obj2.getDesProdotto(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(obj2.getNotProdotto(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
			}
		}
	}
	docPdf.add(tab);
}
protected void addFooter(PdfWriter writer, Document document) {
	PdfContentByte cb = writer.getDirectContent();
	PdfPTable pTabMain = new PdfPTable(new float[]{20, 70, 10});
	pTabMain.setTotalWidth(document.right() - document.left());
	pTabMain.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	pTabMain.getDefaultCell().setPaddingBottom(4);
	pTabMain.getDefaultCell().setBorder(0);
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	pTabMain.addCell(new Phrase(sdf.format(new Date()), font("Normal08")));
	String ref = valContesto[0];
	if (valContesto[1].trim().length()>0) ref = ref + " / " + valContesto[1];
	pTabMain.addCell(new Phrase(ref, font("Italic08")));
	pTabMain.addCell(new Phrase("pag. " + Integer.toString(document.getPageNumber()), font("Italic08")));
	pTabMain.writeSelectedRows(0, -1, document.left(), 62, cb);
}
protected void addHeader(PdfWriter writer, Document document) {
//	if (document.getPageNumber()<=1) return;
	PdfContentByte cb = writer.getDirectContent();
	PdfPTable pTabMain = new PdfPTable(new float[]{20, 80});
	pTabMain.setTotalWidth(document.right() - document.left());
	PdfPCell cell;
	try {
		// HEADER
		Image img = Image.getInstance(userConfig.getConfigPath() + input.getSocieta() + ".jpg");
		pTabMain.addCell(img);
		
		cell = new PdfPCell(new Phrase("CATALOGO PRODOTTI - SCHEDA GENERALE", font("Bold14")));
		cell.setPaddingLeft(2);
		cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
		pTabMain.addCell(cell);
		
		cell = new PdfPCell(new Phrase(pd.getCodProdotto() + " - " + pd.getRifVersione(), font("Bold14")));
		cell.setPaddingLeft(2);
		cell.setPaddingBottom(4);
		pTabMain.addCell(cell);
		
		cell = new PdfPCell(new Phrase(pd.getDesProdotto(), font("Bold14")));
		cell.setPaddingLeft(2);
		cell.setPaddingBottom(4);
		pTabMain.addCell(cell);

		pTabMain.writeSelectedRows(0, -1, document.left(), 800, cb);
		
		pTabMain = new PdfPTable(new float[]{20, 20, 20, 20, 20});
		pTabMain.setTotalWidth(document.right() - document.left());
		pTabMain.getDefaultCell().setHorizontalAlignment(cell.ALIGN_CENTER);
		pTabMain.getDefaultCell().setPaddingBottom(2);
		pTabMain.getDefaultCell().setBackgroundColor(new java.awt.Color(225,225,225));
		for (int i = 0; i < 5; i++){
			pTabMain.addCell(new Phrase(keyContesto[i], font("Italic08")));
		}
		pTabMain.getDefaultCell().setBackgroundColor(java.awt.Color.white);
		for (int i = 0; i < 5; i++){
			String tmp = valContesto[i];
			if (tmp.length()>20) tmp = tmp.substring(0, 20);
			pTabMain.addCell(new Phrase(tmp, font("Bold08")));
		}
		pTabMain.writeSelectedRows(0, -1, document.left(), 750, cb);

		if (document.getPageNumber()>1) {
/*
			cb.rectangle(570, 0, 30, 900);
			cb.setColorFill(new java.awt.Color(225,225,225));
			cb.closePathFillStroke();
			
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);  
			cb.beginText();
			cb.setFontAndSize(bf, 12);
			cb.setTextMatrix(0, -1, 1, 0, 578, 700);
			cb.setColorFill(java.awt.Color.black);
			cb.showText(contesto);
			cb.endText(); 
*/
			
		}
	}
	catch (BadElementException e1) {
		System.out.println(e1.getMessage());
	}
	catch (DocumentException e2) {
		System.out.println(e2.getMessage());
	}
	catch (java.net.MalformedURLException e3) {
		System.out.println(e3.getMessage());
	}
	catch (java.io.IOException e4) {
		System.out.println(e4.getMessage());
	}
}
private void addIdentificativi(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getIdentificativi().getSize()==0) {
		addTitolo("Non sono previsti altri identificativi", "Italic10", 0);
		return;
	}
	Table tab = new Table(2);
	tab.setWidth(100);
	tab.setWidths(new int[] {20, 80});
	tab.setPadding(2);
	tab.setSpacing(0);
	tab.setOffset(0);

	Cell cell;
	
	cell = new Cell(new Phrase("Tipo", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Identificativo", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);

	for (int i = 0; i < pd.getIdentificativi().getSize(); i++){
		Identificativo obj = (Identificativo)pd.getIdentificativi().getElement(i);
		cell = new Cell(new Phrase(obj.getDesTipoIdentificativo(), font("Normal08")));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getCodIdentificativo(), font("Normal08")));
		tab.addCell(cell);
	}
	
	docPdf.add(tab);
		
}
private void addInfoAggiuntive(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getInfo().getSize()==0) {
		addTitolo("Non sono previste informazioni aggiuntive", "Italic10", 0);
		return;
	}
	Table tab = new Table(2);
	tab.setWidth(100);
	tab.setWidths(new int[] {25, 75});
	tab.setPadding(2);
	tab.setSpacing(0);
	tab.setOffset(0);

	Cell cell;
	
	cell = new Cell(new Phrase("Tipo", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Valore", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);

	for (int i = 0; i < pd.getInfo().getSize(); i++){
		InfoProdotto info = (InfoProdotto)pd.getInfo().getElement(i);
		cell = new Cell(new Phrase(info.getDesInfo(), font("Normal08")));
		tab.addCell(cell);
		cell = new Cell(new Phrase(info.getValInfo(), font("Normal08")));
		tab.addCell(cell);
	}
	
	docPdf.add(tab);
	
}
private void addListaCaratteristiche(String prefix, OggettoAssicurazione oggetto, PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	BmaVector lista = oggetto.getCaratteristiche();
	if (lista.getSize() == 0) return;
	for (int i = 0; i < lista.getSize(); i++) {
		Caratteristica obj = (Caratteristica)lista.getElement(i);
		setArgomentoContesto(3, "Caratteristica", obj.getDesCaratteristica());
		Table tab = new Table(5);
		tab.setTableFitsPage(true);
		tab.setWidth(100);
		tab.setWidths(new int[] {20, 10, 10, 25, 35});
		tab.setPadding(2);
		tab.setSpacing(0);
		tab.setOffset(0);
		Cell cell;
		int leading = 8;
		cell = new Cell(new Phrase("Caratteristica", font("Bold10")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesCaratteristica(), font("Bold10")));
		cell.setColspan(4);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Relativa a", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(oggetto.getDesOggetto(), font("Normal08")));
		cell.setColspan(4);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Valore Standard", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getValStandard(), font("Normal08")));
		cell.setColspan(4);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Note", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNotCaratteristica(), font("Normal08")));
		cell.setColspan(4);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Valori per Livello", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		if (obj.getValori().getSize()>0) {
			cell = new Cell(new Phrase("Minimo", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Massimo", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Descrizione", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Note", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			for (int j = 0; j < obj.getValori().getSize(); j++){
				Valore v = (Valore)obj.getValori().getElement(j);
				cell = new Cell(new Phrase(v.getDesLivelloDeroga(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(v.getValMin(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(v.getValMax(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(v.getDesValore(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(v.getNotValore(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
			}
		}
		else {
			cell = new Cell(new Phrase("Non sono previsti valori per la caratterisitca", font("Italic08")));
			cell.setColspan(4);
			cell.setHorizontalAlignment(cell.ALIGN_CENTER);
			cell.setLeading(leading);
			tab.addCell(cell);
		}
		docPdf.add(tab);
	}
}
private void addListaCondizioni(String prefix, OggettoAssicurazione oggetto, Garanzia garanzia, String tipo, PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	BmaVector lista;
	if (tipo.equals("GENERALI")) lista = pd.getCondizioni();
	else if (tipo.equals("IDONEITA")) lista = pd.getIdoneita();
	else lista = garanzia.getCondizioni();
	if (lista.getSize() == 0) {
		addTitolo("Non sono previste condizioni", "Italic10", 0);
		return;
	}	
	String refCondizione = "";
	if (garanzia!=null) refCondizione = "garanzia " + garanzia.getDesGaranzia();
	if (oggetto!=null) refCondizione = refCondizione + " su " + oggetto.getDesOggetto();
	for (int i = 0; i < lista.getSize(); i++) {
		Condizione obj = (Condizione)lista.getElement(i);
		if (oggetto==null) {
			if (garanzia==null) setArgomentoContesto(2, "Condizione", obj.getDesCondizione());
			else setArgomentoContesto(3, "Condizione", obj.getDesCondizione());
		}
		else setArgomentoContesto(4, "Condizione", obj.getDesCondizione());

		Table tab = new Table(5);
		tab.setWidth(100);
		tab.setWidths(new int[] {20, 10, 10, 25, 35});
		tab.setPadding(2);
		tab.setSpacing(0);
		tab.setOffset(0);
		Cell cell;
		int leading = 8;
		String numCondizione = prefix + "." + Integer.toString(i + 1);
		cell = new Cell(new Phrase("Condizione", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesCondizione(), font("Bold10")));
		cell.setColspan(4);
		tab.addCell(cell);

		if (refCondizione.trim().length()>0) {
			cell = new Cell(new Phrase("Relativa a", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			tab.addCell(cell);
			cell = new Cell(new Phrase(refCondizione, font("Normal08")));
			cell.setColspan(4);
			tab.addCell(cell);
		}
		
		cell = new Cell(new Phrase("Valore Standard", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getValStandard(), font("Normal08")));
		cell.setColspan(4);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Note", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNotCondizione(), font("Normal08")));
		cell.setColspan(4);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Valori per Livello", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		if (obj.getValori().getSize()>0) {
			cell = new Cell(new Phrase("Minimo", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Massimo", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Descrizione", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Note", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			for (int j = 0; j < obj.getValori().getSize(); j++){
				Valore v = (Valore)obj.getValori().getElement(j);
				cell = new Cell(new Phrase(v.getDesLivelloDeroga(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(v.getValMin(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(v.getValMax(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(v.getDesValore(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(v.getNotValore(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
			}
		}
		else {
			cell = new Cell(new Phrase("La condizione non prevede valori", font("Italic08")));
			cell.setColspan(4);
			cell.setHorizontalAlignment(cell.ALIGN_CENTER);
			cell.setLeading(leading);
			tab.addCell(cell);
		}
		docPdf.add(tab);
	}
}
private void addListaGaranzie(String prefix, OggettoAssicurazione oggetto, PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	BmaVector lista;
	if (oggetto==null) lista = pd.getGaranzie();
	else lista = oggetto.getGaranzie();	
	if (lista.getSize() == 0) return;
	int leading = 8;
	Table tab;
	Cell cell;
	for (int i = 0; i < lista.getSize(); i++){
		Garanzia garanzia = (Garanzia)lista.getElement(i);
		if (oggetto==null) setArgomentoContesto(2, "Garanzia", garanzia.getDesGaranzia());
		else setArgomentoContesto(3, "Garanzia", garanzia.getDesGaranzia());
		tab = new Table(4);
		tab.setTableFitsPage(true);
		tab.setWidth(100);
		tab.setWidths(new int[] {20, 20, 20, 40});
		tab.setPadding(2);
		tab.setSpacing(0);
		tab.setOffset(0);

		String numGaranzia = prefix + "." + Integer.toString(i + 1);
		String desGaranzia = garanzia.getDesGaranzia();
		cell = new Cell(new Phrase("Garanzia:", font("Bold10")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(garanzia.getDesGaranzia(), font("Bold10")));
		cell.setColspan(3);
		tab.addCell(cell);
	
		if (oggetto!=null) {
			cell = new Cell(new Phrase("Relativa a", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			tab.addCell(cell);
			cell = new Cell(new Phrase(oggetto.getDesOggetto(), font("Normal08")));
			cell.setColspan(3);
			tab.addCell(cell);
		}
		
		cell = new Cell(new Phrase("Facoltativa?", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		String tmp = pd.BMA_FALSE;
		if (garanzia.isFacoltativa()) tmp = pd.BMA_TRUE;
		cell = new Cell(new Phrase(tmp, font("Normal08")));
		cell.setColspan(3);
		tab.addCell(cell);

		cell = new Cell(new Phrase("Note", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(garanzia.getNotGaranzia(), font("Normal08")));
		cell.setColspan(3);
		tab.addCell(cell);
		if (garanzia.getRegole().getSize()>0) {
			cell = new Cell(new Phrase("Regole di Vendita", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Regola", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Riferim.Garanzia", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Note", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			for (int j = 0; j < garanzia.getRegole().getSize(); j++){
				RegolaGaranzia regola = (RegolaGaranzia)garanzia.getRegole().getElement(j);
				cell = new Cell(new Phrase(regola.getDesLivello(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(regola.getDesRegola(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(regola.getRifDesGaranzia(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(regola.getNotRegola(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
			}
		}
		docPdf.add(tab);
			
		if (garanzia.getCondizioni().getSize()>0) {
			addTitolo("Condizioni Particolari per " + garanzia.getDesGaranzia(), "Bold10", 0);
			addListaCondizioni(numGaranzia, oggetto, garanzia, "PARTICOLARI", writer);
			addTitolo("", "Bold10", 20);
		}
	}
}
private void addModuli(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getModuli().getSize() == 0) {
		addTitolo("Non sono previsti moduli", "Italic10", 0);
		return;
	}
	int leading = 8;
	for (int i = 0; i < pd.getModuli().getSize(); i++){
		Modulo obj = (Modulo)pd.getModuli().getElement(i);
		setArgomentoContesto(2, "Modulo", obj.getDesModulo());		
		Table tab = new Table(3);
		tab.setWidth(100);
		tab.setWidths(new int[] {20, 30, 50});
		tab.setPadding(2);
		tab.setSpacing(0);
		tab.setOffset(0);
		Cell cell;
		cell = new Cell(new Phrase("Modulo", font("Bold12")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesModulo(), font("Bold12")));
		cell.setColspan(2);
		tab.addCell(cell);
		
		cell = new Cell(new Phrase("Tipo", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesTipoModulo(), font("Normal08")));
		cell.setColspan(2);
		cell.setLeading(leading);
		tab.addCell(cell);

		if (obj.getRifEsterno().trim().length()>0) {
			cell = new Cell(new Phrase("Rif.Esterno", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase(obj.getRifEsterno(), font("Normal08")));
			cell.setColspan(2);
			cell.setLeading(leading);
			tab.addCell(cell);
		}
		
		if (obj.getRifNome().trim().length()>0) {
			cell = new Cell(new Phrase("Nome Esterno", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase(obj.getRifNome(), font("Normal08")));
			cell.setColspan(2);
			cell.setLeading(leading);
			tab.addCell(cell);
		}
			
		if (obj.getRifApplicazione().trim().length()>0) {
			cell = new Cell(new Phrase("Applicazione", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase(obj.getRifApplicazione(), font("Normal08")));
			cell.setColspan(2);
			cell.setLeading(leading);
			tab.addCell(cell);
		}

		if (obj.getNotModulo().trim().length()>0) {
			cell = new Cell(new Phrase("Note", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase(obj.getNotModulo(), font("Normal08")));
			cell.setColspan(2);
			cell.setLeading(leading);
			tab.addCell(cell);
		}

		if (obj.getDati().getSize()>0) {
			cell = new Cell(new Phrase("Dati", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Note e Regole", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setColspan(2);
			cell.setLeading(leading);
			tab.addCell(cell);
			
			for (int j = 0; j < obj.getDati().getSize(); j++){
				DatoModulo dato = (DatoModulo)obj.getDati().getElement(j);
				cell = new Cell(new Phrase(dato.getDesDato() + "(" + dato.getIndTipoDato() + ")", font("Normal08")));
				cell.setLeading(leading);
				if (dato.getRegole().getSize()>0) {
					cell.setRowspan(dato.getRegole().getSize());
					tab.addCell(cell);
					for (int k = 0; k < dato.getRegole().getSize(); k++){
						RegolaModulo reg = (RegolaModulo)dato.getRegole().getElement(k);
						cell = new Cell(new Phrase(reg.getDesRegola(), font("Normal08")));
						cell.setLeading(leading);
						tab.addCell(cell);
						cell = new Cell(new Phrase(reg.getNotRegola(), font("Normal08")));
						cell.setLeading(leading);
						tab.addCell(cell);
					}	
				}
				else {
					tab.addCell(cell);
					cell = new Cell(new Phrase(dato.getNotDato(), font("Normal08")));
					cell.setColspan(2);
					cell.setLeading(leading);
					tab.addCell(cell);
				}
			}
		}

		if (obj.getRegole().getSize()>0) {
			cell = new Cell(new Phrase("Regole", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Note", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setColspan(2);
			cell.setLeading(leading);
			tab.addCell(cell);
			
			for (int j = 0; j < obj.getRegole().getSize(); j++){
				RegolaModulo reg = (RegolaModulo)obj.getRegole().getElement(j);
				cell = new Cell(new Phrase(reg.getDesRegola(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(reg.getNotRegola(), font("Normal08")));
				cell.setColspan(2);
				cell.setLeading(leading);
				tab.addCell(cell);	
			}
		}		
		docPdf.add(tab);
	}
}
private void addNormative(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getNormative().getSize() == 0) {
		addTitolo("Non sono previste normative", "Italic10", 0);
		return;
	}
	Table tab = new Table(4);
	tab.setWidth(100);
	tab.setWidths(new int[] {20, 10, 30, 40});
	tab.setPadding(2);
	tab.setSpacing(0);
	tab.setOffset(0);
	Cell cell;
	int leading = 8;
	
	for (int i = 0; i < pd.getNormative().getSize(); i++){
		Normativa obj = (Normativa)pd.getNormative().getElement(i);
		String tmp = " (Esterna)";
		if (obj.isInterna()) tmp = " (Interna)";
		cell = new Cell(new Phrase(obj.getDesNormativa() + tmp, font("Bold10")));
		cell.setColspan(4);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Emittente:", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesEmittente(), font("Normal08")));
		cell.setColspan(3);
		tab.addCell(cell);

		cell = new Cell(new Phrase("Validità:", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		tmp = jsp.getDataEsterna(obj.getDatValidita()) + " / " + jsp.getDataEsterna(obj.getDatFineValidita());
		cell = new Cell(new Phrase(tmp, font("Normal08")));
		cell.setColspan(3);
		tab.addCell(cell);

		cell = new Cell(new Phrase("Note:", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNotNormativa(), font("Normal08")));
		cell.setColspan(3);
		tab.addCell(cell);

		cell = new Cell(new Phrase("Allegati", font("Italic08")));
		cell.setLeading(leading);
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase("Data", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Descrizione", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Note", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);

		for (int j = 0; j < obj.getAllegati().getSize(); j++){
			AllegatoNorma obj2 = (AllegatoNorma)obj.getAllegati().getElement(j);
			tmp = Integer.toString(obj2.getNumAllegato());
			cell = new Cell(new Phrase(tmp, font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase(jsp.getDataEsterna(obj2.getDatAllegato()), font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase(obj2.getDesAllegato(), font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase(obj2.getNotAllegato(), font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
		}
	}
	docPdf.add(tab);
}
private void addObiettivi(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getObiettivi().getSize() == 0) {
		addTitolo("Non sono previsti obiettivi", "Italic10", 0);
		return;
	}
	Table tab = new Table(3);
	tab.setWidth(100);
	tab.setWidths(new int[] {20, 30, 50});
	tab.setPadding(2);
	tab.setSpacing(0);
	tab.setOffset(0);
	Cell cell;
	int leading = 8;
	
	cell = new Cell(new Phrase("Peso", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Obiettivo", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Note", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	
	for (int i = 0; i < pd.getObiettivi().getSize(); i++){
		Obiettivo obj = (Obiettivo)pd.getObiettivi().getElement(i);
		cell = new Cell(new Phrase(obj.getIndPeso(), font("Normal08")));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesObiettivo(), font("Normal08")));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNotObiettivo(), font("Normal08")));
		cell.setLeading(leading);
		tab.addCell(cell);
	}
	docPdf.add(tab);
}
private void addOggetti(String prefix, PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getOggetti().getSize() == 0) {
		addTitolo("Non sono previsti oggetti", "Italic10", 0);
		return;
	}
	for (int i = 0; i < pd.getOggetti().getSize(); i++){
		OggettoAssicurazione obj = (OggettoAssicurazione)pd.getOggetti().getElement(i);
		setArgomentoContesto(2, "Oggetto", obj.getDesOggetto());
		String pref1 = prefix + "." + Integer.toString(i + 1);
		Table tab = new Table(2);
		tab.setTableFitsPage(true);
		tab.setWidth(100);
		tab.setWidths(new int[] {20, 80});
		tab.setPadding(2);
		tab.setSpacing(0);
		tab.setOffset(0);
		Cell cell;
		int leading = 8;
		
		if (i>0) docPdf.newPage();
		
		cell = new Cell(new Phrase("Oggetto", font("Bold12")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesOggetto(), font("Bold12")));
		tab.addCell(cell);
		cell = new Cell(new Phrase("Tipo", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesTipoOggetto(), font("Normal08")));
		tab.addCell(cell);
		cell = new Cell(new Phrase("Note", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNotOggetto(), font("Normal08")));
		tab.addCell(cell);
		if (obj.getGaranzie().getSize()>0) {
			cell = new Cell(new Phrase("Sintesi Garanzie", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setRowspan(obj.getGaranzie().getSize());
			tab.addCell(cell);
			for (int j = 0; j < obj.getGaranzie().getSize(); j++){
				Garanzia gar = (Garanzia)obj.getGaranzie().getElement(j);
				cell = new Cell(new Phrase(gar.getDesGaranzia(), font("Normal08")));
				tab.addCell(cell);
			}
		}
		
		docPdf.add(tab);

		if (obj.getCaratteristiche().getSize()>0) {
			addTitolo("Caratteristiche", "Bold12", 20);
			addListaCaratteristiche(pref1 + ".car", obj, writer);
		}
		if (obj.getGaranzie().getSize()>0) {
			addTitolo("Dettaglio Garanzie", "Bold12", 20);
			addListaGaranzie(pref1 + ".gar", obj, writer);
		}
	}
}
private void addOpzioni(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (opzAltre.getSize() == 0) {
		addTitolo("Non sono previste opzioni", "Italic10", 0);
		return;
	}
	Table tab = new Table(3);
	tab.setWidth(100);
	tab.setWidths(new int[] {20, 30, 50});
	tab.setPadding(2);
	tab.setSpacing(0);
	tab.setOffset(0);
	Cell cell;
	int leading = 8;
	
	for (int i = 0; i < opzAltre.getSize(); i++){
		Opzione obj = (Opzione)opzAltre.getElement(i);
		cell = new Cell(new Phrase("Opzione", font("Bold10")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesOpzione(), font("Bold10")));
		cell.setColspan(2);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Tipo", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesTipoOpzione(), font("Normal08")));
		cell.setColspan(2);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Facoltativa", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getFlgFacoltativa(), font("Normal08")));
		cell.setColspan(2);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Regola di Vendita", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesRegola(), font("Normal08")));
		cell.setColspan(2);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Note", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNotOpzione(), font("Normal08")));
		cell.setColspan(2);
		tab.addCell(cell);
		if (obj.getProdottiOpzione().getSize()>0) {
			cell = new Cell(new Phrase("Prodotti in Opzione", font("Italic08")));
			cell.setLeading(leading);
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			tab.addCell(cell);
			cell = new Cell(new Phrase("Prodotto", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Note", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);

			for (int j = 0; j < obj.getProdottiOpzione().getSize(); j++){
				ProdottoInOpzione obj2 = (ProdottoInOpzione)obj.getProdottiOpzione().getElement(j);
				cell = new Cell(new Phrase(obj2.getIndGruppo(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(obj2.getDesProdotto(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(obj2.getNotProdotto(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
			}
		}
	}
	docPdf.add(tab);
}
private void addRaggruppamenti(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getRagguppamenti().getSize()==0) {
		addTitolo("Non sono previsti raggruppamenti", "Italic10", 0);
		return;
	}

	for (int i = 0; i < pd.getRagguppamenti().getSize(); i++){
		Raggruppamento obj = (Raggruppamento)pd.getRagguppamenti().getElement(i);
		Table tab = new Table(2);
		tab.setWidth(100);
		tab.setWidths(new int[] {20, 80});
		tab.setPadding(2);
		tab.setSpacing(0);
		tab.setOffset(0);
		int leading = 8;
		Cell cell;
		cell = new Cell(new Phrase("Schema", font("Bold10")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesSchema(), font("Bold10")));
		tab.addCell(cell);
		
		cell = new Cell(new Phrase("Codice Nodo", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getCodNodo(), font("Normal08")));
		cell.setLeading(leading);
		tab.addCell(cell);
		
		cell = new Cell(new Phrase("Descrizione Nodo", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesNodo(), font("Normal08")));
		cell.setLeading(leading);
		tab.addCell(cell);
		
		cell = new Cell(new Phrase("N° Prodotto nel nodo", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNumProgressivo(), font("Normal08")));
		cell.setLeading(leading);
		tab.addCell(cell);
		
		cell = new Cell(new Phrase("Note", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesProdotto(), font("Normal08")));
		cell.setLeading(leading);
		tab.addCell(cell);
		
		docPdf.add(tab);
	}

}
private void addRegoleAggregati(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getRegoleAggregati().getSize()==0) {
		addTitolo("Non sono previste regole di aggregazione dei premi", "Italic10", 0);
		return;
	}
	// Raggruppa le regole per tipo di aggregato
	Hashtable tipiAggregato = new Hashtable();
	for (int i = 0; i < pd.getRegoleAggregati().getSize(); i++){
		RegolaAggregato regola = (RegolaAggregato)pd.getRegoleAggregati().getElement(i);
		Vector v = (Vector)tipiAggregato.get(regola.getCodTipoAggregato());
		if (v==null) {
			v = new Vector();
			tipiAggregato.put(regola.getCodTipoAggregato(), v);
		}
		v.add(regola);
	}
	Enumeration e = tipiAggregato.elements();
	while (e.hasMoreElements()) {
		Vector lista = (Vector)e.nextElement();
		RegolaAggregato regola = (RegolaAggregato)lista.elementAt(0);
		Table tab = new Table(4);
		tab.setWidth(100);
		tab.setWidths(new int[] {30, 30, 30, 10});
		tab.setTableFitsPage(true);
		tab.setPadding(2);
		tab.setSpacing(0);
		tab.setOffset(0);
		Cell cell;
		int leading = 8;
		
		cell = new Cell(new Phrase(regola.getDesTipoAggregato() , font("Bold10")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setColspan(4);
		tab.addCell(cell);
		
		cell = new Cell(new Phrase("Aggregato", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Garanzia", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Condizione", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase("% Premio", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		String aggTmp = "";
		int righeAgg = 0;
		String garTmp = "";
		int righeGar = 0;
		Vector listaCelle = new Vector();
		Cell cellAgg = null;
		Cell cellGar = null;
		for (int i = 0; i < lista.size(); i++){
			regola = (RegolaAggregato)lista.elementAt(i);
			if (!aggTmp.equals(regola.getDesAggregato())) {
				if (cellAgg!=null) {
					cellAgg.setRowspan(righeAgg);
					righeAgg = 0;
				}
				cellAgg = new Cell(new Phrase(regola.getDesAggregato(), font("Bold08")));
				cellAgg.setLeading(leading);
				listaCelle.add(cellAgg);
				aggTmp = regola.getDesAggregato();
			}
			if (!garTmp.equals(regola.getDesGaranzia())) {
				if (cellGar!=null) {
					cellGar.setRowspan(righeGar);
					righeGar = 0;
				}
				cellGar = new Cell(new Phrase(regola.getDesGaranzia(), font("Bold08")));
				cellGar.setLeading(leading);
				listaCelle.add(cellGar);
				garTmp = regola.getDesGaranzia();
			}
			righeAgg = righeAgg + 1;
			righeGar = righeGar + 1;
			cell = new Cell(new Phrase(regola.getDesCondizione(), font("Normal08")));
			cell.setLeading(leading);
			listaCelle.add(cell);
			cell = new Cell(new Phrase(regola.getPerPremio(), font("Normal08")));
			cell.setHorizontalAlignment(cell.ALIGN_RIGHT);
			cell.setLeading(leading);
			listaCelle.add(cell);
		}
		cellAgg.setRowspan(righeAgg);
		cellGar.setRowspan(righeGar);
		for (int i = 0; i < listaCelle.size(); i++){
			cell = (Cell)listaCelle.elementAt(i);
			tab.addCell(cell);
		}
		docPdf.add(tab);
	}
}
private void addRegoleTariffa(Convenzione convenzione, PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	BmaVector lista;
	if (convenzione==null) lista = pd.getRegoleTariffa();
	else lista = convenzione.getRegoleTariffa();
	if (lista.getSize() == 0) {
		addTitolo("Non sono previste regole", "Italic10", 0);
		return;
	}
	for (int i = 0; i < lista.getSize(); i++){
		RegolaTariffa obj = (RegolaTariffa)lista.getElement(i);
		if (convenzione==null){
			if (obj.getDesOggetto().trim().length()>0) setArgomentoContesto(2, "Oggetto", obj.getDesOggetto());
			if (obj.getDesGaranzia().trim().length()>0) setArgomentoContesto(3, "Garanzia", obj.getDesGaranzia());
			if (obj.getDesCondizione().trim().length()>0) setArgomentoContesto(4, "Condizione", obj.getDesCondizione());
		}
		else {
			setArgomentoContesto(3, "Regole Tariffa", obj.getDesScopo());
			if (obj.getDesOggetto().trim().length()>0) setArgomentoContesto(4, "Oggetto", obj.getDesOggetto());
		}
		Table tab = new Table(4);
		tab.setTableFitsPage(true);
		tab.setWidth(100);
		tab.setWidths(new int[] {20, 25, 25, 30});
		tab.setPadding(2);
		tab.setSpacing(0);
		tab.setOffset(0);
		Cell cell;
		int leading = 8;
		
		cell = new Cell(new Phrase("Scopo", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesScopo(), font("Bold10")));
		cell.setColspan(3);
		tab.addCell(cell);
		
		if (obj.getCodCondizione().trim().length()>0) {
			cell = new Cell(new Phrase("Relativa a", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setRowspan(2);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Oggetto", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			tab.addCell(cell);
			cell = new Cell(new Phrase("Garanzia", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			tab.addCell(cell);
			cell = new Cell(new Phrase("Condizione", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			tab.addCell(cell);
			cell = new Cell(new Phrase(obj.getDesOggetto(), font("Normal08")));
			tab.addCell(cell);
			cell = new Cell(new Phrase(obj.getDesGaranzia(), font("Normal08")));
			tab.addCell(cell);
			cell = new Cell(new Phrase(obj.getDesCondizione(), font("Normal08")));
			tab.addCell(cell);
		}
		
		cell = new Cell(new Phrase("Livello Deroga/Progr.", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesLivello(), font("Normal08")));
		cell.setColspan(2);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNumProgressivo(), font("Normal08")));
		tab.addCell(cell);
		
		cell = new Cell(new Phrase("Regola/Valore/Tariffa", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesRegola(), font("Normal08")));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getValRegola(), font("Normal08")));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getCodTariffa(), font("Normal08")));
		tab.addCell(cell);
		
		cell = new Cell(new Phrase("Note", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNotRegola(), font("Normal08")));
		cell.setColspan(3);
		tab.addCell(cell);

		if (obj.getParametri().getSize()>0) {
			cell = new Cell(new Phrase("Parametri Regola", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Valore", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Riferimento", font("Italic08")));
			cell.setColspan(2);
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			for (int j = 0; j < obj.getParametri().getSize(); j++){
				ParametroRegolaTariffa prt = (ParametroRegolaTariffa)obj.getParametri().getElement(j);
				cell = new Cell(new Phrase(prt.getRifParametro(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);

				if (prt.getRifParametro().equals("TABELLA")) {
					cell = new Cell(new Phrase(prt.getDesTabella(), font("Normal08")));
					cell.setLeading(leading);
					tab.addCell(cell);
					Anchor link = new Anchor(prt.getObjTabella(), font("NormalBlue08"));
					link.setReference(jsp.config.getWebPath() + prt.getObjTabella());
					cell = new Cell(link);
					cell.setColspan(2);
					cell.setLeading(leading);
					tab.addCell(cell);
				} 
				else {
					cell = new Cell(new Phrase(prt.getValParametro(), font("Normal08")));
					cell.setLeading(leading);
					tab.addCell(cell);
					cell = new Cell(new Phrase("", font("Normal08")));
					cell.setColspan(2);
					cell.setLeading(leading);
					tab.addCell(cell);
				}
				

			}
		}
		docPdf.add(tab);
	}
}
private void addResponsabili(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getResponsabili().getSize() == 0) {
		addTitolo("Non sono previsti responsabili", "Italic10", 0);
		return;
	}
	Cell cell;
	int leading = 8;
	
	for (int i = 0; i < pd.getResponsabili().getSize(); i++){
		Responsabile obj = (Responsabile)pd.getResponsabili().getElement(i);
		Table tab = new Table(4);
		tab.setTableFitsPage(true);
		tab.setWidth(100);
		tab.setWidths(new int[] {20, 23, 23, 34});
		tab.setPadding(2);
		tab.setSpacing(0);
		tab.setOffset(0);
		cell = new Cell(new Phrase("Responsabile", font("Bold10")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesTipoResponsabile(), font("Bold10")));
		cell.setColspan(3);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Posizione", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesPosizione(), font("Normal08")));
		cell.setColspan(3);
		tab.addCell(cell);

		cell = new Cell(new Phrase("Note", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNotResponsabile(), font("Normal08")));
		cell.setColspan(3);
		tab.addCell(cell);

		cell = new Cell(new Phrase("Iter di Approvazione", font("Italic08")));
		cell.setLeading(leading);
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase("Stato", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Mail Posizione", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Note", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);

		for (int j = 0; j < obj.getApprovazioni().getSize(); j++){
			Approvazione obj2 = (Approvazione)obj.getApprovazioni().getElement(j);
			cell = new Cell(new Phrase(obj2.getDesTipoStato(), font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase(obj2.getDesStato(), font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase(obj2.getDesMailPosizione(), font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase(obj2.getNotApprovazione(), font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
		}
		docPdf.add(tab);		
	}
}
private void addRigaSommario(String argomento, Table tab) throws DocumentException {
	String mData = "";
	String nElem = "";
	int leading = 8;

	Cell cell;
	if (argomento.equals(pd.BMA_PD_ARGOMENTO_ALLEGATI)) {
		nElem = Integer.toString(pd.getAllegati().getSize());
/*
		for (int i = 0; i < pd.getAllegati().getSize(); i++){
			Allegato obj = (Allegato)pd.getAllegati().getElement(i);
			if (obj.getDatAllegato().compareTo(mData)>0) mData = obj.getDatAllegato();
		}
*/
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_INFO_AGGIUNTIVE)){
		nElem = Integer.toString(pd.getInfo().getSize());
/*
		for (int i = 0; i < pd.getInfo().getSize(); i++){
			InfoProdotto obj = (InfoProdotto)pd.getInfo().getElement(i);
			if (obj.getDatValidita().compareTo(mData)>0) mData = obj.getDatValidita();
		}		
*/
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_IDENTIFICATIVI)){
		nElem = Integer.toString(pd.getIdentificativi().getSize());
/*
		for (int i = 0; i < pd.getIdentificativi().getSize(); i++){
			Identificativo obj = (Identificativo)pd.getIdentificativi().getElement(i);
			if (obj.getDatValidita().compareTo(mData)>0) mData = obj.getDatValidita();
		}		
*/
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_DESCRIZIONI)){
		nElem = Integer.toString(pd.getDescrizioni().getSize());
/*
		for (int i = 0; i < pd.getDescrizioni().getSize(); i++){
			Descrizione obj = (Descrizione)pd.getDescrizioni().getElement(i);
			if (obj.getDatValidita().compareTo(mData)>0) mData = obj.getDatValidita();
		}		
*/
	}
	else if (argomento.equals(ARGOMENTO_PD_ELIMINATI)){
		nElem = Integer.toString(opzPdElim.getSize());
	}
	else if (argomento.equals(ARGOMENTO_PD_SOSTITUIBILI)){
		nElem = Integer.toString(opzPdSost.getSize());
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_RAGGRUPPAMENTI)){
		nElem = Integer.toString(pd.getRagguppamenti().getSize());
/*
		for (int i = 0; i < pd.getRagguppamenti().getSize(); i++){
			Raggruppamento obj = (Raggruppamento)pd.getRagguppamenti().getElement(i);
			if (obj.getDatValidita().compareTo(mData)>0) mData = obj.getDatValidita();
		}		
*/
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_STATI)){
		nElem = Integer.toString(pd.getStati().getSize());
/*
		for (int i = 0; i < pd.getStati().getSize(); i++){
			Stato obj = (Stato)pd.getStati().getElement(i);
			if (obj.getDatValidita().compareTo(mData)>0) mData = obj.getDatValidita();
		}		
*/
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_CONDIZIONI)){
		nElem = Integer.toString(pd.getCondizioni().getSize());
/*
		for (int i = 0; i < pd.getCondizioni().getSize(); i++){
			Condizione obj = (Condizione)pd.getCondizioni().getElement(i);
			if (obj.getDatValidita().compareTo(mData)>0) mData = obj.getDatValidita();
		}		
*/
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_GARANZIE)){
		nElem = Integer.toString(pd.getGaranzie().getSize());
/*
		for (int i = 0; i < pd.getGaranzie().getSize(); i++){
			Garanzia obj = (Garanzia)pd.getGaranzie().getElement(i);
			if (obj.getDatValidita().compareTo(mData)>0) mData = obj.getDatValidita();
		}		
*/
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_OGGETTI)){
		nElem = Integer.toString(pd.getOggetti().getSize());
/*
		for (int i = 0; i < pd.getOggetti().getSize(); i++){
			OggettoAssicurazione obj = (OggettoAssicurazione)pd.getOggetti().getElement(i);
			if (obj.getDatValidita().compareTo(mData)>0) mData = obj.getDatValidita();
		}		
*/
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_REGOLE_TARIFFA)){
		nElem = Integer.toString(pd.getRegoleTariffa().getSize());
/*
		for (int i = 0; i < pd.getRegoleTariffa().getSize(); i++){
			RegolaTariffa obj = (RegolaTariffa)pd.getRegoleTariffa().getElement(i);
			if (obj.getDatValidita().compareTo(mData)>0) mData = obj.getDatValidita();
		}		
*/
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_AGGREGATI)){
		nElem = Integer.toString(pd.getRegoleAggregati().getSize());
/*
		for (int i = 0; i < pd.getRegoleAggregati().getSize(); i++){
			RegolaAggregato obj = (RegolaAggregato)pd.getRegoleAggregati().getElement(i);
			if (obj.getDatValidita().compareTo(mData)>0) mData = obj.getDatValidita();
		}		
*/
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_RESPONSABILI)){
		nElem = Integer.toString(pd.getResponsabili().getSize());
/*
		for (int i = 0; i < pd.getResponsabili().getSize(); i++){
			Responsabile obj = (Responsabile)pd.getResponsabili().getElement(i);
			if (obj.getDatValidita().compareTo(mData)>0) mData = obj.getDatValidita();
		}		
*/
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_AUTORIZZAZIONI)){
		nElem = Integer.toString(pd.getAutorizzazioni().getSize());
/*
		for (int i = 0; i < pd.getAutorizzazioni().getSize(); i++){
			Autorizzazione obj = (Autorizzazione)pd.getAutorizzazioni().getElement(i);
			if (obj.getDatValidita().compareTo(mData)>0) mData = obj.getDatValidita();
		}		
*/
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_NORMATIVE)){
		nElem = Integer.toString(pd.getNormative().getSize());
/*
		for (int i = 0; i < pd.getNormative().getSize(); i++){
			Normativa obj = (Normativa)pd.getNormative().getElement(i);
			if (obj.getDatValidita().compareTo(mData)>0) mData = obj.getDatValidita();
		}		
*/
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_MODULISTICA)){
		nElem = Integer.toString(pd.getModuli().getSize());
/*
		for (int i = 0; i < pd.getModuli().getSize(); i++){
			Modulo obj = (Modulo)pd.getModuli().getElement(i);
			if (obj.getDatValidita().compareTo(mData)>0) mData = obj.getDatValidita();
		}
*/
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_RUOLI)){
		nElem = Integer.toString(pd.getRuoli().getSize());
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_IDONEITA)){
		nElem = Integer.toString(pd.getIdoneita().getSize());
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_DOCUMENTI)){
		nElem = Integer.toString(pd.getDocumenti().getSize());
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_OBIETTIVI)){
		nElem = Integer.toString(pd.getObiettivi().getSize());
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_SEGMENTI)){
		nElem = Integer.toString(pd.getSegmenti().getSize());
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_ARGOMENTI)){
		nElem = Integer.toString(pd.getArgomenti().getSize());
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_OPZIONI)){
		nElem = Integer.toString(pd.getOpzioni().getSize());
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_CONCORRENTI)){
		nElem = Integer.toString(pd.getConcorrenti().getSize());
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_CANALI)){
		nElem = Integer.toString(pd.getCanali().getSize());
	}
	else if (argomento.equals(pd.BMA_PD_ARGOMENTO_CONVENZIONI)){
		nElem = Integer.toString(pd.getConvenzioni().getSize());
	}
		
	if (mData.length()>0) mData = new BmaJsp().getDataEsterna(mData);
	cell = new Cell(new Phrase(formatArgomento(argomento), font("Normal08")));
	cell.setLeading(leading);
	tab.addCell(cell);
	
	cell = new Cell(new Phrase(nElem, font("Normal08")));
	cell.setHorizontalAlignment(cell.ALIGN_CENTER);
	cell.setLeading(leading);
	tab.addCell(cell);
	
	cell = new Cell(new Phrase(mData, font("Normal08")));
	cell.setHorizontalAlignment(cell.ALIGN_CENTER);
	cell.setLeading(leading);
	tab.addCell(cell);
	
	cell = new Cell(new Phrase("", font("Normal08")));
	cell.setHorizontalAlignment(cell.ALIGN_RIGHT);
	cell.setLeading(leading);
	tab.addCell(cell);
}
private void addRuoliCliente(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getRuoli().getSize() == 0) {
		addTitolo("Non sono previste ruoli", "Italic10", 0);
		return;
	}
	Table tab = new Table(4);
	tab.setWidth(100);
	tab.setWidths(new int[] {20, 10, 30, 40});
	tab.setPadding(2);
	tab.setSpacing(0);
	tab.setOffset(0);
	Cell cell;
	int leading = 8;
	
	for (int i = 0; i < pd.getRuoli().getSize(); i++){
		RuoloCliente obj = (RuoloCliente)pd.getRuoli().getElement(i);
		cell = new Cell(new Phrase("Ruolo", font("Bold10")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesRuolo(), font("Bold10")));
		cell.setColspan(3);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Tipo Cliente", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(getDesTipoCliente(obj.getIndTipoCliente()), font("Normal08")));
		cell.setColspan(3);
		tab.addCell(cell);
		if (obj.getDocumenti().getSize()>0) {
			cell = new Cell(new Phrase("Documenti", font("Italic08")));
			cell.setLeading(leading);
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			tab.addCell(cell);
			cell = new Cell(new Phrase("Cliente", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
	 		cell = new Cell(new Phrase("Descrizione", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Note", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);

			for (int j = 0; j < obj.getDocumenti().getSize(); j++){
				Documento obj2 = (Documento)obj.getDocumenti().getElement(j);
				cell = new Cell(new Phrase(obj2.getDesTipoDocumento(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(getDesTipoCliente(obj2.getIndTipoCliente()), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(obj2.getDesDocumento(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(obj2.getNotDocumento(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
			}
		}
	}
	docPdf.add(tab);
}
private void addSegmenti(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getSegmenti().getSize() == 0) {
		addTitolo("Non sono previsti segmenti", "Italic10", 0);
		return;
	}
	Table tab = new Table(3);
	tab.setWidth(100);
	tab.setWidths(new int[] {20, 30, 50});
	tab.setPadding(2);
	tab.setSpacing(0);
	tab.setOffset(0);
	Cell cell;
	int leading = 8;
	
	for (int i = 0; i < pd.getSegmenti().getSize(); i++){
		Segmento obj = (Segmento)pd.getSegmenti().getElement(i);
		cell = new Cell(new Phrase("Segmento", font("Bold10")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesSegmento(), font("Bold10")));
		cell.setColspan(2);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Note", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNotSegmento(), font("Normal08")));
		cell.setColspan(2);
		tab.addCell(cell);
		if (obj.getObiettivi().getSize()>0) {
			cell = new Cell(new Phrase("Peso Obiettivi", font("Italic08")));
			cell.setLeading(leading);
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			tab.addCell(cell);
			cell = new Cell(new Phrase("Obiettivo", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Note", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);

			for (int j = 0; j < obj.getObiettivi().getSize(); j++){
				Obiettivo obj2 = (Obiettivo)obj.getObiettivi().getElement(j);
				cell = new Cell(new Phrase(obj2.getIndPeso(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(obj2.getDesObiettivo(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(obj2.getNotObiettivo(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
			}
		}
	}
	docPdf.add(tab);
}
private void addSintesiDatiTecnici(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	// Condizioni Generali
	Table tab = new Table(3);
	tab.setTableFitsPage(true);
	tab.setWidth(100);
	tab.setWidths(new int[] {5, 35, 60});
	tab.setPadding(2);
	tab.setSpacing(0);
	tab.setOffset(0);
	int leading = 8;
		
	Cell cell = new Cell(new Phrase("Riepilogo delle Condizioni Generali", font("Bold10")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	cell.setColspan(3);
	tab.addCell(cell);
	if (pd.getCondizioni().getSize()>0) {
		cell = new Cell(new Phrase("N°", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Descrizione", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Note", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		for (int i = 0; i < pd.getCondizioni().getSize(); i++){
			Condizione obj = (Condizione)pd.getCondizioni().getElement(i);
			cell = new Cell(new Phrase(Integer.toString(i+1), font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase(obj.getDesCondizione(), font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase(obj.getNotCondizione(), font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
		}
	}
	else {
		cell = new Cell(new Phrase("Non sono previste condizioni", font("Italic10")));
		cell.setColspan(3);
		cell.setLeading(leading);
		tab.addCell(cell);
	}
	docPdf.add(tab);

	// Garanzie Previste
	tab = new Table(4);
	tab.setTableFitsPage(true);
	tab.setWidth(100);
	tab.setWidths(new int[] {5, 35, 55, 5});
	tab.setPadding(2);
	tab.setSpacing(0);
	tab.setOffset(10);
		
	cell = new Cell(new Phrase("Riepilogo delle Garanzie generali", font("Bold10")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	cell.setColspan(4);
	tab.addCell(cell);
	if (pd.getGaranzie().getSize()>0) {
		cell = new Cell(new Phrase("N°", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Descrizione", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Ambito", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Opz.", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		for (int i = 0; i < listaCompletaGaranzie.getSize(); i++){
			Garanzia obj = (Garanzia)listaCompletaGaranzie.getElement(i);
			cell = new Cell(new Phrase(Integer.toString(i+1), font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase(obj.getDesGaranzia(), font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
			String tmp = "Prestata per tutti gli oggetti assicurati";
			if (pd.getGaranzie().getElement(obj.getCodGaranzia())==null) tmp = "Prestata per specifici Oggetti Assicurati";
			cell = new Cell(new Phrase(tmp, font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
			tmp = pd.BMA_FALSE;
			if (obj.isFacoltativa()) tmp = pd.BMA_TRUE;
			cell = new Cell(new Phrase(tmp, font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
		}
	}
	else {
		cell = new Cell(new Phrase("Non sono previste garanzie", font("Italic10")));
		cell.setColspan(4);
		cell.setLeading(leading);
		tab.addCell(cell);
	}
	docPdf.add(tab);

	// Oggetti Assicurati
	tab = new Table(4);
	tab.setTableFitsPage(true);
	tab.setWidth(100);
	tab.setWidths(new int[] {5, 35, 20, 40});
	tab.setPadding(2);
	tab.setSpacing(0);
	tab.setOffset(10);

	cell = new Cell(new Phrase("Riepilogo degli Oggetti Assicurati", font("Bold10")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	cell.setColspan(4);
	tab.addCell(cell);
	if (pd.getOggetti().getSize()>0) {
		cell = new Cell(new Phrase("N°", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Descrizione", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Tipo", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Note", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		cell.setLeading(leading);
		tab.addCell(cell);
		
		for (int i = 0; i < pd.getOggetti().getSize(); i++){
			OggettoAssicurazione obj = (OggettoAssicurazione)pd.getOggetti().getElement(i);
			cell = new Cell(new Phrase(Integer.toString(i + 1), font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase(obj.getDesOggetto(), font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase(obj.getDesTipoOggetto(), font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase(obj.getNotOggetto(), font("Normal08")));
			cell.setLeading(leading);
			tab.addCell(cell);
		}
	}
	else {
		cell = new Cell(new Phrase("Non sono previsti oggetti", font("Italic10")));
		cell.setColspan(4);
		cell.setLeading(leading);
		tab.addCell(cell);
	}
	docPdf.add(tab);
}
private void addSommario() throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	Table tab = new Table(5, 27);
	tab.setWidth(100);
	tab.setWidths(new int[] {20, 45, 10, 15, 10});
	tab.setPadding(2);
	tab.setSpacing(0);

	Cell cell;
	cell = new Cell(new Phrase("Sintesi argomenti", font("Bold12")));
	cell.setColspan(5);
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	
	cell = new Cell(new Phrase("Capitolo", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Sezione", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Elementi", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Ultima Variazione", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Pagina", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	// Info Generali
	cell = new Cell(new Phrase("Informazioni Generali", font("Normal08")));
	cell.setRowspan(8);
	tab.addCell(cell);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_IDENTIFICATIVI, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_DESCRIZIONI, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_RAGGRUPPAMENTI, tab);
	addRigaSommario(ARGOMENTO_PD_ELIMINATI, tab);
	addRigaSommario(ARGOMENTO_PD_SOSTITUIBILI, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_AGGREGATI, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_INFO_AGGIUNTIVE, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_ALLEGATI, tab);
	// Dati Tecnici
	cell = new Cell(new Phrase("Dati Tecnici", font("Normal08")));
	cell.setRowspan(4);
	tab.addCell(cell);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_CONDIZIONI, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_GARANZIE, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_OGGETTI, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_REGOLE_TARIFFA, tab);
	// Organizzazione
	cell = new Cell(new Phrase("Organizzazione", font("Normal08")));
	cell.setRowspan(5);
	tab.addCell(cell);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_STATI, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_RESPONSABILI, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_AUTORIZZAZIONI, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_NORMATIVE, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_MODULISTICA, tab);
	// Clientela
	cell = new Cell(new Phrase("Clientela", font("Normal08")));
	cell.setRowspan(5);
	tab.addCell(cell);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_RUOLI, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_IDONEITA, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_DOCUMENTI, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_OBIETTIVI, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_SEGMENTI, tab);
	// Commerciale
	cell = new Cell(new Phrase("Commerciale", font("Normal08")));
	cell.setRowspan(5);
	tab.addCell(cell);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_ARGOMENTI, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_OPZIONI, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_CONCORRENTI, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_CANALI, tab);
	addRigaSommario(pd.BMA_PD_ARGOMENTO_CONVENZIONI, tab);

	docPdf.add(tab);

}
private void addSostituibili(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (opzPdSost.getSize() == 0) {
		addTitolo("Non sono previsti prodotti sostituibili", "Italic10", 0);
		return;
	}
	Table tab = new Table(3);
	tab.setWidth(100);
	tab.setWidths(new int[] {20, 30, 50});
	tab.setPadding(2);
	tab.setSpacing(0);
	tab.setOffset(0);
	Cell cell;
	int leading = 8;
	
	for (int i = 0; i < opzPdSost.getSize(); i++){
		Opzione obj = (Opzione)opzPdSost.getElement(i);
		cell = new Cell(new Phrase("Opzione", font("Bold10")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesOpzione(), font("Bold10")));
		cell.setColspan(2);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Tipo", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesTipoOpzione(), font("Normal08")));
		cell.setColspan(2);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Facoltativa", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getFlgFacoltativa(), font("Normal08")));
		cell.setColspan(2);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Regola di Vendita", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesRegola(), font("Normal08")));
		cell.setColspan(2);
		tab.addCell(cell);
		cell = new Cell(new Phrase("Note", font("Italic08")));
		cell.setBackgroundColor(new java.awt.Color(225,225,225));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getNotOpzione(), font("Normal08")));
		cell.setColspan(2);
		tab.addCell(cell);
		if (obj.getProdottiOpzione().getSize()>0) {
			cell = new Cell(new Phrase("Prodotti in Opzione", font("Italic08")));
			cell.setLeading(leading);
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			tab.addCell(cell);
			cell = new Cell(new Phrase("Prodotto", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);
			cell = new Cell(new Phrase("Note", font("Italic08")));
			cell.setBackgroundColor(new java.awt.Color(225,225,225));
			cell.setLeading(leading);
			tab.addCell(cell);

			for (int j = 0; j < obj.getProdottiOpzione().getSize(); j++){
				ProdottoInOpzione obj2 = (ProdottoInOpzione)obj.getProdottiOpzione().getElement(j);
				cell = new Cell(new Phrase(obj2.getIndGruppo(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(obj2.getDesProdotto(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
				cell = new Cell(new Phrase(obj2.getNotProdotto(), font("Normal08")));
				cell.setLeading(leading);
				tab.addCell(cell);
			}
		}
	}
	docPdf.add(tab);
}
private void addStati(PdfWriter writer) throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	if (pd.getStati().getSize() == 0) {
		addTitolo("Non sono previsti stati del ciclo di vita", "Italic10", 0);
		return;
	}
	Table tab = new Table(4);
	tab.setTableFitsPage(true);
	tab.setWidth(100);
	tab.setWidths(new int[] {40, 40, 10, 10});
	tab.setPadding(2);
	tab.setSpacing(0);
	tab.setOffset(0);

	Cell cell;
	
	cell = new Cell(new Phrase("Tipo", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Stato", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Inizio", font("Italic08")));
	cell.setHorizontalAlignment(cell.ALIGN_CENTER);
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase("Utente", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);

	for (int i = 0; i < pd.getStati().getSize(); i++){
		Stato obj = (Stato)pd.getStati().getElement(i);
		cell = new Cell(new Phrase(obj.getDesTipoStato(), font("Normal08")));
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getDesStato(), font("Normal08")));
		tab.addCell(cell);
		cell = new Cell(new Phrase(jsp.getDataEsterna(obj.getDatValidita()), font("Normal08")));
		cell.setHorizontalAlignment(cell.ALIGN_CENTER);
		tab.addCell(cell);
		cell = new Cell(new Phrase(obj.getCodUtente(), font("Normal08")));
		tab.addCell(cell);
	}
	
	docPdf.add(tab);
		
}
private void addTitolo(String titolo, String font, int offset) throws DocumentException {
	Font fText = font(font);
	Table tab = new Table(1);
	tab.setWidth(100);
	tab.setWidths(new int[] {100});
	tab.setPadding(2);
	tab.setSpacing(0);
	tab.setOffset(offset);
	tab.setBorder(0);
	Cell cell = new Cell(new Phrase(titolo, fText));
	cell.setBorder(0);
	tab.addCell(cell);
	docPdf.add(tab);
}
protected void clearContesto() {
	for (int i = 0; i < 5; i++){
		keyContesto[i] = "";
		valContesto[i] = "";
	}
}
private void completaGaranzie() {
	for (int i = 0; i < pd.getGaranzie().getSize(); i++){
		Garanzia obj = (Garanzia)pd.getGaranzie().getElement(i);
		listaCompletaGaranzie.add(obj);
	}
	for (int i = 0; i < pd.getOggetti().getSize(); i++){
		OggettoAssicurazione obj = (OggettoAssicurazione)pd.getOggetti().getElement(i);
		for (int j = 0; j < obj.getGaranzie().getSize(); j++){
			Garanzia obj2 = (Garanzia)obj.getGaranzie().getElement(j);
			if (listaCompletaGaranzie.getElement(obj2.getChiave())==null) {
				listaCompletaGaranzie.add(obj2);
			}
		}
	}
}
public String esegui(BmaJdbcTrx trx) throws BmaException {

	testi.fromXmlFile(userConfig.getConfigPath() +  "testischeda.xml");
	float vPos = 860;
	float lCap = 10;
	float lSez = 30;
	// Lettura dei parametri di attivazione
	String codSocieta = input.getSocieta();
	String codProdotto = input.getInfoServizio("codProdotto");
	String rifVersione = input.getInfoServizio("rifVersione");

	// Prepara il Bean Prodotto
	BmaGestoreProdotto gp = new BmaGestoreProdotto();
	gp.jModel = jModel;
	BmaJsp jsp = new BmaJsp();
	String datRiferimento = jsp.getData();
	pd = gp.caricaProdottoCompleto(trx, codSocieta, codProdotto, rifVersione, datRiferimento);
	completaGaranzie();
	smistaOpzioni();
	// Prepara il Documento
	PdfWriter writer;
	Chapter capitolo;
	Paragraph cTit;
	try {
		
		writer = PdfWriter.getInstance(docPdf, outputStream);
		BmaSchedaPdfHelper pdfHelper = new BmaSchedaPdfHelper(this);
		writer.setPageEvent(pdfHelper);
		String argomento = "Sintesi";
		String prefix = "";
		setCapitoloContesto(argomento);
		docPdf.open();
		PdfContentByte cb = writer.getDirectContent();
		PdfOutline root = cb.getRootOutline();
		primaPagina();
		PdfOutline cap0 = new PdfOutline(root, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addSommario();
		
		// Modulo Base
		prefix = "Capitolo 1 - ";
		argomento = formatArgomento(pd.BMA_PD_MODULO_BASE);
		setCapitoloContesto(argomento);
		docPdf.newPage();
		PdfOutline cap0100 = new PdfOutline(root, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold14", 0);
		
		prefix = "1.1 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_IDENTIFICATIVI);
		setSezioneContesto(argomento);
		PdfOutline cap0101 = new PdfOutline(cap0100, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
//		docPdf.add(new Paragraph(testi.getString(argomento), font("Normal10")));		
		addIdentificativi(writer);

		prefix = "1.2 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_DESCRIZIONI);
		setSezioneContesto(argomento);
		PdfOutline cap0102 = new PdfOutline(cap0100, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addDescrizioni(writer);

		prefix = "1.3 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_RAGGRUPPAMENTI);
		setSezioneContesto(argomento);
		PdfOutline cap0103 = new PdfOutline(cap0100, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addRaggruppamenti(writer);

		prefix = "1.4 ";
		argomento = formatArgomento(ARGOMENTO_PD_ELIMINATI);
		setSezioneContesto(argomento);
		PdfOutline cap0104 = new PdfOutline(cap0100, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addEliminati(writer);
		
		prefix = "1.5 ";
		argomento = formatArgomento(ARGOMENTO_PD_SOSTITUIBILI);
		setSezioneContesto(argomento);
		PdfOutline cap0105 = new PdfOutline(cap0100, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addSostituibili(writer);

		prefix = "1.6 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_AGGREGATI);
		setSezioneContesto(argomento);
		PdfOutline cap0106 = new PdfOutline(cap0100, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addRegoleAggregati(writer);
		
		prefix = "1.7 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_INFO_AGGIUNTIVE);
		setSezioneContesto(argomento);
		PdfOutline cap0107 = new PdfOutline(cap0100, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addInfoAggiuntive(writer);
		
		prefix = "1.8 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_ALLEGATI);
		setSezioneContesto(argomento);
		PdfOutline cap0108 = new PdfOutline(cap0100, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addAllegati(writer);
		
		// Modulo Dati Tecnici	
		prefix = "Capitolo 2 - ";
		argomento = formatArgomento(pd.BMA_PD_MODULO_DATI_TECNICI);
		setCapitoloContesto(argomento);
		docPdf.newPage();
		PdfOutline cap0200 = new PdfOutline(root, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold14", 0);

		prefix = "2.1 ";
		argomento = "Sintesi";
		setSezioneContesto(argomento);
		PdfOutline cap0201 = new PdfOutline(cap0200, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addSintesiDatiTecnici(writer);

		prefix = "2.2 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_CONDIZIONI);
		setSezioneContesto(argomento);
		docPdf.newPage();
		PdfOutline cap0202 = new PdfOutline(cap0200, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold14", 0);
		addListaCondizioni(prefix.trim(), null, null, "GENERALI", writer);

		prefix = "2.3 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_GARANZIE);
		setSezioneContesto(argomento);
		docPdf.newPage();
		PdfOutline cap0203 = new PdfOutline(cap0200, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold14", 0);
		addListaGaranzie(prefix.trim(), null, writer);

		prefix = "2.4 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_OGGETTI);
		setSezioneContesto(argomento);
		docPdf.newPage();
		PdfOutline cap0204 = new PdfOutline(cap0200, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold14", 0);
		addOggetti(prefix.trim(), writer);

		prefix = "2.5 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_REGOLE_TARIFFA);
		setSezioneContesto(argomento);
		docPdf.newPage();
		PdfOutline cap0205 = new PdfOutline(cap0200, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold14", 0);
		addRegoleTariffa(null, writer);

		// Modulo Organizzazione	
		prefix = "Capitolo 3 - ";
		argomento = formatArgomento(pd.BMA_PD_MODULO_ORGANIZZAZIONE);
		setCapitoloContesto(argomento);
		docPdf.newPage();
		PdfOutline cap0300 = new PdfOutline(root, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold14", 0);
		
		prefix = "3.1 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_STATI);
		setSezioneContesto(argomento);
		PdfOutline cap0301 = new PdfOutline(cap0300, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addStati(writer);

		prefix = "3.2 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_RESPONSABILI);
		setSezioneContesto(argomento);
		PdfOutline cap0302 = new PdfOutline(cap0300, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addResponsabili(writer);

		prefix = "3.3 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_AUTORIZZAZIONI);
		setSezioneContesto(argomento);
		PdfOutline cap0303 = new PdfOutline(cap0300, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addAutorizzazioni(writer);

		prefix = "3.4 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_NORMATIVE);
		setSezioneContesto(argomento);
		PdfOutline cap0304 = new PdfOutline(cap0300, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addNormative(writer);

		prefix = "3.5 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_MODULISTICA);
		setSezioneContesto(argomento);
		PdfOutline cap0305 = new PdfOutline(cap0300, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addModuli(writer);
		
		// Modulo Clientela	
		prefix = "Capitolo 4 - ";
		argomento = formatArgomento(pd.BMA_PD_MODULO_CLIENTELA);
		setCapitoloContesto(argomento);
		docPdf.newPage();
		PdfOutline cap0400 = new PdfOutline(root, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold14", 0);
		
		prefix = "4.1 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_RUOLI);
		setSezioneContesto(argomento);
		PdfOutline cap0401 = new PdfOutline(cap0400, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addRuoliCliente(writer);
		
		prefix = "4.2 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_IDONEITA);
		setSezioneContesto(argomento);
		PdfOutline cap0402 = new PdfOutline(cap0400, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addListaCondizioni(prefix.trim(), null, null, "IDONEITA", writer);

		prefix = "4.3 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_DOCUMENTI);
		setSezioneContesto(argomento);
		PdfOutline cap0403 = new PdfOutline(cap0400, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addDocumenti(writer);
		
		prefix = "4.4 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_OBIETTIVI);
		setSezioneContesto(argomento);
		PdfOutline cap0404 = new PdfOutline(cap0400, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addObiettivi(writer);
		
		prefix = "4.5 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_SEGMENTI);
		setSezioneContesto(argomento);
		PdfOutline cap0405 = new PdfOutline(cap0400, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addSegmenti(writer);
		
		// Modulo Commerciale
		prefix = "Capitolo 5 - ";
		argomento = formatArgomento(pd.BMA_PD_MODULO_COMMERCIALE);
		setCapitoloContesto(argomento);
		docPdf.newPage();
		PdfOutline cap0500 = new PdfOutline(root, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold14", 0);
		
		prefix = "5.1 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_ARGOMENTI);
		setSezioneContesto(argomento);
		PdfOutline cap0501 = new PdfOutline(cap0500, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addArgomenti(writer);
		
		prefix = "5.2 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_OPZIONI);
		setSezioneContesto(argomento);
		PdfOutline cap0502 = new PdfOutline(cap0500, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addOpzioni(writer);
		
		prefix = "5.3 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_CONCORRENTI);
		setSezioneContesto(argomento);
		PdfOutline cap0503 = new PdfOutline(cap0500, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addConcorrenti(writer);
		
		prefix = "5.4 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_CANALI);
		setSezioneContesto(argomento);
		PdfOutline cap0504 = new PdfOutline(cap0500, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold12", 10);
		addCanali(writer);
		
		prefix = "5.5 ";
		argomento = formatArgomento(pd.BMA_PD_ARGOMENTO_CONVENZIONI);
		setSezioneContesto(argomento);
		docPdf.newPage();
		PdfOutline cap0505 = new PdfOutline(cap0500, new PdfDestination(PdfDestination.XYZ, 0, vPos, 0), argomento);
		addTitolo(prefix + argomento, "Bold14", 0);
		addConvenzioni(writer);
		
		docPdf.close();
		
	} 
	catch (DocumentException de) {
		if (docPdf.isOpen()) docPdf.close();
		throw new BmaException(BMA_ERR_IO__FILE, "Document Pdf: ", de.getMessage(), this);
	}
	return null;
}
protected Font font(String name) {
	Font f = (Font)fontSet.get(name);
	if (f==null) f = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, Color.black);
	return f;
}
private String formatArgomento(String argomento) {
	return argomento.replace('_', ' ');	
}
public String getChiave() {
	return null;
}
private String getDesTipoCliente(String codTipo) {
	if (codTipo.equals("P")) return "Persona Fisica";
	else if (codTipo.equals("G")) return "Persona Giuridica";
	else if (codTipo.equals("A")) return "Ente Privato";
	else if (codTipo.equals("B")) return "Ente Pubblico";
	else if (codTipo.equals("C")) return "Ente Religioso";
	return codTipo;
}
/**
 * getXmlTag method comment.
 */
protected String getXmlTag() {
	return null;
}
private void primaPagina() throws DocumentException {
	BmaJsp jsp = new BmaJsp();
	Table tab = new Table(2);
	tab.setWidth(100);
	tab.setWidths(new int[] {20, 80});
	tab.setPadding(2);
	tab.setSpacing(0);

	Cell cell;
	cell = new Cell(new Phrase("Situazione", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	String tmp = pd.getDesStato() + " dal " + jsp.getDataEsterna(pd.getDatStato());
	cell = new Cell(new Phrase(tmp, font("Bold08")));
	tab.addCell(cell);
	
	cell = new Cell(new Phrase("Emesso da", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase(pd.getDesEmittente(), font("Bold08")));
	tab.addCell(cell);
	
	cell = new Cell(new Phrase("Versione", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	tmp = "Chiusa";
	if (pd.getFlgAperta().equals(BMA_TRUE)) tmp = "Aperta";		
	tmp = pd.getRifVersione() + " - " + tmp;
	cell = new Cell(new Phrase(tmp, font("Bold08")));
	tab.addCell(cell);
	
	cell = new Cell(new Phrase("Validità", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	tmp = "Valida dal " + jsp.getDataEsterna(pd.getDatVersione()) +
				" al " + jsp.getDataEsterna(pd.getDatFineVersione());
	cell = new Cell(new Phrase(tmp, font("Bold08")));
	tab.addCell(cell);

	cell = new Cell(new Phrase("Note", font("Italic08")));
	cell.setBackgroundColor(new java.awt.Color(225,225,225));
	tab.addCell(cell);
	cell = new Cell(new Phrase(pd.getNotProdotto(), font("Normal08")));
	tab.addCell(cell);

	docPdf.add(tab);

}
private void setArgomentoContesto(int livello, String nome, String valore) {
	keyContesto[livello] = nome;
	valContesto[livello] = valore;
	for (int i = livello + 1; i < 5; i++){
		keyContesto[i] = "";
		valContesto[i] = "";
	}
}
private void setCapitoloContesto(String valore) {
	keyContesto[0] = "Capitolo";
	valContesto[0] = valore;
	for (int i = 1; i < 5; i++){
		keyContesto[i] = "";
		valContesto[i] = "";
	}
}
private void setSezioneContesto(String valore) {
	keyContesto[1] = "Sezione";
	valContesto[1] = valore;
	for (int i = 2; i < 5; i++){
		keyContesto[i] = "";
		valContesto[i] = "";
	}
}
private void smistaOpzioni() {
	opzPdSost.setSize(0);
	opzPdElim.setSize(0);
	opzAltre.setSize(0);
	for (int i = 0; i < pd.getOpzioni().getSize(); i++){
		Opzione opzione = (Opzione)pd.getOpzioni().getElement(i);
		if (opzione.getCodOpzione().equals(OPZPD_ELIMINATI)) opzPdElim.add(opzione);
		else if (opzione.getCodOpzione().equals(OPZPD_SOSTITUIBILI)) opzPdSost.add(opzione);
		else opzAltre.add(opzione);
	}
}
}
