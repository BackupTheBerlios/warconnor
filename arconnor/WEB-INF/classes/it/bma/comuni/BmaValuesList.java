package it.bma.comuni;
import java.util.*;
public class BmaValuesList extends BmaObject {
	private String VALUES_TOKEN = ":";
	private String name = "";
	private String values = "";
	public BmaValuesList() {
		super();
	}
	public BmaValuesList(String name) {
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
	public void setName(String aName) {
		name = aName;
	}
	public String[] getValues() {
		if (values==null || values.trim().length()==0) return new String[0];
		return values.split(VALUES_TOKEN);
	}
	public void setValues(String[] valueArray) {
		if (valueArray==null || valueArray.length==0) return;
		values = "";
		for (int i=0;i<valueArray.length;i++) {
			if (i>0) values = values + VALUES_TOKEN;
			values = values + valueArray[i];
		}
	}
	public void addValue(String value) {
		if (values.length()>0) values = values + VALUES_TOKEN;
		values = values + value;
	}
	public int length() {
		return getValues().length;
	}
	public String getValue(int i) {
		String[] v = getValues();
		if (i<0 || i>v.length) return "";
		return v[i];
	}
}
