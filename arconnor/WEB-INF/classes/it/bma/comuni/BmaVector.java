package it.bma.comuni;

import java.util.*;
public class BmaVector extends BmaCollection {
	private Vector vect = new Vector();
public BmaVector() {
  super();
}
public BmaVector(String name) {
	super(name);
}
public void add(BmaObject obj) {
  vect.add(obj);
}
public void clear() {
	vect.clear();
}
public Enumeration elements() {
	return vect.elements();
}
public void fromXml(String xml) throws BmaException {
	vect = parseXmlTagVector(xml, getXmlTag(), 0);
}
public BmaHashtable getBmaHashtable() {
	if (this == null) return null;
	BmaHashtable table = new BmaHashtable(name);
	Enumeration e = vect.elements();
	while (e.hasMoreElements()) {
		BmaObject o = (BmaObject) e.nextElement();
		table.add(o);
	}
	return table;
}
public BmaObject getElement(int elementIndex) {
	if (this == null) return null;
	return (BmaObject) vect.elementAt(elementIndex);
}
public BmaObject getElement(String elementKey) {
	Enumeration e = vect.elements();
	while (e.hasMoreElements()) {
		BmaObject o = (BmaObject) e.nextElement();
		if (o.getChiave().equals(elementKey)) {
			return o;
		}
	}
	return null;
}
public int getSize() {
  if (this == null) return 0;
  return vect.size();
}
public String getString(int i) {
	if (i<0 || i>getSize()) return "";
	String v = "";
	Object o = vect.elementAt(i);
	if (o==null) return "";
	if (o.getClass().getName().equals("java.lang.String")) {
		v = (String)o;
	}
	else if (o.getClass().getName().equals("BmaParametro")) {
		BmaParametro obj = (BmaParametro)o;
		v = obj.getValore();
	}
	return v;
}
public Vector getVector() {
	return vect;
}
public void insertElementAt(BmaObject obj, int elementIndex) {
	vect.insertElementAt(obj, elementIndex);
}
public void remove(int i) {
	vect.remove(i);
}
public void setSize(int size) {
	vect.setSize(size);
}
public void setString(String nome, String valore) {
	add(new BmaParametro(nome, valore));
}
public void setString(String valore) {
	vect.add(valore);
}
public void setVector(Vector newVector) {
	vect = newVector;
}
public void sort() {
	Object[] list = vect.toArray();
	Arrays.sort(list);
	Vector v = new Vector();
	for (int i = 0; i < list.length; i++){
		v.add(list[i]);	
	}
	vect = v;
}
public String toXml() {
	return getXml(name, vect);
}
}
