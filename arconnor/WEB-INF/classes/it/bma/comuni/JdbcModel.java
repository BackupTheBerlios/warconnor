package it.bma.comuni;

import java.sql.*;
import java.util.*;
// XML
import org.w3c.dom.*;

public class JdbcModel extends BmaObject {
	private BmaJdbcSource jSource = new BmaJdbcSource();
	private String schema = "";
	private String prefix = "%";
	private boolean keyUppercase = false;
	protected BmaHashtable tables = new BmaHashtable("Tables");
	public JdbcModel() {
		super();
	}
	public String getChiave() {
		return "JdbcModel";
	}
	public boolean isKeyUppercase() {
		return keyUppercase;
	}
	public void setKeyUppercase(boolean value) {
		keyUppercase = value;
	}
	public void xmlDomSave(String fileName) throws BmaException {
		XMLDriver xDriver = new XMLDriver();
		Document doc = xDriver.getDocument();
		Element root = xDriver.setRootElement(doc, getXmlTag());
		root.setAttribute("schema", schema);
		root.setAttribute("prefix", prefix);
		String temp = BMA_FALSE;
		if (keyUppercase) temp = BMA_TRUE;
		root.setAttribute("keyuppercase", temp);
		Element eSource = xDriver.addElement(root, "JdbcSource");
		eSource.setAttribute("nome", jSource.getChiave());
		eSource.setAttribute("driver", jSource.getDriver());
		eSource.setAttribute("url", jSource.getUrl());
		eSource.setAttribute("user", jSource.getUser());
		eSource.setAttribute("pass", jSource.getPass());
		eSource.setAttribute("schema", jSource.getSchema());
		eSource.setAttribute("prefix", jSource.getPrefix());
		Enumeration eTables = tables.elements();
		while (eTables.hasMoreElements()) {
			JdbcTable jTable = (JdbcTable)eTables.nextElement();
			Element eTable = xDriver.addElement(root, jTable.getXmlTag());
			jTable.toXmlDomElement(eTable, xDriver);
		}
		xDriver.serializeDocument(doc, fileName);
	}
	public void xmlDomLoad(String fileName) throws BmaException {
		XMLDriver xDriver = new XMLDriver();
		Document doc = xDriver.getDocumentFromXmlFile(fileName);
		Element root = doc.getDocumentElement();
		schema = root.getAttribute("schema");
		prefix = root.getAttribute("prefix");
		String temp = root.getAttribute("keyuppercase");
		keyUppercase = temp.equals(BMA_TRUE);
		Element eSource = xDriver.getElementByTag(root, "JdbcSource");
		String nome = eSource.getAttribute("nome");
		jSource = new BmaJdbcSource(nome);
		jSource.setDriver(eSource.getAttribute("driver"));
		jSource.setUrl(eSource.getAttribute("url"));
		jSource.setUser(eSource.getAttribute("user"));
		jSource.setPass(eSource.getAttribute("pass"));
		jSource.setSchema(eSource.getAttribute("schema"));
		jSource.setPrefix(eSource.getAttribute("prefix"));
		tables.clear();
		JdbcTable jTable = new JdbcTable();
		NodeList list = root.getElementsByTagName(jTable.getXmlTag());
		for (int i=0;i<list.getLength();i++) {
			Element eTable = (Element)list.item(i);
			jTable = new JdbcTable();
			jTable.fromXmlDomElement(eTable, xDriver);
			tables.add(jTable);
		}		
	}	
	public BmaDataTable getDataTable(BmaJdbcTrx jTrx, String jName) throws BmaException {
		BmaDataTable dTable = new BmaDataTable();
		JdbcTable jTable = (JdbcTable)tables.getElement(jName);
		if (jTable==null) {
			jTable = (JdbcTable)tables.getElement(schema + "." + jName);
			if (jTable==null) {
				String prefix = jName + "%";
				load(jTrx, schema, prefix);
			}
		}
		return getDataTable(jName);
	}
	public BmaDataTable getDataTable(String jName) throws BmaException {
		BmaDataTable dTable = new BmaDataTable();
		JdbcTable jTable = (JdbcTable)tables.getElement(jName);
		if (jTable==null) {
			jTable = (JdbcTable)tables.getElement(schema + "." + jName);
			if (jTable==null) {
				throw new BmaException(BMA_ERR_JDB_MODEL, "Table not found:" + jName,"",this);
			}
		}
		dTable.nome = jTable.tableName;
		dTable.nomeFisico = jTable.tableName;
		dTable.nomeUtente = jTable.tableName;
		for (int i = 0; i < jTable.columns.size(); i++){
			JdbcColumn jColumn = (JdbcColumn)jTable.columns.elementAt(i);
			BmaDataColumn dColumn = new BmaDataColumn();
			dColumn.nome = jColumn.columnName;
			dColumn.nomeFisico = jColumn.columnName;
			dColumn.nomeUtente = jColumn.columnName;
			dColumn.tipo = getBmaDataType(jColumn.dataType, jColumn.typeName);
			dColumn.decimali = Integer.toString(jColumn.decimalDigits);
			dColumn.lunghezza = Integer.toString(jColumn.columnSize);
			dColumn.annullabile = (jColumn.nullable!=0);
			if (jTable.isPrimaryKey(jColumn.columnName)) {
				dColumn.chiavePrimaria = true;
				dColumn.caseSensitive = !keyUppercase;
			}
			else {
				dColumn.caseSensitive = true;
			}
			dTable.getColonne().add(dColumn);
		}
		return dTable;
	}
	public BmaJdbcSource getJSource() {
		return jSource;
	}
	public java.lang.String getPrefix() {
		return prefix;
	}
	public java.lang.String getSchema() {
		return schema;
	}
	public Vector getTableNames() {
		BmaVector v = tables.getBmaVector();
		Vector vNames = new Vector();
		for (int i = 0; i < v.getSize(); i++){
			JdbcTable jT = (JdbcTable)v.getElement(i);
			vNames.add(jT.getChiave());
		}
		return vNames;
	}
	protected java.lang.String getXmlTag() {
		return getClassName();
	}
	public boolean isValid() {
		return (tables.size()>0);
	}
	public void load(BmaJdbcTrx trx, String schema, String prefix) throws BmaException {
		this.schema = schema;
		this.prefix = prefix;
		BmaHashtable tempTables = new BmaHashtable("Temp");
		jSource = trx.getFonte();
		if (jSource.getSchema().trim().length()>0) this.schema = jSource.getSchema();
		boolean statoConnessione = true;
		JdbcTable table = null;
		String schemaPattern = this.schema;
		if (schemaPattern.trim().length()==0) schemaPattern = null;
		try {
			Connection cn = trx.getConnessione();
			statoConnessione = cn.getAutoCommit();
			cn.setAutoCommit(true);
			ResultSet rs = cn.getMetaData().getTables(null, schemaPattern, prefix, new String[] { "TABLE" });
			if (rs==null) throw new BmaException("Lettura Catalogo", "Lettura Tabelle");
			while (rs.next()) {
				table = new JdbcTable();
				//
				table.tableCat = rs.getString("TABLE_CAT");
				if (table.tableCat==null) table.tableCat = "";
				//
				table.tableSchem = rs.getString("TABLE_SCHEM");
				if (table.tableSchem==null) table.tableSchem = "";
				else this.schema = table.tableSchem;
				//
				table.tableName = rs.getString("TABLE_NAME");
				if (table.tableName==null) table.tableName = "";
				//
				table.tableType = rs.getString("TABLE_TYPE");
				if (table.tableType==null) table.tableType = "";
				//
				//table.remarks = rs.getString("REMARKS");
				//if (table.remarks==null) table.remarks = "";
				//
				tempTables.add(table);
			}
			rs.close();
			// LOAD COLUMNS
			Enumeration et = tempTables.elements();
			while (et.hasMoreElements()) {
				table =(JdbcTable)et.nextElement();
				// LOAD COLUMNS DATA
				String catTmp = table.tableCat;
				if (catTmp.trim().length()==0) catTmp = null;
				rs = cn.getMetaData().getColumns(catTmp, schemaPattern, table.tableName, "%");
				if (rs==null) throw new BmaException("Lettura Catalogo", "Lettura Colonne per " + table.tableName);
				while (rs.next()) {
					JdbcColumn column = new JdbcColumn();
					//-04
					column.columnName = rs.getString("COLUMN_NAME");
					if (column.columnName==null) column.columnName = "";
					//-05
					column.dataType = rs.getShort("DATA_TYPE");
					//-06
					column.typeName = rs.getString("TYPE_NAME");
					if (column.typeName==null) column.typeName = "";
					//-07
					column.columnSize = rs.getInt("COLUMN_SIZE");
					//-09
					column.decimalDigits = rs.getInt("DECIMAL_DIGITS");
					//-10
					column.numPrecRadix = rs.getInt("NUM_PREC_RADIX");
					//-11
					column.nullable = rs.getInt("NULLABLE");
					//-12
					column.remarks = rs.getString("REMARKS");
					if (column.remarks==null) column.remarks = "";
					//-17
					column.ordinalPosition = rs.getInt("ORDINAL_POSITION");
					//
					table.columns.add(column);
				}		
				//
				rs.close();	
				// LOAD PRIMARY KEYS
				rs = cn.getMetaData().getPrimaryKeys(catTmp, schemaPattern, table.tableName);
				if (rs==null) throw new BmaException("Lettura Catalogo", "Lettura Primary Keys per " + table.tableName);
				while (rs.next()) {
					String colName = rs.getString("COLUMN_NAME");
					table.primaryKeys.add(colName);
				}
				rs.close();

				if (table.primaryKeys.size()==0) {
					rs = cn.getMetaData().getIndexInfo(catTmp, schemaPattern, table.tableName, true, true);
					if (rs==null) throw new BmaException("Lettura Catalogo", "Lettura Primary Keys per " + table.tableName);
					while (rs.next()) {
						short type = rs.getShort("TYPE");
						if (type != 0) {
						//-04
							String colName = rs.getString("COLUMN_NAME");
							table.primaryKeys.add(colName);
						}
					}
					rs.close();
				}
				// LOAD IMPORTED KEYS
				rs = cn.getMetaData().getImportedKeys(catTmp, schemaPattern, table.tableName);
				if (rs==null) throw new BmaException("Lettura Catalogo", "Lettura Imported Keys per " + table.tableName);
				String control = "";
				JdbcImportedKey importTable = null;
				while (rs.next()) {
					//-01
					String pkCat = rs.getString("PKTABLE_CAT");
					if (pkCat==null) pkCat = "";
					//-02
					String pkSchem = rs.getString("PKTABLE_SCHEM");
					if (pkSchem==null) pkSchem = "";
					//-03
					String pkName = rs.getString("PKTABLE_NAME");
					if (pkName==null) pkName = "";
					//-04
					String pkCol = rs.getString("PKCOLUMN_NAME");
					if (pkCol==null) pkCol = "";
					//-08
					String fkCol = rs.getString("FKCOLUMN_NAME");
					if (fkCol==null) fkCol = "";
					//-10
					String updRul = rs.getString("UPDATE_RULE");
					if (updRul==null) updRul = "";
					//-11
					String delRul = rs.getString("DELETE_RULE");
					if (delRul==null) delRul = "";
					//-12
					String fkRef = rs.getString("FK_NAME");
					if (fkRef==null) fkRef = "";
					//-13
					String pkRef = rs.getString("PK_NAME");
					if (pkRef==null) pkRef = "";
					//-14
					/* Commentato perche su DB2 7.1 non funziona */
					//String defer = rs.getString("DEFERRABILITY");
					//if (defer==null) defer = "";

					if (control.length()==0 || !control.equals(pkSchem + BMA_KEY_SEPARATOR + pkName)) {
						importTable = new JdbcImportedKey();
						importTable.pkTableCat = pkCat;	
						importTable.pkTableSchem = pkSchem;
						importTable.pkTableName = pkName;
						importTable.fkTableCat = table.tableCat;
						importTable.fkTableSchem = table.tableSchem;
						importTable.fkTableName = table.tableName;
						table.importedKeys.add(importTable);
						control = pkSchem + BMA_KEY_SEPARATOR + pkName;
					}
					importTable.updateRule = updRul;
					importTable.deleteRule = delRul;
					importTable.fkName = fkRef;
					importTable.pkName = pkRef;
					
					/* Commentato perche su DB2 7.1 non funziona */
					//importTable.defferrability = defer;

					importTable.fkColumns.put(pkCol, fkCol);
				}
				rs.close();
				// Deve essere l'ultima istruizione
				tables.add(table);
			}
			cn.setAutoCommit(statoConnessione);
		}
		catch (BmaException e) {
			trx.invalida();
			throw e;
		}
		catch (SQLException eSql) {
			trx.invalida();
			throw new BmaException("Lettura Catalogo", BMA_ERR_JDB_SQL, eSql.getMessage(), this);
		}
	}
	public void setPrefix(java.lang.String newPrefix) {
		prefix = newPrefix;
	}
	public void setSchema(java.lang.String newSchema) {
		schema = newSchema;
	}

