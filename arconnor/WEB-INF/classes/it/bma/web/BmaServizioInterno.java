package it.bma.web;
import it.bma.comuni.*;
public abstract class BmaServizioInterno extends BmaObject implements BmaJspLiterals {
	protected BmaUserConfig userConfig;
	protected BmaInputServizio input;
	protected BmaOutputServizio outputObject;
	protected JdbcModel jModel = null;
	protected java.io.OutputStream outputStream = null;
	public BmaServizioInterno() {
		super();
	}
	public String esegui() throws BmaException {
		String nSource = input.getInfoServizio(BMA_JSP_CAMPO_FONTEDATI);	
		if (nSource==null || nSource.trim().length()==0) nSource = userConfig.getFunzioneDefault();
		BmaJdbcSource jSource = userConfig.getJdbcSource(nSource);	
		BmaJdbcTrx trx = new BmaJdbcTrx(jSource);
		try {
			trx.open("system");
			if (jModel==null) {
				jModel = new JdbcModel();
				jModel.load(trx, jSource.getSchema(), jSource.getPrefix());
			}
			String output = esegui(trx);
	/*		
			// Scrive log servizi
			if (userConfig.isLogServiziAttivo() && !input.getIdServizio().equals(CRM_SERVIZI_QUERYLOG)) {
				// Leggo Contratto
				tabella = new PFContratto();
				condizioni.clear();
				condizioni.put("CodIstituto", input.getIstituto());
				condizioni.put("NumContratto", numContratto);
				dati = trx.eseguiSqlSelect(tabella.getSqlReadKey(condizioni));
				if (dati.size() != 1) {
					throw new Eccezione("Contratto non trovato", "Contratto= " + numContratto);
				}
				tabella.setValori((Vector)dati.elementAt(0));
				// Scrivo Log
				TabellaSql log = new PFLogServizi();
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS");
				condizioni.clear();
				condizioni.put("CodIstituto", input.getIstituto());
				condizioni.put("CodProdotto", input.getApplicazione());
				condizioni.put("CodTmp", sdf.format(new Date()));

				condizioni.put("CodSessione", input.getSessione());
				condizioni.put("CodCanale", input.getCanale());
				condizioni.put("CodUtente", input.getUtenza());
				condizioni.put("CodOperatore", input.getOperatore());
				condizioni.put("CodIp", input.getIdLocale());
				condizioni.put("CodServizio", input.getIdServizio());
				condizioni.put("NumContratto", numContratto);
				condizioni.put("CodContratto", tabella.getColonna("CodContratto").getValore());
				condizioni.put("CodFiliale", tabella.getColonna("CodFiliale").getValore());
				condizioni.put("CodNdg", tabella.getColonna("CodNdg").getValore());
				condizioni.put("CodOperazione", input.getOperazione());
				condizioni.put("CodAzienda", input.getAzienda());
				condizioni.put("CodRapporto", input.getRapporto());
				condizioni.put("DesInfoServizio", input.getListaInfo().toXml());
				condizioni.put("ValOperazione", input.getImportoOperazione());

				trx.eseguiSqlUpdate(log.getSqlInsert(condizioni));
			}
	*/

			trx.chiudi();
			return output;
		}
		catch (BmaException e) {
			if (trx.isAperta() || trx.isValida()) {
				trx.invalida();
			}
			throw e;
		}
	}
	public abstract String esegui(BmaJdbcTrx trx) throws BmaException;
	public BmaOutputServizio esegui(BmaSessione sessione, BmaInputServizio is) throws BmaException {
		input = is;
		BmaOutputServizio output = new BmaOutputServizio();
		output.setSessione(is.getSessione());
		output.setTimeStamp(is.getTimeStamp());
		output.setApplicazione(is.getApplicazione());
		setUserConfig(sessione.getUserConfig());
		/* Sezione di controllo della validità del jModel di sessione */
		String nSource = input.getInfoServizio(BMA_JSP_CAMPO_FONTEDATI);	
		if (nSource==null || nSource.trim().length()==0) nSource = userConfig.getFunzioneDefault();
		JdbcModel jModelSessione = (JdbcModel)sessione.getBeanApplicativo(BMA_JSP_BEAN_JMODEL);
		if (jModelSessione!=null) {
			/* Verifica se il servizio utilizza un'altra sorgente dati */
			String jSource = jModelSessione.getJSource().getChiave();
			if (!jSource.equals(nSource)) jModelSessione = null;
		}
		jModel = jModelSessione;

		init(is, output);
		String esito = esegui();
		if (esito!=null) {
			sessione.setBeanApplicativo(BMA_JSP_BEAN_JMODEL, getJModel());
			output.setXmlOutput(esito);
			output.setCodiceEsito("0");
			output.setMessaggioErrore("");
			output.setInfoErrore("");
			if (jModel!=null) sessione.setBeanApplicativo(BMA_JSP_BEAN_JMODEL, jModel);
		}
		return output;
	}
	public JdbcModel getJModel() { return jModel; }
	public BmaUserConfig getUserConfig() { return userConfig; }
	public void setJModel(JdbcModel newJModel) { jModel = newJModel; }
	public void setUserConfig(BmaUserConfig newUserConfig) { userConfig = newUserConfig; }
	public void setOutputStream(java.io.OutputStream newOut) { outputStream = newOut; }
	public String getChiave() {
		return getClassName();
	}
	protected String getXmlTag() {
		return getClassName();
	}
	public void init(BmaInputServizio input, BmaOutputServizio Output) throws BmaException {
		this.outputObject = outputObject;
		// Controllo valori obbligatori per tutti i tipi di input
	/*
		if (input.getApplicazione().trim().length() == 0)
			throw new Eccezione("Parametro Applicazione Chiamante non valorizzato", this.getClass().getName());

		if (input.getSessione().trim().length() == 0)
			throw new Eccezione("Parametro Sessione non valorizzato", this.getClass().getName());

		if (input.getTimeStamp().trim().length() == 0)
			throw new Eccezione("Parametro TimeStamp non valorizzato", this.getClass().getName());

		if (input.getCanale().trim().length() == 0)
			throw new Eccezione("Parametro Canale non valorizzato", this.getClass().getName());

		if (input.getIdLocale().trim().length() == 0)
			throw new Eccezione("Parametro Identificativo Locale(IP) non valorizzato", this.getClass().getName());

		if (input.getIstituto().trim().length() == 0)
			throw new Eccezione("Codice Istituto non valorizzato", this.getClass().getName());

		if (input.getUtenza().trim().length() == 0)
			throw new Eccezione("Codice Utenza non valorizzato", this.getClass().getName());
	*/
	}
	public void init(String xmlInput, BmaOutputServizio output) throws BmaException {
		input = new BmaInputServizio(xmlInput);
		init(input,  output);
	}
}
