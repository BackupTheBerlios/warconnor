package it.bma.comuni;

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
}
