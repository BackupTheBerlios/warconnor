package it.bma.servizi;

import it.bma.comuni.*;
import it.bma.web.*;
import java.util.*;

public abstract class BmaServizio extends BmaServizioInterno {
	protected final String BMA_SRV_PKG_PORTAFOGLIO = "it.bma.ptf.";
	public BmaServizio() {
		super();
	}
	protected void applicaStandardDati(BmaDataTable tabella) {
		for (int i = 0; i < tabella.getColonne().size(); i++){
			BmaDataColumn c = (BmaDataColumn)tabella.getColonne().elementAt(i);
//			c.setCaseSensitive(!c.isChiave());
			String n = c.getNome();
			if (n.length()>4) n = n.substring(0, 4);
			if (n.equals("FLG_")) {
				c.setTipoControllo(BMA_CONTROLLO_BOOLEAN);
				c.getValoriControllo().put(BMA_TRUE, "Sì");
				c.getValoriControllo().put(BMA_FALSE, "No");
			}
			else if (n.equals("DAT_")) {
				c.setTipo(BMA_SQL_TYP_DATECHAR);
			}
			else if (n.equals("IND_")) c.setTipoControllo(BMA_CONTROLLO_BOOLEAN);
			else if (n.equals("NOT_")) {
				c.setTipoControllo(BMA_CONTROLLO_TEXT);
				c.setCaseSensitive(true);
			}
			else if (n.equals("OBJ_")) {
				c.setTipoControllo(BMA_CONTROLLO_LINK);
				c.setCaseSensitive(true);
			}
		}
	}
}
