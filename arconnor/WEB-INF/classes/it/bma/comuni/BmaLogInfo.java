package it.bma.comuni;

import java.util.*;
/**
 * Insert the type's description here.
 * Creation date: (19/09/03 16.01.37)
 * @author: Administrator
 */
public class BmaLogInfo extends BmaObject {
	private String time = "";
	private String table = "";
	private String key = "";
	private String action = "";
	private String info = "";
/**
 * BmaLogInfo constructor comment.
 */
public BmaLogInfo() {
	super();
}
/**
 * BmaLogInfo constructor comment.
 */
public BmaLogInfo(String time, String table, String key, String action, String info) {
	super();
	if (time!=null) this.time = time;
	if (table!=null) this.table = table;
	if (key!=null) this.key = key;
	if (action!=null) this.action = action;
	if (info!=null) this.info = info;
}
/**
 * Insert the method's description here.
 * Creation date: (19/09/03 16.04.12)
 * @return java.lang.String
 */
public java.lang.String getAction() {
	return action;
}
/**
 * getChiave method comment.
 */
public String getChiave() {
	String k = table + BMA_KEY_SEPARATOR + key;
	if (time.trim().length()>0) k = time + BMA_KEY_SEPARATOR + k;
	return k;
}
/**
 * Insert the method's description here.
 * Creation date: (19/09/03 16.04.12)
 * @return java.lang.String
 */
public java.lang.String getInfo() {
	return info;
}
/**
 * Insert the method's description here.
 * Creation date: (19/09/03 16.04.12)
 * @return java.lang.String
 */
public java.lang.String getKey() {
	return key;
}
/**
 * Insert the method's description here.
 * Creation date: (19/09/03 16.04.12)
 * @return java.lang.String
 */
public java.lang.String getTable() {
	return table;
}
/**
 * Insert the method's description here.
 * Creation date: (19/09/03 16.04.12)
 * @return java.lang.String
 */
public java.lang.String getTime() {
	return time;
}
/**
 * getXmlTag method comment.
 */
protected String getXmlTag() {
	return getClassName();
}
/**
 * Insert the method's description here.
 * Creation date: (19/09/03 16.04.12)
 * @param newAction java.lang.String
 */
public void setAction(java.lang.String newAction) {
	action = newAction;
}
/**
 * Insert the method's description here.
 * Creation date: (19/09/03 16.04.12)
 * @param newInfo java.lang.String
 */
public void setInfo(java.lang.String newInfo) {
	info = newInfo;
}
/**
 * Insert the method's description here.
 * Creation date: (19/09/03 16.04.12)
 * @param newKey java.lang.String
 */
public void setKey(java.lang.String newKey) {
	key = newKey;
}
/**
 * Insert the method's description here.
 * Creation date: (19/09/03 16.04.12)
 * @param newTable java.lang.String
 */
public void setTable(java.lang.String newTable) {
	table = newTable;
}
/**
 * Insert the method's description here.
 * Creation date: (19/09/03 16.04.12)
 * @param newTime java.lang.String
 */
public void setTime(java.lang.String newTime) {
	time = newTime;
}
}
