package it.bma.comuni;

public class BmaException extends Exception
													implements java.io.Serializable, 
													BmaLiterals {
	private BmaErrore error = new BmaErrore();
public BmaException() {
	super();
}
public BmaException(String s) {
	super(s);
	error.setInfo(s);
	error.setInfoEstese(s);
}
public BmaException(String info, String infoEstese) {
	super(info);
	error.setInfo(info);
	error.setInfoEstese(infoEstese);
}
public BmaException(String codErrore, String info, String infoEstese, BmaObject obj) {
	super(info);
	BmaErrori list = BmaErrori.getInstance();
	if (list == null) {
		error = new BmaErrore();
		error.setCodErrore(error.BMA_ERR_CONFIGURAZIONE);
		error.setTipo(error.BMA_ERR_ABEND);
		error.setMsgUtente(error.BMA_MSG_LISTA_ERRORI_VUOTA);
		error.setMsgSistema(error.BMA_MSG_LISTA_ERRORI_VUOTA);
	}
	else {
		error = list.getErrore(codErrore);
		if (error == null) {
			error = new BmaErrore();
			error.setCodErrore(error.BMA_ERR_NON_PREVISTO);
			error.setTipo(error.BMA_ERR_ABEND);
			error.setMsgUtente(error.BMA_MSG_ERRORE_NON_PREVISTO);
			error.setMsgSistema(error.BMA_MSG_ERRORE_NON_PREVISTO + ": " + codErrore);
		}
	}
	error.setInfo(info);
	error.setInfoEstese(infoEstese);
	error.setNomeOggetto(obj.getClass().getName());
}
public BmaErrore getErrore() {
	return error;
}
public String getInfo() {
	if (error == null) return "";
	else return error.getInfo();
}
public String getInfoEstese() {
	if (error==null) return "";
	else return error.getInfoEstese();
}
public String getNomeOggetto() {
	if (error == null) return "";
	else return error.getNomeOggetto();
}
public void setErrore(BmaErrore newErrore) {
	error = newErrore;
}
public void setInfo(String info) {
	error.setInfo(info);
}
public void setInfoEstese(String info) {
	error.setInfoEstese(info);
}
public void setNomeOggetto(String obj) {
	error.setNomeOggetto(obj);
}
}
