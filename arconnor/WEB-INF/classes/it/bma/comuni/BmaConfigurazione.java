package it.bma.comuni;

import java.io.*;
import java.util.*;
import java.text.*;

public abstract class BmaConfigurazione extends BmaObject {

	private String configPath = "";
	private String serverName = "";
	private String serverPort = "";
	private String protocol = "";
	private boolean completa = false;
	private BmaApplicationInfo info = new BmaApplicationInfo();
	private String webPath = "";
	private String servletPath = "";
	private String minutiTimeout = "";
	private String minutiValidita = "";
	private String urlErrore = "";
	private String urlUscita = "";
	private String funzioneDefault = "";
	private String fonteDefault = "";
	private String traceLevel = "";
	private BmaHashtable parametri = new BmaHashtable("Parametri");
	private BmaHashtable fontiJdbc = new BmaHashtable("FontiJdbc");

	public final String BMA_CFGTIP_SOC = "SOCIETA";
	public final String BMA_CFGTIP_APP = "APPLICAZIONE";
	public final String BMA_CFGTIP_APP_FONTE = "FONTE-APPLICAZIONE";

	// CODICI PARAMETRI CONFIGURAZIONE
	public final String BMA_CFGCOD_SOC_CODICE = "SOCIETA";
	public final String BMA_CFGCOD_APP_DIVISA = "DIVISA";
	public final String BMA_CFGCOD_URL_SERVIZI = "URL-SERVIZI";
	
	// CODICI PARAMETRI CONFIGURAZIONE DI TIPI APPLICAZIONE
	public final String BMA_CFGCOD_APP_FONTE_TARGET = "FONTE-TARGET";
	
	public final String BMA_CFGCOD_LOG_SERVIZI = "LOG-SERVIZI";

	// CODICI DI TRACE
	public final String BMA_CFGTRC_LIVSRV = "LIVSRV";
	public final String BMA_CFGTRC_LIVFNZ = "LIVFNZ";
	public final String BMA_CFGTRC_OUTSRV = "OUTSRV";
	public final String BMA_CFGTRC_OUTFNZ = "OUTFNZ";
	public final String BMA_CFGTRC_LIVERR_A = "LIVERRA";
	public final String BMA_CFGTRC_LIVERR_N = "LIVERRN";
	public final String BMA_CFGTRC_LIVERR_W = "LIVERRW";
	public BmaConfigurazione() {
		super();
	}
	public void addParametro(ParametroConfigurazione param) {
		parametri.add(param);
	}
	public void carica(String path, String fileConfig, String fileErrori) throws BmaException {
		String xml = "";
		fromXmlFile(path + fileConfig);
		configPath = path;
		BmaErrori ec = BmaErrori.getInstance(fileErrori);
	}
	public String getChiave() {
		return getXmlTag();
	}
	public java.lang.String getConfigPath() {
		return configPath;
	}
	public String getData() {
		Date d = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		return sd.format(d);
	}
	public java.lang.String getFonteDefault() {
		return fonteDefault;
	}
  public String getNomeFonteApplicazione(String applicazione) {
    Enumeration e = parametri.elements();
    while (e.hasMoreElements()) {
      ParametroConfigurazione pc = (ParametroConfigurazione)e.nextElement();
      if (pc.getTipo().equals(BMA_CFGTIP_APP_FONTE) && 
          pc.getNome().equals(applicazione)) return pc.getValore();
    }   
    return getFonteDefault();
  }  
  public BmaJdbcSource getFonteApplicazione(String applicazione) {
    return getJdbcSource(getNomeFonteApplicazione(applicazione));
  }  
  
	public BmaJdbcSource getJdbcSource(String nomeFonte) {
    if (nomeFonte==null || nomeFonte.trim().length()==0) nomeFonte = getFonteDefault();
    return (BmaJdbcSource)fontiJdbc.getElement(fonteDefault);
	}
	public BmaHashtable getFontiJdbc() {
		return fontiJdbc;
	}
	public Hashtable getListaUrlFonti() {
		Hashtable lista = new Hashtable();
		Enumeration e = fontiJdbc.keys();
		while (e.hasMoreElements()) {
			String k = (String)e.nextElement();
			BmaJdbcSource f = (BmaJdbcSource)fontiJdbc.getElement(k);
			lista.put(k, k + " (" + f.getUrl() + ")");
		}
		return lista;
	}
	public java.lang.String getFunzioneDefault() {
		return funzioneDefault;
	}
	public BmaApplicationInfo getInfo() {
		return info;
	}

