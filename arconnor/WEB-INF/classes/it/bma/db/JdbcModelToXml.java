package it.bma.db;
import java.io.*;
import java.sql.*;
import it.bma.comuni.*;
public class JdbcModelToXml {
	private static final String COMMAND_TERMINATOR = ";";
	public JdbcModelToXml() {
	}
	public static void main(String[] args) {
		if (args.length!=2) {
      System.out.println("Impostare il path della cartella di lavoro e il nome del file di definizione della sorgente dati");
      return;
    }
    String filePath = args[0] + "/";    
		String fileSource = args[1];
		BmaJdbcSource source = new BmaJdbcSource("JMODEL");
		BmaJdbcTrx trx = new BmaJdbcTrx(source);
		JdbcModel jModel = new JdbcModel();
		try {
			if (1==1) throw new BmaException("Un Test", "Di Errore");
			source.fromXmlFile(filePath + fileSource);
			String dbName = source.getChiave();
			trx.open("IO");
			jModel.load(trx, "", source.getPrefix());
			trx.chiudi();
			jModel.xmlDomSave(filePath + dbName + "_model.xml");
			// Test reload...
			jModel.xmlDomLoad(filePath + dbName + "_model.xml");
			System.out.println("Ok");
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
