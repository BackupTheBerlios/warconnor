package it.bma.comuni;

import java.util.*;

public class JdbcImportedKey extends BmaObject {
	protected String pkTableCat = "";
	protected String pkTableSchem = "";
	protected String pkTableName = "";
	protected String fkTableCat = "";
	protected String fkTableSchem = "";
	protected String fkTableName = "";
	protected Hashtable fkColumns = new Hashtable();
	protected String updateRule = "";
	protected String deleteRule = "";
	protected String fkName = "";
	protected String pkName = "";
	protected String defferrability = "";
public JdbcImportedKey() {
	super();
}
public String getChiave() {
	return pkTableSchem + BMA_KEY_SEPARATOR + pkTableName;
}
protected java.lang.String getXmlTag() {
	return getClassName();
}
}
