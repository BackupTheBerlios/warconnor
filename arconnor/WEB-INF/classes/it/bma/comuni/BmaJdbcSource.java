package it.bma.comuni;

public class BmaJdbcSource extends BmaObject {
	private String nome = "";
	private String driver = "";
	private String url = "";
	private String user = "";
	private String pass = "";
	private String schema = "";
	private String prefix = "";	
protected BmaJdbcSource() {
	super();
}
public BmaJdbcSource(String nome) {
	super();
	this.nome = nome;
}
public java.lang.String getChiave() {
	return nome;
}
public java.lang.String getDriver() {
	return driver;
}
public java.lang.String getPass() {
	return pass;
}
public java.lang.String getPrefix() {
	return prefix;
}
public java.lang.String getSchema() {
	return schema;
}
public java.lang.String getUrl() {
	return url;
}
public java.lang.String getUser() {
	return user;
}
protected java.lang.String getXmlTag() {
	return "FonteJdbc";
}
public void setDriver(java.lang.String newDriver) {
	driver = newDriver;
}
public void setPass(java.lang.String newPass) {
	pass = newPass;
}
public void setPrefix(java.lang.String newPrefix) {
	prefix = newPrefix;
}
public void setSchema(java.lang.String newSchema) {
	schema = newSchema;
}
public void setUrl(java.lang.String newUrl) {
	url = newUrl;
}
public void setUser(java.lang.String newUser) {
	user = newUser;
}
}
