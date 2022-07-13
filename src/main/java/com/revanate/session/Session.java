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
		// ID can be any type, figure out  best way to do it
		String tableName = entityClass.getName();
		/* 
		 * 1) Get the table name (Entity annotation is empty string, use class name as table name as lower case), or specified in Entity annotation tableName
		 * 2) Get the name of primary key field(s)
		 * 3) Create query based on that data
		 * 4) Build an Object based on the result of query?
		 * 5) If everything went OK, just store the Object inside the cache
		 * 6) Return primary key (ID), it is set to Object because it can be any type
		 */	
		return null;
	}
	
	public Object save(Object object) {
		Query2 query;
		//Class clazz = object.getClass();
		EntityModel<?> entity = EntityModel.of(getClass());
		int index = entityModelList.indexOf(entity);
		if (index != -1) {
			query = new Query2(conn, entityModelList.get(index));
			query.save(object);
		}
		/* 
		 * 1) Convert object to EntityModel
		 * 2) Get field names and field data
		 * 3) Create query using data from previous data
		 * 4) Run that query on the DB, using session connection
		 * 5) If everything went OK, just store the Object inside the cache
		 * 6) Return primary key (ID), it is set to Object because it can be any type
		 */
		return null;
	}
	
	public void delete(Object object) {
		/* 
		 * 1) Convert object to EntityModel
		 * 2) Get fprimaryKey ID
		 * 3) Create query using data from previous data
		 * 4) Run that query on the DB, using session connection
		 * 5) If everything went OK, just store the Object inside the cache
		 * 6) Return primary key (ID), it is set to Object because it can be any type
		 */	
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
