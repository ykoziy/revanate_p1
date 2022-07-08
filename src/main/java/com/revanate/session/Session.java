package com.revanate.session;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.revanate.transaction.Transaction;

// each session is a connection to the database
public class Session {
	
	private Connection conn;
	
	private SessionCache cache;
	
	public Session(Connection conn) {
		this.conn = conn;
	}
	public Object get(Class<?> entityClass, int id) {
		// ID can be any type, figure out  best way to do it
		String tableName = entityClass.getName();
		/* 
		 * 1) Get the table name (Entity annotation is empty string, use class name as table name as lower case), or specified in Entity annotation tableName
		 * 2) Get the name of primary key field(s)
		 * 3) Create query based on that data
		 * 4) Build an Object based on the result of query?
		 * 5) If everything went OK, just store the Object inside the cache
		 * 6) Return primary key (ID), it is set to Object because it can be any type
		 */	
		return null;
	}
	
	public Object save(Object object) {		
		/* 
		 * 1) Convert object to EntityModel
		 * 2) Get field names and field data
		 * 3) Create query using data from previous data
		 * 4) Run that query on the DB, using session connection
		 * 5) If everything went OK, just store the Object inside the cache
		 * 6) Return primary key (ID), it is set to Object because it can be any type
		 */
		return null;
	}
	
	public void delete(Object object) {
		/* 
		 * 1) Convert object to EntityModel
		 * 2) Get fprimaryKey ID
		 * 3) Create query using data from previous data
		 * 4) Run that query on the DB, using session connection
		 * 5) If everything went OK, just store the Object inside the cache
		 * 6) Return primary key (ID), it is set to Object because it can be any type
		 */	
	}
	
	public Transaction beginTransaction() {
		// check if there is already a transaction for this session, continue that transaction
		// if no transaction for the session create one
		// return associated transaction object
		return null;
	}
	
	// clean up and release JDBC connection
	public void close() {	
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
