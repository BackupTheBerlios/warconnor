package it.bma.bim;
// JAVA
import java.util.*;
import java.text.*;
import java.math.BigDecimal;
// XML
import org.w3c.dom.*;
// BMA
import it.bma.comuni.*;
import it.bma.web.*;				

public class DriverOperazione extends BmaObject {
	private final long TIME_STEP = 1;
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
		// Aggiorna eventuali riferimenti al documento generati dalla protocollazione
		String rifDocumento = (String)valori.get("RIF_DOCUMENTO");
		if (rifDocumento.trim().length()>0) {
			Element eDoc = (Element)logOperazione.getElementsByTagName("RIF_DOCUMENTO").item(0);
			eDoc.getFirstChild().setNodeValue(rifDocumento);
			eDoc = (Element)logOperazione.getElementsByTagName("DAT_DOCUMENTO").item(0);
			eDoc.getFirstChild().setNodeValue((String)valori.get("DAT_DOCUMENTO"));
		}
		// Aggiorna il Database
		insertDaLog(logOperazione);
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
		
		insertLog("OPERAZIONI", valori, nodoLog);
	}
	private void makeIva(Element guidaOpe, Hashtable valori, Element nodoLog) throws BmaException {
		NodeList guideIva = guidaOpe.getElementsByTagName("iva");
		for (int i=0;i<guideIva.getLength();i++) {
			Element guidaIva = (Element)guideIva.item(i);
			if (guidaIva.hasChildNodes()) {
				Element regola;
				String dato = "";

				regola = (Element)guidaIva.getElementsByTagName("imponibile").item(0);
				dato = regolaBase(regola, valori);
				double control = Double.parseDouble(dato);
				valori.put("IMP_IMPONIBILE", dato);

				regola = (Element)guidaIva.getElementsByTagName("imp_iva").item(0);
				dato = regolaBase(regola, valori);
				control = control + Double.parseDouble(dato);
				valori.put("IMP_IVA", dato);

				if (control>0) {
					regola = (Element)guidaIva.getElementsByTagName("codice").item(0);
					dato = regolaBase(regola, valori);
					valori.put("COD_IVA", dato);

					insertLog("IVAOPE", valori, nodoLog);
				}
			}
		}
	}
	private void makeRate(Element guidaOpe, Hashtable valori, Element nodoLog) throws BmaException {
		NodeList guideRata = guidaOpe.getElementsByTagName("rata");
		for (int i=0;i<guideRata.getLength();i++) {
			Element guidaRata = (Element)guideRata.item(i);
			if (guidaRata.hasChildNodes()) {
				valori.put("NUM_GG_MORA", "0");

				Element regola;
				String dato = "";

				regola = (Element)guidaRata.getElementsByTagName("scadenza").item(0);
				dato = regolaBase(regola, valori);
				valori.put("DAT_SCADENZA", dato);

				regola = (Element)guidaRata.getElementsByTagName("importo").item(0);
				dato = regolaBase(regola, valori);
				valori.put("IMP_SCADENZA", dato);

				regola = (Element)guidaRata.getElementsByTagName("modo_pagamento").item(0);
				dato = regolaBase(regola, valori);
				valori.put("DES_MODO_ESI", dato);

				insertLog("RATEFATTURA", valori, nodoLog);
			}
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
				
			// Inserisce solo movimenti con importo arrotondato diverso da zero
			String v = (String)valori.get("IMP_MOVIMENTO");
			if (v.trim().length()==0) v = "0";
			double n = parseImporto(v);
			if (n!=0) {
				valori.put("IMP_MOVIMENTO", Double.toString(n));
				importoQuadratura = importoQuadratura + n;
				numeroMovimenti++;
				insertLog("MOVIMENTI", valori, nodoLog);
		
				makeProtocolli(guidaCau, valori, nodoLog);
				makePartite(guidaCau, valori, nodoLog);
				makeCompetenze(guidaCau, valori, nodoLog);
				
			}
		}
		// Controllo Movimenti e Quadratura
		if (numeroMovimenti<2) throw new BmaException("Operazione zoppa", "Numero Movimenti=" + Integer.toString(numeroMovimenti));
		importoQuadratura = round(importoQuadratura, 2);
		if (importoQuadratura!=0) throw new BmaException("Operazione non quadrata", "Saldo=" + Double.toString(importoQuadratura));
	}
	private void makeCompetenze(Element guidaMov, Hashtable valori, Element nodoLog) throws BmaException {
		NodeList guideComp = guidaMov.getElementsByTagName("competenza");
		for (int i=0;i<guideComp.getLength();i++) {
			Element guidaComp = (Element)guideComp.item(i);
			if (guidaComp.hasChildNodes()) {
				Element regola;
				String dato = "";

				regola = (Element)guidaComp.getElementsByTagName("inizio").item(0);
				dato = regolaBase(regola, valori);
				valori.put("DAT_INIZIO_COMP", dato);

				regola = (Element)guidaComp.getElementsByTagName("fine").item(0);
				dato = regolaBase(regola, valori);
				valori.put("DAT_FINE_COMP", dato);

				insertLog("COMPETENZE", valori, nodoLog);
			}
		}
	}
	private void makeProtocolli(Element guidaMov, Hashtable valori, Element nodoLog) throws BmaException {
		NodeList guideProt = guidaMov.getElementsByTagName("protocollo");
		for (int i=0;i<guideProt.getLength();i++) {
			Element guidaProt = (Element)guideProt.item(i);
			
			String dato = regolaValore(guidaProt, "Auto");
			if (dato!=null) {
				valori.put("COD_TIPO_PROT", dato);
				valori.put("NUM_PROTOCOLLO", maxProtocollo(valori));
				valori.put("DAT_PROTOCOLLO", (String)valori.get("DAT_REGISTRAZIONE"));
				
				insertLog("NUMERI_PROT", valori,  nodoLog);
				String codEsercizio = (String)valori.get("COD_ESERCIZIO");
				String numProtocollo = (String)valori.get("NUM_PROTOCOLLO");
				valori.put("RIF_DOCUMENTO", codEsercizio.substring(2) + "/" + numProtocollo);
				valori.put("DAT_DOCUMENTO", (String)valori.get("DAT_REGISTRAZIONE"));
			}
		}
	}
	private void makePartite(Element guidaMov, Hashtable valori, Element nodoLog) throws BmaException {
		NodeList guidePart = guidaMov.getElementsByTagName("partita");
		for (int i=0;i<guidePart.getLength();i++) {
			Element guidaPart = (Element)guidePart.item(i);
			
			String dato = regolaValore(guidaPart, "Crea");
			if (dato!=null) {
				valori.put("PKT_PARTITA_RIF", (String)valori.get("PKT_MOVIMENTO"));
				valori.put("RIF_PARTITA", (String)valori.get(dato));
				valori.put("FLG_CHIUSURA", "N");
				valori.put("DAT_ACCOPPIAMENTO", (String)valori.get("DAT_REGISTRAZIONE"));
				insertLog("PARTITE", valori,  nodoLog);
			}
			dato = regolaValore(guidaPart, "Abbina");
			if (dato!=null) {
				valori.put("RIF_PARTITA", (String)valori.get(dato));
				String pktPartitaRif = partitaRiferimento(valori);
				if (pktPartitaRif==null) pktPartitaRif = (String)valori.get("PKT_MOVIMENTO");
				valori.put("PKT_PARTITA_RIF", pktPartitaRif);
				valori.put("FLG_CHIUSURA", "N");
				insertLog("PARTITE", valori,  nodoLog);
			}
		}
	}
	// Motore Regole
	private String regolaValore(Element regola, String nome) throws BmaException {
		if (regola==null) throw new BmaException("Regola assente", "");
		if (!regola.hasAttributes()) return null;
		String desRegola = regola.getAttribute("Regola");
		if (!desRegola.equals(nome)) return null;
		return regola.getAttribute("Valore");
	}
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
	private String partitaRiferimento(Hashtable valori) throws BmaException {
		String rifPartita = (String)valori.get("RIF_PARTITA");
		if (rifPartita==null) throw new BmaException("Riferimento partita non specificato", "");
		String codNatura = (String)valori.get("COD_NATURA");
		if (codNatura==null) throw new BmaException("Conto partita non specificato", "");
		String sql = "";
		sql = "SELECT A.RIF_PARTITA " +
					" FROM PARTITE A, MOVIMENTI B " +
					" WHERE A.PKT_MOVIMENTO = B.PKT_MOVIMENTO " +
					" AND		B.COD_NATURA='" + codNatura + "' " +
					" AND		A.RIF_PARTITA='" + rifPartita + "'";
		Vector dati = jTrx.eseguiSqlSelect(sql);
		if (dati.size()!=1) return null;
		dati = (Vector)dati.elementAt(0);
		return (String)dati.elementAt(0);		
	}
	private String maxProtocollo(Hashtable valori) throws BmaException {
		String codTipoProt = (String)valori.get("COD_TIPO_PROT");
		if (codTipoProt==null) throw new BmaException("Protocollo non specificato", "");
		String codEsercizio = (String)valori.get("COD_ESERCIZIO");
		if (codEsercizio==null) throw new BmaException("Esercizio non specificato", "");
		String sql = "";
		sql = "SELECT MAX(NUM_PROTOCOLLO) " +
					" FROM	NUMERI_PROT " +
					" WHERE	COD_TIPO_PROT='" + codTipoProt + "' " +
					" AND		COD_ESERCIZIO='" + codEsercizio + "' ";
		Vector dati = jTrx.eseguiSqlSelect(sql);
		if (dati.size()!=1) throw new BmaException("Errore nel calcolo del numero protocollo", "");
		dati = (Vector)dati.elementAt(0);
		String s = (String)dati.elementAt(0);
		if (s==null || s.length()==0) return "1";
		int n = Integer.parseInt(s);
		return Integer.toString(n + 1);
	}
	private void insertDaLog(Element nodoLog) throws BmaException {
		Vector list = getChildElements(nodoLog);
		for (int i=0;i<list.size();i++) {
			Element eTab = (Element)list.elementAt(i);
			BmaDataTable tabella = jModel.getDataTable(eTab.getNodeName());
			for (int j=0;j<tabella.getColonne().size();j++) {
				BmaDataColumn col = (BmaDataColumn)tabella.getColonne().elementAt(j);
				Element eCol = (Element)eTab.getElementsByTagName(col.getNome()).item(0);
				String s = eCol.getFirstChild().getNodeValue();
				col.setValore(s);
			}
			jTrx.eseguiSqlUpdate(tabella.getSqlInsert());
		}
	}
	private void insertLog(String tabella, Hashtable valori, Element nodoLog) throws BmaException {
		BmaDataTable table = jModel.getDataTable(jTrx, tabella);
		table.setValori(valori);
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
	public double round(double v, int scale) {
		BigDecimal bDec = new BigDecimal(v);
		bDec = bDec.setScale(scale, bDec.ROUND_HALF_UP);
		return bDec.doubleValue();
	}
	public double parseImporto(String myImporto) throws BmaException {
		try {
			BigDecimal bd = new BigDecimal(myImporto);
			bd = bd.setScale(2, bd.ROUND_HALF_UP);
			return bd.doubleValue();
		}
		catch (NumberFormatException nf) {
			throw new BmaException("Importo Errato: " + myImporto, nf.getMessage()); 
		}
	}
}
