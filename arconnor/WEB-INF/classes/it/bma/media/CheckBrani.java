package it.bma.media;
// BMA
import it.bma.comuni.*;
import it.bma.web.*;

public class CheckBrani {
	private final String APP_NAME = "MDA";
	private BmaUserConfig config = null;
	private JdbcModel jModel = null;
	public CheckBrani() {
		super();
	}
	public CheckBrani(BmaUserConfig userConfig, JdbcModel model) {
		super();
		setConfig(userConfig);
		setJModel(model);
	}
	public void setConfig(BmaUserConfig userConfig) { config = userConfig; }
	public void setJModel(JdbcModel model) { jModel=model; }
	
	public String esegui(String checkDir) throws BmaException {
		BmaJdbcSource jSource = config.getFonteApplicazione(APP_NAME);
		MDDriver driver = new MDDriver();
		driver.setUserConfig(config);
		driver.setJdbcSource(jSource);
		driver.setJModel(jModel);
		
		String xml = driver.checkBrani(checkDir);
		return xml;
	}
	public static void main(String[] args) {
		if (args.length!=2) {
      System.out.println("Usa con argomenti <webRealPath> <checkDir>");
      return;
    }
		BmaJsp jsp = new BmaJsp();
    String path = args[0] + jsp.BMA_JSP_CONF_PATH;
    String checkDir = args[1];
		try {
			CheckBrani app = new CheckBrani();
			app.config = new BmaUserConfig();
			app.jModel = new JdbcModel();
			app.jModel.setKeyUppercase(false);
			app.config.carica(path, jsp.BMA_JSP_CONF_FILE, path + jsp.BMA_JSP_ERRS_FILE);
			String result = app.esegui(checkDir);
			System.out.println(result);
		}
		catch (BmaException bma) {
			System.out.println("Exception BMA");
			System.out.println("Inf=" + bma.getInfo());
			System.out.println("Ext=" + bma.getInfoEstese());
		}
	}
}
