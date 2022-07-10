package com.revanate.session;

import java.util.HashMap;
import java.util.HashSet;

public class SessionCache {
	
	// By "Session-based caching" think of a hashmap or some data struture that you would store previously retrieved objects in.
	// Evey time you retrieve or add an object to an from the DB, you store it in this data structure in your Java program.
	// That way, when you try to query it a second time, you check the data structure first (your hashmap, for example) and 
	// if the object exists you can quickly retrieve it without spending time connecting to the DB and executing the same query.

	private HashMap<Class<?>, HashSet<Object>> cache;
	
	public SessionCache() {
		cache = new HashMap<>();
	}
	
	public HashMap<Class<?>, HashSet<Object>> get() {
		return cache;
	}
	
	public HashSet<Object> getObjects(final Class<?> clazz) {
		return cache.get(clazz);
	}
	
	public void add(final Class<?> clazz, final Object obj) {
		if (cache.containsKey(clazz)) {
			cache.get(clazz).add(obj);
		} else {
			HashSet<Object> objSet = new HashSet<>();
			objSet.add(obj);
			cache.put(clazz, objSet);
		}
	}
	
	public Object get(final Class<?> clazz, final Object obj) {
		if (cache.containsKey(clazz)) {
			HashSet<Object> objSet = cache.get(clazz);
			for (Object o : objSet) {
				if (o.equals(obj)) {
					return o;
				}
			}
			return null;
		} else {
			return null;
		}
	}
	
	public void clear() {
		cache.clear();
	}
}
