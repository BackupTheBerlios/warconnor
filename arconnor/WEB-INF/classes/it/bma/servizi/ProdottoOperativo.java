package it.bma.servizi;

import java.util.*;
import it.bma.comuni.*;
import it.bma.prodotti.*;
import it.bma.web.*;
public class ProdottoOperativo extends BmaServizio {
/**
 * ProdottoOperativo constructor comment.
 */
public ProdottoOperativo() {
	super();
}
public String esegui(it.bma.comuni.BmaJdbcTrx trx) throws it.bma.comuni.BmaException {

	// Lettura dei parametri di attivazione
	String codSocieta = input.getSocieta();
	String codProdotto = input.getInfoServizio("codProdotto");
	String rifVersione = input.getInfoServizio("rifVersione");

	BmaGestoreProdotto gp = new BmaGestoreProdotto();

	BmaJsp jsp = new BmaJsp();
	String datRiferimento = jsp.getData();

	Prodotto pd = gp.caricaProdottoCompleto(trx, codSocieta, codProdotto, rifVersione, datRiferimento);
	return pd.toXml();
}
}
