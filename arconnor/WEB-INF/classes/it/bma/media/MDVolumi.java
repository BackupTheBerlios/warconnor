package it.bma.media;

import java.util.*;
import it.bma.comuni.*;
import it.bma.web.*;
public class MDVolumi extends MDFunzioneEdit {
	public MDVolumi() {
		super();
	}
	public java.lang.String[] getFunzioniEditDettaglio() {
		return new String[] {"MD_CARTELLEVOLUME"};
	}
	protected java.lang.String ricavaDesContesto(it.bma.comuni.BmaDataForm df) {
		return df.getValoreCampo("DES_VOLUME");
	}
	protected void completaModulo(BmaDataForm modulo) throws BmaException {
		MDDriver myDriver = (MDDriver)driver;
		BmaDataField campo = modulo.getDato("COD_TIPOVOLUME");
		if (campo==null) return;
		campo.setTipoControllo(BMA_CONTROLLO_LISTA);
		campo.setValoriControllo(myDriver.getTipiVolume());
	}
	protected void exitAggiorna(Hashtable valori, String comando) throws BmaException {
		if (!comando.equals(BMA_SQL_DELETE)) {
			MDDriver myDriver = (MDDriver)driver;
			String desFile = (String)valori.get("DES_PATH");
			if (desFile==null || desFile.trim().length()==0) return;
			valori.put("DES_FILE", "");
			String xml = myDriver.getXmlVolume(desFile, (String)valori.get("COD_VOLUME"));
			valori.put("NOT_VOLUME", xml);
		}
	}
}
