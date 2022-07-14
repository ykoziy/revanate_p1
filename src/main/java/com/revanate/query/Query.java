package com.revanate.query;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import com.revanate.entity.EntityModel;

public class Query {
	
	Connection conn;
	EntityModel<?> entity;
	
	public Query(Connection conn) {
		this.conn = conn;
		this.entity = entity;
	}
	
	private void setParameter(PreparedStatement pstmt, Class<?> entityClass, Object object, int index, String fieldName) {
        String type = entityClass.getTypeName();
        System.out.println(type);
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
            } else if (type.equals("long")) {
                pstmt.setLong(index, (long) value);
            }  else if (type.equals("boolean")) {
                pstmt.setBoolean(index, (boolean) value);
            }
            
            } catch (NoSuchFieldException | SecurityException | SQLException | IllegalArgumentException | IllegalAccessException e) {
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
        System.out.println(sb.toString());
        System.out.println(entity.GetPrimaryKey().getName());
        setParameter(pstmt, entity.GetPrimaryKey().getType(), object, 1, entity.GetPrimaryKey().getName());
        int rows = pstmt.executeUpdate();        
    }    	

    // this save method, accepts an object. Have to store that object as a row inside the DB
    public Object save(Object object) throws SQLException {
        // get the runtime class of the object
        Class<?> clazz = object.getClass();

        // get the fields included in the class
        Field[] fields = clazz.getDeclaredFields();

        // create new StringBuilder instance called sb
        StringBuilder sb = new StringBuilder();

        // append parts of query to sb
        sb.append("INSERT INTO ");

        // get the class name, ORM uses tables based on Model objects
        sb.append(clazz.getSimpleName().toLowerCase());
        sb.append(" (");

        // this loop, just inserting column names
        for (Field field : fields)
        {
            sb.append(field.getName() + ", ");
            // field can be private so make it accessible
            field.setAccessible(true);
        }

        // get rid of last comma and space
        sb.replace(sb.length() - 2, sb.length(), "");
        sb.append(") VALUES(");

        // this loop just appending placeholders question
        // marks for the parameters of the PreparedStatement
        for (Field field : fields)
        {
            sb.append("?, ");
        }

        // get rid of last comma and space
        sb.replace(sb.length() - 2, sb.length(), "");
        // end the query
        sb.append(");");

        // creating PreparedStatement
        PreparedStatement pstmt = conn.prepareStatement(sb.toString());

        // set prepared statement fields, using correct data types
        for (int idx = 1; idx <= fields.length; idx++) {
            setParameter(null, fields[idx-1], object, idx);
        }

        /// executeUpdate using built prepared statement
        try {
            int rows = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        /// Figure out what to do with the primary key in query, and how to return it?
        return null;
    }
    
    // get
    public Object get(Object object) throws SQLException {
    	Class<?> clazz = object.getClass();
    	Field[] fields = clazz.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(clazz.getSimpleName().toLowerCase());
        sb.append(" WHERE ");
        //sb.append(field.getName());
        sb.append(" == ");
        //sb.append(field.getId());
        sb.append(";");

        PreparedStatement pstmt = conn.prepareStatement(sb.toString());        
        
        setParameter(pstmt, fields[0], object, 1);
        
        try {
            int rows = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void resultSetToObject(ResultSet rs, Object object) {

        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            // field might be private, set accessible to true
            field.setAccessible(true);
            Object value;
            try  {
                value = rs.getObject(field.getName());
                Class<?> type = field.getType();
            	String fieldType = type.getName();
                // figure out what type, need to get data type wrapper class
                // for example int, needs to be Integer.class
                /// String, needs to be String.class
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
                field.set(object, value); // use this to set field value

                
            } catch (SQLException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}