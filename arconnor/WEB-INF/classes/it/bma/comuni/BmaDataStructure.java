package it.bma.comuni;

import java.text.*;
import java.util.*;
/**
 * Insert the type's description here.
 * Creation date: (04/09/03 11.40.29)
 * @author: Administrator
 */
public class BmaDataStructure extends BmaObject {
	public String nome = "";
	public BmaDataList mainList = new BmaDataList();
	public BmaHashtable dependences = new BmaHashtable("Dependences");
/**
 * BmaDataStructure constructor comment.
 */
public BmaDataStructure() {
	super();
}
/**
 * getChiave method comment.
 */
public java.lang.String getChiave() {
	return mainList.getChiave() + BMA_KEY_SEPARATOR + nome;
}
/**
 * getXmlTag method comment.
 */
protected java.lang.String getXmlTag() {
	return mainList.getXmlTag() + "_" + nome;
}
}