	public Hashtable getListaParametri(String tipo) {
		Hashtable lista = new Hashtable();
		Enumeration e = parametri.elements();
		while (e.hasMoreElements()) {
			ParametroConfigurazione pc = (ParametroConfigurazione) e.nextElement();
			if (pc.getTipo().equals(tipo)) {
				lista.put(pc.getNome(), pc.getValore());
			}
		}
		return lista;
	}
	public java.lang.String getMinutiTimeout() {
		return minutiTimeout;
	}
	public java.lang.String getMinutiValidita() {
		return minutiValidita;
	}
	public String getOra() {
		Date d = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
		return sd.format(d);
	}
	public java.lang.String getParametroApplicazione(String nome) {
		Enumeration e = parametri.elements();
		while (e.hasMoreElements()) {
			ParametroConfigurazione pc = (ParametroConfigurazione)e.nextElement();
			if (pc.getTipo().equals(BMA_CFGTIP_APP) && pc.getNome().equals(nome)) {
				return pc.getValore();
			}
		}
		return "";
	}
	public void setParametroApplicazione(String nome, String valore) {
		ParametroConfigurazione p = new ParametroConfigurazione();
		p.setTipo(BMA_CFGTIP_APP);
		p.setNome(nome);
		p.setValore(valore);
		addParametro(p);
	}
	public java.lang.String getParametroSocieta(String nome) {
		Enumeration e = parametri.elements();
		while (e.hasMoreElements()) {
			ParametroConfigurazione pc = (ParametroConfigurazione)e.nextElement();
			if (pc.getTipo().equals(BMA_CFGTIP_SOC) && pc.getNome().equals(nome)) {
				return pc.getValore();
			}
		}
		return "";
	}
	private ParametroConfigurazione getParametroUnico(String tipoParametro) {
		Enumeration e = parametri.elements();
		while (e.hasMoreElements()) {
			ParametroConfigurazione pc = (ParametroConfigurazione)e.nextElement();
			if (pc.getTipo().equals(tipoParametro)) {
				return pc;
			}
		}
		return null;
	}
	public java.lang.String getProtocol() {
		return protocol;
	}
	public java.lang.String getServerName() {
		return serverName;
	}
	public java.lang.String getServerPort() {
		return serverPort;
	}
	public String getServletPath() {
		return servletPath;
	}
	public java.lang.String getTraceLevel() {
		return traceLevel;
	}
	public java.lang.String getUrlErrore() {
		return urlErrore;
	}
	public java.lang.String getUrlServizi() {
		return getParametroApplicazione(BMA_CFGCOD_URL_SERVIZI);
	}
	public java.lang.String getUrlUscita() {
		return urlUscita;
	}
	public String getWebPath() {
		return webPath;
	}
	protected java.lang.String getXmlTag() {
		return "Configurazione";
	}
	public boolean isCompleta() {
		return completa;
	}
	public boolean isTraceInfoAttiva() {
		int flagInfo = Integer.parseInt(getTraceLevel().substring(0, 1));
		return (flagInfo > 0);
	}
	public void setConfigPath(java.lang.String newConfigPath) {
		configPath = newConfigPath;
	}
	public void setFonteDefault(java.lang.String newFonteDefault) {
		fonteDefault = newFonteDefault;
	}
	public void setFontiJdbc(BmaHashtable newFontiJdbc) {
		fontiJdbc = newFontiJdbc;
	}
	public void setFunzioneDefault(java.lang.String newFunzioneDefault) {
		funzioneDefault = newFunzioneDefault;
	}
	public void setInfo(BmaApplicationInfo newInfo) {
		info = newInfo;
	}
	public void setMinutiTimeout(java.lang.String newMinutiTimeout) {
		minutiTimeout = newMinutiTimeout;
	}
	public void setMinutiValidita(java.lang.String newMinutiValidita) {
		minutiValidita = newMinutiValidita;
	}
	public void setProtocol(java.lang.String newProtocol) {
		protocol = newProtocol;
	}
	public void setServerName(java.lang.String newServerName) {
		serverName = newServerName;
	}
	public void setServerPort(java.lang.String newServerPort) {
		serverPort = newServerPort;
	}
	public void setServletPath(java.lang.String newServletPath) {
		servletPath = newServletPath;
	}
	public void setTraceLevel(java.lang.String newTraceLevel) {
		traceLevel = newTraceLevel;
	}
	public void setUrlErrore(java.lang.String newUrlErrore) {
		urlErrore = newUrlErrore;
	}
	public void setUrlUscita(java.lang.String newUrlUscita) {
		urlUscita = newUrlUscita;
	}
	public void setWebPath(java.lang.String newWebPath) {
		webPath = newWebPath;
	}
	public void trace(java.lang.String message) throws BmaException {
		String file = configPath + "trace.log";
		String time = Long.toString(new Date().getTime());
		writeFile(time + " - " + message, file, true);
	}
}
