package it.bma.media;

import java.io.*;
import java.util.*;
import it.bma.comuni.*;
// Export & Import
import javax.xml.parsers.*;
// Export
import org.w3c.dom.*;
// Export
import org.apache.xml.serialize.*;
// Import
import org.xml.sax.*;
// Import
import org.xml.sax.helpers.*;

public class LoadVolume {
	public LoadVolume() {
	}
	private static class VolumeHandler extends DefaultHandler {
		int indent = 0;
		public ByteArrayOutputStream out = new ByteArrayOutputStream();
		public VolumeHandler() {
			super();
		}
		public void startElement(String uri, String name, String qName, Attributes attr) throws SAXException {
			String s = attr.getValue("Name");
			for (int i=0;i<indent;i++) {
				s = " " + s;
			}
			s = s + '\n';
			try {
				out.write(s.getBytes());
			}
			catch (IOException io) {
				System.out.println(io.getMessage());
			}
			if (name.equals("Volume")) indent++;
			else if (name.equals("Dir")) indent++;
		}
		public void endElement(String uri, String name, String qName) throws SAXException {
			if (name.equals("Dir")) indent--;
		}
	}
	public static void main(String[] args) {
		if (args.length!=2) {
      System.out.println("Impostare il percorso del volume ed il file di output");
      return;
    }
    String drive = args[0];
    String outFile = args[1];
		try {
			DocumentBuilderFactory bFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBilder = bFactory.newDocumentBuilder();
			Document document = dBilder.newDocument();
			Element root = document.createElement("Volume");
			root.setAttribute("Name", "Test Volume");
			document.appendChild(root);
			File startPoint = new File(drive);
			getFiles(document, root, startPoint);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			OutputFormat outFormat = new OutputFormat((Document)document);
			outFormat.setIndenting(true);
			XMLSerializer serializer = new XMLSerializer(out, outFormat);
			serializer.asDOMSerializer();
			serializer.serialize(document);
			out.close();
			String xml = out.toString();
			System.out.println(xml);
			String sOut = printXmlToFile(xml);
			FileOutputStream fOut = new FileOutputStream(outFile);
			fOut.write(sOut.getBytes());
			System.out.println("Ok");
		}
		catch (ParserConfigurationException pce) {
			System.out.println(pce.getMessage());
		}
		catch (IOException io) {
			System.out.println(io.getMessage());
		}
	}
	private static String printXmlToFile(String xml) {
		ByteArrayInputStream b = new ByteArrayInputStream(xml.getBytes());
		InputSource is = new InputSource(b);
		VolumeHandler vh = new VolumeHandler();
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		try {
			SAXParser saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			xmlReader.setContentHandler(vh);
			xmlReader.parse(is);
			return vh.out.toString();
		}
		catch (ParserConfigurationException pce) {
			System.out.println(pce.getMessage());
			return "";
		}
		catch (SAXException se) {
			System.out.println(se.getMessage());
			return "";
		}
		catch (IOException io) {
			System.out.println(io.getMessage());
			return "";
		}
	}
	private static void getFiles(Document document, Element element, File headFile) {
		if (headFile==null) return;
		Element oneElement = element;
		String fileName = pulisciNome(headFile.getName());
		if (headFile.isDirectory()) {
			if (fileName.trim().length()>0) {
				oneElement = document.createElement("Dir");
				oneElement.setAttribute("Name", fileName);
				element.appendChild(oneElement);
				
			}
			File[] list = headFile.listFiles();
			for (int i=0;i<list.length;i++) {
				getFiles(document, oneElement, list[i]);
			}
		}
		else {
			oneElement = document.createElement("File");
			oneElement.setAttribute("Size", Long.toString(headFile.length()));
			oneElement.setAttribute("Date", Long.toString(headFile.lastModified()));
			oneElement.setAttribute("Name", fileName);
			element.appendChild(oneElement);
		}
	}
	private static String pulisciNome(String name) {
		String pulita = name;
		String bad = "Á¢¼Ó";
		int i = pulita.indexOf(bad);
		if (i>=0) {
			pulita = pulita.substring(0, i) + pulita.substring(i + bad.length());
		}
//		String elementName = name.replaceAll("\\s", "_");
//		elementName = elementName.replaceAll("\\p{Punct}", "");
		return pulita;
	}
}
