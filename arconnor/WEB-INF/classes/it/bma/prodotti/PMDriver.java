package it.bma.prodotti;
// Java
import java.io.*;
import java.util.*;
// XML
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.apache.xml.serialize.*;
// BMA
import it.bma.comuni.*;
import it.bma.web.*;
public class PMDriver extends BmaDataDriverGeneric {
	private XMLDriver xmlDriver = new XMLDriver();
	public PMDriver() {
		super();
	}
  public void makeManuale() throws BmaException {
    String xml = buildXml();
  }
  private String buildXml() throws BmaException {
    Document document = xmlDriver.getDocument();
    return "";
  }
}
