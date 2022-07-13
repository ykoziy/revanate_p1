package com.revanate.query;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Table {
	private Connection conn;
	
	public Table(Connection conn) {
		this.conn = conn;
	}
	
	public void createTable(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ");
		sb.append(clazz.getSimpleName().toLowerCase());
		sb.append(" (");

		for (Field field : fields) {
			sb.append(mapJavaFieldToSQL(field, clazz));
			sb.append(", ");
		}

		sb.replace(sb.length() - 2, sb.length(), "");
		sb.append(");");

		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.executeQuery(sb.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static String mapJavaFieldToSQL(Field field, Class<?> clazz) {
		String type = field.getType().getSimpleName().toLowerCase();
		String fieldName = field.getName();
		if (type.equals("int")) {
			return fieldName + " INTEGER";
		} else if (type.equals("double")) {
			return fieldName + " DOUBLE PRECISION";
		} else if (type.equals("string")) {
			return fieldName + " VARCHAR";
		} else if (type.equals("byte")) {

		} else if (type.equals("short")) {
			return fieldName + " SMALLINT";
		} else if (type.equals("float")) {
			return fieldName + " REAL";
		} else if (type.equals("char")) {
			return fieldName + " CHAR";
		} else if (type.equals("long")) {
			return fieldName + " BIGINT";
		} else if (type.equals("boolean")) {
			return fieldName + " BOOL";
		}
		return "";
	}
}
