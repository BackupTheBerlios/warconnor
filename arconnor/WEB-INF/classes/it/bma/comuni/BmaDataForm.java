package it.bma.comuni;

import java.util.*;
public class BmaDataForm extends BmaObject {
	private String nome = "";
	private BmaVector dati = new BmaVector("Dati");
	public BmaDataForm() {
		super();
	}
	public BmaDataForm(String nome) {
		super();
		this.nome = nome;
	}
	public String getChiave() {
		return nome;
	}
	public BmaVector getDati() {
		return dati;
	}
	public java.lang.String getNome() {
		return nome;
	}
	protected String getXmlTag() {
		return getClassName();
	}
	public void setDati(BmaVector newDati) {
		dati = newDati;
	}
	public void setNome(java.lang.String newNome) {
		nome = newNome;
	}
	
	public BmaDataForm(BmaDataTable tabella) throws BmaException{
		super();
		this.nome = tabella.nomeUtente;
		for (int i = 0; i < tabella.getColonne().size(); i++){
			BmaDataColumn c = (BmaDataColumn)tabella.getColonne().elementAt(i);
			String xml = parseXmlTag(c.toXml(), c.getXmlTag(),0);
			BmaDataField f = new BmaDataField();
			xml = getXml(f.getXmlTag(),xml);
			f.fromXml(xml);
			dati.add(f);
		}
	}
	
	public String getValoreCampo(String nome) {
		BmaDataField f = (BmaDataField)dati.getElement(nome);
		if (f==null) return "";
		else return f.getValore();
	}
	public String getDescrizioneValore(String nomeCampo, String valore) {
		BmaDataField f = (BmaDataField)dati.getElement(nomeCampo);
		if (f==null) return "";
		return (String)f.getValoriControllo().get(valore);
	}
	
	public void setValoreCampo(String nome, String valore) {
		BmaDataField f = (BmaDataField)dati.getElement(nome);
		if (f==null) return;
		f.setValore(valore);
	}
	
	public void setValori(Vector valori) {
		for (int i = 0; i < dati.getSize(); i++){
			BmaDataField dd = (BmaDataField)dati.getElement(i);
			dd.setValore((String)valori.elementAt(i));
		}
	}
	public void setValori(Hashtable valori) {
		for (int i = 0; i < dati.getSize(); i++){
			BmaDataField dd = (BmaDataField)dati.getElement(i);
			String valore = (String)valori.get(dd.getNome());
			if (valore!=null)	dd.setValore(valore);
		}
	}
}
