package it.bma.comuni;
public class JdbcTableList extends BmaObject {
	private String name = "";
	private BmaHashtable tableList = new BmaHashtable("TableList");
	public JdbcTableList() {
		super();
	}
	public JdbcTableList(String name) {
		super();
		this.name = name;
	}
	
	public String getChiave() {
		return name;
	}
	
	protected String getXmlTag() {
		return getClassName();
	}
	public String getName() {
		return name;
	}
	public BmaHashtable getTableList() {
		return tableList;
	}
	public void setName(String aName) {
		name = aName;
	}
	public void setTableList(BmaHashtable aList) {
		tableList = aList;
	}
}
