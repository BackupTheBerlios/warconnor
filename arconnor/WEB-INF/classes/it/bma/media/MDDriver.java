package it.bma.media;

import java.io.*;
import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.apache.xml.serialize.*;

public class MDDriver extends BmaDataDriverGeneric {
	public MDDriver() {
		super();
	}
	public Hashtable getTipiVolume() throws BmaException {
		String sql = "SELECT COD_TIPOVOLUME, DES_TIPOVOLUME FROM MD_TIPIVOLUME";
		return getValoriControllo(sql);
	}
	public String getIndiceVolume(String xml) {
		return "Sono l'indice";
	}
	public String getXmlVolume(String path, String name) throws BmaException {
		try {
			DocumentBuilderFactory bFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBilder = bFactory.newDocumentBuilder();
			Document document = dBilder.newDocument();
			Element root = document.createElement("Volume");
			root.setAttribute("Name", name);
			document.appendChild(root);
			File startDir = new File(path);
			getFiles(document, root, startDir);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			OutputFormat outFormat = new OutputFormat((Document)document);
			outFormat.setIndenting(true);
			XMLSerializer serializer = new XMLSerializer(out, outFormat);
			serializer.asDOMSerializer();
			serializer.serialize(document);
			out.close();
			return out.toString();
		}
		catch (ParserConfigurationException pce) {
			throw new BmaException(BMA_ERR_XML_PARSE, "ParserConfiguration", pce.getMessage(), this);
		}
		catch (IOException io) {
			throw new BmaException(BMA_ERR_XML_PARSE, "IO Exception", io.getMessage(), this);
		}
	}
	private static void getFiles(Document document, Element element, File headFile) {
		if (headFile==null) return;
		Element oneElement = element;
		String fileName = headFile.getName();
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
}
