package com.revanate.session;

import java.util.HashMap;
import java.util.HashSet;

public class SessionCache {
	
	// By "Session-based caching" think of a hashmap or some data struture that you would store previously retrieved objects in.
	// Evey time you retrieve or add an object to an from the DB, you store it in this data structure in your Java program.
	// That way, when you try to query it a second time, you check the data structure first (your hashmap, for example) and 
	// if the object exists you can quickly retrieve it without spending time connecting to the DB and executing the same query.

	
	// gets all object for a class
	public HashMap<Class<?>, HashSet<Object>> getCache()
	{
		return null;
	}
	
	// some method to add to cache
	public void add(Class<?> clazz, Object obj) {
		
	}
	
	// some method to clear cache
	public void clear() {
		
	}
	
}
