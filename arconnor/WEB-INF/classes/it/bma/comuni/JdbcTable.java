package it.bma.comuni;

import java.util.*;

public class JdbcTable extends BmaObject {
	protected String tableCat = "";
	protected String tableSchem = "";
	protected String tableName = "";
	protected String tableType = "";
	protected String remarks = "";
	protected Vector columns = new Vector();
	protected Vector primaryKeys = new Vector();
	protected Vector importedKeys = new Vector();
public JdbcTable() {
	super();
}
public String getChiave() {
	if (tableSchem.trim().length()==0) return tableName;
	if (tableSchem.equals("%")) return tableName;
	return tableSchem + "." + tableName;
}
protected java.lang.String getXmlTag() {
	return getClassName();
}
public boolean isPrimaryKey(String columnName) {
	Enumeration e = primaryKeys.elements();
	while (e.hasMoreElements()) {
		String name = (String)e.nextElement();
		if (name.equals(columnName)) return true;
	}
	return false;
}
}
