package it.bma.web;

import java.util.*;
import it.bma.comuni.*;
public class BmaOggettoServizio extends BmaObject {
	protected String nome = "";
	protected String classe = "";
	protected String xml = "";
public BmaOggettoServizio() {
	super();
}
public BmaOggettoServizio(BmaObject oggetto) {
	super();
	nome = oggetto.getChiave();
	classe = oggetto.getClass().getName();
	xml = oggetto.toXml();
}
public BmaObject getBmaObject() throws BmaException {
	it.bma.comuni.BmaObject o = objectInstance(classe);
	if (o==null) return null;
	if (o.isCollection()) {
		BmaCollection o2 = (BmaCollection)o;
		o2.setName(nome);
		o2.fromXml(xml);
		return o2;
	}
	else {
		o.fromXml(xml);
		return o;
	}
}
public String getChiave() {
	return nome;
}
protected String getXmlTag() {
	return getClassName();
}
}
