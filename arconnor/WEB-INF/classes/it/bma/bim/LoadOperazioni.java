package it.bma.bim;

import java.io.*;
import java.util.*;
// POI
import org.apache.poi.poifs.filesystem.*;
import org.apache.poi.hssf.usermodel.*;
// XML
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.apache.xml.serialize.*;
// BMA
import it.bma.comuni.*;
import it.bma.web.*;

public class LoadOperazioni {
	private BmaJsp jsp = new BmaJsp();
	private String baseDir = "";
	private String logTime = "";
	private JdbcModel jModel = null;
	private Document guida = null;
	private HSSFCellStyle styleData = null;
	public LoadOperazioni() {
		super();
	}
	public static void main(String[] args) {
		if (args.length!=2) {
      System.out.println("Impostare la directory di lavoro ed il file Excel di input");
      return;
    }
		LoadOperazioni app = new LoadOperazioni();
		app.esegui(args[0], args[1]);
	}
	private void esegui(String arg1, String arg2) {
		logTime = Long.toString(new Date().getTime());
		baseDir = arg1;
		String fileExcel = baseDir + "\\" + arg2;
		String fileXml = baseDir + "\\Ope-" + logTime + ".xml";
		
		BmaJdbcSource source = new BmaJdbcSource("TEST");
		source.setDriver("jdbc.gupta.sqlbase.SqlbaseDriver");
		source.setUrl("jdbc:sqlbase://PCWIN002/AR2002");
		source.setUser("sysadm");
		source.setPass("sysadm");
		source.setSchema("default");
		BmaJdbcTrx jTrx = new BmaJdbcTrx(source);
		
		try {
			jTrx.open(logTime);
			// Crea DOM document
			DocumentBuilderFactory bFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBilder = bFactory.newDocumentBuilder();
			Document document = dBilder.newDocument();
			Element root = document.createElement("root");
			document.appendChild(root);
			// Apre File Guida
			FileInputStream isGuida = new FileInputStream(baseDir + "\\profiliOperazione.xml");
			guida = dBilder.parse(isGuida);
			// Apre Excel
			FileInputStream fileIn = new FileInputStream(fileExcel);
			POIFSFileSystem poiFS = new POIFSFileSystem(fileIn);
			HSSFWorkbook wb = new HSSFWorkbook(poiFS);
			styleData = wb.createCellStyle();
			HSSFDataFormat df = wb.createDataFormat();
			styleData.setDataFormat(df.getFormat("dd/MM/yyyy"));

			// Legge dati input e prepara XML operazioni
			for (int i=0;i<wb.getNumberOfSheets();i++) {
				HSSFSheet sheet = wb.getSheetAt(i);
				readSheet(jTrx, wb.getSheetName(i), sheet, root);
			}
			// Salva XML
			FileOutputStream fileOut = new FileOutputStream(fileXml);
			OutputFormat outFormat = new OutputFormat((Document)document);
			outFormat.setIndenting(true);
//			outFormat.setEncoding("ISO-8859-1");
			outFormat.setEncoding("UTF-16");
			XMLSerializer serializer = new XMLSerializer(fileOut, outFormat);
			serializer.asDOMSerializer();
			serializer.serialize(document);
			fileOut.close();
			// Salva Excel
			fileOut = new FileOutputStream(fileExcel);
			wb.write(fileOut);
			fileOut.close();
			jTrx.chiudi();
			System.out.println("Ok");
		}
		catch (IOException io) {
			if (jTrx.isAperta()) jTrx.invalida();			
			System.out.println("IOException: " + io.getMessage());
		}
		catch (ParserConfigurationException pce) {
			if (jTrx.isAperta()) jTrx.invalida();			
			System.out.println("ParserConfigurationException: " + pce.getMessage());
		}
		catch (SAXException sxe) {
			if (jTrx.isAperta()) jTrx.invalida();			
			System.out.println("SAXException: " + sxe.getMessage());
		}
		catch (BmaException bma) {
			if (jTrx.isAperta()) jTrx.invalida();			
			System.out.println(bma.getInfo() + ": " + bma.getInfoEstese());
		}
	}
	private void readSheet(BmaJdbcTrx jTrx, String name, HSSFSheet mySheet, Element root) throws BmaException {
		if (!name.substring(0,2).equals("I-")) return;
		DriverOperazione driver = new DriverOperazione();
		if (jModel!=null) driver.setJModel(jModel);
		if (guida!=null) driver.setGuida(guida);
		// Determina i tipi ed i nomi dei campi
		int maxCol = 100;
		int maxRow = 1000;
		HSSFRow row = mySheet.getRow(0);
		for (int i=0; i<maxCol; i++) {
			HSSFCell cell = row.getCell((short)i);
			if (cell==null || cell.getCellType()==cell.CELL_TYPE_BLANK) maxCol = i;
		}
		String[] types = new String[maxCol];
		String[] names = new String[maxCol];
		for (int i=0;i<maxCol;i++) {
			types[i] = mySheet.getRow(0).getCell((short)i).getStringCellValue().trim();
			names[i] = mySheet.getRow(1).getCell((short)i).getStringCellValue().trim();
		}
		// Carica i valori di ogni riga
		for (int i=2;i<maxRow;i++) {
			row = mySheet.getRow(i);
			if (row==null) maxRow = i;
			else if (isNuovaOperazione(row)) {
				Hashtable valori = new Hashtable();
				for (int j=1;j<maxCol;j++) {
					HSSFCell cell = row.getCell((short)j);
					if (cell!=null && types[j].equals(jsp.BMA_SQL_TYS_CHR)) {
						valori.put(names[j], cell.getStringCellValue().trim());
					}
					else if (cell!=null && types[j].equals(jsp.BMA_SQL_TYS_DAT)) {
						java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(jsp.BMA_JSP_DATE_FMT_DATIDB);
						valori.put(names[j], sdf.format(cell.getDateCellValue()));
					}
					else if (cell!=null && types[j].equals(jsp.BMA_SQL_TYS_NUM)) {
						valori.put(names[j], Double.toString(cell.getNumericCellValue()));
					}
				}
				// Prepara XML operazione
				driver.creaOperazione(jTrx, valori, root);
				HSSFCell cell = row.getCell((short)0);
				cell.setCellStyle(styleData);
				cell.setCellValue(new Date());
				if (jModel==null) jModel = driver.getJModel();
			}
		}
	}
	private boolean isNuovaOperazione(HSSFRow row) {
		HSSFCell c0 = row.getCell((short)0);
		if (c0==null) c0 = row.createCell((short)0);
		HSSFCell c1 = row.getCell((short)1);
		if (c1==null) c1 = row.createCell((short)1);
		return c0.getCellType()==c0.CELL_TYPE_BLANK	&& c1.getCellType()!=c1.CELL_TYPE_BLANK;
	}
}
