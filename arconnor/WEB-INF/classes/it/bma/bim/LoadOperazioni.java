package it.bma.bim;

import java.io.*;
import java.util.*;
// POI
import org.apache.poi.poifs.filesystem.*;
import org.apache.poi.hssf.usermodel.*;
// XML
import org.w3c.dom.*;
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
		XMLDriver xDriver = new XMLDriver();
		BmaJdbcSource source = new BmaJdbcSource("TEST");
		source.setDriver("jdbc.gupta.sqlbase.SqlbaseDriver");
		source.setUrl("jdbc:sqlbase://PCWIN001/AR2002");
		source.setUser("sysadm");
		source.setPass("sysadm");
		source.setSchema("default");
		BmaJdbcTrx jTrx = new BmaJdbcTrx(source);
		
		try {
			jTrx.open(logTime);
			// Crea DOM document
			Document document = xDriver.getDocument();
			Element root = xDriver.setRootElement(document, "root");
			// Apre File Guida
			FileInputStream isGuida = new FileInputStream(baseDir + "\\profiliOperazione.xml");
			guida = xDriver.getDocumentFromXml(isGuida);
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
			xDriver.serializeDocument(document, fileXml);
			// Salva Excel
			FileOutputStream fileOut = new FileOutputStream(fileExcel);
			wb.write(fileOut);
			fileOut.close();
			jTrx.chiudi();
			System.out.println("Ok");
		}
		catch (IOException io) {
			if (jTrx.isAperta()) jTrx.invalida();			
			System.out.println("IOException: " + io.getMessage());
		}
		catch (BmaException bma) {
			if (jTrx.isAperta()) jTrx.invalida();			
			System.out.println(bma.getInfo() + ": " + bma.getInfoEstese());
		}
		catch (Throwable t) {
			if (jTrx!=null && jTrx.isAperta()) jTrx.invalida();			
			System.out.println(t.getClass().getName() + " - " + t.getMessage());
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
		String[] names = new String[maxCol];
		for (int i=0;i<maxCol;i++) {
			names[i] = row.getCell((short)i).getStringCellValue().trim();
		}
		// Carica i valori di ogni riga
		for (int i=1;i<maxRow;i++) {
			row = mySheet.getRow(i);
			if (row==null) maxRow = i;
			else if (isNuovaOperazione(row)) {
				Hashtable valori = new Hashtable();
				for (int j=1;j<maxCol;j++) {
					HSSFCell cell = row.getCell((short)j);
					valori.put(names[j], readCell(cell, names[j]));
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
	private String readCell(HSSFCell cell, String nome) {
		if (cell==null) return "";
		if (cell.getCellType()==cell.CELL_TYPE_STRING) return cell.getStringCellValue();
		if (nome.substring(0,3).equals("DAT")) {
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(jsp.BMA_JSP_DATE_FMT_DATIDB);
			Date d = cell.getDateCellValue();
			if (d==null) return "";
			return sdf.format(d);
		}
		else if (nome.substring(0,3).equals("COD")) {
			long n = (long)cell.getNumericCellValue();
			return Long.toString(n);
		}
		else {
			double n = cell.getNumericCellValue();
			return Double.toString(n);
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
