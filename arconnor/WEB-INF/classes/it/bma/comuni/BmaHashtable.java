package it.bma.comuni;

import java.util.*;
public class BmaHashtable extends BmaCollection {
	private Hashtable table = new Hashtable();
public BmaHashtable() {
	super();
}
public BmaHashtable(String name) {
	super(name);
}
public void add(BmaObject obj) {
	table.put(obj.getChiave(), obj);
}
public void clear() {
	table.clear();
}
public Enumeration elements() {
	return table.elements();
}
public void fromXml(String xml) throws BmaException {
	table = parseXmlTagHashtable(xml, getXmlTag(), 0);
}
public BmaVector getBmaVector() {
	BmaVector v = new BmaVector(name);
	String[] list = getSortedTableKeys(table);
	for (int i = 0; i < list.length; i++){
		v.add((BmaObject)table.get(list[i]));
	}
	return v;
}
public BmaObject getElement(String elementKey) {
	return (BmaObject) table.get(elementKey);
}
public Hashtable getHashtable() {
	return table;
}
public String getString(String nome) {
	String v = "";
	Object o = table.get(nome);
	if (o==null) return v;
	if (o.getClass().getName().equals("java.lang.String")) return (String)o;
	try {
		BmaParametro obj = (BmaParametro)o;
		return obj.getValore();
	}
	catch (ClassCastException e) {
		return v;
	}
}
public Hashtable getStringTable() {
	Hashtable ht = new Hashtable();
	Enumeration e = table.keys();
	while (e.hasMoreElements()) {
		String k = (String)e.nextElement();
		Object o = table.get(k);
		String n = o.getClass().getName();
		if (n.equals("java.lang.String")) ht.put(k, o);
		else if (n.equals("it.bma.comuni.BmaParametro")) {
			BmaParametro p = (BmaParametro)o;
			ht.put(p.getNome(), p.getValore());
		}
	}
	return ht;
}
public java.util.Hashtable getTable() {
	return table;
}
public Enumeration keys() {
	return table.keys();
}
public void remove(String key) {
	table.remove(key);
}
public void setElement(String name, BmaObject obj) {
	table.put(name, obj);
}
public void setHashtable(Hashtable values) {
	table = values;
}
public void setString(String nome, String valore) {
	table.put(nome, valore);
}
public void setTable(java.util.Hashtable newTable) {
	table = newTable;
}
public int size() {
	return table.size();
}
public String toXml() {
	return getXml(getXmlTag(), table);
}
}
