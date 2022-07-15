package com.revanate.query;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.revanate.entity.ColumnField;
import com.revanate.entity.EntityModel;
import com.revanate.entity.PrimaryKeyField;

public class Query {

	private Connection conn;
	private EntityModel<?> entity;
	private Object[] objectArray;
	private Class<?> resultClass;

	public Query(Connection conn, EntityModel<?> entity) {
		this.conn = conn;
		this.entity = entity;
	}

	public <T> List<T> list() {
		List<T> list = new LinkedList<>();
		for (int i = 0; i < objectArray.length; i++) {
			list.add((T) resultClass.cast(objectArray[i]));
		}
		return list;
	}

	public Query getAll(Class<?> resultType) throws SQLException {
		resultClass = resultType;
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ");
		sb.append(resultType.getSimpleName().toLowerCase());
		sb.append(";");
		PreparedStatement pstmt = conn.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = pstmt.executeQuery();
		int rowCount = rs.last() ? rs.getRow() : 0;
		int index = 0;
		rs.beforeFirst();
		objectArray = new Object[rowCount];
		while (rs.next()) {
			try {
				Constructor<?> constructor = resultType.getConstructor();
				Object resultObj = constructor.newInstance();
				resultSetToObject(rs, resultObj);
				objectArray[index] = resultObj;
				index++;
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}

		}
		return this;
	}

