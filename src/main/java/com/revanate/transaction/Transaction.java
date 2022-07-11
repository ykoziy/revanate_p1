package com.revanate.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.HashMap;

public class Transaction {
	// 	begin database commit.
	private Connection conn;
	private HashMap<String, Savepoint> savePoints;
	
	public Transaction(Connection conn) {
		this.conn = conn;
		savePoints = new HashMap<String, Savepoint>();
		
	}
		
	   public void commit() {
		   
		   try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
	}
	
	//Rollback to previous commit.
	public void rollback() {
		
		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	//Rollback to previous commit with given name.
	public void rollback(final String name) throws SQLException {
		Savepoint sp = savePoints.get(name);
		
		if (sp != null) {
			conn.rollback(sp);			
		}
		
	}
	
	//Set a savepoint with the given name.
	public void setSavepoint(final String name) {
		
		if (!savePoints.containsKey(name)) {
			try {
				Savepoint sp = conn.setSavepoint();
				savePoints.put(name, sp);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
			
	}
	
	//Release the savepoint with the given name.
	public void releaseSavepoint(final String name) {
		
Savepoint sp = savePoints.get(name);
		
		if (sp != null) {
			try {
				conn.releaseSavepoint(sp);
				savePoints.remove(name);
			} catch (SQLException e) {
				e.printStackTrace();
			}	
	}
}

	//Enable auto commits on the database.
	public void setAutoCommit(boolean isEnabled) {
		
		try {
			conn.setAutoCommit(isEnabled);
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		
	}
	
	//Start a transaction block.
	public void setTransaction() {
		
		
		
	}

}
