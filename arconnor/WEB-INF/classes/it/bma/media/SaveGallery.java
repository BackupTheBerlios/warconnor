package it.bma.media;
import java.io.*;
import java.util.*;
import it.bma.comuni.*;
public class SaveGallery {
	private static final String NAME_SEPARATOR = "|";
	public SaveGallery() {
	}
	public static void main(String[] args) {
		if (args.length!=2) {
      System.out.println("Impostare il nome della gallery ed il percorso di destinazione");
      return;
    }
    String galName = args[0];    
    String dirDest = args[1];    
		BmaJdbcSource source = new BmaJdbcSource("THUMBS");
		source.setDriver("sun.jdbc.odbc.JdbcOdbcDriver");
		source.setUrl("jdbc:odbc:THUMBS");
		source.setUser("admin");
		source.setPass("");
		source.setSchema("");
		BmaJdbcTrx trx = new BmaJdbcTrx(source);
		try {
			trx.open("System");
			String sql = "";
			sql = "SELECT NUM_PROGRESSIVO, DES_DRIVE, " +
						"				DES_PATH, DES_FILE " +
						" FROM	MD_QueryImmaginiAlbum " +
						" WHERE	COD_CARTELLA='" + galName + "' " +
						" ORDER BY NUM_PROGRESSIVO ";
			Vector dati = trx.eseguiSqlSelect(sql);
			for (int i=0; i<dati.size(); i++) {
				Vector riga = (Vector)dati.elementAt(i);
				String nameIn = (String)riga.elementAt(1) + "\\" +
												(String)riga.elementAt(2) + "\\" +
												(String)riga.elementAt(3);
				String p = (String)riga.elementAt(0);
				while (p.length()<3) {
					p = "0" + p;
				}
				String nameOut = dirDest + "/" + p + "-" + (String)riga.elementAt(3);
				File fileIn = new File(nameIn);
				InputStream is = new BufferedInputStream(new FileInputStream(fileIn));
				OutputStream os = new BufferedOutputStream(new FileOutputStream(nameOut));
				byte[] buffer = new byte[(int)fileIn.length()];
				is.read(buffer);
				os.write(buffer);
				is.close();
				os.close();
			}
			trx.chiudi();
			System.out.println("Ok");
		}
		catch (IOException io) {
			if (trx.isAperta()) trx.invalida();
			System.out.println(io.getMessage());		
		}
		catch (BmaException e) {
			if (trx.isAperta()) trx.invalida();
			System.out.println(e.getMessage());
			System.out.println(e.getErrore().getCodErrore());
			System.out.println(e.getErrore().getMsgSistema());
			System.out.println(e.getErrore().getMsgUtente());
		}
	}	
}
