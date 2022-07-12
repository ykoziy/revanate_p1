package com.revanate.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.HashMap;

public class Transaction {
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

	public void rollback() {

		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void rollback(final String name) throws SQLException {
		Savepoint sp = savePoints.get(name);

		if (sp != null) {
			conn.rollback(sp);
		}

	}

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

	public void setAutoCommit(boolean isEnabled) {

		try {
			conn.setAutoCommit(isEnabled);
		} catch (SQLException e) {
			e.printStackTrace();

		}

	}
}
