package com.revanate.session;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.revanate.query.Query;
import com.revanate.query.Query2;
import com.revanate.query.Table;
import com.revanate.entity.EntityModel;
import com.revanate.transaction.Transaction;

// each session is a connection to the database
public class Session {
	
	private Connection conn;
	
	private SessionCache cache;
	
	private Transaction currenTransaction;
	
	private List<EntityModel<Class<?>>> entityModelList;
	
	public Session(Connection conn, List<EntityModel<Class<?>>> entityModelList) {
		this.entityModelList = entityModelList;
		this.conn = conn;
		createTables();
	}
	
    private void createTables() {
        Table t = new Table(conn);
        for (EntityModel<?> entity : entityModelList) {
            t.createTable(entity);
        }
    }
    
	public List<EntityModel<Class<?>>> getEntityList() {
		return entityModelList;
	}
	
    public Object get(Class<?> entityClass, int id) {
        for (EntityModel<?> entity : entityModelList) {
            if (entity.getClassName().equals(entityClass.getCanonicalName())) {
            	Query query = new Query(conn, entity);
                try {
                    return query.get(entityClass, id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
	
	public Object save(Object object) {
		EntityModel<?> entity = EntityModel.of(getClass());
		int index = entityModelList.indexOf(entity);
		if (index != -1) {
			Query query = new Query(conn, entityModelList.get(index));
			query.save(object);
		}
		return null;
	}
	
    public void delete(Object object) {
        for (EntityModel<?> entity : entityModelList) {
            if (entity.getClassName().equals(object.getClass().getCanonicalName())) {
                Query query = new Query(conn, entity);
                try {
                    query.delete(object);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	public Transaction beginTransaction() {
		if (currenTransaction == null) {
			currenTransaction = new Transaction(conn);
			currenTransaction.setAutoCommit(false);
		}
		return currenTransaction;
	}

	public void close() {	
		try {
			conn.close();
			cache.clear();
			currenTransaction = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
