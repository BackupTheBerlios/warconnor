package it.bma.bim;
// JAVA
import java.util.*;
import java.text.*;
// XML
import org.w3c.dom.*;
// BMA
import it.bma.comuni.*;
import it.bma.web.*;				

public class DriverOperazione extends BmaObject {
	private final long TIME_STEP = 5;
	private JdbcModel jModel = new JdbcModel();
	private BmaJdbcTrx jTrx = null;
	private Document guida = null;
	public DriverOperazione() {
		super();
	}
	// Implementazione dei metodi astratti
	public String getChiave() { return ""; }
	public String getXmlTag() {	return getClassName(); }
	// Accessors
	public JdbcModel getJModel() { 
		return jModel; 
	}
	public void setJModel(JdbcModel model) {
		jModel = model;
	}
	public void setGuida(Document newGuida) {
		guida = newGuida;
	}
	// Main di elaborazione e registrazione di una nuova operazione
	public void creaOperazione(BmaJdbcTrx jTrx, Hashtable valori, Element nodoLog) throws BmaException {
		this.jTrx = jTrx;
		// Prepara Guida del codice operazione
		String codOperazione = (String)valori.get("COD_OPERAZIONE");
		Element guidaRoot = guida.getDocumentElement();
		Element guidaOpe = getElementByAttribute(guidaRoot, "operazione", "Codice", codOperazione);
		if (guidaOpe==null) throw new BmaException("Manca guida per: " + codOperazione, "");
		// Crea elemento operazione per il log
		Element logOperazione = getNewElement(nodoLog, "Operazione");
		// Inizializza l'operazione
		String pktOperazione = getTimeStamp();
		valori.put("PKT_OPERAZIONE", pktOperazione);
		// Registra componenti operazione
		makeOperazione(guidaOpe, valori, logOperazione);
		makeIva(guidaOpe, valori, logOperazione);
		makeRate(guidaOpe, valori, logOperazione);
		makeMovimenti(guidaOpe, valori, logOperazione);
		makeCompetenze(guidaOpe, valori, logOperazione);
		makeProtocolli(guidaOpe, valori, logOperazione);
		makePartite(guidaOpe, valori, logOperazione);
	}
	private void makeOperazione(Element guidaOpe, Hashtable valori, Element nodoLog) throws BmaException {
		valori.put("COD_ESERCIZIO", getEsercizio((String)valori.get("DAT_OPERAZIONE")));
		valori.put("COD_PERIODO", getPeriodo((String)valori.get("DAT_OPERAZIONE")));
		Element regola;
		String dato = "";
		
		regola = (Element)guidaOpe.getElementsByTagName("tipo").item(0);
		dato = regolaBase(regola, valori);
		valori.put("TIP_DOCUMENTO", dato);
		
		regola = (Element)guidaOpe.getElementsByTagName("rif_documento").item(0);
		dato = regolaBase(regola, valori);
		valori.put("RIF_DOCUMENTO", dato);
		
		regola = (Element)guidaOpe.getElementsByTagName("dat_documento").item(0);
		dato = regolaBase(regola, valori);
		valori.put("DAT_DOCUMENTO", dato);
		
		regola = (Element)guidaOpe.getElementsByTagName("soggetto").item(0);
		dato = regolaBase(regola, valori);
		valori.put("COD_SOGGETTO", dato);
		
		regola = (Element)guidaOpe.getElementsByTagName("divisa").item(0);
		dato = regolaBase(regola, valori);
		valori.put("COD_DIVISA", dato);
		
		regola = (Element)guidaOpe.getElementsByTagName("ritenuta").item(0);
		dato = regolaBase(regola, valori);
		valori.put("IMP_RITENUTA", dato);
		
		insert("OPERAZIONI", valori, nodoLog);
	}
	private void makeIva(Element guidaOpe, Hashtable valori, Element nodoLog) throws BmaException {
		NodeList guideIva = guidaOpe.getElementsByTagName("iva");
		for (int i=0;i<guideIva.getLength();i++) {
			Element guidaIva = (Element)guideIva.item(i);
			Element regola;
			String dato = "";
			
			regola = (Element)guidaIva.getElementsByTagName("codice").item(0);
			dato = regolaBase(regola, valori);
			valori.put("COD_IVA", dato);
			
		}
	}
	private void makeRate(Element guidaOpe, Hashtable valori, Element nodoLog) throws BmaException {
		NodeList guideRata = guidaOpe.getElementsByTagName("rata");
		for (int i=0;i<guideRata.getLength();i++) {
			Element guidaRata = (Element)guideRata.item(i);
		Element regola;
		String dato = "";
			System.out.print(i);
			System.out.print(guidaRata.hasAttributes());
			System.out.println(guidaRata.hasChildNodes());
		}
	}
	private void makeMovimenti(Element guidaOpe, Hashtable valori, Element nodoLog) throws BmaException {
		double importoQuadratura = 0;
		int numeroMovimenti = 0;
		String pktMovimento = (String)valori.get("PKT_OPERAZIONE");
		NodeList guideCau = guidaOpe.getElementsByTagName("causale");
		for (int i=0;i<guideCau.getLength();i++) {
			Element guidaCau = (Element)guideCau.item(i);
			valori.put("PKT_MOVIMENTO", pktMovimento); 
			pktMovimento = getTimeStamp(pktMovimento);
			valori.put("DAT_REGISTRAZIONE", (String)valori.get("DAT_OPERAZIONE"));
			valori.put("IMP_CAMBIO", "1.000");
			
			String codCausale = guidaCau.getAttribute("Codice");
			if (codCausale==null) throw new BmaException("Causale non specificata", "");
			valori.put("COD_CAUSALE", codCausale);
			
			String segno = guidaCau.getAttribute("Segno");
			if (segno==null) throw new BmaException("Segno non specificato", "");
			if (segno.equals("D")) segno = "";
			else if (segno.equals("A")) segno = "-";
			else throw new BmaException("Segno errato", segno);
			
			Element regola;
			String dato = "";
		
			regola = (Element)guidaCau.getElementsByTagName("tipo").item(0);
			dato = regolaBase(regola, valori);
			valori.put("FLG_TIPOMOV", dato);
			
			regola = (Element)guidaCau.getElementsByTagName("ente").item(0);
			dato = regolaBase(regola, valori);
			valori.put("COD_ENTE", dato);
			
			regola = (Element)guidaCau.getElementsByTagName("prodotto").item(0);
			dato = regolaBase(regola, valori);
			valori.put("COD_PRODOTTO", dato);
			
			regola = (Element)guidaCau.getElementsByTagName("valuta").item(0);
			dato = regolaBase(regola, valori);
			valori.put("DAT_VALUTA", dato);
			
			regola = (Element)guidaCau.getElementsByTagName("conto").item(0);
			dato = regolaBase(regola, valori);
			valori.put("COD_NATURA", dato);
			
			regola = (Element)guidaCau.getElementsByTagName("importo").item(0);
			dato = regolaBase(regola, valori);
			valori.put("IMP_MOVIMENTO", segno + dato);
			
			regola = (Element)guidaCau.getElementsByTagName("descrizione").item(0);
			dato = regolaBase(regola, valori);
			valori.put("DES_MOVIMENTO", dato);
				
			// Inserisce solo movimenti con importo diverso da zero
			String v = (String)valori.get("IMP_MOVIMENTO");
			if (v.trim().length()==0) v = "0";
			double n = Double.parseDouble(v);
			if (n!=0) {
				importoQuadratura = importoQuadratura + n;
				numeroMovimenti++;
				insert("MOVIMENTI", valori, nodoLog);
			}
		}
		// Controllo Movimenti e Quadratura
		if (numeroMovimenti<2) throw new BmaException("Operazione zoppa", "Numero Movimenti=" + Integer.toString(numeroMovimenti));
		if (importoQuadratura!=0) throw new BmaException("Operazione non quadrata", "Saldo=" + Double.toString(importoQuadratura));
	}
	private void makeCompetenze(Element guidaOpe, Hashtable valori, Element nodoLog) throws BmaException {
	}
	private void makeProtocolli(Element guidaOpe, Hashtable valori, Element nodoLog) throws BmaException {
	}
	private void makePartite(Element guidaOpe, Hashtable valori, Element nodoLog) throws BmaException {
	}
	// Motore Regole
	private String regolaBase(Element regola, Hashtable valori) throws BmaException {
		if (regola==null) throw new BmaException("Regola assente", "");
		if (!regola.hasAttributes()) return "";
		String desRegola = regola.getAttribute("Regola");
		String desValore = regola.getAttribute("Valore");
		if (desRegola==null) throw new BmaException("Regola non specificata", "");
		if (desRegola.equals("Costante")) return desValore;
		else if (desRegola.equals("Input")) return (String)valori.get(desValore);
		else if (desRegola.equals("TextMerge")) {
			String s = "";
			Vector v = getChildElements(regola);
			for (int j=0;j<v.size();j++) {
				Element e = (Element)v.elementAt(j);
				if (e.getNodeName().equals("text")) {
					s = s + e.getChildNodes().item(0).getNodeValue();
				}
				else if (e.getNodeName().equals("input")) {
					String x = e.getChildNodes().item(0).getNodeValue();
					s = s + (String)valori.get(x);
				}
				else throw new BmaException("Variabile text-merge non prevista", e.getNodeName());
			}
			return s;
		}
		else throw new BmaException("Regola non gestita", desRegola);
	}
	// DOM Document Helpers
	private Element getNewElement(Element parent, String tag) {
		Document document = parent.getOwnerDocument();
		Element e = document.createElement(tag);
		parent.appendChild(e);
		return e;
	}
	private Element getElementByAttribute(Element parent, String tag, String attr, String value) {
		NodeList list = parent.getElementsByTagName(tag);
		for (int i=0;i<list.getLength();i++) {
			Element e = (Element)list.item(i);
			String c = e.getAttribute(attr);
			if (c.equals(value)) return e;
		}
		return null;
	}
	private Vector getChildElements(Element parent) {
		Vector v = new Vector();
		NodeList list = parent.getChildNodes();
		for (int i=0;i<list.getLength();i++) {
			Node n = list.item(i);
			if (n.getNodeType()==n.ELEMENT_NODE) v.add(n);
		}
		return v;
	}
	private void addDomTextElement(Element parent, String tag, String value) {
		Element nodo = parent.getOwnerDocument().createElement(tag);
		parent.appendChild(nodo);
		Text testo = parent.getOwnerDocument().createTextNode(value);
		nodo.appendChild(testo);
	}
	// Accesso al database
	protected void insert(String tabella, Hashtable valori, Element nodoLog) throws BmaException {
		BmaDataTable table = jModel.getDataTable(jTrx, tabella);
		table.setValori(valori);
//		jTrx.eseguiSqlUpdate(table.getSqlInsert());
		
		// Aggiorna il documento di log
		Element logTabella = getNewElement(nodoLog, tabella);
		for (int i=0; i<table.getColonne().size();i++) {
			BmaDataColumn c = (BmaDataColumn)table.getColonne().elementAt(i);
			String n = c.getNome();
			addDomTextElement(logTabella, n, c.getValore());
		}
	}
	// Utility del motore regole
	public String getTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS"); 
		return sdf.format(new Date());
	}
	public String getTimeStamp(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS"); 
		Date d = new Date();
		d.setTime(time + TIME_STEP);
		return sdf.format(d);
	}
	public String getTimeStamp(String oldTime) throws BmaException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS"); 
		try {
			Date d = sdf.parse(oldTime);
			Date dd = new Date(d.getTime() + TIME_STEP);
			return sdf.format(dd);
		}
		catch (ParseException pe) {
			throw new BmaException("Data Errata: " + oldTime, pe.getMessage()); 
		}
	}
	public String getPeriodo(String datOperazione) throws BmaException {
		Date d = parseDate(datOperazione);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		String m = Integer.toString(gc.get(gc.MONTH)+1);
		if (m.length()==1) m = "0" + m;
		return m;
	}
	public String getEsercizio(String datOperazione) throws BmaException {
		Date d = parseDate(datOperazione);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		String m = Integer.toString(gc.get(gc.YEAR));
		return m;
	}
	public Date parseDate(String myDate) throws BmaException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = sdf.parse(myDate);
			return d;
		}
		catch (ParseException pe) {
			throw new BmaException("Data Errata: " + myDate, pe.getMessage()); 
		}
	}
	public double parseImporto(String myImporto) throws BmaException {
		try {
			double d = Double.parseDouble(myImporto);
			return d;
		}
		catch (NumberFormatException nf) {
			throw new BmaException("Importo Errato: " + myImporto, nf.getMessage()); 
		}
	}
}
