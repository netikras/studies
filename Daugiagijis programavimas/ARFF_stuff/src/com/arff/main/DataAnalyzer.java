package com.arff.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DataAnalyzer {
	
	private String databasePath = "";
	private String arffDirectory = "";
	//private boolean useInMemoryDatabase = false;
	private int threadPoolSize = 1;
	private int threadExecutionTimeout = 10;
	
	ARFF_FileAnalyzer[] threads = null;
	ThreadPoolExecutor executor = null;
	
	Map<String, ArrayList<String>> RESULTS = null;
	Map<String, String> TYPES = null;
	
	RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
			
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				try {
					//System.err.println("Thread pool queue is full. Waiting to add thread for file: "+((ARFF_FileAnalyzer)r).sourceFile);
					System.out.print(".");
					executor.getQueue().put(r);
				} catch(InterruptedException e){
					throw new RejectedExecutionException("Interrupted!", e);
				}
			}
		};
	
	
	
	
	
	
	
	
	
	public void setDatabasePath(String newPath){
		databasePath = newPath;
	}
	
	//public void setUseInMemoryDatabase(boolean useInMemory){ 
	//	useInMemoryDatabase = useInMemory;
	//}
	
	public void setArffDirectory(String path){
		arffDirectory = path;
	}
	
	
	
	public String[] getAttributesListFromFiles(){
		
		ArrayList<String> retVal = new ArrayList<String>();
		File arffDir = null;
		File arffFile = null;
		File[] dirContents = null;
		
		BufferedReader reader = null;
		String line = "";
		boolean inDataSection = false;
		String[] lineFields = null;
		
		
		if (arffDirectory != null && !arffDirectory.isEmpty()){
			arffDir = new File(arffDirectory);
			if (arffDir.exists() && arffDir.isDirectory()){
				dirContents = arffDir.listFiles();
				if (dirContents != null && dirContents.length > 0){
					arffFile = dirContents[0];
					
					try {
						reader = new BufferedReader(new FileReader(arffFile));
						
						while (!inDataSection && (line = reader.readLine()) != null){
							if (line.isEmpty()) continue;
							
							if (line.startsWith(ARFF_FileAnalyzer.attribLinePrefix)){
								if ((lineFields = line.split(" ", 3)) != null && lineFields.length == 3){
									retVal.add(lineFields[1]);
								}
								
							} else
							if (line.startsWith(ARFF_FileAnalyzer.dataPrefix)){
								inDataSection = true;
							}
							
						}
						
					} catch (Exception e) {
						System.err.println("Failed to open a file ["+arffFile.toString()+"]. E: ["+e.getMessage()+"]");
					} finally {
						try{
							reader.close();
						} catch(Exception e2){
							System.err.println("Cannot close file reader: ["+e2.getMessage()+"]");
						}
					}
					
				}
			}
		}
		
		return retVal.toArray(new String[retVal.size()]);
	}
	
	public String[] getAttributesListFromDatabase(){
		ArrayList<String> retVal = new ArrayList<String>();
		ResultSet result = null;
		
		boolean haveResults = true;
		
		String sql = "select distinct "+DBconnection.colKey+" from "+DBconnection.tableMain+";";
		
		
		result = DBconnection.getConnection(databasePath).executeQuery(sql);
		
		if (result != null){
			try {
				while(haveResults){
					retVal.add(result.getString(DBconnection.colKey  ));
					haveResults = result.next();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return retVal.toArray(new String[retVal.size()]);
	}
	
	
	public HashMap<String, String> getAttributesTypesFromFiles(){
		
		HashMap<String, String> retVal = new HashMap<String, String>();
		File arffDir = null;
		File arffFile = null;
		File[] dirContents = null;
		
		BufferedReader reader = null;
		String line = "";
		boolean inDataSection = false;
		String[] lineFields = null;
		
		
		if (arffDirectory != null && !arffDirectory.isEmpty()){
			arffDir = new File(arffDirectory);
			if (arffDir.exists() && arffDir.isDirectory()){
				dirContents = arffDir.listFiles();
				if (dirContents != null && dirContents.length > 0){
					arffFile = dirContents[0];
					
					try {
						reader = new BufferedReader(new FileReader(arffFile));
						
						while (!inDataSection && (line = reader.readLine()) != null){
							if (line.isEmpty()) continue;
							
							if (line.startsWith(ARFF_FileAnalyzer.attribLinePrefix)){
								if ((lineFields = line.split(" ", 3)) != null && lineFields.length == 3){
									retVal.put(lineFields[1], lineFields[2]);
								}
								
							} else
							if (line.startsWith(ARFF_FileAnalyzer.dataPrefix)){
								inDataSection = true;
							}
							
						}
						
					} catch (Exception e) {
						System.err.println("Failed to open a file ["+arffFile.toString()+"]. E: ["+e.getMessage()+"]");
					} finally {
						try{
							reader.close();
						} catch(Exception e2){
							System.err.println("Cannot close file reader: ["+e2.getMessage()+"]");
						}
					}
					
				}
			}
		}
		
		return retVal;
	}
	
	public HashMap<String, String> getAttributesTypesFromDatabase(){
		HashMap<String, String> retVal = new HashMap<String, String>();
		
		StringBuilder sql = new StringBuilder("SELECT * FROM "+DBconnection.tableTypes+" ;");
		ResultSet resultSet = null;
		
		String key = "";
		String value = "";
		
		boolean haveResults = true;
		
		resultSet = DBconnection.getConnection(databasePath).executeQuery(sql.toString());
		
		if (resultSet != null){
			
			try {
				
				while(haveResults){
					
					key   = resultSet.getString(DBconnection.colKey  );
					value = resultSet.getString(DBconnection.colValue);
					
					retVal.put(key, value);
					
					haveResults = resultSet.next();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return retVal;
	}
	
	
	
	public int findAttributesInDatabase(String[] attributesNames){
		int retVal = 0;
		
		threads = new ARFF_FileAnalyzer[1];
		threads[0] = new ARFF_FileAnalyzer(DBconnection.getConnection(databasePath), attributesNames);
		
		startAllThreads();
		waitForThreads();
		packResults();
		
		retVal = RESULTS.size();
		
		return retVal;
	}
	
	
	public int findAttributesInFiles(String[] attributesNames){
		int retVal = 0;
		
		File directory = new File(arffDirectory);
		File[] files = null;
		
		if (directory.exists() && directory.isDirectory() && (files = directory.listFiles()) != null){
			
			threads = new ARFF_FileAnalyzer[files.length];
			
			for (int i=0; i<threads.length; i++){
				threads[i] = new ARFF_FileAnalyzer(files[i], attributesNames);
				retVal++;
			}
			
			System.out.println("Total files: "+retVal);
			
			
			if (threads.length > 0){
				TYPES = ARFF_FileAnalyzer.getTypesMap(directory, attributesNames);
			}
			
			startAllThreads();
			waitForThreads();
			packResults();
			
			retVal = RESULTS.size();
			
		}
		
		
		
		return retVal;
	}
	
	

	public int saveAttributesToDatabase(boolean cacheInMemory, String databaseDumpPath){
		int retVal = 0;
		
		File directory = new File(arffDirectory);
		File[] files = null;
		
		DBconnection dBconnection = null;
		
		if (directory.exists() && directory.isDirectory() && (files = directory.listFiles()) != null){
			
			threads = new ARFF_FileAnalyzer[files.length];
			
			if (cacheInMemory){
				dBconnection = DBconnection.getConnection(DBconnection.IN_MEMORY);
			} else {
				dBconnection = DBconnection.getConnection(databaseDumpPath);
			}
			
			dBconnection.flushDB();
			dBconnection.createDBtables();
			
			for (int i=0; i<threads.length; i++){
				threads[i] = new ARFF_FileAnalyzer(files[i], dBconnection);
				retVal++;
			}
			System.out.println("Total files: "+retVal);
			
			startAllThreads();
			waitForThreads();
			packResults();
			
			if (TYPES != null && TYPES.size() > 0){
				ARFF_FileAnalyzer.saveTypesToDB((HashMap<String, String>)TYPES, dBconnection);
			}
			
			if (cacheInMemory){
				System.out.println("Writing database to the filesystem");
				dBconnection.dumpDB(databaseDumpPath);
				System.out.println("Done!");
			}
			
			
		}
		
		return retVal;
	}
	
	
	
	
	
	public void printResults(){
		if (RESULTS != null){
			ArrayList<String> list = null;
			for (String key : RESULTS.keySet()){
				System.out.println("Key: "+key);
				list = RESULTS.get(key);
				if (list != null){
					for (String value : list){
						System.out.println("\t"+value);
					}
				}
			}
		}
	}
	
	private void packResults(){
		
		if (threads != null){
			
			RESULTS = new HashMap<String, ArrayList<String>>();
			ArrayList<String> list = null;
			
			for (int t=0; t<threads.length; t++){
				Map<String, ArrayList<String>> threadResults = threads[t].getResultsMap();
				
				for (String key : threadResults.keySet()){
					list = RESULTS.get(key);
					if (list == null){
						list = new ArrayList<String>();
						RESULTS.put(key, list);
					}
					
					for (String string : threadResults.get(key)){
						list.add(string);
					}
				}
				
				threadResults = null;
			}
			
			
			
			
			for (int t=0; t<threads.length; t++){
				threads[t] = null;
			}
			
			threads = null;
			
		}
		
	}
	
	public Map<String, ArrayList<String>> getResults(){
		Map<String, ArrayList<String>> retVal = RESULTS;
		return retVal;
	}
	
	public Map<String, String> getCachedTypes(){
		Map<String, String> retVal = null;
		
		if (TYPES != null){
			retVal = TYPES;
		} else {
			retVal = new HashMap<String, String>();
		}
		
		return retVal;
	}
	
	
	public boolean setThreadPoolSize(int size){
		boolean retVal = false;
		if (size < 0){
			System.err.println("Cannot set pool size lower than zero ("+size+")");
		} else {
			threadPoolSize = size;
			retVal = true;
		}
		
		return retVal;
	}
	
	public boolean setOperationTimeout(int seconds){
		boolean retVal = false;
		if (seconds < 0){
			System.err.println("Cannot set timeout lower than zero ("+seconds+")");
		} else {
			threadExecutionTimeout = seconds;
			retVal = true;
		}
		
		return retVal;
	}
	
	
	
	
	
	private void startAllThreads(){
		
		if (threads != null && threads.length > 0){
			
			System.out.println("Starting workers: "+threads.length+"; pool size: "+threadPoolSize+"; single worker MAX execution time: "+threadExecutionTimeout+"sec");
			
			executor = new ThreadPoolExecutor(threadPoolSize/5, threadPoolSize, threadExecutionTimeout, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(threadPoolSize));
			
			executor.setRejectedExecutionHandler(rejectedExecutionHandler);
			
			for (int i=0; i<threads.length; i++){
				try{
					executor.execute(threads[i]);
				} catch(Exception e){
					System.err.println("Unable to execute a thread: ["+e.getMessage()+"]");
					e.printStackTrace();
				}
			}
			
			System.out.println("All workers have been started");
		}
	}
	
	private void waitForThreads(){
		
		if (executor != null){
			executor.shutdown();
			
			try{
				
				System.out.println("Waiting for all workers to finish");
				
				if (executor.awaitTermination(threadExecutionTimeout, TimeUnit.SECONDS)){
					System.out.println("All workers have finished");
				} else {
					System.err.println("Workers failed to finish on time");
				}
				
			} catch(Exception e){
				System.err.println("await has been interrupted");
			}
			
			if (executor.isTerminated()){
				System.out.println("All workers are terminated");
			} else {
				System.err.println("Not all workers are terminated");
			}
		}
	}
	
}
