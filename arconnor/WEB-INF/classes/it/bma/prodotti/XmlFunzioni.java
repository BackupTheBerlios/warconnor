package it.bma.prodotti;
import java.io.*;
import java.util.*;
import it.bma.comuni.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.apache.xml.serialize.*;

//JAXP
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.sax.SAXResult;

//Avalon
import org.apache.avalon.framework.ExceptionUtil;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;

//FOP
import org.apache.fop.apps.Driver;
import org.apache.fop.apps.FOPException;
import org.apache.fop.messaging.MessageHandler;

public class XmlFunzioni {
	private static BmaParametro par = new BmaParametro();
  public XmlFunzioni() {
    super();
  }
  public static void main(String[] args) {
		String baseDir = "C:/Progetti/warconnor_cvs/arconnor/bma/";
		BmaJdbcSource source = new BmaJdbcSource("PDAS");
		source.setDriver("ianywhere.ml.jdbcodbc.IDriver");
		source.setUrl("jdbc:odbc:dsn=PDAS");
		source.setUser("DBA");
		source.setPass("sql");
		source.setSchema("");
		BmaJdbcTrx jTrx = new BmaJdbcTrx(source);
    XmlFunzioni app = new XmlFunzioni();
		try {
/*			
      jTrx.open("System");
      String xml = app.makeXmlFunzioni(jTrx);
      jTrx.chiudi();
      source.writeFile(xml, baseDir + "FunzioniProdotti.xml", false);
*/
			app.makePdf(baseDir + "FunzioniProdotti.xml", baseDir + "FunzioniProdotti_fo.xsl", baseDir + "FunzioniProdotti.pdf");
			System.out.println("Ok");
		}
		catch (BmaException e) {
      if (jTrx.isAperta()) jTrx.invalida();
			System.out.println(e.getMessage());
			System.out.println(e.getInfo());
			System.out.println(e.getInfoEstese());
		}
  }
  public String makeXmlFunzioni(BmaJdbcTrx jTrx) throws BmaException {
    DocumentBuilderFactory bFactory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder dBilder = bFactory.newDocumentBuilder();
      Document document = dBilder.newDocument();
			
      Element root = document.createElement("FunzioniProdotti");
      document.appendChild(root);
      String sql = "";
      sql = "SELECT COD_BLOCCO, NUM_LIVELLO, " +
            "       DES_TITOLO, FLG_NUOVAPAGINA, " +
            "       COD_FUNZIONE, COD_IMMAGINE, " +
            "       NOT_BLOCCO " +
            " FROM  PM_BLOCCHI " +
            " WHERE COD_DOCUMENTO='FUNZIONI' " +
            " ORDER BY COD_BLOCCO ";
      Vector dati = jTrx.eseguiSqlSelect(sql);
      for (int i=0;i<dati.size();i++) {
        Vector riga = (Vector)dati.elementAt(i);
        String codBlocco = (String)riga.elementAt(0);
        int numLivello = Integer.parseInt((String)riga.elementAt(1));
        String desTitolo = (String)riga.elementAt(2);
        String flgNuovaPagina = (String)riga.elementAt(3);
        String codFunzione = (String)riga.elementAt(4);
        String codImmagine = (String)riga.elementAt(5);
        String notBlocco = (String)riga.elementAt(6);

        Element blocco = document.createElement("Blocco");
        blocco.setAttribute("Id", codBlocco);
        blocco.setAttribute("NuovaPagina", flgNuovaPagina);
        blocco.setAttribute("Titolo", desTitolo);
        
        Element funzione = document.createElement("Funzione");
        Element immagine = document.createElement("Immagine");        
				Element testo = document.createElement("TestoBlocco");
				if (notBlocco.trim().length()>0) parseXhtml(testo, notBlocco);
        
        blocco.appendChild(funzione);
        blocco.appendChild(immagine);
        blocco.appendChild(testo);
        
        root.appendChild(blocco);
      }
			document.normalize();
      
 			ByteArrayOutputStream out = new ByteArrayOutputStream();
			OutputFormat outFormat = new OutputFormat((Document)document);
			outFormat.setEncoding("ISO-8859-1");
			outFormat.setIndenting(true);
			XMLSerializer serializer = new XMLSerializer(out, outFormat);
			serializer.asDOMSerializer();
			serializer.serialize(document);
			out.close();
			return out.toString();
    }
		catch (ParserConfigurationException pce) {
			throw new BmaException("ParserConfiguration", pce.getMessage());
		}
		catch (IOException io) {
			throw new BmaException("IO Exception", io.getMessage());
		}
  }
	private void parseXhtml(Element myNode, String xml) throws BmaException {
		String s = "<?xml version='1.0' encoding='ISO-8859-1'?><xml>" + xml + "</xml>";
		ByteArrayInputStream htmlStream = new ByteArrayInputStream(s.getBytes());
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			org.w3c.dom.Document domDocument = builder.parse(htmlStream);
			Element root = domDocument.getDocumentElement();
			NodeList list = root.getChildNodes();
			for (int i=0;i<list.getLength();i++) {
				Node n = list.item(i);
				Node newNode = myNode.getOwnerDocument().importNode(n, true);
				myNode.appendChild(newNode);
			}
			
//			mergeNode(myNode, root);
		}
		catch (ParserConfigurationException pce) {
			throw new BmaException("ParserConfiguration", pce.getMessage());
		}
		catch (org.xml.sax.SAXException se) {
			throw new BmaException("SAXException", se.getMessage());
		}
		catch (java.io.IOException io) {
			throw new BmaException("IOException", io.getMessage());
		}
	}
	private void mergeNode(Element target, Element actual) {
		NamedNodeMap attrMap = actual.getAttributes();
		for (int i=0;i<attrMap.getLength();i++) {
			Node attrItem = attrMap.item(i);
			target.setAttribute(attrItem.getNodeName(), attrItem.getNodeValue());
		}
		NodeList list = actual.getChildNodes();
		for (int i=0;i<list.getLength();i++) {
			Node n = list.item(i);
			if (n.getNodeType()==n.TEXT_NODE) {
				Node newTarget = target.getOwnerDocument().createTextNode(n.getNodeValue());
				target.appendChild(newTarget);
			}
			else if (n.getNodeType()==n.ELEMENT_NODE) {
				Element nextNode = (Element)list.item(i);
				Element newTarget = target.getOwnerDocument().createElement(nextNode.getNodeName());
				mergeNode(newTarget, nextNode);
				target.appendChild(newTarget);
			}
		}
	}
	private void makePdf(String xml, String xsl, String pdf) throws BmaException {
		OutputStream out = null;
		try {
			out = new java.io.FileOutputStream(pdf);
			//Setup input and output files            
			Driver driver = new Driver();

			Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_INFO);
			driver.setLogger(logger);
			MessageHandler.setScreenLogger(logger);

			driver.setRenderer(Driver.RENDER_PDF);

			driver.setOutputStream(out);

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(xsl));

			Source src = new StreamSource(xml);

			Result res = new SAXResult(driver.getContentHandler());

			transformer.transform(src, res);
			
		}
		catch (IOException io) {
			throw new BmaException("IO Exception", io.getMessage());
		}
		catch (TransformerException te) {
			throw new BmaException("TransformerException", te.getMessage());
		}
	}
}
