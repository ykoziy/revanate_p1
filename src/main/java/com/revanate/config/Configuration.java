package com.revanate.config;

import java.util.LinkedList;
import java.util.List;

import com.revanate.entity.EntityModel;

public class Configuration {
	private String dbUrl;
	private String dbUsername;
	private String dbPassword;

	// store list of object mappings, commented out for now
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

	//for now just prints into console
	public Configuration configure(String resource) {
		System.out.println("Not fully implemented, read configuration file");
		XmlConfigParser xml = new XmlConfigParser(resource);
		xml.readXmlFile();
		return this;
	}
}
