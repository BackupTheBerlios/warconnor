package it.bma.comuni;

public class BmaException extends Exception
													implements java.io.Serializable, 
													BmaLiterals, BmaErrorCodes {
	private BmaErrore error = new BmaErrore();
	public BmaException() {
		super();
		impostaErrore(BMA_ERR_NON_PREVISTO, "", "", "");
	}
	public BmaException(String info) {
		super(info);
		impostaErrore(BMA_ERR_NON_PREVISTO, info, info, "");
	}
	public BmaException(String info, String infoEstese) {
		super(info);
		impostaErrore(BMA_ERR_NON_PREVISTO, info, infoEstese, "");
	}
	public BmaException(String codErrore, String info, String infoEstese, String nomeOggetto) {
		super(info);
		impostaErrore(codErrore, info, infoEstese, nomeOggetto);
	}
	public BmaException(String codErrore, String info, String infoEstese, BmaObject obj) {
		super(info);
		impostaErrore(codErrore, info, infoEstese, obj.getClass().getName());
	}
	private void impostaErrore(String codErrore, String info, String infoEstese, String nomeOggetto) {
		BmaErrori list = BmaErrori.getInstance();
		if (list == null) {
			error.setCodErrore(error.BMA_ERR_CONFIGURAZIONE);
			error.setTipo(error.BMA_ERR_ABEND);
			error.setMsgUtente(error.BMA_MSG_LISTA_ERRORI_VUOTA);
			error.setMsgSistema(error.BMA_MSG_LISTA_ERRORI_VUOTA);
		}
		else {
			error = list.getErrore(codErrore);
			if (error == null) {
				error.setCodErrore(error.BMA_ERR_NON_PREVISTO);
				error.setTipo(error.BMA_ERR_ABEND);
				error.setMsgUtente(error.BMA_MSG_ERRORE_NON_PREVISTO);
				error.setMsgSistema(error.BMA_MSG_ERRORE_NON_PREVISTO + ": " + codErrore);
			}
		}
		error.setInfo(info);
		error.setInfoEstese(infoEstese);
		error.setNomeOggetto(nomeOggetto);
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
