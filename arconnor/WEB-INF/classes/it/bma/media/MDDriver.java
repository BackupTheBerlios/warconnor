package it.bma.media;
// Java
import java.io.*;
import java.util.*;
import java.util.regex.*;
// XML
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.apache.xml.serialize.*;
// MP3
import org.farng.mp3.*;
import org.farng.mp3.id3.*;
// CDDB
import com.antelmann.cddb.*;
import java.text.ParseException;

// BMA
import it.bma.comuni.*;
import it.bma.web.*;

public class MDDriver extends BmaDataDriverGeneric {
	private XMLDriver xmlDriver = new XMLDriver();
	public MDDriver() {
		super();
	}
	public BmaDataList getDataList(String tabella, Hashtable condizioni) throws BmaException {
		if (tabella.equals("MD_CARTELLEVOLUME")) {
			String codVolume = (String)condizioni.get("COD_VOLUME");
			String sql="";
			sql = "SELECT A.COD_VOLUME, A.COD_CARTELLA, " +
						"				A.NUM_PROGRESSIVO, B.DES_CARTELLA, " +
						"				B.COD_TIPOCARTELLA, A.NOT_CARTELLA " +
						" FROM	MD_CARTELLEVOLUME A, MD_CARTELLE B " +
						" WHERE	A.COD_CARTELLA = B.COD_CARTELLA " +
						" AND		A.COD_VOLUME='" + codVolume + "' " +
						" ORDER BY A.NUM_PROGRESSIVO";
			BmaDataList list = getDataList(sql, tabella, new String[] {"COD_VOLUME", "COD_CARTELLA"});
			BmaDataColumn col = list.getTabella().getColonna("COD_TIPOCARTELLA");
			col.setTipoControllo(BMA_CONTROLLO_LISTA);
			col.setValoriControllo(getTipiCartella());
			return list;
		}
		else return super.getDataList(tabella, condizioni);
	}
	public void aggiorna(String tabella, Hashtable valori, String azione) throws BmaException {
		if (tabella.equals("MD_CARTELLEVOLUME")) {
			BmaJdbcTrx jTrx = new BmaJdbcTrx(getJdbcSource());
			try {
				jTrx.open("System");
				BmaDataTable tbBase = getJModel().getDataTable(jTrx, "MD_CARTELLE");
				BmaDataTable tbRel = getJModel().getDataTable(jTrx, "MD_CARTELLEVOLUME");
				int stato = jTrx.controllaRiga(tbBase, valori);
				if (azione.equals(BMA_SQL_INSERT)) {
					if (stato==0) jTrx.eseguiSqlUpdate(tbBase.getSqlInsert(valori));
					jTrx.eseguiSqlUpdate(tbRel.getSqlInsert(valori));
				}
				else if (azione.equals(BMA_SQL_UPDATE)) {
					jTrx.eseguiSqlUpdate(tbRel.getSqlUpdate(valori));
				}
				else if (azione.equals(BMA_SQL_DELETE)) {
					jTrx.eseguiSqlUpdate(tbRel.getSqlDelete(valori));
				}
				jTrx.chiudi();
			}
			catch (BmaException bma) {
				if (jTrx.isAperta()) jTrx.invalida();
				throw bma;
			}
		}
		else super.aggiorna(tabella, valori, azione);
	}

