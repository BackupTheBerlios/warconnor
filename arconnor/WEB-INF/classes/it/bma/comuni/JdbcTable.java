package it.bma.comuni;

import java.util.*;
// XML
import org.w3c.dom.*;

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
	public void toXmlDomElement(Element element, XMLDriver xDriver) throws BmaException {
		element.setAttribute("tableCat", tableCat);
		element.setAttribute("tableSchem", tableSchem);
		element.setAttribute("tableName", tableName);
		element.setAttribute("tableType", tableType);
		element.setAttribute("remarks", remarks);
		for (int i=0;i<primaryKeys.size();i++) {
			String colName = (String)primaryKeys.elementAt(i);
			Element eKey = xDriver.addElement(element, "primaryKey");
			eKey.setAttribute("id", Integer.toString(i));
			eKey.setAttribute("name", colName);
		}
		for (int i=0;i<columns.size();i++) {
			JdbcColumn jCol = (JdbcColumn)columns.elementAt(i);
			Element eCol = xDriver.addElement(element, jCol.getXmlTag());
			jCol.toXmlDomElement(eCol, xDriver);
		}
		for (int i=0;i<importedKeys.size();i++) {
			JdbcImportedKey jKey = (JdbcImportedKey)importedKeys.elementAt(i);
			Element eKey = xDriver.addElement(element, jKey.getXmlTag());
			jKey.toXmlDomElement(eKey, xDriver);
		}
	}
	public void fromXmlDomElement(Element element, XMLDriver xDriver) throws BmaException {
		tableCat = element.getAttribute("tableCat");
		tableSchem = element.getAttribute("tableSchem");
		tableName = element.getAttribute("tableName");
		tableType = element.getAttribute("tableType");
		remarks = element.getAttribute("remarks");
		NodeList list = element.getElementsByTagName("primaryKey");
		primaryKeys = new Vector(list.getLength());
		for (int i=0;i<list.getLength();i++) {
			primaryKeys.add("");
		}
		for (int i=0;i<list.getLength();i++) {
			Element eKey = (Element)list.item(i);
			String id = eKey.getAttribute("id");
			if (id==null || id.trim().length()==0) id = "0";
			primaryKeys.setElementAt(eKey.getAttribute("name"), Integer.parseInt(id));
		}
		columns.clear();
		JdbcColumn jCol = new JdbcColumn();
		list = element.getElementsByTagName(jCol.getXmlTag());
		for (int i=0;i<list.getLength();i++) {
			Element eCol = (Element)list.item(i);
			jCol = new JdbcColumn();
			jCol.fromXmlDomElement(eCol, xDriver);
			columns.add(jCol);
		}		
		importedKeys.clear();
		JdbcImportedKey jKey = new JdbcImportedKey();
		list = element.getElementsByTagName(jKey.getXmlTag());
		for (int i=0;i<list.getLength();i++) {
			Element eKey = (Element)list.item(i);
			jKey = new JdbcImportedKey();
			jKey.fromXmlDomElement(eKey, xDriver);
			importedKeys.add(jKey);
		}		
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
