package it.bma.comuni;

public interface BmaErrorCodes {
	public final String BMA_ERR_ABEND = "A";
	public final String BMA_ERR_NORMAL = "N";
	public final String BMA_ERR_WARNING = "W";
	public final String BMA_ERR_NON_PREVISTO = "E000";
	public final String BMA_ERR_CONFIGURAZIONE = "E001";
	public final String BMA_ERR_SYS_INTERNO = "E002";
	public final String BMA_ERR_XML_GENERICO = "E003";
	public final String BMA_ERR_XML_PARSE = "E004";	
	public final String BMA_ERR_JDB_DRIVER = "E005";
	public final String BMA_ERR_JDB_URL = "E006";
	public final String BMA_ERR_JDB_CONNESSIONE = "E007";
	public final String BMA_ERR_JDB_SQL = "E008";
	public final String BMA_ERR_JDB_PKEY = "E009";
	public final String BMA_ERR_JDB_FKEY = "E010";
	public final String BMA_ERR_JDB_MODEL = "E011";
	public final String BMA_ERR_IO__FILE = "E012";
	public final String BMA_ERR_IO__READ = "E013";
	public final String BMA_ERR_IO__WRITE = "E014";
	public final String BMA_ERR_SQL_NOTFOUND = "E015";
	public final String BMA_ERR_SQL_NOTUNIQUE = "E016";
	public final String BMA_ERR_WEB_PARAMETRO = "E017";
	public final String BMA_ERR_APP_NOTFOUND = "EA01";
	public final String BMA_ERR_APP_NOTUNIQUE = "EA02";
}
