package it.bma.comuni;

public abstract class BmaCollection extends BmaObject {
	protected String name = "";
/**
 * BmaCollection constructor comment.
 */
public BmaCollection() {
	super();
}
public BmaCollection(String name) {
	super();
	this.name = name;
}
public String getChiave() {
	return name;
}
public java.lang.String getName() {
	return name;
}
protected java.lang.String getXmlTag() {
	return name;
}
public boolean isCollection() {
	return true;
}
public void setName(java.lang.String newName) {
	name = newName;
}
}
