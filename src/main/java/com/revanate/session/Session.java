package com.revanate.session;

import java.sql.Connection;
import java.sql.SQLException;
// each session is a connection to the database
public class Session {
	
	private Connection conn;
	
	private SessionCache cache;
	
	public Session(Connection conn) {
		this.conn = conn;
	}
	public Object get(Class<?> entityClass, int id) {
		return null;
	}
	
	public Object save(Object object) {
		return null;
	}
	
	public void delete(Object object) {
		
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
