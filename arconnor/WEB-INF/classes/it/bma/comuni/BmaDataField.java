package it.bma.comuni;

import java.util.*;
public class BmaDataField extends BmaDataColumn {
	public BmaDataField() {
		super();
	}
	public java.lang.String getDecimali() {
		return decimali;
	}
	public java.lang.String getDescrizione() {
		return descrizione;
	}
	public java.lang.String getEtichetta() {
		return nomeUtente;
	}
	public java.lang.String getFormato() {
		return formato;
	}
	public java.lang.String getLunghezza() {
		return lunghezza;
	}
	public java.lang.String getNome() {
		return nome;
	}
	public java.lang.String getTipo() {
		return getTipoSemplice();
	}
	public java.lang.String getTipoControllo() {
		return tipoControllo;
	}
	public java.lang.String getValore() {
		return super.getValore();
	}
	public boolean isAnnullabile() {
		return annullabile;
	}
	public void setNome(String newNome) {
		nome = newNome;
		nomeFisico = nome;
	}
	public void setAnnullabile(boolean newAnnullabile) {
		annullabile = newAnnullabile;
	}
	public void setDecimali(java.lang.String newDecimali) {
		decimali = newDecimali;
	}
	public void setDescrizione(java.lang.String newDescrizione) {
		descrizione = newDescrizione;
	}
	public void setEtichetta(java.lang.String newNome) {
		nomeUtente = newNome;
	}
	public void setFormato(java.lang.String newFormato) {
		formato = newFormato;
	}
	public void setLunghezza(java.lang.String newLunghezza) {
		lunghezza = newLunghezza;
	}
	public void setTipoControllo(java.lang.String newTipoControllo) {
		tipoControllo = newTipoControllo;
	}
	public void setValore(java.lang.String newValore) {
		super.setValore(newValore);
	}
}
