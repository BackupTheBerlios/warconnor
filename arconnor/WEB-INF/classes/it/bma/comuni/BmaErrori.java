package it.bma.comuni;

import java.io.*;
import java.util.*;
public class BmaErrori {
	private static BmaHashtable lista = null;
	private static BmaErrori instance = new BmaErrori();
public BmaErrori() {
	super();
}
public BmaErrore getErrore(String codice) {
	return (BmaErrore)lista.getElement(codice);
}
public static BmaErrori getInstance() {
	if (lista == null) return null;
	else return instance;
}
public static BmaErrori getInstance(String file) throws BmaException {
	if (lista == null) {
		lista = new BmaHashtable(lista.BMA_XML_NOMELISTAERRORI);
		String xml = "";
		lista.fromXmlFile(file);
	}
	return instance;
}
}
