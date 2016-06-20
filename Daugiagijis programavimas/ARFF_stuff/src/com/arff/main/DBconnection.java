package com.arff.main;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.JDBC;

public class DBconnection {
	
	private volatile Connection connection = null;
	private String dbFile = "";
	private volatile static DBconnection dbConnection = null;
	private volatile Statement statement = null;
	
	public static final String tableMain = "tb_attribs";
	public static final String tableTypes = "tb_types";
	public static final String colKey    = "c_attrib";
	public static final String colValue  = "c_value";
	
	
	public static final String IN_MEMORY = ":memory:";
	
	public int max_sql_stack_size = 450;
	
	public DBconnection() {}
	
	public DBconnection(String dbPath) {
		dbFile = dbPath;
	}
	
	
	
	public void flushDB(){
		try {
			statement = connection.createStatement();
			statement.executeUpdate("drop table if exists "+tableMain);
			statement.executeUpdate("drop table if exists "+tableTypes);
		} catch (Exception e) {
			System.err.println("Unable to flush database: ["+e.getMessage()+"]");
		}
	}
	
	
	public void createDBtables(){
		try {
			statement = connection.createStatement();
			statement.executeUpdate("create table "+tableMain+" ("
						+ "  "+colKey  +" string"
						+ ", "+colValue+" string"
						//+ ", "+colFile +" string"
					+ ")");
			
			statement.executeUpdate("create table "+tableTypes+" ("
					+ "  "+colKey  +" string"
					+ ", "+colValue+" string"
					//+ ", "+colFile +" string"
				+ ")");
		} catch (Exception e) {
			System.err.println("Unable to prepare database: ["+e.getMessage()+"]");
		}
	}
	
	
	
	
	
	public boolean executeUpdate(String sql){
		boolean retVal = false;
		
		synchronized (connection) {
			try {
				
				statement = connection.createStatement();
				
				statement.executeUpdate(sql);
				retVal = true;
			} catch (SQLException e) {
				System.err.println("Unable to run an SQL update on DB: ["+e.getMessage()+"]");
			}
		}
		
		return retVal;
	}
	
	
	public ResultSet executeQuery(String sql){
		ResultSet retVal = null;
		
		synchronized (connection) {
			try {
				
				statement = connection.createStatement();
				
				retVal = statement.executeQuery(sql);
				
			} catch (SQLException e) {
				System.err.println("Unable to run an SQL update on DB: ["+e.getMessage()+"]");
			}
		}
		
		return retVal;
	}
	
	
	public void dumpDB(String filepath){
		try {
			
			statement = connection.createStatement();
			
			System.out.print("Dumping DB with ");
			ResultSet resultSet = statement.executeQuery("select count("+colKey+") from "+tableMain+";");
			
			System.out.println(""+resultSet.getInt(1)+" results");
			
			statement.executeUpdate("backup to '"+filepath+"'");
			System.out.println("DB backup has been successfully made");
		} catch (SQLException e) {
			System.err.println("E: Some error happened while dumping the DB: ["+e.getMessage()+"]. SQL state: ["+e.getSQLState()+"]");
		}
			
	}
	
	
	public boolean connectToDB(){
		return connectToDB(this.dbFile);
	}
	
	public boolean connectToDB(String path){
		boolean retVal = false;
		this.dbFile = path;
		try {
			// create a database connection
			File dbFile = new File(path);
			
			if (!dbFile.exists()) {
				dbFile.createNewFile();
				dbFile.setWritable(true);
				dbFile.setReadable(true);
			}
			
			
			if (dbFile.exists() && dbFile.isFile()) {
				connection = DriverManager.getConnection("jdbc:sqlite:"+path);
				retVal = true;
			} else {
				connection = null;
				
			}
		} catch(SQLException e) {
			// if the error message is "out of memory", 
			// it probably means no database file is found
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return retVal;
	}
	
	public void disconnect(){
		System.out.println("Closing database connection");
		if (connection != null){
			try {
				connection.close();
				connection = null;
				System.out.println("Database has been closed");
			} catch (SQLException e) {
				System.err.println("Unable to close DB connection: "+e.getMessage());
			}
		} else {
			System.err.println("Database has been closed");
		}
	}
	
	public String getPath(){
		return this.dbFile;
	}
	
	public boolean isValid(){
		boolean retVal = false;
		
		try {
		
			if (connection != null && connection.isValid(5)){
				retVal = true;
			} else {
				retVal = connectToDB();
			}
		} catch(Exception e) {
			
		}
		return retVal;
	}
	
	public static DBconnection getConnection(String dbfilepath){
		DBconnection retVal = null;
		
		if (dbConnection != null && dbConnection.isValid()){
			if (dbConnection.getPath().equals(dbfilepath)){
				//retVal = dbConnection;
			} else {
				dbConnection.disconnect();
				dbConnection = new DBconnection(dbfilepath);
				dbConnection.connectToDB();
				//retVal = dbConnection;
			}
			
		} else {
			dbConnection = new DBconnection(dbfilepath);
			dbConnection.connectToDB();
		}
		
		if (dbConnection != null){
			retVal = dbConnection;
		} else {
			dbConnection = new DBconnection(dbfilepath);
			retVal = dbConnection;
		}
		
		return retVal;
	}
	
}
