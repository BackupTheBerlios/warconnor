package it.bma.prodotti;
// BMA
import it.bma.comuni.*;
import it.bma.web.*;

public class MakeManuale {
	private final String APP_NAME = "PDAS";
	private BmaUserConfig config = null;
	private JdbcModel jModel = null;
	public MakeManuale() {
		super();
	}
	public MakeManuale(BmaUserConfig userConfig, JdbcModel model) {
		super();
		setConfig(userConfig);
		setJModel(model);
	}
	public void setConfig(BmaUserConfig userConfig) { config = userConfig; }
	public void setJModel(JdbcModel model) { jModel=model; }
	
	public String esegui() throws BmaException {
		BmaJdbcSource jSource = config.getFonteApplicazione(APP_NAME);
		PMDriver driver = new PMDriver();
		driver.setUserConfig(config);
		driver.setJdbcSource(jSource);
		driver.setJModel(jModel);
		driver.makeManuale();
		return "Ok";
	}
	public static void main(String[] args) {
		if (args.length!=1) {
      System.out.println("Usa con argomento <webRealPath>");
      return;
    }
		BmaJsp jsp = new BmaJsp();
    String path = args[0] + jsp.BMA_JSP_CONF_PATH;
		try {
			MakeManuale app = new MakeManuale();
			app.config = new BmaUserConfig();
			app.jModel = new JdbcModel();
			app.jModel.setKeyUppercase(false);
			app.config.carica(path, jsp.BMA_JSP_CONF_FILE, path + jsp.BMA_JSP_ERRS_FILE);
			String result = app.esegui();
			System.out.println(result);
		}
		catch (BmaException bma) {
			System.out.println("Exception BMA");
			System.out.println("Inf=" + bma.getInfo());
			System.out.println("Ext=" + bma.getInfoEstese());
		}
	}
}
