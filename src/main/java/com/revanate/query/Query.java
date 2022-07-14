package com.revanate.query;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import com.revanate.entity.ColumnField;
import com.revanate.entity.EntityModel;

public class Query {
	
	Connection conn;
	EntityModel<?> entity;
	
	public Query(Connection conn, EntityModel<?> entity) {
		this.conn = conn;
		this.entity = entity;
	}
	
    private void setParameter(PreparedStatement pstmt, Class<?> entityClass, Object object, int index, String fieldName) {
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
            }  else if (type.equals("byte")) {
                pstmt.setByte(index, (byte) value);
            } else if (type.equals("short")) {
                pstmt.setShort(index, (short) value);
            } else if (type.equals("float")) {
                pstmt.setFloat(index, (float) value);
            }else if (type.equals("long")) {
                pstmt.setLong(index, (long) value);
            }  else if (type.equals("boolean")) {
                pstmt.setBoolean(index, (boolean) value);
            }
        } catch (NoSuchFieldException | SecurityException | SQLException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }     
    }
    
    private void setParameter(PreparedStatement pstmt, Field field, Object object, int index) {
        String type = field.getType().toString();
        try {
            Object value = field.get(object);
            if (type.equals("int")) {
                pstmt.setInt(index, (int) value);
            } else if (type.equals("double")) {
                pstmt.setDouble(index, (double) value);
            } else if (type.contains("String")) {
                pstmt.setString(index, (String) value);
            }  else if (type.equals("byte")) {
                pstmt.setByte(index, (byte) value);
            } else if (type.equals("short")) {
                pstmt.setShort(index, (short) value);
            } else if (type.equals("float")) {
                pstmt.setFloat(index, (float) value);
            }else if (type.equals("long")) {
                pstmt.setLong(index, (long) value);
            }  else if (type.equals("boolean")) {
                pstmt.setBoolean(index, (boolean) value);
            }
            
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
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
    
    // this save method, accepts an object. Have to store that object as a row inside the DB
    public Object save(Object object) {
        // create new StringBuilder instance called sb
        StringBuilder sb = new StringBuilder();

        // append parts of query to sb
        sb.append("INSERT INTO ");

        // get the class name, ORM uses tables based on Model objects
        sb.append(entity.getSimpleClassName().toLowerCase());
        sb.append(" (");

        PrimaryKeyField pk = entity.GetPrimaryKey();
        int columnCount = entity.GetColumns().size();

        for (int idx = 0; idx < columnCount; idx++)
        {
            ColumnField column = entity.GetColumns().get(idx);
            String columnName = column.getColumnName();
            if (columnName.equals(pk.getColumnName())) {
                if (pk.getGenerationType().equals("none")) {
                    sb.append(column.getColumnName() + ", ");
                }
            } else {
                sb.append(column.getColumnName() + ", ");
            }
        }

        // get rid of last comma and space
        sb.replace(sb.length() - 2, sb.length(), "");
        sb.append(") VALUES(");

        if (pk != null && pk.getGenerationType().equals("auto")) {
            columnCount -= 1;
        }
        for (int idx = 0; idx < columnCount; idx++)
        {
            sb.append("?, ");
        }

        // get rid of last comma and space
        sb.replace(sb.length() - 2, sb.length(), "");
        // end the query
        sb.append(");");

        // creating PreparedStatement
        try {
            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            //.GetColumns() - returns list of ColumnFields
            for (int idx = 1; idx <= columnCount; idx++) {
                ColumnField field = entity.GetColumns().get(idx);
                // using field.getType()
                // field.getName() <- this gets field name in java
                // field.getColumnName() <- gets column name as represented in DB
                setParameter(pstmt, field.getType(), object, idx, field.getName());
            }
            System.out.println(pstmt.toString());
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
    public Object get(Class<?> clazz, int id) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(entity.getSimpleClassName().toLowerCase());
        sb.append(" WHERE ");
        sb.append(entity.GetPrimaryKey().getColumnName());
        sb.append(" = ?;");

        PreparedStatement pstmt = conn.prepareStatement(sb.toString());
        pstmt.setInt(1, id);
        
        // need to execute query, and convert result set back to object
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
			try {
				Constructor<?> constructor = clazz.getConstructor();
	        	Object resultObj = constructor.newInstance();
	            resultSetToObject(rs, resultObj);
	            return resultObj;
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
        }
        return null;
    }
    
    private void resultSetToObject(ResultSet rs, Object object) {
        for (ColumnField field : entity.GetColumns()) {
            Object value;
            try  {
                value = rs.getObject(field.getColumnName());
                Class<?> type = field.getType();
                String fieldType = type.getName();

                if (fieldType.equals("int")) {
                    type = Integer.class;
                } else if (fieldType.equals("double")) {
                    type = Double.class;
                } else if (fieldType.contains("String")) {
                    type = String.class;
                }  else if (fieldType.equals("byte")) {
                    type = Byte.class;
                } else if (fieldType.equals("short")) {
                    type = Short.class;
                } else if (fieldType.equals("float")) {
                    type = Float.class;
                } else if (fieldType.equals("long")) {
                    type = Long.class;
                }  else if (fieldType.equals("boolean")) {
                    type = Boolean.class;
                }

                // need to cast that value to specific class
                // example casting value to String
                //value = String.class.cast(value);
                value = type.cast(value);
                
                Field classField = object.getClass().getDeclaredField(field.getName());
                classField.setAccessible(true);
                classField.set(object, value);

                
            } catch (SQLException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
        }
    }
}