package it.bma.comuni;

import java.util.*;
public class BmaCrossTable extends BmaObject {
	private String name = "";
	private Hashtable table = new Hashtable();
public BmaCrossTable() {
	super();
}
public BmaCrossTable(String name) {
	super();
	this.name = name;
}
public String getChiave() {
	return name;
}
public String getCrossValue(String baseValue) {
	return (String)table.get(baseValue);
}

protected String getXmlTag() {
	return name;
}

public String getBaseValue(String crossValue) {
	Enumeration e = table.keys();
	while (e.hasMoreElements()) {
		String k = (String)e.nextElement();
		String v = (String)table.get(k);
		if (v.equals(crossValue)) return k;
	}
	return "";
}

public void putValues(String baseValue, String crossValue) {
	table.put(baseValue, crossValue);
}
}