	private void setParameter(PreparedStatement pstmt, Class<?> entityClass, Object object, int index,
			String fieldName) {
		String type = entityClass.getTypeName();
		Field field;
		try {
			field = object.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			Object value = field.get(object);
			if (type.equals("int")) {
				pstmt.setInt(index, (int) value);
			} else if (type.equals("double")) {
				pstmt.setDouble(index, (double) value);
			} else if (type.contains("String")) {
				pstmt.setString(index, (String) value);
			} else if (type.equals("byte")) {
				pstmt.setByte(index, (byte) value);
			} else if (type.equals("short")) {
				pstmt.setShort(index, (short) value);
			} else if (type.equals("float")) {
				pstmt.setFloat(index, (float) value);
			} else if (type.equals("long")) {
				pstmt.setLong(index, (long) value);
			} else if (type.equals("boolean")) {
				pstmt.setBoolean(index, (boolean) value);
			}
		} catch (NoSuchFieldException | SecurityException | SQLException | IllegalArgumentException
				| IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private void setParameter(PreparedStatement pstmt, Object value, int index) {
		String fieldName = value.getClass().getTypeName();
		try {
			if (fieldName.equals("java.lang.Integer")) {
				pstmt.setInt(index, (int) value);
			} else if (fieldName.equals("java.lang.Double")) {
				pstmt.setDouble(index, (double) value);
			} else if (fieldName.equals("java.lang.String")) {
				pstmt.setString(index, (String) value);
			} else if (fieldName.equals("java.lang.Byte")) {
				pstmt.setByte(index, (byte) value);
			} else if (fieldName.equals("java.lang.Short")) {
				pstmt.setShort(index, (short) value);
			} else if (fieldName.equals("java.lang.Float")) {
				pstmt.setFloat(index, (float) value);
			} else if (fieldName.equals("java.lang.Long")) {
				pstmt.setLong(index, (long) value);
			} else if (fieldName.equals("java.lang.Boolean")) {
				pstmt.setBoolean(index, (boolean) value);
			}
		} catch (SecurityException | SQLException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	// delete
	public void delete(Object object) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(entity.getSimpleClassName().toLowerCase());
		sb.append(" WHERE ");
		sb.append(entity.GetPrimaryKey().getColumnName());
		sb.append(" = ?");
		sb.append(";");

		PreparedStatement pstmt = conn.prepareStatement(sb.toString());
		setParameter(pstmt, entity.GetPrimaryKey().getType(), object, 1, entity.GetPrimaryKey().getName());
		int rows = pstmt.executeUpdate();
	}

	// update
	public void update(Object object) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ");

		sb.append(entity.getSimpleClassName().toLowerCase());
		sb.append(" SET ");

		PrimaryKeyField pk = entity.GetPrimaryKey();
		int columnCount = entity.GetColumns().size();

		for (int idx = 0; idx < columnCount; idx++) {
			ColumnField column = entity.GetColumns().get(idx);
			String columnName = column.getColumnName();
			if (columnName.equals(pk.getColumnName())) {
				if (pk.getGenerationType().equals("none")) {
					sb.append(column.getColumnName() + "=?, ");
				}
			} else {
				sb.append(column.getColumnName() + "=?, ");
			}
		}

		sb.replace(sb.length() - 2, sb.length(), "");

		if (pk != null && pk.getGenerationType().equals("auto")) {
			columnCount -= 1;
		}

		sb.append(" WHERE " + entity.GetPrimaryKey().getColumnName() + "=?;");

		try {
			PreparedStatement pstmt = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			for (int idx = 1; idx <= columnCount; idx++) {
				ColumnField field = entity.GetColumns().get(idx);
				setParameter(pstmt, field.getType(), object, idx, field.getName());
			}
			setParameter(pstmt, pk.getType(), object, columnCount + 1, pk.getName());
			int rows = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// this save method, accepts an object. Have to store that object as a row
	// inside the DB
	public Object save(Object object) {
		// create new StringBuilder instance called sb
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();

		sb2.append("VALUES (");
		// append parts of query to sb
		sb.append("INSERT INTO ");

		// get the class name, ORM uses tables based on Model objects
		sb.append(entity.getSimpleClassName().toLowerCase());
		sb.append(" (");

		PrimaryKeyField pk = entity.GetPrimaryKey();
		int columnCount = entity.GetColumns().size();

		for (int idx = 0; idx < columnCount; idx++) {
			ColumnField column = entity.GetColumns().get(idx);
			String columnName = column.getColumnName();
			if (columnName.equals(pk.getColumnName())) {
				if (pk.getGenerationType().equals("none")) {
					sb.append(column.getColumnName() + ", ");
				}
			} else {
				sb.append(column.getColumnName() + ", ");
				sb2.append("?, ");
			}
		}

		// get rid of last comma and space
		sb.replace(sb.length() - 2, sb.length(), "");
		sb.append(") ");
		sb2.replace(sb2.length() - 2, sb2.length(), "");
		sb2.append(")");

		if (pk != null && pk.getGenerationType().equals("auto")) {
			columnCount -= 1;
		}

		// end the query
		sb.append(sb2 + ";");

		// creating PreparedStatement
		try {
			PreparedStatement pstmt = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			// .GetColumns() - returns list of ColumnFields
			for (int idx = 1; idx <= columnCount; idx++) {
				ColumnField field = entity.GetColumns().get(idx);
				// using field.getType()
				// field.getName() <- this gets field name in java
				// field.getColumnName() <- gets column name as represented in DB
				setParameter(pstmt, field.getType(), object, idx, field.getName());
			}
			int rows = pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				return rs.getObject(1);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// get
	public <T> Object get(Class<?> clazz, T id) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ");
		sb.append(entity.getSimpleClassName().toLowerCase());
		sb.append(" WHERE ");
		sb.append(entity.GetPrimaryKey().getColumnName());
		sb.append(" = ?;");

		PreparedStatement pstmt = conn.prepareStatement(sb.toString());
		setParameter(pstmt, id, 1);
		// pstmt.setInt(1, (int) id);

		// need to execute query, and convert result set back to object
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			try {
				Constructor<?> constructor = clazz.getConstructor();
				Object resultObj = constructor.newInstance();
				resultSetToObject(rs, resultObj);
				return resultObj;
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private void resultSetToObject(ResultSet rs, Object object) {
		for (ColumnField field : entity.GetColumns()) {
			Object value;
			try {
				value = rs.getObject(field.getColumnName());
				Class<?> type = field.getType();
				String fieldType = type.getName();

				if (fieldType.equals("int")) {
					type = Integer.class;
				} else if (fieldType.equals("double")) {
					type = Double.class;
				} else if (fieldType.contains("String")) {
					type = String.class;
				} else if (fieldType.equals("byte")) {
					type = Byte.class;
				} else if (fieldType.equals("short")) {
					type = Short.class;
				} else if (fieldType.equals("float")) {
					type = Float.class;
				} else if (fieldType.equals("long")) {
					type = Long.class;
				} else if (fieldType.equals("boolean")) {
					type = Boolean.class;
				}

				// need to cast that value to specific class
				// example casting value to String
				// value = String.class.cast(value);
				value = type.cast(value);

				Field classField = object.getClass().getDeclaredField(field.getName());
				classField.setAccessible(true);
				classField.set(object, value);

			} catch (SQLException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException
					| SecurityException e) {
				e.printStackTrace();
			}
		}
	}
}