package it.bma.comuni;
// XML
import org.w3c.dom.*;

public class JdbcColumn extends BmaObject {
	protected String columnName = "";
	protected short dataType = 0;
	protected String typeName = "";
	protected int columnSize = 0;
	protected int decimalDigits = 0;
	protected int numPrecRadix = 10;
	protected int nullable = 0;
	protected String remarks = "";
	protected String columnDef = "";
	protected int ordinalPosition = 0;
	public JdbcColumn() {
		super();
	}
	public String getChiave() {
		return columnName;
	}
	protected java.lang.String getXmlTag() {
		return getClassName();
	}
	public void toXmlDomElement(Element element, XMLDriver xDriver) throws BmaException {
		element.setAttribute("columnName", columnName);
		element.setAttribute("typeName", typeName);
		element.setAttribute("remarks", remarks);
		element.setAttribute("columnDef", columnDef);
		
		element.setAttribute("dataType", Short.toString(dataType));
		element.setAttribute("columnSize", Integer.toString(columnSize));
		element.setAttribute("decimalDigits", Integer.toString(decimalDigits));
		element.setAttribute("numPrecRadix", Integer.toString(numPrecRadix));
		element.setAttribute("nullable", Integer.toString(nullable));
		element.setAttribute("ordinalPosition", Integer.toString(ordinalPosition));
	}
	public void fromXmlDomElement(Element element, XMLDriver xDriver) throws BmaException {
		columnName = element.getAttribute("columnName");
		typeName = element.getAttribute("typeName");
		remarks = element.getAttribute("remarks");
		columnDef = element.getAttribute("columnDef");
		
		String temp = element.getAttribute("dataType");
		if (temp==null || temp.trim().length()==0) temp = "0";
		dataType = Short.parseShort(temp);
		
		temp = element.getAttribute("columnSize");
		if (temp==null || temp.trim().length()==0) temp = "0";
		columnSize = Integer.parseInt(temp);
		
		temp = element.getAttribute("decimalDigits");
		if (temp==null || temp.trim().length()==0) temp = "0";
		decimalDigits = Integer.parseInt(temp);
		
		temp = element.getAttribute("numPrecRadix");
		if (temp==null || temp.trim().length()==0) temp = "10";
		numPrecRadix = Integer.parseInt(temp);
		
		temp = element.getAttribute("nullable");
		if (temp==null || temp.trim().length()==0) temp = "0";
		nullable = Integer.parseInt(temp);
		
		temp = element.getAttribute("ordinalPosition");
		if (temp==null || temp.trim().length()==0) temp = "0";
		ordinalPosition = Integer.parseInt(temp);
	}
}
