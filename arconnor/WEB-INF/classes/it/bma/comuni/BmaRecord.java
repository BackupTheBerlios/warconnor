package it.bma.comuni;

import java.util.*;
public class BmaRecord extends BmaObject {
	public final String PIC_X = "X";
	public final String PIC_9= "9";
	public final String PIC_S = "S";
	public final String PIC_COMP3 = "COMP-3";
	private Vector fields = new Vector();
public BmaRecord() {
	super();
}
public void addField(String name, String type, int len, int decimals) {
	BmaRecordField f = new BmaRecordField(name, type, len, decimals);
	fields.add(f);
}
private String fill (String in, int len, String type) {
	if (in.length()>=len) return in;
	char[] z = new char[len - in.length()];
	if (type.equals(PIC_X)) {
		java.util.Arrays.fill(z, '\u0020');
		return in + new String(z);
	}
	else {
		java.util.Arrays.fill(z, '0');
		return new String(z) + in;
	}	
}
public String getChiave() {
	return getClassName();
}
public String getRecordValue(BmaHashtable values) throws BmaException {
	String rec = "";
	int recLen = 0;
	for (int i = 0; i < fields.size(); i++){
		BmaRecordField f = (BmaRecordField)fields.elementAt(i);
		recLen = recLen + f.len;
		String v = values.getString(f.name);
		if (v==null) v = "";
		f.setValue(v);
		rec = rec + f.value;
	}
	return fill(rec,recLen,PIC_X);
}
protected String getXmlTag() {
	return getClassName();
}
public BmaHashtable parseRecord(String rec, int startByte) {
	BmaHashtable table = new BmaHashtable("RecordValues");
	// Determina la lunghezza record e completa il record
	int recLen = 0;
	for (int i = 0; i < fields.size(); i++){
		BmaRecordField f = (BmaRecordField)fields.elementAt(i);
		recLen = recLen + f.len;
	}
	rec = fill(rec, recLen,PIC_X);
	int startPos = startByte;
	for (int i = 0; i < fields.size(); i++){
		BmaRecordField f = (BmaRecordField)fields.elementAt(i);
		int endPos = startPos + f.len;
		if (endPos <= rec.length()) {
			f.value = f.parseValue(rec.substring(startPos, endPos));		
		} 
		else {
			f.value = "";	
		}
		startPos = endPos;
		table.setString(f.name, f.value);
	}
	return table;
}
}
