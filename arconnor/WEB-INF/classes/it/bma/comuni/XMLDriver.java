package it.bma.comuni;
// JAVA
import java.io.*;
import java.util.*;
// XML
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.apache.xml.serialize.*;

public class XMLDriver {
	public XMLDriver() {
		super();
	}
	public Document getDocument() throws BmaException {
		try {
			DocumentBuilderFactory bFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = bFactory.newDocumentBuilder();
			Document document = dBuilder.newDocument();
			return document;
		}
		catch (ParserConfigurationException pce) {
			throw new BmaException("ParserConfiguration", pce.getMessage());
		}
	}
	public Document getDocumentFromXml(String xml) throws BmaException {
		try {
			DocumentBuilderFactory bFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = bFactory.newDocumentBuilder();
			Document document = dBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
			return document;
		}
		catch (ParserConfigurationException pce) {
			throw new BmaException("ParserConfiguration", pce.getMessage());
		}
		catch (SAXException sxe) {
			throw new BmaException("SAXEception", sxe.getMessage());
		}
		catch (IOException io) {
			throw new BmaException("IOException", io.getMessage());
		}
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
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputFormat outFormat = new OutputFormat((Document)document);
		outFormat.setEncoding("ISO-8859-1");
		outFormat.setIndenting(true);
		XMLSerializer serializer = new XMLSerializer(out, outFormat);
		try {
			serializer.asDOMSerializer();
			serializer.serialize(document);
			out.close();
			return out.toString();
		}
		catch (IOException io) {
			throw new BmaException("IOException", io.getMessage());
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
			throw new BmaException("IOException", io.getMessage());
		}
	}
	public String recodeISO_8859_1(String name) throws BmaException {
		if (name==null) return "";
		String result = "";
		try {
			result = new String(name.getBytes(), "ISO-8859-1");
		}
		catch (UnsupportedEncodingException uee) {
			throw new BmaException("UnsupportedEncodingException", uee.getMessage());
		}
		result = result.replaceAll("&", "+");
		return result;
		/*
		String pulita = name;
		String bad = "����";
		int i = pulita.indexOf(bad);
		while (i>=0) {
			pulita = pulita.substring(0, i) + pulita.substring(i + bad.length());
			i = pulita.indexOf(bad);
		}
		bad = "&";
		i = pulita.indexOf(bad);
		while (i>=0) {
			pulita = pulita.substring(0, i) + "+" + pulita.substring(i + bad.length());
			i = pulita.indexOf(bad);
		}
		bad=" ";
		i = pulita.indexOf(bad);
		while (i>=0) {
			pulita = pulita.substring(0, i) + pulita.substring(i + bad.length());
			i = pulita.indexOf(bad);
		}
		
//		String elementName = name.replaceAll("\\s", "_");
//		elementName = elementName.replaceAll("\\p{Punct}", "");
		return pulita;
		 */
	}
}
