package it.bma.web;

import java.util.*;
import it.bma.comuni.*;
public class BmaJsp extends BmaObject implements BmaJspLiterals {
	public int maxColLen = 50;
	public int maxColLen2 = 20;
	public BmaUserConfig config = new BmaUserConfig();
	public BmaJsp() {
		super();
	}
	public String getWebPath() {
		return config.getWebPath();
	}
	public void impostaMenuPrincipale(BmaSessione sessione, BmaVector azioniMenu) {
	}
	public void addMenuSinistra(BmaUtente user, String codFunzione, int livello, BmaVector azioniMenu) {
		BmaFunzione f = user.getFunzione(codFunzione);
		if (f!=null && f.isAzioneAmmessa(f.getAzioneDefault())) { 
			azioniMenu.add(new BmaMenu(livello, f, BMA_JSP_MENU_SINISTRA));
		}
	}
	public void applicaStandard(Vector listaCampi) {
		for (int i = 0; i < listaCampi.size(); i++){
			BmaDataColumn campo = (BmaDataColumn)listaCampi.elementAt(i);
			if (!campo.getTipoControllo().equals(BMA_CONTROLLO_HIDDEN) && 
					campo.getNome().length()>=4) {
				// Assunzioni sulla base degli standard di nomenclatura dei dati
				String n = campo.getNome().substring(0, 4);
				if (n.equals("FLG_")) {
					campo.setTipoControllo(BMA_CONTROLLO_BOOLEAN);
					campo.getValoriControllo().put(BMA_TRUE, "Sì");
					campo.getValoriControllo().put(BMA_FALSE, "No");
				}
				else if (n.equals("DAT_")) {
					campo.setTipo(BMA_SQL_TYP_DATECHAR);
					campo.setLunghezza("10");
					// Imposta temporaneamente i valori di default per superare la storicizzazione
					if (campo.getValore()==null || campo.getValore().trim().length()==0) {
						if (campo.getNome().equals("DAT_INIZIO") || campo.getNome().equals("DAT_INIZIOGAR") ||
								campo.getNome().equals("DAT_INIZIOCOND") || campo.getNome().equals("DAT_INIZIOREG")) {
							campo.setValore("20030331");
							campo.setTipoControllo(BMA_CONTROLLO_BLOCCATO);
						}
						else if (campo.getNome().equals("DAT_FINE") || campo.getNome().equals("DAT_FINEGAR") ||
							campo.getNome().equals("DAT_FINECOND") || campo.getNome().equals("DAT_FINEREG")) {
							campo.setValore("99991231");
							campo.setTipoControllo(BMA_CONTROLLO_BLOCCATO);
						}
						else {
							try {						
								campo.setValore(getDataInterna(getData()));
							} 
							catch (BmaException e) {
								// Do nothing
							}
						}	
					}
				}
				else if (n.equals("IND_")) campo.setTipoControllo(BMA_CONTROLLO_BOOLEAN);
				else if (n.equals("NOT_")) {
					campo.setTipoControllo(BMA_CONTROLLO_TEXT);
					campo.setCaseSensitive(true);
				}
				else if (n.equals("XML_")) {
					campo.setTipoControllo(BMA_CONTROLLO_TEXT);
					campo.setCaseSensitive(true);
				}
				else if (n.equals("OBJ_")) {
					campo.setTipoControllo(BMA_CONTROLLO_LINK);
					campo.setCaseSensitive(true);
				}
				else if (campo.isAnnullabile() && campo.getTipoControllo().equals(BMA_CONTROLLO_LISTA)) {
					campo.getValoriControllo().put("","");
				}
			}
		}
	}
	public java.lang.String getChiave() {
		return "BmaJsp";
	}
	public String getData() {
		java.text.SimpleDateFormat sdfout = new java.text.SimpleDateFormat(BMA_JSP_DATE_FMT_ESTERNO);
		Date d = new Date();
		return sdfout.format(d);
	}
	public String getOggiFormatoInterno() {
		java.text.SimpleDateFormat sdfout = new java.text.SimpleDateFormat(BMA_JSP_DATE_FMT_INTERNO);
		Date d = new Date();
		return sdfout.format(d);
	}
	public String getDataEsterna(String dataInterna) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(BMA_JSP_DATE_FMT_INTERNO);
		java.text.SimpleDateFormat sdfout = new java.text.SimpleDateFormat(BMA_JSP_DATE_FMT_ESTERNO);
		if (dataInterna==null || dataInterna.trim().length()==0) return "";
		try {
			Date d = sdf.parse(dataInterna);
			return sdfout.format(d);
		} 
		catch (java.text.ParseException e) {
			return "";
		}
	}
	public String getDataInterna(String dataEsterna) throws BmaException {
		if (dataEsterna==null || dataEsterna.trim().length()==0) return "";
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(BMA_JSP_DATE_FMT_ESTERNO);
		java.text.SimpleDateFormat sdfout = new java.text.SimpleDateFormat(BMA_JSP_DATE_FMT_INTERNO);
		try {
			Date d = sdf.parse(dataEsterna);
			return sdfout.format(d);
		} 
		catch (java.text.ParseException e) {
			throw new BmaException(BMA_ERR_WEB_PARAMETRO,"Data Errata", dataEsterna, this);
		}
	}
	public String dataConvertita(String data, String formatoInput, String formatoOutput) throws BmaException {
		if (data==null || data.trim().length()==0) return "";
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(formatoInput);
		java.text.SimpleDateFormat sdfout = new java.text.SimpleDateFormat(formatoOutput);
		try {
			Date d = sdf.parse(data);
			return sdfout.format(d);
		} 
		catch (java.text.ParseException e) {
			throw new BmaException(BMA_ERR_WEB_PARAMETRO,"Data Errata", data, this);
		}
	}
	public String getDataDb(String dataInterna) throws BmaException {
		if (dataInterna==null || dataInterna.trim().length()==0) return "";
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(BMA_JSP_DATE_FMT_INTERNO);
		java.text.SimpleDateFormat sdfout = new java.text.SimpleDateFormat(BMA_JSP_DATE_FMT_DATIDB);
		try {
			Date d = sdf.parse(dataInterna);
			return sdfout.format(d);
		} 
		catch (java.text.ParseException e) {
			throw new BmaException(BMA_ERR_WEB_PARAMETRO,"Data Errata",dataInterna,this);
		}
	}
	public BmaVector getElementiContesto(BmaSessione sessione) {
		BmaVector elementi = new BmaVector("ElementiContesto");
		BmaVector fAttive = sessione.getListaFunzioni();
		for (int i=0;i < fAttive.getSize() - 1;i++) {
			BmaFunzioneAttiva f = (BmaFunzioneAttiva)fAttive.getElement(i);
			if (f.getDesContesto().length()>0) {
				BmaParametro p = new BmaParametro(f.getDesFunzione(), f.getDesContesto());
				elementi.add(p);
			}
		}
		return elementi;
	}
	protected String getFormatoEsterno(BmaDataColumn campo) {
		String valore = campo.getValore();
		if (campo.getTipoSemplice().equals(BMA_SQL_TYS_DAT)) valore = getDataEsterna(valore);
		else if (campo.getTipoSemplice().equals(BMA_SQL_TYS_NUM)) valore = getNumeroEsterno(valore, campo.getDecimali());
		if (campo.getTipoControllo().equals(BMA_CONTROLLO_LINK)) valore = getLinkEsterno(valore);
		return valore;
	}
	public String getHtmlBottone(	BmaFunzioneAttiva corrente, 
																		String codFunzione, 
																		String codAzione,
																		String codComando,
																		String testo) {
		String h = "";
		if (codFunzione.trim().length()==0) codFunzione = corrente.getCodFunzione();
		if (codAzione.trim().length()==0) codAzione = corrente.getCodAzione();
		if (codComando.trim().length()==0) codComando = BMA_JSP_COMANDO_PREPARA;
		if (testo.trim().length()==0) testo = corrente.getDesAzione();

		if (codComando.equals(BMA_JSP_COMANDO_ANNULLA)) {
			BmaFunzioneAttiva fa = corrente.sessione.getFunzioneAttivante();
			if (fa==null) return "";
			codFunzione = fa.getCodFunzione();
			codAzione = fa.getCodAzione();
			codComando = BMA_JSP_COMANDO_RIPRISTINA;
		}
		else if (codComando.equals(BMA_JSP_COMANDO_ELIMINA)) {
			if (corrente.getAzione(codFunzione, codAzione)==null) return "";
			else {
				h = h + "&nbsp;&nbsp;<a class=\"Button\"" +
					" href='javascript:" + BMA_JSP_SCRIPT_ELIMINA + "()'>" +
					testo + "</a>" + '\n';
				return h;
			}
		}
		else if (!codFunzione.equals(corrente.getCodFunzione())) {
			if (corrente.getAzione(codFunzione, codAzione)==null) return "";
		}
		h = h + "&nbsp;&nbsp;<a class=\"Button\"" +
				" href='javascript:invia(" +
				"\"" + codFunzione + "\"," +
				"\"" + codAzione + "\"," +
				"\"" + codComando + "\"," +
				"\"\")'>" + testo + "</a>" + '\n';
		return h;
	}
	public String getHtmlInput(BmaDataColumn campo, String azione, String classe) {

		if (azione.equals(BMA_JSP_AZIONE_ELIMINA)) return getHtmlInputHidden(campo, classe);
		else if (campo.getTipoControllo().equals(BMA_CONTROLLO_HIDDEN)) return getHtmlInputHidden(campo, classe);
		else if (campo.getTipoControllo().equals(BMA_CONTROLLO_BLOCCATO)) return getHtmlInputHidden(campo, classe);
		else if (azione.equals(BMA_JSP_AZIONE_MODIFICA) && campo.isChiave()) return getHtmlInputHidden(campo, classe);

		String h = "";
		String cls = "";
		int s = 3;
		if (campo.getLunghezza().trim().length()>0) s = Integer.parseInt(campo.getLunghezza()) + 1;
		if (s>60) s=60;
		if (s<3) s=3;
		if (classe.trim().length()>0) cls = " class=\"" + classe + "\"";	
		if (campo.getTipoControllo().equals(BMA_CONTROLLO_BOOLEAN)) {
			String sel = "";
			if (campo.getValore().equals(BMA_TRUE)) sel = " checked";
			h = h + "<input " + cls + " type=\"Checkbox\"" + sel +
							" name=\"" + campo.getNome() + "\"" +
							" value=\"S\">" + '\n';
		} 
		else if (campo.getTipoControllo().equals(BMA_CONTROLLO_LISTA) && campo.getValoriControllo().size()>0) {
			h = h + "<select " + cls + " name=\"" + campo.getNome() + "\">" + '\n';
			String[] list = getSortedTableKeys(campo.getValoriControllo());
			for (int i = 0; i < list.length; i++){
				String k = (String)list[i];
				String v = (String)campo.getValoriControllo().get(k);
				String sel = "";
				if (campo.getValore().equals(k)) sel = " selected";
				h = h + "<option value=\"" + k + "\"" + sel + ">" + v + "</option>" + '\n';
			}
			h = h + "</select>" + '\n';
		} 
		else if (campo.getTipoControllo().equals(BMA_CONTROLLO_TEXT)) {
			h = h + "<textarea " + cls + " cols=\"50\"" +
							" name=\"" + campo.getNome() + "\"" +
							" rows=\"5\">" + campo.getValore() + "</textarea>" + '\n';
		} 
		else if (campo.getTipoControllo().equals(BMA_CONTROLLO_FILE)) {
			h = h + "<input " + cls + " type=\"File\"" +
							" name=\"" + campo.getNome() + "\"" +
							" value=\"" + campo.getValore() + "\"" +
							" maxlength=\"" + campo.getLunghezza() + "\"" +
							" size=\"" + s + "\">" + '\n';
		} 
		else if (campo.getTipoControllo().equals(BMA_CONTROLLO_LINK)) {
			if (azione.equals(BMA_JSP_AZIONE_NUOVO) || 
				 (azione.equals(BMA_JSP_AZIONE_MODIFICA) && campo.getValore().trim().length()==0)) {
				h = h + "<input " + cls + " type=\"File\"" +
								" name=\"" + campo.getNome() + "\"" +
								" value=\"" + campo.getValore() + "\"" +
								" size=\"40\"" + '\n';
			}
			else if (!azione.equals(BMA_JSP_AZIONE_ELIMINA)) {
				h = h + "<input type=\"Hidden\"" +
								" name=\"" + campo.getNome() + "\"" +
								" value=\"" + campo.getValore() + "\">" + '\n';
				h = h + "<a " + cls + " href=\"" + getLinkEsterno(campo.getValore()) + "\"" +
								" target=\"View Allegati\">" + campo.getValore() +
								"</a>" + '\n';
			}
		} 
		else {
			String valore = getFormatoEsterno(campo);
			h = h + "<input " + cls + " type=\"Text\"" +
							" name=\"" + campo.getNome() + "\"" +
							" value=\"" + valore + "\"" +
							" maxlength=\"" + campo.getLunghezza() + "\"" +
							" size=\"" + s + "\">" + '\n';
		} 	
		return h;
	}
	private String getHtmlInputHidden(BmaDataColumn campo, String classe) {
		String valore = getFormatoEsterno(campo);
		String h = "";
		h = h + "<input type=\"Hidden\"" +
						" name=\"" + campo.getNome() + "\"" +
						" value=\"" + valore + "\">" + '\n';
		h = h + getHtmlOutputForm(campo, classe);
		return h;
	}
	public String getHtmlOutputForm(BmaDataColumn campo, String classe) {

		String valore;

	//	if (campo.getTipoControllo().equals(BMA_CONTROLLO_LISTA)) {
		if (campo.getValoriControllo().size()>0) {
			valore = (String)campo.getValoriControllo().get(campo.getValore());
			if (valore==null) valore = campo.getValore();
		}
		else {
			valore = getFormatoEsterno(campo);
		}

		String h = "";
		String cls = "";
		if (classe.trim().length()>0) cls = " class=\"" + classe + "\"";	
		h = "<span" + cls + h + ">" + valore + "</span>" + '\n';
		return h;
	}
	public String getHtmlOutputList(BmaDataColumn campo, String classe) {

		String valore;

		if (campo.getTipoControllo().equals(BMA_CONTROLLO_LISTA)) {
			valore = (String)campo.getValoriControllo().get(campo.getValore());
			if (valore==null) valore = campo.getValore();
			if (valore.length()>maxColLen2) valore = valore.substring(0, maxColLen2);
		}
		else {
			valore = getFormatoEsterno(campo);
			if (valore.length()>maxColLen) valore = valore.substring(0, maxColLen);
		}

		String h = "";
		String cls = "";
		if (classe.trim().length()>0) cls = " class=\"" + classe + "\"";	
		h = "<span" + cls + h + ">" + valore + "</span>" + '\n';
		return h;
	}
	public String getLinkEsterno(String linkInterno) {
		return config.getWebPath() + BMA_JSP_PATH_ALLEGATI + '\\' + linkInterno;
	}
	public String getLinkInterno(String linkEsterno) throws BmaException {
		int p = linkEsterno.indexOf(BMA_JSP_PATH_ALLEGATI);
		if (p < 0) return linkEsterno;
		String v = linkEsterno.substring(p + BMA_JSP_PATH_ALLEGATI.length() + 1);
		v.replace('\\', '/');
		return v;
	}
	public String getNumeroEsterno(String numeroInterno, String decimali) {
		if (numeroInterno==null || numeroInterno.trim().length()==0) return "";
		if (decimali==null || decimali.trim().length()==0) decimali = "0";
		int scale = Integer.parseInt(decimali);
		double n = Double.parseDouble(numeroInterno);
		java.math.BigDecimal bd = new java.math.BigDecimal(n);
		bd = bd.setScale(scale, bd.ROUND_HALF_UP);
		return bd.toString();
	}
	public String getNumeroInterno(String numeroEsterno) throws BmaException {
		String v = "";
		try {
			double d = Double.parseDouble(numeroEsterno);
			return Double.toString(d);
		}
		catch (NumberFormatException e) {
			throw new BmaException(BMA_ERR_WEB_PARAMETRO, "Numero Errato: " + numeroEsterno, e.getMessage(), this);
		}
	}
	public String getOnLoadScript(BmaSessione sessione) {
		BmaErrore err = (BmaErrore)sessione.getBeanTemporaneo(BMA_JSP_BEAN_ERRORE);
		BmaFunzioneAttiva f = sessione.getFunzioneCorrente();
		String comando = f.getParametri().getString(BMA_JSP_CAMPO_COMANDO);
		String selezione = f.getParametri().getString(BMA_JSP_CAMPO_SELEZIONE);
		String s = "";
		if (err!=null) s = err.getMsgUtente();
		s = BMA_JSP_SCRIPT_INIT + "(" + 
									'\"' + f.getCodFunzione() + '\"' + "," +
									'\"' + f.getCodAzione() + '\"' + "," +
									'\"' + comando + '\"' + "," +
									'\"' + selezione + '\"' + "," +
									'\"' + s + '\"' + ")";
		return "'javascript:"+ s + "'";
	}
	protected java.lang.String getXmlTag() {
		return getClassName();
	}
	public boolean isChiaveContesto(BmaDataColumn colonna, BmaFunzioneAttiva funzione) {

		String valore = funzione.getChiaveContesto(colonna.getNome());
		return (valore!=null && valore.trim().length()>0);
	}
	public boolean isChiaveContestoCorrente(BmaDataColumn colonna, BmaFunzioneAttiva funzione) {
		if (!isChiaveContesto(colonna, funzione)) return false;

		BmaFunzioneAttiva attivante = funzione.sessione.getFunzioneAttivante();
		if (attivante==null) return true;

		String valore = attivante.getChiaveContesto(colonna.getNome());
		return (valore==null || valore.trim().length()==0);

	}
	public boolean isColonnaNote(BmaDataColumn colonna) {
		return colonna.getTipoControllo().equals(BMA_CONTROLLO_TEXT);
	}
	public boolean isVisibileForm(BmaDataColumn colonna, BmaFunzioneAttiva funzione) {
	/* 
			Per le funzioni di editing la colonna non è visibile se:
					a) Appartiene al contesto della funzione superiore e cioè se appartiene 
							- al contesto della funzione 
							- ma non al contesto corrente
	*/
		return (!isChiaveContesto(colonna, funzione) || isChiaveContestoCorrente(colonna, funzione));
	}
	public boolean isVisibileList(BmaDataColumn colonna, BmaFunzioneAttiva funzione) {
	/* 
			Per la funzione Elenco la colonna non è visibile se:
					a) Appartiene al contesto della funzione
						oppure...
					b) E' una colonna note
						oppur...
					c) Ha un tipo di controllo HIDDEN
						oppur...
					d) Ha un tipo di controllo LINK
	*/
		if (isChiaveContesto(colonna, funzione)) return false;
		else if (isColonnaNote(colonna)) return false;
		else if (colonna.getTipoControllo().equals(BMA_CONTROLLO_HIDDEN)) return false;
		else if (colonna.getTipoControllo().equals(BMA_CONTROLLO_LINK)) return false;
		else return true;
	}
}
