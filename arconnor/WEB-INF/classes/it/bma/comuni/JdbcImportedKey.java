package it.bma.comuni;

import java.util.*;
// XML
import org.w3c.dom.*;

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
	public void toXmlDomElement(Element element, XMLDriver xDriver) throws BmaException {
		element.setAttribute("pkTableCat", pkTableCat);
		element.setAttribute("pkTableSchem", pkTableSchem);
		element.setAttribute("pkTableName", pkTableName);
		element.setAttribute("fkTableCat", fkTableCat);
		element.setAttribute("fkTableSchem", fkTableSchem);
		element.setAttribute("fkTableName", fkTableName);
		element.setAttribute("updateRule", updateRule);
		element.setAttribute("deleteRule", deleteRule);
		element.setAttribute("fkName", fkName);
		element.setAttribute("pkName", pkName);
		element.setAttribute("defferrability", defferrability);
		Enumeration e = fkColumns.keys();
		while(e.hasMoreElements()) {
			String k = (String)e.nextElement();
			String v = (String)fkColumns.get(k);
			Element eColumn = xDriver.addElement(element, "fkColumn");
			eColumn.setAttribute("pkcolumn", k);
			eColumn.setAttribute("fkcolumn", v);
		}
	}
	public void fromXmlDomElement(Element element, XMLDriver xDriver) throws BmaException {
		pkTableCat = element.getAttribute("pkTableCat");
		pkTableSchem = element.getAttribute("pkTableSchem");
		pkTableName = element.getAttribute("pkTableName");
		fkTableCat = element.getAttribute("fkTableCat");
		fkTableSchem = element.getAttribute("fkTableSchem");
		fkTableName = element.getAttribute("fkTableName");
		updateRule = element.getAttribute("updateRule");
		deleteRule = element.getAttribute("deleteRule");
		fkName = element.getAttribute("fkName");
		pkName = element.getAttribute("pkName");
		defferrability = element.getAttribute("defferrability");
		fkColumns.clear();
		NodeList list = element.getElementsByTagName("fkColumn");
		for (int i=0;i<list.getLength();i++) {
			Element eColumn = (Element)list.item(i);
			fkColumns.put(eColumn.getAttribute("pkcolumn"), eColumn.getAttribute("fkcolumn"));
		}
	}
}
