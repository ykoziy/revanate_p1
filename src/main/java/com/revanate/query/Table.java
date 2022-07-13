package com.revanate.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.revanate.entity.ColumnField;
import com.revanate.entity.EntityModel;
import com.revanate.entity.ForeignKeyField;
import com.revanate.entity.PrimaryKeyField;

public class Table {
    private Connection conn;
    
    public Table(Connection conn) {
        this.conn = conn;
    }
    
    public void createTable(EntityModel<?> entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS " + entity.getSimpleClassName().toLowerCase());
        sb.append(" (");
        for (ColumnField column : entity.GetColumns()) {
            sb.append(mapJavaFieldToSQL(column.getColumnName(), column.getType().toString()));
            sb.append(", ");
        }
        
        sb.replace(sb.length() - 2, sb.length(), "");
        PrimaryKeyField pk = entity.GetPrimaryKey();
        if (pk != null) {
            sb.append(", PRIMARY KEY(" + pk.getColumnName() + ")");
        }
        sb.append(");");
        
        System.out.println(sb.toString());
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.execute(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String mapJavaFieldToSQL(String fieldName, String type) {
        if (type.equals("int")) {
            return fieldName + " INTEGER";
        } else if (type.equals("double")) {
            return fieldName + " DOUBLE PRECISION";
        } else if (type.contains("String")) {
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