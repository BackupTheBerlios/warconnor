package it.bma.servizi;

import it.bma.comuni.*;
import it.bma.web.*;
import java.util.*;
public class BmaDataModel extends BmaServizio {
	public BmaDataModel() {
		super();
	}
	public String esegui(BmaJdbcTrx trx) throws BmaException {
		String flgLoad = input.getInfoServizio("flgLoad");
		String execSql = input.getInfoServizio("execSql");
		if (flgLoad!=null && flgLoad.equals(BMA_TRUE)) {
			return jModel.getLoadOrder().toXml();
		}
		else if (execSql!=null && execSql.equalsIgnoreCase("Query")) {
			String sql = input.getInfoServizio("DES_SQL");
			BmaDataList result = trx.eseguiQuery(sql, "SqlSelect");
			return result.toXml();
		}
		else if (execSql!=null && execSql.equalsIgnoreCase("Command")) {
			String sql = input.getInfoServizio("DES_SQL");
			trx.eseguiSqlUpdate(sql);
			return "";
		}
		else {
			return jModel.toXml();
		}
	}
}
