package com.revanate.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.revanate.entity.ColumnField;
import com.revanate.entity.EntityModel;
import com.revanate.entity.ForeignKeyField;
import com.revanate.entity.PrimaryKeyField;

public class Table {
    private Connection conn;
    private List<EntityModel<Class<?>>> entityModelList;
    
    public Table(Connection conn, List<EntityModel<Class<?>>> entityModelList) {
    	this.entityModelList = entityModelList;
        this.conn = conn;
    }
    
    public void createTable(EntityModel<?> entity) {
        StringBuilder sb = new StringBuilder();
        PrimaryKeyField pk = entity.GetPrimaryKey();
        List<ForeignKeyField> foreignKeys = entity.GetForiegnKeys();
        sb.append("CREATE TABLE IF NOT EXISTS " + entity.getSimpleClassName().toLowerCase());
        sb.append(" (");
        for (ColumnField column : entity.GetColumns()) {
            if (pk.getColumnName().equals(column.getColumnName())) {
                if (pk.getGenerationType().equals("auto")) {
                    sb.append(column.getColumnName() + " SERIAL NOT NULL");
                } else {
                    sb.append(mapJavaFieldToSQL(column.getColumnName(), column.getType().toString()));
                }

            } else {
                sb.append(mapJavaFieldToSQL(column.getColumnName(), column.getType().toString()));
            }
            sb.append(", ");
        }

        if (foreignKeys != null) {
            for (ForeignKeyField fk : foreignKeys) {
                for (EntityModel<?> model : entityModelList) {
                    if (fk.getType().getName().equals(model.getClassName())) {
                        Class<?> type = model.GetPrimaryKey().getType();
                        sb.append(mapJavaFieldToSQL(fk.getColumnName(), type.toString()));
                         sb.append(", ");
                    }
                }

            }
        }

        sb.replace(sb.length() - 2, sb.length(), "");
        if (pk != null) {
            sb.append(", PRIMARY KEY(" + pk.getColumnName() + ")");
        }

        if (foreignKeys != null) {
            for (ForeignKeyField fk : foreignKeys) {
                for (EntityModel<?> model : entityModelList) {
                    if (fk.getType().getName().equals(model.getClassName())) {
                        String pkName = model.GetPrimaryKey().getColumnName();
                        sb.append(", CONSTRAINT " + fk.getColumnName());
                        sb.append(" FOREIGN KEY(" + fk.getColumnName() + ")");
                        sb.append(" REFERENCES " + model.getSimpleClassName().toLowerCase());
                        sb.append("(" + pkName + ")");
                    }
                }

            }
        }
        sb.append(");");

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