	private String getBmaDataType(short jdbcType, String jdbcTypeName) {
		switch (jdbcType) {
			case Types.ARRAY:
				return BMA_SQL_TYP_VARCHAR;
			case Types.BIGINT:
				return BMA_SQL_TYP_DECIMAL;
			case Types.BINARY:
				return BMA_SQL_TYP_VARCHAR;
			case Types.BIT:
				return BMA_SQL_TYP_DECIMAL;
			case Types.BLOB:
				return BMA_SQL_TYP_VARCHAR;
			case Types.CHAR:
				return BMA_SQL_TYP_CHAR;
			case Types.CLOB:
				return BMA_SQL_TYP_VARCHAR;
			case Types.DATE:
				return BMA_SQL_TYP_DATE;
			case Types.DECIMAL:
				return BMA_SQL_TYP_DECIMAL;
			case Types.DISTINCT:
				return BMA_SQL_TYP_VARCHAR;
			case Types.DOUBLE:
				return BMA_SQL_TYP_DECIMAL;
			case Types.FLOAT:
				return BMA_SQL_TYP_DECIMAL;
			case Types.INTEGER:
				return BMA_SQL_TYP_DECIMAL;
			case Types.JAVA_OBJECT:
				return BMA_SQL_TYP_VARCHAR;
			case Types.LONGVARBINARY:
				return BMA_SQL_TYP_VARCHAR;
			case Types.LONGVARCHAR:
				return BMA_SQL_TYP_VARCHAR;
			case Types.NULL:
				return BMA_SQL_TYP_CHAR;
			case Types.NUMERIC:
				return BMA_SQL_TYP_DECIMAL;
			case Types.OTHER:
				return BMA_SQL_TYP_VARCHAR;
			case Types.REAL:
				return BMA_SQL_TYP_DECIMAL;
			case Types.REF:
				return BMA_SQL_TYP_VARCHAR;
			case Types.SMALLINT:
				return BMA_SQL_TYP_DECIMAL;
			case Types.STRUCT:
				return BMA_SQL_TYP_VARCHAR;
			case Types.TIME:
				return BMA_SQL_TYP_TIME;
			case Types.TIMESTAMP:
				return BMA_SQL_TYP_TIMESTAMP;
			case Types.TINYINT:
				return BMA_SQL_TYP_DECIMAL;
			case Types.VARBINARY:
				return BMA_SQL_TYP_VARCHAR;
			case Types.VARCHAR:
				return BMA_SQL_TYP_VARCHAR;
		}
		if (jdbcTypeName.equalsIgnoreCase(BMA_SQL_TYP_CHAR)) return BMA_SQL_TYP_CHAR;
		else if (jdbcTypeName.equalsIgnoreCase(BMA_SQL_TYP_DATE)) return BMA_SQL_TYP_DATE;
		else if (jdbcTypeName.equalsIgnoreCase(BMA_SQL_TYP_DECIMAL)) return BMA_SQL_TYP_DECIMAL;
		else if (jdbcTypeName.equalsIgnoreCase(BMA_SQL_TYP_TIME)) return BMA_SQL_TYP_TIME;
		else if (jdbcTypeName.equalsIgnoreCase(BMA_SQL_TYP_TIMESTAMP)) return BMA_SQL_TYP_TIMESTAMP;
		else if (jdbcTypeName.equalsIgnoreCase(BMA_SQL_TYP_VARCHAR)) return BMA_SQL_TYP_VARCHAR;
		else return BMA_SQL_TYP_VARCHAR;
	}

