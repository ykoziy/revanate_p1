import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public class Query {
	
	Connection conn;
	
	public Query(Connection conn) {
		this.conn = conn;
	}
	
	private static void setParameter(PreparedStatement pstmt, Field field, Object object, int index) {
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
                pstmt.setString(index, (String) value);
            } else if (type.equals("short")) {
                pstmt.setString(index, (String) value);
            } else if (type.equals("float")) {
                pstmt.setString(index, (String) value);
            } else if (type.equals("char")) {
                pstmt.setString(index, (String) value);
            } else if (type.equals("long")) {
                pstmt.setString(index, (String) value);
            }  else if (type.equals("boolean")) {
                pstmt.setString(index, (String) value);
            }
            
            } catch (IllegalAccessException e) {
            	throw new RuntimeException(e);
            } catch (SQLException e) {
            	throw new RuntimeException(e);
        }
    }

     // delete
    public Object delete(Object object) {
    	Class<?> clazz = object.getClass();
    	Field[] fields = clazz.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
    	sb.append("DELETE ");
        sb.append(clazz.getSimpleName().toLowerCase());
        sb.append(" (");

        for (Field field : fields)
        {
            sb.append(field.getName() + ", ");
            field.setAccessible(true);
        }

        // get rid of last comma and space
        sb.replace(sb.length() - 2, sb.length(), "");
        sb.append(") VALUES(");
        for (Field field : fields)
        {
            sb.append("?, ");
        }
        sb.replace(sb.length() - 2, sb.length(), "");
        sb.append(");");

        for (Field field : fields)
        {
            System.out.println(field.getType());
        }

        System.out.println(sb.toString());

        // set prepared statement fields, using correct types
        PreparedStatement pstmt = conn.prepareStatement(sb.toString());        
        
        /// executeUpdate using built prepared statement
        for (int idx = 1; idx <= fields.length; idx++) {
            setParameter(pstmt, fields[idx-1], object, idx);
        } 
        
        /// Figure out what to do with the primary key in query, and how to return it?

        return null;
    }    	

    // save/insert
    public Object save(Object object) {
    	// create sql query
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(clazz.getSimpleName().toLowerCase());
        sb.append(" (");

        for (Field field : fields)
        {
            sb.append(field.getName() + ", ");
            field.setAccessible(true);
        }

        // get rid of last comma and space
        sb.replace(sb.length() - 2, sb.length(), "");
        sb.append(") VALUES(");
        for (Field field : fields)
        {
            sb.append("?, ");
        }
        sb.replace(sb.length() - 2, sb.length(), "");
        sb.append(");");

        // set prepared statement fields, using correct types
        PreparedStatement pstmt = conn.prepareStatement(sb.toString());
        
        for (int idx = 1; idx <= fields.length; idx++) {
            setParameter(pstmt, fields[idx-1], object, idx);
        } 
        /// executeUpdate using built prepared statement
        /// Figure out what to do with the primary key in query, and how to return it?

        return null;
    }
    
    // get
//    public static Object get(Object object) {
//        Class<?> clazz = object.getClass();
//        Field[] fields = clazz.getDeclaredFields();
//        StringBuilder sb = new StringBuilder();
//        sb.append(" ");
//        sb.append(clazz.getSimpleName().toLowerCase());
//        sb.append(" (");
//
//        for (Field field : fields)
//        {
//            sb.append(field.getName() + ", ");
//            field.setAccessible(true);
//        }
//
//        // get rid of last comma and space
//        sb.replace(sb.length() - 2, sb.length(), "");
//        sb.append(") VALUES(");
//        for (Field field : fields)
//        {
//            sb.append("?, ");
//        }
//        sb.replace(sb.length() - 2, sb.length(), "");
//        sb.append(");");
//
//        for (Field field : fields)
//        {
//            System.out.println(field.getType());
//        }
//
//        System.out.println(sb.toString());
//
//        // set prepared statement fields, using correct types
//        /// executeUpdate using built prepared statement
//        /// Figure out what to do with the primary key in query, and how to return it?
//
//        return null;
//    }
}