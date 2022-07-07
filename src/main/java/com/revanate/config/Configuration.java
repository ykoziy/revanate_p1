package com.revanate.config;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.revanate.entity.EntityModel;

public class Configuration {
	private String dbUrl;
	private String dbUsername;
	private String dbPassword;

	// store list of object mappings, not functional right now
	private List<EntityModel<Class<?>>> entityModelList;

	public Configuration addEntittyModel(Class<?> annotetedClass) {
		if (entityModelList == null) {
			entityModelList = new LinkedList<>();
		}
		entityModelList.add(EntityModel.of(annotetedClass));
		return this;
	}

	public Configuration setConnection(String dbUrl, String dbUsername, String dbPassword) {
		this.dbUrl = dbUrl;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
		return this;
	}
	
	public Configuration configure(String resource) {
		XmlConfigParser xml = new XmlConfigParser(resource);
		
		Map<String, String> properties = xml.readXmlFile();
		
		this.dbUrl = properties.get("db_url");
		this.dbUsername = properties.get("db_username");
		this.dbPassword = properties.get("db_password");
		properties.remove("db_url");
		properties.remove("db_username");
		properties.remove("db_password");
		
		for (String key : properties.keySet()) {
		    if (key.contains("mapping")) {
		    	addEntityModel(properties.get(key));
		    } 
		}
		return this;
	}
	
	private void addEntityModel(String className) {
		System.out.println(className);
	}
}
