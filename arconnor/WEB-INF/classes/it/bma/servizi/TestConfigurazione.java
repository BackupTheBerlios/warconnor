package it.bma.servizi;

import it.bma.comuni.*;
/**
 * Insert the type's description here.
 * Creation date: (03/12/2002 11.31.58)
 * @author: Administrator
 */
public class TestConfigurazione extends BmaServizio {
/**
 * TestConfigurazione constructor comment.
 */
public TestConfigurazione() {
	super();
}
public String esegui(BmaJdbcTrx trx) throws BmaException {
	return userConfig.toXml();
}
}
