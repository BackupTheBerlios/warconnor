package it.bma.db;
import java.io.*;
import java.sql.*;
import it.bma.comuni.*;
public class JdbcRunSqlScript {
	private static final String COMMAND_TERMINATOR = ";";
	public JdbcRunSqlScript() {
	}
	public static void main(String[] args) {
		if (args.length==0) {
      System.out.println("Impostare il percorso del file contenente lo script SQL");
      return;
    }
    String fileName = args[0];    
		BmaJdbcSource source = new BmaJdbcSource("TEST");
//		source.setDriver("com.ibm.db2.jcc.DB2Driver");
//		source.setUrl("jdbc:db2://PCWIN002:50000/PDAS");
		source.setDriver("COM.ibm.db2.jdbc.net.DB2Driver");
		source.setUrl("jdbc:db2://PCWIN001/PDAS_PC1");
		source.setUser("db2admin");
		source.setPass("db2admin");
		source.setSchema("");
		BmaJdbcTrx trx = new BmaJdbcTrx(source);
		try {
			FileReader file = new FileReader(fileName);
			BufferedReader fileBuffer = new BufferedReader(file);
			String streamIn = "";
			String s = null;
			while ((s = fileBuffer.readLine()) != null) {
				streamIn = streamIn + s + '\n';
			}
			fileBuffer.close();
			file.close();
			trx.open("IO");
			int i = streamIn.indexOf(COMMAND_TERMINATOR);

			while(i>=0) {
				String sql = streamIn.substring(0, i).trim();
				streamIn = streamIn.substring(i + 1);
				trx.eseguiSqlUpdate(sql);
				System.out.println(sql);
				i = streamIn.indexOf(COMMAND_TERMINATOR);
			}

			trx.chiudi();
			System.out.println("Ok");
		}
		catch (IOException io) {
			if (trx.isAperta()) trx.invalida();
			System.out.println(io.getMessage());		
		}
/*
		catch (java.sql.SQLException se) {
			if (trx.isAperta()) trx.invalida();
			System.out.println(se.getMessage());		
		}
*/
		catch (BmaException e) {
			if (trx.isAperta()) trx.invalida();
			System.out.println(e.getMessage());
			System.out.println(e.getErrore().getCodErrore());
			System.out.println(e.getErrore().getMsgSistema());
			System.out.println(e.getErrore().getMsgUtente());
		}
	}	
}
