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
		if (flgLoad!=null && flgLoad.equals(BMA_TRUE)) {
			return jModel.getLoadOrder().toXml();
		} 
		else {
			return jModel.toXml();
		}
	}
}
