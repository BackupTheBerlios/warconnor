package it.bma.media;

import java.io.*;
import java.util.*;
import it.bma.comuni.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.apache.xml.serialize.*;

public class LoadVolume {
	public LoadVolume() {
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
			document.appendChild(root);
			File startPoint = new File(drive);
			getFiles(document, root, startPoint);
			FileOutputStream out = new FileOutputStream(outFile);
			OutputFormat outFormat = new OutputFormat((Document)document);
			outFormat.setIndenting(true);
			XMLSerializer serializer = new XMLSerializer(out, outFormat);
			serializer.asDOMSerializer();
			serializer.serialize(document);
			out.close();
			System.out.println("Ok");
		}
		catch (ParserConfigurationException pce) {
			System.out.println(pce.getMessage());
		}
		catch (IOException io) {
			System.out.println(io.getMessage());
		}
	}
	private static void getFiles(Document document, Element element, File headFile) {
		if (headFile==null) return;
		Element oneElement = element;
		String fileName = headFile.getName();
		String elementName = getFileElementName(fileName);
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
	private static String getFileElementName(String name) {
		String elementName = name.replaceAll("\\s", "_");
		elementName = elementName.replaceAll("\\p{Punct}", "");
		return elementName;
	}
}
