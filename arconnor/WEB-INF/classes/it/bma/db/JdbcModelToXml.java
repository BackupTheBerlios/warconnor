package it.bma.db;
import java.io.*;
import java.sql.*;
import it.bma.comuni.*;
public class JdbcModelToXml {
	private static final String COMMAND_TERMINATOR = ";";
	public JdbcModelToXml() {
	}
	public static void main(String[] args) {
		if (args.length==0) {
      System.out.println("Impostare il path della cartella di lavoro");
      return;
    }
    String filePath = args[0];    
		BmaJdbcSource source = new BmaJdbcSource("JMODEL");
		BmaJdbcTrx trx = new BmaJdbcTrx(source);
		JdbcModel jModel = new JdbcModel();
		try {
			source.fromXmlFile(filePath + "/source.xml");
			trx.open("IO");
			jModel.load(trx, "", "PD_%");
			trx.chiudi();
			jModel.toXmlFile(filePath + "/jModel.xml");
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
