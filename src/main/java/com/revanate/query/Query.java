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
    public void delete(Object object) {
    	Class<?> clazz = object.getClass();
    	Field[] fields = clazz.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
    	sb.append("DELETE FROM ");
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

    // this save method, accepts an object. Have to store that object as a row inside the DB
    public Object save(Object object) {
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