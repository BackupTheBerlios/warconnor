package it.bma.comuni;

import java.util.*;
public abstract class BmaObject implements java.io.Serializable, 
																						Cloneable, BmaLiterals,
																						BmaErrorCodes, BmaMessages {
	private int xmlParsePos = 0;
public BmaObject() {
	super();
}
public String booleanToString(boolean valore) {
	String s = BMA_FALSE;
	if (valore) s = BMA_TRUE;
	return s;
}
public String getNumeroXChiave(int intero, int digits) {
	String s = Integer.toString(intero);
	while (s.length()<digits) {
		s = "0" + s;
	}
	return s;
}
public BmaObject cloneXml() throws BmaException {
	return cloneXml(this);
}
public BmaObject cloneXml(BmaObject old) throws BmaException {
	try {
		BmaObject o = (BmaObject) old.clone();
		String xml = this.toXml();
		o.fromXml(xml);
		return o;
	}
	catch (CloneNotSupportedException e) {
		throw new BmaException(BMA_ERR_XML_GENERICO, BMA_MSG_XML_CLONE, e.getMessage(), this);
	}
}
private String fieldsToXml() {
	String xml = "";
	java.lang.reflect.Field[] props = getMyFields(new java.lang.reflect.Field[0], this.getClass());
	for (int i = 0; i < props.length; i++) {
		java.lang.reflect.Field fld = props[i];
		int fldModifiers = fld.getModifiers();
		if (!java.lang.reflect.Modifier.isStatic(fldModifiers) &&
				!java.lang.reflect.Modifier.isFinal(fldModifiers)) {
			String tipo = fld.getType().getName();
			String nome = fld.getName();
			try {
				boolean isA = fld.isAccessible();
				fld.setAccessible(true);
//				if (fld.get(this)==null) return getXml(fld.getName(),BMA_XML_NULL_ELEMENT);
				Object obj = fld.get(this);
				if (tipo.equals("boolean")) {
					xml = xml + getXml(nome, fld.getBoolean(this));
				}
				else if (tipo.equals("java.lang.String")) {
					xml = xml + getXml(nome, obj.toString());
				}
				else if (tipo.equals("byte")) {
					xml = xml + getXml(nome, fld.getByte(this));
				}
				else if (tipo.equals("char")) {
					xml = xml + getXml(nome, fld.getChar(this));
				}
				else if (tipo.equals("double")) {
					xml = xml + getXml(nome, fld.getDouble(this));
				}
				else if (tipo.equals("float")) {
					xml = xml + getXml(nome, fld.getFloat(this));
				}
				else if (tipo.equals("int")) {
					xml = xml + getXml(nome, fld.getInt(this));
				}
				else if (tipo.equals("long")) {
					xml = xml + getXml(nome, fld.getLong(this));
				}
				else if (tipo.equals("short")) {
					xml = xml + getXml(nome, fld.getShort(this));
				}
				else if (tipo.equals("java.util.Hashtable")) {
					xml = xml + getXml(nome, (Hashtable)fld.get(this));
				}
				else if (tipo.equals("java.util.Vector")) {
					xml = xml + getXml(nome, (Vector)fld.get(this));
				}
				else {
					try {
						BmaObject objc = (BmaObject) obj;
						if (objc==null) {
							xml = xml + getXml(nome, "");
						} 
						else {
							xml = xml + objc.toXml();						
						}
					}
					catch (ClassCastException cce) {
						xml = xml + getXml(nome, "Not BmaObject - Parsing error: " + tipo);
					}
					fld.setAccessible(isA);
				}
			}
			catch (IllegalAccessException iae) {
				xml = xml + getXml(fld.getName(), iae.getMessage());
			}
		}
	}
	return xml;
}
private String findXmlTag(String text, String tag) throws BmaException {
	String closeTag =  "";
	String openTag = "<" + tag + ">";
	int pos = text.indexOf(openTag, xmlParsePos);
	if (pos >= 0) {
		pos = pos + openTag.length();
		closeTag = "</" + tag + ">";
		int pos2 = text.indexOf(closeTag, pos);
		if (pos2 < 0) {
			throw new BmaException(BMA_ERR_XML_PARSE, BMA_MSG_XML_TAG_NONTROVATO + ": " + tag);
		}
		else {
			xmlParsePos = pos2 + closeTag.length();
			return text.substring(pos, pos2);
		}
	}
	closeTag = "<" + tag + "/>";
	pos = text.indexOf(closeTag, xmlParsePos);
	if (pos < 0) {
		xmlParsePos = pos;
	}
	else {
		xmlParsePos = xmlParsePos + closeTag.length();
	}
	return "";
}
public void fromXml(String text) throws BmaException {
	String temp = parseXmlTag(text, getXmlTag(), 0);
	java.lang.reflect.Field[] props = getMyFields(new java.lang.reflect.Field[0], this.getClass());
	for (int i = 0; i < props.length; i++) {
		java.lang.reflect.Field fld = props[i];
		int fldModifiers = fld.getModifiers();
		if (!java.lang.reflect.Modifier.isStatic(fldModifiers) &&
				!java.lang.reflect.Modifier.isFinal(fldModifiers)) {
			String fldType = fld.getType().getName();
			String fldName = fld.getName();
			try {
				boolean isA = fld.isAccessible();
				fld.setAccessible(true);
				Object obj = fld.get(this);
				if (fldType.equals("boolean")) {
					fld.setBoolean(this, parseXmlTagBoolean(temp, fldName, 0));
				}
				else if (fldType.equals("java.lang.String")) {
					fld.set(this, parseXmlTag(temp, fldName, 0));
				}
				else if (fldType.equals("char")) {
					fld.setChar(this, parseXmlTagChar(temp, fldName, 0));
				}
				else if (fldType.equals("int")) {
					fld.setInt(this, parseXmlTagInt(temp, fldName, 0));
				}
				else if (fldType.equals("byte")) {
					fld.setByte(this, parseXmlTagByte(temp, fldName, 0));
				}
				else if (fldType.equals("short")) {
					fld.setShort(this, parseXmlTagShort(temp, fldName, 0));
				}
				else if (fldType.equals("float")) {
					fld.setFloat(this, parseXmlTagFloat(temp, fldName, 0));
				}
				else if (fldType.equals("long")) {
					fld.setLong(this, parseXmlTagLong(temp, fldName, 0));
				}
				else if (fldType.equals("double")) {
					fld.setDouble(this, parseXmlTagDouble(temp, fldName, 0));
				}
				else if (fldType.equals("java.util.Hashtable")) {
					fld.set(this, parseXmlTagHashtable(temp, fldName, 0));
				}
				else if (fldType.equals("java.util.Vector")) {
					fld.set(this, parseXmlTagVector(temp, fldName, 0));
				}
				else {
					try {
						BmaObject objc = (BmaObject) obj;
						objc.fromXml(temp);
					}
					catch (ClassCastException cce) {
						throw new BmaException(BMA_ERR_XML_GENERICO, cce.getClass().getName(),cce.getMessage(),this);
					}
					fld.setAccessible(isA);
				}
			}
			catch (IllegalAccessException iae) {
				throw new BmaException(BMA_ERR_XML_GENERICO, iae.getClass().getName(),iae.getMessage(),this);
			}
		}
	}
}
public void fromXmlFile(String file) throws BmaException {
	String xml = "";
	try {
		java.io.FileReader fr = new java.io.FileReader(file);
		java.io.BufferedReader br = new java.io.BufferedReader(fr);
		String s = null;
		while ((s = br.readLine()) != null) {
			xml = xml + s;
		}
		br.close();
		fr.close();
		br = null;
		fr = null;
		fromXml(xml);
	}
	catch (java.io.FileNotFoundException fnf) {
		throw new BmaException(BMA_ERR_IO__FILE, BMA_MSG_ERROREFILE, fnf.getMessage(), this);
	}
	catch (java.io.IOException ioe) {
		throw new BmaException(BMA_ERR_IO__READ, BMA_MSG_ERROREFILE, ioe.getMessage(), this);
	}
}
public abstract String getChiave();
public String getClassName() {
	return getClassName(getClass().getName());
}
public String getClassName(String n) {
	int i = n.indexOf(".");
	while (i >= 0) {
		n = n.substring(i + 1);
		i = n.indexOf(".");
	}
	return n;
}
private java.lang.reflect.Field[] getMyFields(java.lang.reflect.Field[] oldList, Class cls) {
	java.lang.reflect.Field[] myList = cls.getDeclaredFields();
	java.lang.reflect.Field[] newList = new java.lang.reflect.Field[oldList.length + myList.length];
	int i = 0;
	while (i<oldList.length) {
		newList[i] = oldList[i];
		i++;
	}
	for (int j = 0; j < myList.length; j++) {
		newList[i + j] = myList[j];
	}
	Class sc = cls.getSuperclass();
	if (!getClassName(sc.getName()).equals("BmaObject")) {
		newList = getMyFields(newList, sc);
	}	
	return newList;
}
public String getXml(String nome, byte valore) {
	String s = Byte.toString(valore);
	return getXml(nome, s);
}
public String getXml(String nome, char valore) {
	String s = "" + valore + "";
	return getXml(nome, s);
}
public String getXml(String nome, double valore) {
	String s = Double.toString(valore);
	return getXml(nome, s);
}
public String getXml(String nome, float valore) {
	String s = Float.toString(valore);
	return getXml(nome, s);
}
public String getXml(String nome, int valore) {
	String s = Integer.toString(valore);
	return getXml(nome, s);
}
public String getXml(String nome, long valore) {
	String s = Long.toString(valore);
	return getXml(nome, s);
}
public String getXml(String nome, String valore) {
	if (valore==null || valore.length()==0) return "<" + nome + "/>";
	else return "<" + nome + ">" + valore + "</" + nome + ">";
}
public String getXml(String name, Hashtable list) {
	String className = "";
	String xml = "";
	if (list.size()==0) return getXml(name, "");
	xml = xml + getXml(BMA_XML_ELEMENTS_NUMBER, Integer.toString(list.size()));
	Enumeration e = list.keys();
	while (e.hasMoreElements()) {
		String k = (String) e.nextElement();
		Object obj = list.get(k);
		if (className.length()==0) {
			className = obj.getClass().getName();
			xml = xml + getXml(BMA_XML_ELEMENTS_CLASS, className);
		}
		if (obj.getClass().getName().equals("java.lang.String")) {
			String t = getXml("nome", k) + getXml("valore", (String)obj);
			xml = xml + getXml(BMA_XML_STRING_ELEMENT, t);
		}
		else {
			try {
				BmaObject oBma = (BmaObject)obj;
				xml = xml + oBma.toXml();
			} 
			catch (ClassCastException cce) {			
				return getXml(name, "ClassCastException: " + cce.getMessage());
			}
		}	
	}
	return getXml(name, xml);
}
public String getXml(String name, Vector list) {
	String className = "";
	String xml = "";
	if (list.size()==0) return getXml(name, "");
	xml = xml + getXml(BMA_XML_ELEMENTS_NUMBER, Integer.toString(list.size()));
	Enumeration e = list.elements();
	while (e.hasMoreElements()) {
		Object obj = e.nextElement();
		if (className.length()==0) {
			className = obj.getClass().getName();
			xml = xml + getXml(BMA_XML_ELEMENTS_CLASS, className);
		}
		if (obj.getClass().getName().equals("java.lang.String")) {
			xml = xml + getXml("valore", (String)obj);
		}
		else {	
			try {
				BmaObject oBma = (BmaObject)obj;
				xml = xml + oBma.toXml();
			} 
			catch (ClassCastException cce) {			
				return getXml(name, "ClassCastException: " + cce.getMessage());
			}
		}
	}
	return getXml(name, xml);
}
public String getXml(String nome, short valore) {
	String s = Short.toString(valore);
	return getXml(nome, s);
}
public String getXml(String nome, boolean valore) {
	return getXml(nome, booleanToString(valore));
}
protected abstract String getXmlTag();
public String getXmlTop(String nome, String valore) {
	String top = "<?xml version='1.0' encoding='ISO-8859-1' ?>" + '\n';
	top = top + "<?xml:stylesheet type='text/xsl' ?>" + '\n';
	if (nome.trim().length()==0) {
		return top + valore;
	}
	else {
		return  top + getXml(nome, valore);
	}
}
public Object invokeMethod(String name, Object[] params) throws BmaException {
	Class[] pList = new Class[params.length];
	for (int i = 0; i < params.length; i++){
		pList[i]=params[i].getClass();
	}
	try {
		java.lang.reflect.Method m = this.getClass().getMethod(name, pList);
		return m.invoke(this, params);
	} 
	catch (NoSuchMethodException e1) {
		throw new BmaException(BMA_ERR_SYS_INTERNO, BMA_MSG_REF_METODO_ERRATO + "(" + name + ")","NoSuchMethodException",this);
	}
	catch (IllegalAccessException e2) {
		throw new BmaException(BMA_ERR_SYS_INTERNO, BMA_MSG_REF_METODO_ERRATO + "(" + name + ")","IllegalAccessException",this);
	}
	catch (java.lang.reflect.InvocationTargetException e3) {
		throw new BmaException(BMA_ERR_NON_PREVISTO, BMA_MSG_REF_METODO_ERRATO + "(" + name + ")", 
				e3.getTargetException().getClass().getName() + "(" + 
				e3.getTargetException().getMessage() + ")",this);
	}
}
public boolean isCollection() {
	return false;
}
protected String makeKey(String[] keyList) {
	String k = keyList[0];
	for (int i = 1; i < keyList.length; i++){
		k = k + BMA_KEY_SEPARATOR + keyList[i];
	}
	return k;
}
public BmaObject objectInstance(String objClass) throws BmaException {
	try {
		BmaObject newObject = (BmaObject)Class.forName(objClass).newInstance();
		return newObject;
	}
	catch (ClassNotFoundException e0) {
		throw new BmaException(BMA_ERR_SYS_INTERNO, BMA_MSG_REF_CLASSE_ERRATA, e0.getMessage(), this);
	}
	catch (InstantiationException e1) {
		throw new BmaException(BMA_ERR_SYS_INTERNO, BMA_MSG_REF_CLASSE_ERRATA, e1.getMessage(), this);
	}
	catch (IllegalAccessException e2) {
		throw new BmaException(BMA_ERR_SYS_INTERNO, BMA_MSG_REF_CLASSE_ERRATA, e2.getMessage(), this);
	}
	
}
protected Vector parseXmlList(String text, String tag) throws BmaException {
	String openTag = "<" + tag + ">";
	Vector v = new Vector();
	xmlParsePos = 0;
	while (xmlParsePos >= 0) {
		String value = findXmlTag(text, tag);
		if (xmlParsePos > 0) {
			v.add(getXml(tag, value));
		}
	}	
	return v;
}
protected String parseXmlTag(String text, String tag, int start) throws BmaException {
	xmlParsePos = start;
	String temp = findXmlTag(text, tag);
	if (xmlParsePos < 0) {
		throw new BmaException(BMA_ERR_XML_GENERICO, BMA_MSG_XML_TAG_CHIUSURA + ": " + tag, "", this);
	}
	return temp;
}
protected boolean parseXmlTagBoolean(String text, String tag, int start) throws BmaException {
	String s = parseXmlTag(text, tag, start);
	return s.equals(BMA_TRUE);
}
protected byte parseXmlTagByte(String text, String tag, int start) throws BmaException {
	String s = parseXmlTag(text, tag, start);
	if (s.length()==0) return 0;
	return Byte.parseByte(s);
}
protected char parseXmlTagChar(String text, String tag, int start) throws BmaException {
	String s = parseXmlTag(text, tag, start);
	return s.charAt(0);
}
protected double parseXmlTagDouble(String text, String tag, int start) throws BmaException {
	String s = parseXmlTag(text, tag, start);
	if (s.length()==0) return 0;
	return Double.parseDouble(s);
}
protected float parseXmlTagFloat(String text, String tag, int start) throws BmaException {
	String s = parseXmlTag(text, tag, start);
	if (s.length()==0) return 0;
	return Float.parseFloat(s);
}
protected Hashtable parseXmlTagHashtable(String text, String tag, int start) throws BmaException {
	Hashtable ht = new Hashtable();
	BmaObject obj = null;
	String temp = parseXmlTag(text, tag, start);
	if (temp.length() > 0) {
		String className = parseXmlTag(temp, BMA_XML_ELEMENTS_CLASS, 0);
		if (className.equals("java.lang.String")) {
			Vector v = parseXmlList(temp, BMA_XML_STRING_ELEMENT);
			for (int i = 0; i < v.size(); i++) {
				String tagElement = (String) v.elementAt(i);
				String nome = parseXmlTag(tagElement, "nome", 0);
				String valore = parseXmlTag(tagElement, "valore", 0);
				ht.put(nome, valore);
			}
		}
		else {
			try {
				BmaObject o = (BmaObject)objectInstance(className);
				Vector v = parseXmlList(temp, o.getXmlTag());
				for (int i = 0; i < v.size(); i++) {
					String tagElement = (String) v.elementAt(i);
					o = (BmaObject)objectInstance(className);
					o.fromXml(tagElement);
					ht.put(o.getChiave(), o);
				}
			} 
			catch (ClassCastException cce) {
				throw new BmaException(BMA_ERR_SYS_INTERNO, BMA_MSG_REF_CLASSE_ERRATA + ": " + cce.getMessage(), "", this);
			}
		}
	}
	return ht;
}
protected int parseXmlTagInt(String text, String tag, int start) throws BmaException {
	String s = parseXmlTag(text, tag, start);
	if (s.length()==0) return 0;
	return Integer.parseInt(s);
}
protected long parseXmlTagLong(String text, String tag, int start) throws BmaException {
	String s = parseXmlTag(text, tag, start);
	if (s.length()==0) return 0;
	return Long.parseLong(s);
}
protected short parseXmlTagShort(String text, String tag, int start) throws BmaException {
	String s = parseXmlTag(text, tag, start);
	if (s.length()==0) return 0;
	return Short.parseShort(s);
}
protected Vector parseXmlTagVector(String text, String tag, int start) throws BmaException {
	Vector ht = new Vector();
	BmaObject obj = null;
	String temp = parseXmlTag(text, tag, start);
	if (temp.length() > 0) {
		String className = parseXmlTag(temp, BMA_XML_ELEMENTS_CLASS, 0);
		if (className.equals("java.lang.String")) {
			Vector v = parseXmlList(temp, "valore");
			for (int i = 0; i < v.size(); i++){
				String tagElement = (String) v.elementAt(i);
				String valore = parseXmlTag(tagElement, "valore",  0);
				ht.add(valore);
			}
		}
		else {
			try {
				BmaObject o = (BmaObject)objectInstance(className);
				Vector v = parseXmlList(temp, o.getXmlTag());
				for (int i = 0; i < v.size(); i++) {
					String tagElement = (String) v.elementAt(i);
					o = (BmaObject)objectInstance(className);
					o.fromXml(tagElement);
					ht.add(o);
				}
			} 
			catch (ClassCastException cce) {
				throw new BmaException(BMA_ERR_SYS_INTERNO, BMA_MSG_REF_CLASSE_ERRATA + ": " + cce.getMessage(), "", this);
			}
		}
	}
	return ht;
}
public String toXml() {
	return getXml(getXmlTag(), fieldsToXml());
}
public void toXmlFile(String file) throws BmaException {
	String xml = getXmlTop("", toXml());
	try {
		java.io.FileWriter fw = new java.io.FileWriter(file, false);
		java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
		bw.write(xml);
		bw.newLine();
		bw.flush();
		bw.close();
		fw.close();
	}
	catch (java.io.IOException ioe) {
		throw new BmaException(BMA_ERR_IO__WRITE, BMA_MSG_ERROREFILE, ioe.getMessage(), this);
	}
}
public String urlDecode(String text) {
	try {
		return java.net.URLDecoder.decode(text, "UTF-8");	
	} 
	catch (Exception e) {
		return "";
	}
}
public String urlEncode(String text) {
	try {
		return java.net.URLEncoder.encode(text, "UTF-8");
	}
	catch (Exception e) {
		return "";
	}
}

public String[] getSortedTableKeys(Hashtable table) {
	String[] list = new String[table.size()];
	Enumeration e = table.keys();
	int i = 0;
	while (e.hasMoreElements()) { 
		list[i] = (String)e.nextElement();
		i = i + 1;
	}
	Arrays.sort(list);
	return list;
}

public boolean stringToBoolean(String valore) {
	return (valore.equals(BMA_TRUE));
}

public void writeFile(String message, String fileFullPath, boolean append) throws BmaException {
	try {
		java.io.FileWriter fw = new java.io.FileWriter(fileFullPath, append);
		java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
		bw.write(message);
		bw.newLine();
		bw.flush();
		bw.close();
		fw.close();
	}
	catch (java.io.IOException ioe) {
		throw new BmaException(BMA_ERR_IO__WRITE, BMA_MSG_ERROREFILE, ioe.getMessage(), this);
	}
}
}
