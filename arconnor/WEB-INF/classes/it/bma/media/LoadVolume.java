package it.bma.media;
// BMA
import it.bma.comuni.*;
import it.bma.web.*;

public class LoadVolume {
	private final String APP_NAME = "MDA";
	private BmaUserConfig config = null;
	private JdbcModel jModel = null;
	public LoadVolume() {
		super();
	}
	public LoadVolume(BmaUserConfig userConfig, JdbcModel model) {
		super();
		setConfig(userConfig);
		setJModel(model);
	}
	public void setConfig(BmaUserConfig userConfig) { config = userConfig; }
	public void setJModel(JdbcModel model) { jModel=model; }
	
	public String esegui(String drive, String codVolume) throws BmaException {
		BmaJdbcSource jSource = config.getFonteApplicazione(APP_NAME);
		MDDriver driver = new MDDriver();
		driver.setUserConfig(config);
		driver.setJdbcSource(jSource);
		driver.setJModel(jModel);
		
		driver.loadVolume(drive, codVolume);
		return "Ok";
	}
	public static void main(String[] args) {
		if (args.length!=3) {
      System.out.println("Usa con argomento <webRealPath> <driveInput> <codVolume>");
      return;
    }
		BmaJsp jsp = new BmaJsp();
    String path = args[0] + jsp.BMA_JSP_CONF_PATH;
    String drive = args[1];
    String codVolume = args[2];
		try {
			LoadVolume app = new LoadVolume();
			app.config = new BmaUserConfig();
			app.jModel = new JdbcModel();
			app.jModel.setKeyUppercase(false);
			app.config.carica(path, jsp.BMA_JSP_CONF_FILE, path + jsp.BMA_JSP_ERRS_FILE);
			String result = app.esegui(drive, codVolume);
			System.out.println(result);
		}
		catch (BmaException bma) {
			System.out.println("Exception BMA");
			System.out.println("Inf=" + bma.getInfo());
			System.out.println("Ext=" + bma.getInfoEstese());
		}
	}
}
