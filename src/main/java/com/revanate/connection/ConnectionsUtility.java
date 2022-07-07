package com.revanate.connection;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

public class ConnectionsUtility {

	private static BasicDataSource ds = new BasicDataSource();

	static {

		ds.setDriverClassName("org.postgresql.Driver");
		ds.setMinIdle(5);
		ds.setMaxIdle(10);
		ds.setMaxOpenPreparedStatements(100);
	}

	public static Connection getConnection(String url, String Username, String Password) throws SQLException {
		ds.setUrl(url);
		ds.setUsername(Username);
		ds.setPassword(Password);

		return ds.getConnection();

	}

	private ConnectionsUtility() {
	}
}