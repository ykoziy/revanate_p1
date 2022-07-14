package com.revanate.session;

import java.util.HashMap;

public class SessionCache {
    
    private HashMap<String, Object> cache;
    
    public SessionCache() {
        cache = new HashMap<>();
    }
    
    public HashMap<String, Object> get() {
        return cache;
    }

    public void add(String key, Object obj) {
        cache.put(key, obj);
    }
    
    public void remove(String key) {
        cache.remove(key);
    }
    
    public Object get(String key) {
        System.out.println("got from cache " + key);
        return cache.get(key);
    }
    
    public boolean contains(final String key) {
        return cache.containsKey(key);
    }
    
    public void clear() {
        cache.clear();
    }
}
