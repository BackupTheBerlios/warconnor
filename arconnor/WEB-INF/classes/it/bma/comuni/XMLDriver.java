package it.bma.comuni;
// JAVA
import java.io.*;
import java.util.*;
// XML
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.apache.xml.serialize.*;
import javax.xml.transform.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.stream.*;
// FOP
import org.apache.fop.apps.*;
import org.apache.avalon.framework.logger.*;
import org.apache.fop.messaging.MessageHandler;

public class XMLDriver implements BmaErrorCodes {
	private String encodingCharset = "UTF-8";
	public XMLDriver() {
		super();
	}
	public XMLDriver(String encoding) {
		super();
		encodingCharset = encoding;
	}
	public String recode(String inXml, String encoding) throws BmaException {
		if (inXml==null || inXml.trim().length()==0) return ""; 
		try {
			String outXml = new String(inXml.getBytes(), encoding);
			return outXml;
		}
		catch (UnsupportedEncodingException uee) {
			throw new BmaException(BMA_ERR_XML_GENERICO, "Errore di codifica Xml", "UnsupportedEncodingException: " + uee.getMessage(), "XMLDriver");
		}
	}
	public String recode(String inXml) throws BmaException {
		return recode(inXml, encodingCharset);
	}
	public String getTag(String tag, String valore) {
		if (valore==null || valore.trim().length()==0) return "<" + tag + "/>";
		else return "<" + tag + ">" + valore + "</" + tag + ">";
	}
	public Document getDocument() throws BmaException {
		try {
			DocumentBuilderFactory bFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = bFactory.newDocumentBuilder();
			Document document = dBuilder.newDocument();
			return document;
		}
		catch (ParserConfigurationException pce) {
			throw new BmaException(BMA_ERR_XML_GENERICO, "Errore di codifica Xml", "ParserConfiguration: " + pce.getMessage(), "XMLDriver");
		}
	}
	public Document getDocumentFromXml(InputStream streamXml) throws BmaException {
		try {
			DocumentBuilderFactory bFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = bFactory.newDocumentBuilder();
			InputSource source = new InputSource(streamXml);
			source.setEncoding(encodingCharset);
			Document document = dBuilder.parse(source);
			return document;
		}
		catch (ParserConfigurationException pce) {
			throw new BmaException(BMA_ERR_XML_GENERICO, "Errore di codifica Xml", "ParserConfiguration: " + pce.getMessage(), "XMLDriver");
		}
		catch (SAXException sxe) {
			throw new BmaException(BMA_ERR_XML_GENERICO, "Errore di codifica Xml", "SAXEception: " + sxe.getMessage(), "XMLDriver");
		}
		catch (IOException io) {
			throw new BmaException(BMA_ERR_XML_GENERICO, "Errore di codifica Xml", "IOException: " + io.getMessage(), "XMLDriver");
		}
	}
	public Document getDocumentFromXml(String xml) throws BmaException {
		String temp = xml;
		return getDocumentFromXml(new ByteArrayInputStream(temp.getBytes()));
	}
	public Document getDocumentFromXmlFile(String fileName) throws BmaException {
		try {
			FileInputStream isXml = new FileInputStream(fileName);
			return getDocumentFromXml(isXml);
		}
		catch (IOException io) {
			throw new BmaException(BMA_ERR_XML_GENERICO, "Errore di codifica Xml", "IOException: " + io.getMessage(), "XMLDriver");
		}
	}
	public Element getElementByTag(Element parent, String tag) throws BmaException {
		NodeList list = parent.getElementsByTagName(tag);
		if (list.getLength()>0) {
			Node n = list.item(0);
			if (n.getNodeType()==n.ELEMENT_NODE) return (Element)n;
		}
		throw new BmaException(BMA_ERR_XML_GENERICO, "Errore di codifica Xml", "Tag inesistente: " + tag, "XMLDriver");
	}
	public Element setRootElement(Document document, String tag) {
		Element element = document.createElement(tag);
		document.appendChild(element);
		return element;
	}
	public Element addElement(Element parent, String tag) {
		Document document = parent.getOwnerDocument();
		Element element = document.createElement(tag);
		parent.appendChild(element);
		return element;
	}
	public Element addTextElement(Element parent, String tag, String value) {
		Document document = parent.getOwnerDocument();
		Element element = document.createElement(tag);
		parent.appendChild(element);
		if (value!=null && value.trim().length()>0) {
			Text testo = document.createTextNode(value);
			element.appendChild(testo);
		}
		return element;
	}
	public void addText(Element parent, String value) {
		if (value!=null && value.trim().length()>0) {
			Document document = parent.getOwnerDocument();
			Text testo = document.createTextNode(value);
			parent.appendChild(testo);
		}
	}
	public void addCDATASection(Element parent, String value) {
		if (value!=null && value.trim().length()>0) {
			Document document = parent.getOwnerDocument();
			CDATASection cData = document.createCDATASection(value);
			parent.appendChild(cData);
		}
	}
	public Element getElementByAttribute(Element parent, String tag, String attr, String value) {
		NodeList list = parent.getElementsByTagName(tag);
		for (int i=0;i<list.getLength();i++) {
			Element e = (Element)list.item(i);
			String c = e.getAttribute(attr);
			if (c.equals(value)) return e;
		}
		return null;
	}
	public Vector getChildsElements(Element parent) {
		Vector v = new Vector();
		NodeList list = parent.getChildNodes();
		for (int i=0;i<list.getLength();i++) {
			Node n = list.item(i);
			if (n.getNodeType()==n.ELEMENT_NODE) v.add(n);
		}
		return v;
	}	
	public String serializeDocument(Document document) throws BmaException {
		ByteArrayOutputStream out = getOutputStream(document);
		return new String(out.toByteArray());
	}
	public ByteArrayOutputStream getOutputStream(Document document) throws BmaException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputFormat outFormat = new OutputFormat((Document)document);
		outFormat.setEncoding(encodingCharset);
		outFormat.setIndenting(true);
		XMLSerializer serializer = new XMLSerializer(out, outFormat);
		try {
			serializer.asDOMSerializer();
			serializer.serialize(document);
			out.close();
			return out;
		}
		catch (IOException io) {
			throw new BmaException(BMA_ERR_XML_GENERICO, "Errore di codifica Xml", "IOException: " + io.getMessage(), "XMLDriver");
		}
	}
	public String serializeDocument(Document document, String fileOutput) throws BmaException {
		String xml = serializeDocument(document);
		try {
			FileOutputStream fOut = new FileOutputStream(fileOutput);
			fOut.write(xml.getBytes());
			fOut.close();
			return xml;
		}
		catch (IOException io) {
			throw new BmaException(BMA_ERR_XML_GENERICO, "Errore di codifica Xml", "IOException: " + io.getMessage(), "XMLDriver");
		}
	}
	public OutputStream transform(InputStream isXml, String xslFileName, Hashtable params) throws BmaException {
		try {
			FileInputStream isXsl = new FileInputStream(xslFileName);
			return transform(isXml, isXsl, params);
		}
		catch (IOException io) {
			throw new BmaException(BMA_ERR_XML_GENERICO, "Errore di codifica Xml", "IOException: " + io.getMessage(), "XMLDriver");
		}
	}
	public OutputStream transform(InputStream isXml, InputStream isXsl, Hashtable params) throws BmaException {
		try {
	    Source xmlSource = new StreamSource(isXml);
	    Source xslSource = new StreamSource(isXsl);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer(xslSource);
			transformer.setOutputProperty("encoding", encodingCharset);
			if (params!=null) {
				Enumeration epar = params.keys();
				while (epar.hasMoreElements()) {
					String k = (String)epar.nextElement();
					String v = (String)params.get(k);
					transformer.setParameter(k,  v);
				}
			}
			transformer.transform(xmlSource, new StreamResult(outStream));
			outStream.close();
			return outStream;
		}
		catch (Exception e) {
			throw new BmaException(BMA_ERR_XML_GENERICO, "Errore di codifica Xml", e.getClass().getName() + ": " + e.getMessage(), "XMLDriver");
		}
	}
	public OutputStream transformPdf(InputStream isXml, String xslFileName, Hashtable params) throws BmaException {
		try {
			FileInputStream isXsl = new FileInputStream(xslFileName);
			return transformPdf(isXml, isXsl, params);
		}
		catch (IOException io) {
			throw new BmaException(BMA_ERR_XML_GENERICO, "Errore di codifica Xml", "IOException: " + io.getMessage(), "XMLDriver");
		}
	}
	public OutputStream transformPdf(InputStream isXml, InputStream isXsl, Hashtable params) throws BmaException {
		try {
	    Source xmlSource = new StreamSource(isXml);
	    Source xslSource = new StreamSource(isXsl);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer(xslSource);
			transformer.setOutputProperty("encoding", encodingCharset);
			if (params!=null) {
				Enumeration epar = params.keys();
				while (epar.hasMoreElements()) {
					String k = (String)epar.nextElement();
					String v = (String)params.get(k);
					transformer.setParameter(k,  v);
				}
			}
			Driver driver = new Driver();
			Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_ERROR);
			driver.setLogger(logger);
			MessageHandler.setScreenLogger(logger);
			driver.setRenderer(Driver.RENDER_PDF);
			driver.setOutputStream(outStream);
			Result result = new SAXResult(driver.getContentHandler());
			transformer.transform(xmlSource, result);
			outStream.close();
			return outStream;
		}
		catch (Exception e) {
			throw new BmaException(BMA_ERR_XML_GENERICO, "Errore di codifica Xml", e.getClass().getName() + ": " + e.getMessage(), "XMLDriver");
		}
	}
}