	public Hashtable getTipiVolume() throws BmaException {
		String sql = "SELECT COD_TIPOVOLUME, DES_TIPOVOLUME FROM MD_TIPIVOLUME";
		return getValoriControllo(sql);
	}
	public Hashtable getTipiCartella() throws BmaException {
		String sql = "SELECT COD_TIPOCARTELLA, DES_TIPOCARTELLA FROM MD_TIPICARTELLE";
		return getValoriControllo(sql);
	}
	/* 
	 * MAIN SERVICE: Load Volume
	*/
	public void loadVolume(String drive, String codVolume) throws BmaException {
		String sql = "";
		BmaJdbcTrx jTrx = new BmaJdbcTrx(getJdbcSource());
		try {
			Document document = xmlDriver.getDocument();
			Element root = xmlDriver.setRootElement(document, "Volume");
			root.setAttribute("Name", codVolume);
			
			File fDrive = new File(drive);
			browseDirectory(root, fDrive);
			
			String workDir = getUserConfig().getParametroApplicazione("MEDIA-WORK-DIR");
			String logFile = workDir + "/" + codVolume + ".xml";
			String xmlVolume = xmlDriver.serializeDocument(document, logFile);
			
			jTrx.open("System");
			
			Hashtable valori = new Hashtable();
			valori.put("COD_VOLUME", codVolume);
			valori.put("COD_TIPOVOLUME", "CD-MP3");
			valori.put("DES_VOLUME", codVolume);
			valori.put("XML_VOLUME", xmlVolume);
			BmaDataTable tabella = getJModel().getDataTable(jTrx, "MD_VOLUMI");
			Vector dati = jTrx.eseguiSqlSelect(tabella.getSqlReadKey(valori));
			if (dati.size()==0) jTrx.eseguiSqlUpdate(tabella.getSqlInsert(valori));
			else jTrx.eseguiSqlUpdate(tabella.getSqlUpdate(valori));
			
			String codCartella = "";
			String codTipoCartella = "MP3-ALBUM";
			Vector albums = xmlDriver.getChildsElements(root);
			int numProgressivo = 0;
			for (int i=0;i<albums.size();i++) {
				Element album = (Element)albums.elementAt(i);
				valori.clear();
				numProgressivo++;
				String numCartella = Integer.toString(numProgressivo);
				while (numCartella.length()<3) { numCartella = "0" + numCartella; }
				codCartella = codVolume + "-" + numCartella;
				valori.put("COD_VOLUME", codVolume);
				valori.put("COD_CARTELLA", codCartella);
				valori.put("COD_TIPOCARTELLA", codTipoCartella);
				valori.put("DES_CARTELLA", album.getAttribute("Name"));
				valori.put("NUM_PROGRESSIVO", numCartella);
				valori.put("DES_PERCORSOBASE", workDir);
				
				tabella = getJModel().getDataTable(jTrx, "MD_CARTELLE");
				dati = jTrx.eseguiSqlSelect(tabella.getSqlReadKey(valori));
				if (dati.size()==0) jTrx.eseguiSqlUpdate(tabella.getSqlInsert(valori));
				else jTrx.eseguiSqlUpdate(tabella.getSqlUpdate(valori));
				
				valori.put("NOT_CARTELLA", album.getAttribute("Name"));
				tabella = getJModel().getDataTable(jTrx, "MD_CARTELLEVOLUME");
				dati = jTrx.eseguiSqlSelect(tabella.getSqlReadKey(valori));
				if (dati.size()==0) jTrx.eseguiSqlUpdate(tabella.getSqlInsert(valori));
				else jTrx.eseguiSqlUpdate(tabella.getSqlUpdate(valori));

				Vector files = xmlDriver.getChildsElements(album);
				for (int j=0;j<files.size(); j++) {
					Element eFile = (Element)files.elementAt(j);
					String fileName = eFile.getAttribute("Name");
					String ext = fileName.substring(fileName.length()-3);
					if (ext.equalsIgnoreCase("mp3")) {
						valori.put("NUM_PROGRESSIVO", Integer.toString(j+1));
						valori.put("DES_FILE", fileName);
						valori.put("DES_TRACK", eFile.getAttribute("Track"));
						valori.put("DES_ALBUM", eFile.getAttribute("Album"));
						valori.put("DES_ARTIST", eFile.getAttribute("Artist"));
						valori.put("DES_GENRE", eFile.getAttribute("Genre"));
						valori.put("DES_TITLE", eFile.getAttribute("Title"));
						valori.put("DES_YEAR", eFile.getAttribute("Year"));
						tabella = getJModel().getDataTable(jTrx, "MD_BRANITEMP");
						dati = jTrx.eseguiSqlSelect(tabella.getSqlReadKey(valori));
						if (dati.size()==0) jTrx.eseguiSqlUpdate(tabella.getSqlInsert(valori));
						else jTrx.eseguiSqlUpdate(tabella.getSqlUpdate(valori));
					}
				}
			}
			sql = "DELETE FROM MD_CARTELLEVOLUME " +
						" WHERE COD_VOLUME='" + codVolume + "' " +
						" AND		COD_CARTELLA>'" + codCartella + "'";
			jTrx.eseguiSqlUpdate(sql);
			
			jTrx.chiudi();
		}
		catch (BmaException bma) {
			if (jTrx.isAperta()) jTrx.invalida();
			throw bma;
		}
	}
	public void browseDirectory(Element element, File dir) throws BmaException {
		Document document = element.getOwnerDocument();
		if (dir==null) return;
		Element oneElement = element;
		String fileName = xmlDriver.recodeISO_8859_1(dir.getName());
		if (dir.isDirectory()) {
			if (fileName.trim().length()>0) {		// Se nome è vuoto dir è il drive
				oneElement = xmlDriver.addElement(element, "Dir");
				oneElement.setAttribute("Name", fileName);
			}
			File[] list = dir.listFiles();
			for (int i=0;i<list.length;i++) {
				browseDirectory(oneElement, list[i]);
			}
		}
		else {
			oneElement = document.createElement("File");
			element.appendChild(oneElement);
			oneElement.setAttribute("Size", Long.toString(dir.length()));
			oneElement.setAttribute("Date", Long.toString(dir.lastModified()));
			oneElement.setAttribute("Name", fileName);
			parseFileTags(oneElement, dir);
		}
	}
	private void parseFileTags(Element element, File file) throws BmaException {
		String ext = file.getName().substring(file.getName().length()-3);
		if (ext.equalsIgnoreCase("mp3")) {
			MP3Driver driver = new MP3Driver(file.getAbsolutePath());
			Properties props = driver.getProperties();
			Enumeration e = props.keys();
			while (e.hasMoreElements()) {
				String k = (String)e.nextElement();
				String v = xmlDriver.recodeISO_8859_1(props.getProperty(k));
				element.setAttribute(k, v);
			}
		}
	}
	public void reloadBrani(String codCartella) throws BmaException {
		MP3Driver mp3Driver = new MP3Driver();
		XMLDriver xmlDriver = new XMLDriver();
		Document document = xmlDriver.getDocument();
		Element root = xmlDriver.setRootElement(document, "ReloadBrani");
		String sql = "";
		BmaJdbcTrx jTrx = new BmaJdbcTrx(getJdbcSource());
		String dirPath = getUserConfig().getParametroApplicazione("MEDIA-WORK-DIR") + "/";
		try {
			jTrx.open("System");
			sql = "SELECT DES_CARTELLA " +
						" FROM	MD_CARTELLE " +
						" WHERE COD_CARTELLA='" + codCartella + "'";
			Vector dati = jTrx.eseguiSqlSelect(sql);
			if (dati.size()!=1) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Cartella Inesistente: " + codCartella, sql, this);
			dati = (Vector)dati.elementAt(0);
			String desCartella = (String)dati.elementAt(0);
			
			File dir = new File(dirPath + desCartella);
			if (!dir.exists()) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Cartella: " + desCartella + " non trovata in: " + dirPath, codCartella, this);
			
			sql = "DELETE FROM MD_BRANITEMP " +
						" WHERE COD_CARTELLA='" + codCartella + "'";
			jTrx.eseguiSqlUpdate(sql);
			BmaDataTable tabella = getJModel().getDataTable(jTrx, "MD_BRANITEMP");
			
			File[] list = dir.listFiles();
			int numProgressivo = 0;
			for (int i=0;i<list.length;i++) {
				if (mp3Driver.isMP3File(list[i].getAbsolutePath())) {
					mp3Driver.setFile(list[i].getAbsolutePath());
					Properties props = mp3Driver.getProperties();
					numProgressivo++;
					Hashtable valori = new Hashtable();
					valori.put("COD_CARTELLA", codCartella);
					valori.put("NUM_PROGRESSIVO", Integer.toString(numProgressivo));
					valori.put("DES_FILE", list[i].getName());
					valori.put("DES_TRACK", props.getProperty(mp3Driver.TAG_TRACK));
					valori.put("DES_ALBUM", props.getProperty(mp3Driver.TAG_ALBUM));
					valori.put("DES_ARTIST", props.getProperty(mp3Driver.TAG_ARTIST));
					valori.put("DES_GENRE", props.getProperty(mp3Driver.TAG_GENRE));
					valori.put("DES_TITLE", props.getProperty(mp3Driver.TAG_TITLE));
					valori.put("DES_YEAR", props.getProperty(mp3Driver.TAG_YEAR));
					valori.put("DES_CDID", props.getProperty(mp3Driver.TAG_MUSICID));
					jTrx.eseguiSqlUpdate(tabella.getSqlInsert(valori));
				}
			}
			jTrx.chiudi();
		}
		catch (BmaException bma) {
			if (jTrx.isAperta()) jTrx.invalida();
			throw bma;
		}
	}
	public void readCDDB(String codCartella) throws BmaException {
		String sql = "";
		BmaJdbcTrx jTrx = new BmaJdbcTrx(getJdbcSource());
		try {
			jTrx.open("System");
			sql = "SELECT NOT_CARTELLA " +
						" FROM	MD_CARTELLE " +
						" WHERE COD_CARTELLA='" + codCartella + "'";
			Vector dati = jTrx.eseguiSqlSelect(sql);
			if (dati.size()!=1) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Cartella Inesistente: " + codCartella, sql, this);
			dati = (Vector)dati.elementAt(0);
			String notCartella = (String)dati.elementAt(0);
			int pos = notCartella.indexOf("|");
			if (pos<0) throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Mancano parametri CDDB: " + notCartella, sql, this);
			String cddbCat = notCartella.substring(0,pos);
			String cddbId = notCartella.substring(pos + 1);
			FreeDB cd = new FreeDB();
			CDDBRecord cdRec = new CDDBRecord(cddbCat, cddbId, "");
			CDInfo cdInfo = cd.readCDInfo(cdRec);
			String xmcd = cdInfo.getXmcdContent();
			CDDBXmcdParser cdParser = new CDDBXmcdParser(xmcd);
			
			BmaDataTable tabella = getJModel().getDataTable(jTrx, "MD_BRANITEMP");
			int nTrack = cdParser.readNumberOfTracks();
			for (int i=0; i<nTrack; i++) {
				String sTrack = Integer.toString(i+1);
				while (sTrack.length()<2) sTrack = "0" + sTrack;
				sql = "SELECT NUM_PROGRESSIVO, DES_FILE " +
							" FROM MD_BRANITEMP " +
							" WHERE COD_CARTELLA='" + codCartella + "' " +
							" AND DES_FILE LIKE '%" + sTrack + " -%'";
				dati = jTrx.eseguiSqlSelect(sql);
				if (dati.size()!=1) {
					String word = getMaxWord(cdParser.readTrackTitle(i), 15);
					int p = word.indexOf("'");
					while (p>=0) {
						word = word.substring(0, p) + "'" + word.substring(p);
						p = word.indexOf("'", p+2);
					}
					sql = "SELECT NUM_PROGRESSIVO, DES_FILE " +
								" FROM MD_BRANITEMP " +
								" WHERE COD_CARTELLA='" + codCartella + "' " +
								" AND DES_FILE LIKE '%" + word + "%'";
					dati = jTrx.eseguiSqlSelect(sql);
				}					
				if (dati.size()==1) {
					dati = (Vector)dati.elementAt(0);
					String numProgressivo = (String)dati.elementAt(0);
					String desFile = (String)dati.elementAt(1);
					Hashtable valori = new Hashtable();
					valori.put("COD_CARTELLA", codCartella);
					valori.put("NUM_PROGRESSIVO", numProgressivo);
					valori.put("DES_FILE", desFile);
					valori.put("DES_TRACK", sTrack);
					valori.put("DES_ALBUM", capitalizeWords(cdParser.readCDTitle()));
					valori.put("DES_ARTIST", capitalizeWords(cdParser.readTrackArtist(i)));
					valori.put("DES_GENRE", capitalizeWords(cdParser.readGenre()));
					valori.put("DES_TITLE", capitalizeWords(cdParser.readTrackTitle(i)));
					valori.put("DES_YEAR", Integer.toString(cdParser.readYear()));
					jTrx.eseguiSqlUpdate(tabella.getSqlUpdate(valori));
				}
			}
			jTrx.chiudi();
		}
		catch (BmaException bma) {
			if (jTrx.isAperta()) jTrx.invalida();
			throw bma;
		}
		catch (IOException io) {
			if (jTrx.isAperta()) jTrx.invalida();
			throw new BmaException("IOException", io.getMessage());
		}
		catch (ParseException pe) {
			if (jTrx.isAperta()) jTrx.invalida();
			throw new BmaException("ParseException", pe.getMessage());
		}
	}
	public String checkBrani(String checkDir) throws BmaException {
		MP3Driver mp3Driver = new MP3Driver();
		XMLDriver xmlDriver = new XMLDriver();
		Document document = xmlDriver.getDocument();
		Element root = xmlDriver.setRootElement(document, "CheckBrani");
		String dirPath = getUserConfig().getParametroApplicazione("MEDIA-WORK-DIR") + "/" + checkDir;
		File dir = new File(dirPath);
		File[] list = dir.listFiles();
		for (int i=0;i<list.length;i++) {
			if (mp3Driver.isMP3File(list[i].getAbsolutePath())) {
				mp3Driver.setFile(list[i].getAbsolutePath());
				Properties props = mp3Driver.getProperties();
				if (!mp3Driver.isComplete(props, new String[]{mp3Driver.TAG_COMMENT})) {
					Element element = xmlDriver.addElement(root, "File");
					element.setAttribute("Name", list[i].getName());
					String[] tags = mp3Driver.getPropertiesNames();
					for (int j=0;j<tags.length;j++) {
						element.setAttribute(tags[j], props.getProperty(tags[j]));
					}
				}
			}
		}
		return xmlDriver.serializeDocument(document);
	}
	public void matchBrani(String codCartella) throws BmaException {
		MP3Driver mp3Driver = new MP3Driver();
		XMLDriver xmlDriver = new XMLDriver();
		Document document = xmlDriver.getDocument();
		Element root = xmlDriver.setRootElement(document, "ReloadBrani");
		String sql = "";
		BmaJdbcTrx jTrx = new BmaJdbcTrx(getJdbcSource());
		String dirPath = getUserConfig().getParametroApplicazione("MEDIA-WORK-DIR") + "/";
		try {
			jTrx.open("System");
			sql = "SELECT NUM_PROGRESSIVO, DES_FILE, " +
			"				DES_ARTIST, DES_ALBUM, " +
			"				DES_TRACK,	DES_TITLE, " +
			"				DES_GENRE,	DES_YEAR " +
			" FROM	MD_BRANITEMP " +
			" WHERE	COD_CARTELLA='" + codCartella + "' " +
			" ORDER BY NUM_PROGRESSIVO ";
			Vector dati = jTrx.eseguiSqlSelect(sql);
			
			if (dati.size()>0) {
				sql = "DELETE FROM MD_FILETRACCE " +
							" WHERE	COD_CARTELLA='" + codCartella + "'";
				jTrx.eseguiSqlUpdate(sql);
			}
			
			for (int i=0; i<dati.size();i++) {
				Vector riga = (Vector)dati.elementAt(i);
				String numProgressivo = (String)riga.elementAt(0);
				String desFile				= (String)riga.elementAt(1);
				String desArtist			= (String)riga.elementAt(2);
				String desAlbum				=	(String)riga.elementAt(3);
				String desTrack				= (String)riga.elementAt(4);
				String desTitle				= (String)riga.elementAt(5);
				String desGenre				= (String)riga.elementAt(6);
				String desYear				= (String)riga.elementAt(7);
				
				Hashtable valori = new Hashtable();
				valori.put("DES_AUTORE", desArtist);
				BmaDataTable tabella = getJModel().getDataTable(jTrx, "MD_AUTORI");
				Vector result = jTrx.eseguiSqlSelect(tabella.getSqlReadKey(valori));
				if (result.size()==0) jTrx.eseguiSqlUpdate(tabella.getSqlInsert(valori));
				
				valori.put("DES_ALBUM", desArtist + " - " + desAlbum);
				tabella = getJModel().getDataTable(jTrx, "MD_ALBUMAUDIO");
				result = jTrx.eseguiSqlSelect(tabella.getSqlReadKey(valori));
				if (result.size()==0) jTrx.eseguiSqlUpdate(tabella.getSqlInsert(valori));
				
				valori.put("DES_BRANO", desTitle);
				tabella = getJModel().getDataTable(jTrx, "MD_BRANI");
				result = jTrx.eseguiSqlSelect(tabella.getSqlReadKey(valori));
				if (result.size()==0) jTrx.eseguiSqlUpdate(tabella.getSqlInsert(valori));

				valori.put("NUM_TRACCIA", desTrack);
				valori.put("DES_GENERE", desGenre);
				valori.put("DES_ANNO", desYear);
				tabella = getJModel().getDataTable(jTrx, "MD_TRACCEAUDIO");
				result = jTrx.eseguiSqlSelect(tabella.getSqlReadKey(valori));
				if (result.size()==0) jTrx.eseguiSqlUpdate(tabella.getSqlInsert(valori));
				
				valori.put("COD_CARTELLA", codCartella);
				valori.put("NUM_PROGRESSIVO", numProgressivo);
				valori.put("DES_FILE", desFile);
				tabella = getJModel().getDataTable(jTrx, "MD_FILETRACCE");
				result = jTrx.eseguiSqlSelect(tabella.getSqlReadKey(valori));
				if (result.size()==0) jTrx.eseguiSqlUpdate(tabella.getSqlInsert(valori));
				
			}
			
			if (dati.size()>0) {
				sql = "DELETE FROM MD_BRANITEMP " +
							" WHERE	COD_CARTELLA='" + codCartella + "'";
				jTrx.eseguiSqlUpdate(sql);
			}
			
			jTrx.chiudi();
		}
		catch (BmaException bma) {
			if (jTrx.isAperta()) jTrx.invalida();
			throw bma;
		}
	}
	private String capitalizeWords(String in) {
		Matcher m = Pattern.compile("\\b(\\w)").matcher(in.toLowerCase());
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, m.group(1).toUpperCase());
		}
		m.appendTail(sb);
		return sb.toString();
	}
	private String getMaxWord(String in, int len) {
		if (in.length()<=len) return in;
		int p = in.indexOf(" ");
		while (p>=0) {
			if (p>=len) return in.substring(0, p).trim();
			p = in.indexOf(" ", p+1);
		}
		return in.substring(0, len).trim();
	}
}