	public BmaVector getLoadOrder() {
		BmaVector vMain = new BmaVector("LoadOrder");
		String sTemp = "";
		boolean bLoop = true;
		int level = 0;
		while (bLoop) {
			level = level + 1;
			String codLivello = Integer.toString(level);
			BmaValuesList listaTabelle = new BmaValuesList("Level-" + codLivello);
			bLoop = false;
			Enumeration e = tables.elements();
			String sTemp2 = sTemp;
			int prog = 0;
			while (e.hasMoreElements()) {
				JdbcTable jTable = (JdbcTable)e.nextElement();
				if (sTemp.indexOf("|" + jTable.tableName + "|") < 0) {
					if (jTable.importedKeys.size()==0) {
						sTemp2 = sTemp2 + "|" + jTable.tableName + "|";
						listaTabelle.addValue(jTable.tableName);
					}
					else {
						boolean bLoad = true;
						for (int i = 0; i < jTable.importedKeys.size(); i++){
							JdbcImportedKey ref = (JdbcImportedKey)jTable.importedKeys.elementAt(i);
							if (sTemp.indexOf("|" + ref.pkTableName + "|") < 0) bLoad = false;
						}
						if (bLoad) {
							sTemp2 = sTemp2 + "|" + jTable.tableName + "|";
							listaTabelle.addValue(jTable.tableName);
						}
						else {
							bLoop = true;
						}
					}
				} 
			}
			sTemp = sTemp2;
			vMain.add(listaTabelle);
		}
		return vMain;
	}
	public BmaHashtable getTables() {
		return tables;
	}
}
