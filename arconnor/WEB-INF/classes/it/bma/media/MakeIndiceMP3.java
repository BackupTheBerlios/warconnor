package it.bma.media;

//Java
import java.io.*;
import java.util.*;
//JAXP
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
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

// BMA
import it.bma.comuni.*;

public class MakeIndiceMP3 {
	public MakeIndiceMP3() {
		super();
	}
	public static void main(String[] args) {
		BmaJdbcSource source = new BmaJdbcSource("MEDIA");
		source.setDriver("ianywhere.ml.jdbcodbc.IDriver");
		source.setUrl("jdbc:odbc:dsn=MEDIA");
		source.setUser("DBA");
		source.setPass("sql");
		source.setSchema("");
		BmaJdbcTrx trx = new BmaJdbcTrx(source);
		try {
			// 1° Costruire il file XML globale
			System.out.println("Making xml...");
			String baseDir = "C:/Progetti/warconnor_cvs/arconnor/media/";
			trx.open("System");
			String sql = "";
			sql = "SELECT COD_VOLUME, NOT_VOLUME " +
						" FROM	MD_VOLUMI " +
						" WHERE	COD_TIPOVOLUME='CD-MP3' " +
//						" AND		COD_VOLUME BETWEEN 'MP3-002' AND 'MP3-003' " +
						" ORDER BY COD_VOLUME ";
			Vector dati = trx.eseguiSqlSelect(sql);
			String xmlBrani = "<Brani>" + '\n';
			for (int i=0; i<dati.size(); i++) {
				Vector riga = (Vector)dati.elementAt(i);
				String sVolume = (String)riga.elementAt(1);
				int x = sVolume.indexOf("?>");
				if (x>0) {
					xmlBrani = xmlBrani + sVolume.substring(x+2) + '\n';
				}
			}	
			trx.chiudi();
			xmlBrani = xmlBrani + "</Brani>" + '\n';
			File fileXmlBrani = new File(baseDir + "indiceMP3.xml");
			FileOutputStream osXmlBrani = new FileOutputStream(fileXmlBrani);
			osXmlBrani.write(xmlBrani.getBytes());
			osXmlBrani.close();
			
			// 2° Preparare il PDF
			System.out.println("Preparing...");

			//Setup input and output files            
			File xmlFile = new File(baseDir + "indiceMP3.xml");
			File xslFile = new File(baseDir + "indiceMP3_fo.xsl");
			File pdfFile = new File(baseDir + "indiceMP3.pdf");

			System.out.println("Input: XML (" + xmlFile + ")");
			System.out.println("Stylesheet: " + xslFile);
			System.out.println("Output: PDF (" + pdfFile + ")");
			System.out.println("Transforming...");

			MakeIndiceMP3 app = new MakeIndiceMP3();
			app.convertXML2PDF(xmlFile, xslFile, pdfFile);

			System.out.println("Success!");
		} 
		catch (Exception e) {
			System.err.println(ExceptionUtil.printStackTrace(e));
			System.exit(-1);
		}
	}
	public void convertXML2PDF(File xml, File xslt, File pdf) 
							throws IOException, FOPException, TransformerException {
		Driver driver = new Driver();

		Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_INFO);
		driver.setLogger(logger);
		MessageHandler.setScreenLogger(logger);

		driver.setRenderer(Driver.RENDER_PDF);

		OutputStream out = new java.io.FileOutputStream(pdf);
		try {
			driver.setOutputStream(out);

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(xslt));

			Source src = new StreamSource(xml);

			Result res = new SAXResult(driver.getContentHandler());

			transformer.transform(src, res);
		} 
		finally {
			out.close();
		}
	}
}
