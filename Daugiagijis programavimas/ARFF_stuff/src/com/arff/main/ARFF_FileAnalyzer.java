package com.arff.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class ARFF_FileAnalyzer  implements Runnable{
	
	File sourceFile = null;
	DBconnection dBconnection = null;
	String[] searchNames = null;
	
	
	ArrayList<String> attr_names = null;
	ArrayList<String> attr_values = null;
	HashMap<String, String> attr_types_map = null;
	
	
	Map<String, ArrayList<String>> result_valuesMap = null;
	Map<String, String> result_typesMap = null;
	
	public static final int S_UNKNOWN    = -1;
	public static final int S_CREATED    = 0;
	public static final int S_RUNNING    = 1;
	public static final int S_TERMINATED = 2;
	public static final int S_FINISHED   = 3;
	
	public int STATE = S_UNKNOWN;
	
	
	public final static String attribLinePrefix = "@attribute";
	public final static String attribLineDelim = " ";
	public final static String dataPrefix = "@data";
	public final static String dataDelim = ",";
	
	
	
	
	private ARFF_FileAnalyzer() {
		STATE=S_CREATED;
		attr_names  = new ArrayList<String>();
		attr_values = new ArrayList<String>();
		attr_types_map = new HashMap<String, String>();
		
		result_typesMap = new HashMap<String, String>();
	}
	
	
	public ARFF_FileAnalyzer(File file, DBconnection db) {
		this();
		this.sourceFile = file;
		this.dBconnection = db;
	}
	
	
	public ARFF_FileAnalyzer(DBconnection db, String[] whatToFind) {
		this();
		this.dBconnection = db;
		this.searchNames = whatToFind;
	}
	
	
	public ARFF_FileAnalyzer(File file, String[] whatToFind) {
		this();
		this.sourceFile = file;
		this.searchNames = whatToFind;
	}
	
	
	
	
	
	
	
	@Override
	public void run() {
		STATE = S_RUNNING;
		
		
		if (sourceFile != null){
			//try {Thread.sleep(300);} catch (InterruptedException e) {}
			readFile(sourceFile, attr_names, attr_types_map, attr_values);
			
			if (dBconnection != null){
				saveEntriesToDatabase(attr_names, attr_values, dBconnection);
				clearAll();
			} else 
			if (searchNames != null){
				result_valuesMap = findValuesInFiles(searchNames, attr_names, attr_values);
				clearAll();
			}
			
		} else 
		if (searchNames != null && dBconnection != null){
			result_valuesMap = findValuesInDB(searchNames, dBconnection);
			attr_types_map = getTypesFromDB(searchNames, dBconnection);
		}
		
		
		
		//saveEntriesToDatabase(attr_names, attr_values, dBconnection);
		
		
		STATE = S_FINISHED;
	}
	
	
	
	
	
	private void readFile(File file, ArrayList<String> _attr_names, HashMap<String, String> _attr_types, ArrayList<String> _attr_values){
		
		BufferedReader reader = null;
		FileReader fReader = null;
		String line = "";
		boolean inDataSection = false;
		String[] lineFields = null;

		
		try {
			fReader = new FileReader(file);
			reader = new BufferedReader(fReader);
			
			//System.out.println(""+Thread.currentThread().getName()+": reading lines");
			while ((line = reader.readLine()) != null){
				if (line.isEmpty()) continue;
				
				if (inDataSection){
					lineFields = line.split(dataDelim);
					_attr_values.addAll(Arrays.asList(lineFields));
					
				} else
				if (line.startsWith(attribLinePrefix)){
					if ((lineFields = line.split(attribLineDelim, 3)) != null && lineFields.length == 3){
						_attr_names.add(lineFields[1]);
						_attr_types.put(lineFields[1], lineFields[2]);
						
					}
					
				} else
				if (line.startsWith(dataPrefix)){
					inDataSection = true;
				}
				
			}
			//System.out.println(""+Thread.currentThread().getName()+": finished reading");
			//System.out.println("Filename: ["+file+"], Attributes count: ["+_attr_names.size()+"], Values count: ["+_attr_values.size()+"]");
			
			
		} catch (Exception e) {
			System.err.println("Failed to open a file ["+file.toString()+"]. E: ["+e.getMessage()+"]");
		} finally {
			try{
				//System.out.println("Closing file: "+file.getName());
				fReader.close();
				reader.close();
				
			} catch(Exception e2){
				System.err.println("Cannot close file reader: ["+e2.getMessage()+"]");
			}
		}
	}
	
	
	
	
	
	
	private void saveEntriesToDatabase(ArrayList<String> namesList, ArrayList<String> valuesList, DBconnection db){
		
		StringBuilder sql = null;
		String[] keys   = null;
		String[] values = null;
		
		try {
			keys   = namesList.toArray (new String[namesList.size() ]);
			values = valuesList.toArray(new String[valuesList.size()]);
			
			int queries_ct = ((keys.length/db.max_sql_stack_size)+1);
			
			namesList.clear();
			valuesList.clear();
			namesList=null;
			valuesList=null;
			
			if (keys.length != values.length){
				System.out.println("W: Attributes keys and values sets size does not match. Keys count: "+keys.length+", values count: "+values.length);
			}
			
			
			
			int i=0;
			int qcounter = 0;
			
			//for (int q=0; q<queries.length && i< keys.length; q++){
			for (int q=0; q<queries_ct && i< keys.length; q++){
				
				sql = new StringBuilder();
				qcounter = 0;
				
				
				sql.append("insert into \""+DBconnection.tableMain+"\"")
					.append(" select ")
						.append("  \"").append(keys[i]       ).append("\" as \""+DBconnection.colKey  +"\"")
						.append(", \"").append(values[i]     ).append("\" as \""+DBconnection.colValue+"\"");
					
				
				for (++i; qcounter < db.max_sql_stack_size && i<keys.length; i++){
					qcounter++;
					try{
						sql.append(" union all select ")
							.append("  \"").append(keys[i]       ).append("\"")
							.append(", \"").append(values[i]     ).append("\"");
					} catch(Exception e){
						System.err.println("E: SQL creation error: ["+e.getMessage()+"]");
					}
					
				}
				
				db.executeUpdate(sql.append(";").toString());
				sql.setLength(0);
			}
			
			
			
			//for (i=0; i<queries.length; i++){
			//	db.executeUpdate(queries[i].toString());
			//}
			STATE = S_FINISHED;
		} catch(Exception e){
			STATE = S_TERMINATED;
			System.err.println("E: Cannot save all the keys and values to the database: ["+e.getMessage()+"]");
		} finally {
			//namesList  = null;
			//valuesList = null;
			sql = null;
			keys = null;
			values = null;
		}
		
	}
	
	
	
	private HashMap<String, String> getTypesFromDB(String[] names, DBconnection db){
		HashMap<String, String> retVal = new HashMap<String, String>();
		
		StringBuilder sql = new StringBuilder("SELECT * FROM "+DBconnection.tableTypes+" WHERE ");
		ResultSet resultSet = null;
		
		//int rowId = 0;
		String key = "";
		String value = "";
		
		
		boolean haveResults = true;
		
		
		for (int i=0; i<names.length; i++){
			sql.append(DBconnection.colKey).append(" = '").append(names[i]).append("'").append((i<names.length-1?" OR ":";"));
		}
		
		resultSet = db.executeQuery(sql.toString());
		
		if (resultSet != null){
			
			try {
				
				while(haveResults){
					
					key   = resultSet.getString(DBconnection.colKey  );
					value = resultSet.getString(DBconnection.colValue);
					
					retVal.put(key, value);
					
					haveResults = resultSet.next();
					//rowId++;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return retVal;
	}
	
	
	private Map<String, ArrayList<String>> findValuesInDB(String[] lookForNames, DBconnection db){
		HashMap<String, ArrayList<String>> retVal = new HashMap<String, ArrayList<String>>();
		
		ArrayList<String> valuesList = null;
		
		StringBuilder sql = new StringBuilder("SELECT * FROM "+DBconnection.tableMain+" WHERE ");
		ResultSet resultSet = null;
		
		//int rowId = 0;
		String key = "";
		String value = "";
		
		
		boolean haveResults = true;
		
		
		for (int i=0; i<lookForNames.length; i++){
			sql.append(DBconnection.colKey).append(" = '").append(lookForNames[i]).append("'").append((i<lookForNames.length-1?" OR ":";"));
			retVal.put(lookForNames[i], new ArrayList<String>());
		}
		
		resultSet = db.executeQuery(sql.toString());
		
		if (resultSet != null){
			
			try {
				
				while(haveResults){
					
					key   = resultSet.getString(DBconnection.colKey  );
					value = resultSet.getString(DBconnection.colValue);
					
					valuesList = retVal.get(key);
					
					if (valuesList != null){
						valuesList.add(value);
						result_typesMap.put(key, attr_types_map.get(key));
					}
					
					haveResults = resultSet.next();
					//rowId++;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return retVal;
	}
	
	
	
	
	private Map<String, ArrayList<String>> findValuesInFiles(String[] lookForNames, ArrayList<String> names, ArrayList<String> values){
		return findValuesInFiles(lookForNames, names.toArray(new String[names.size()]), values.toArray(new String[values.size()]));
	}
	
	private Map<String, ArrayList<String>> findValuesInFiles(String[] lookForNames, String[] names, String[] values){
		
		Map<String, ArrayList<String>> retVal = new HashMap<String, ArrayList<String>>();
		ArrayList<String> valuesList = null;
		
		
		//retVal = new String[lookForNames.length];
		
		int leftToFind = 0;
		
		for (String findName : lookForNames){
			retVal.put(findName, new ArrayList<String>());
		}
		
		leftToFind = lookForNames.length;
		
		if (names != null && values != null && names.length > 0 && names.length == values.length){
		
			for (int i=0; leftToFind > 0 && i<names.length; i++){
				for (int f=0; leftToFind > 0 && f<lookForNames.length; f++){
					if (names[i].equals(lookForNames[f])){
						valuesList = retVal.get(lookForNames[f]);
						if (valuesList != null){
							valuesList.add(values[i]);
							//result_typesMap.put(names[i], attr_types_map.get(names[i]));
						}
						//retVal[f] = values[i];
						leftToFind--;
					}
				}
				
			}
		}
		
		
		return retVal;
		
	}
	
	
	public Map<String, ArrayList<String>> getResultsMap(){ 
		Map<String, ArrayList<String>> retVal = null;
		if (result_valuesMap == null){
			retVal = new HashMap<String, ArrayList<String>>();
		} else {
			retVal = result_valuesMap;
		}
		
		return retVal;
	}
	
	public static Map<String, String> getTypesMap(File arffDir, String[] atrributesNames){ 
		Map<String, String> retVal = new HashMap<String, String>();
		
		if (arffDir != null && arffDir.exists() && arffDir.isDirectory()){
			try {
				File[] files = arffDir.listFiles();
				
				if (files != null && files.length > 0){
					FileReader fReader = new FileReader(files[0]);
					BufferedReader reader = new BufferedReader(fReader);
					
					String line;
					String[] lineFields;
					boolean inDataSection = false;
					
					
					
					while ((line = reader.readLine()) != null && !inDataSection){
						if (line.isEmpty()) continue;
						
						if (line.startsWith(attribLinePrefix)){
							if ((lineFields = line.split(attribLineDelim, 3)) != null && lineFields.length == 3){
								retVal.put(lineFields[1], lineFields[2]);
							}
							
						} else
						if (line.startsWith(dataPrefix)){
							inDataSection = true;
						}
						
					}
					
				}
				
			} catch (Exception e) {
				
			}
		}
		
		return retVal;
	}
	
	
	public static void saveTypesToDB(HashMap<String, String> types, DBconnection db){
		
		String[] keys = null;
		StringBuilder sql = null;
		
		if (types != null && types.size() > 0){
			keys = types.keySet().toArray(new String[types.size()]);
			
			int queries_ct = ((keys.length/db.max_sql_stack_size)+1);
			
			int i=0;
			int qcounter = 0;
			
			try{
				for (int q=0; q<queries_ct && i< keys.length; q++){
					
					sql = new StringBuilder();
					qcounter = 0;
					
					
					sql.append("insert into \""+DBconnection.tableTypes+"\"")
						.append(" select ")
							.append("  \"").append(keys[i]           ).append("\" as \""+DBconnection.colKey  +"\"")
							.append(", \"").append(types.get(keys[i])).append("\" as \""+DBconnection.colValue+"\"");
						
					
					for (++i; qcounter < db.max_sql_stack_size && i<keys.length; i++){
						qcounter++;
						try{
							sql.append(" union all select ")
								.append("  \"").append(keys[i]           ).append("\"")
								.append(", \"").append(types.get(keys[i])).append("\"");
						} catch(Exception e){
							System.err.println("E: SQL creation error: ["+e.getMessage()+"]");
						}
						
					}
					
					db.executeUpdate(sql.append(";").toString());
					sql.setLength(0);
				}
			}catch(Exception e){
				System.err.println("Cannot save types to DB");
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	
	public void clearAll(){
		if (attr_names != null) {
			attr_names.clear();
			attr_names = null;
		}
		if (attr_values != null) {
			attr_values.clear();
			attr_values = null;
		}
		if (attr_types_map != null) {
			attr_types_map.clear();
			attr_types_map = null;
		}
		
	}
	
}
