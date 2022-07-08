package com.revanate.session;

import java.sql.SQLException;

import com.revanate.config.Configuration;
import com.revanate.connection.ConnectionsUtility;

public class SessionManager {
	
	private Configuration cfg;
	// one per data source, one SessionManager, multiple Sessions?
	// set up a connection
	
	public SessionManager(Configuration cfg) {
		this.cfg = cfg;
	}
	
	public Session openSession() {
		//do setup and return new session, with connection to the DB
		try {
			return new Session(ConnectionsUtility.getConnection(cfg.getDbUrl(), cfg.getDbUsername(), cfg.getDbPassword()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
