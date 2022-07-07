package com.revanate.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class XmlConfigParser {
	private final String FILENAME;
	private Map<String, String> properties;

	public XmlConfigParser(String fileName) {
		FILENAME = fileName;
		properties = new HashMap<>();
	}

	private InputStream getResourceFileStream() {
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(FILENAME);
		if (inStream == null) {
			throw new IllegalArgumentException("Config file not found! " + inStream);
		} else {
			return inStream;
		}
	}

	public Map<String, String> readXmlFile() {
		int objectMapCount = 0;
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		InputStream xmlStream = getResourceFileStream();
		try {
			XMLEventReader reader = xmlInputFactory.createXMLEventReader(xmlStream);
			while (reader.hasNext()) {
				XMLEvent xmlEvent = reader.nextEvent();
				if (xmlEvent.isStartElement()) {
					StartElement startElement = xmlEvent.asStartElement();
					switch (startElement.getName().getLocalPart()) {
					case "property":
						Attribute name = startElement.getAttributeByName(new QName("name"));
						if (name != null) {
							String attribute = name.getValue();
							if (attribute.equals("revanate.connection.url")) {
								xmlEvent = reader.nextEvent();
								readConnectionCfg("dburl", xmlEvent);
							} else if (attribute.equals("revanate.connection.username")) {
								xmlEvent = reader.nextEvent();
								readConnectionCfg("username", xmlEvent);
							} else if (attribute.equals("revanate.connection.password")) {
								xmlEvent = reader.nextEvent();
								readConnectionCfg("password", xmlEvent);
							}
						}
						break;
					case "mapping":
						Attribute clazz = startElement.getAttributeByName(new QName("class"));
						if (clazz != null) {
							String attribute = clazz.getValue();
							properties.put("mapping" + objectMapCount, attribute);
							objectMapCount++;
						}
						break;
					default:
						break;
					}
				}
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return properties;
	}

	private void readConnectionCfg(String attribute, XMLEvent xevent) {
		String url = "";
		String uname = "";
		String pwd = "";
		if (attribute.equals("dburl")) {
			url = xevent.asCharacters().getData();
			properties.put("db_url", url);
		} else if (attribute.equals("username")) {
			uname = xevent.asCharacters().getData();
			properties.put("db_username", uname);
		} else if (attribute.equals("password")) {
			pwd = xevent.asCharacters().getData();
			properties.put("db_password", pwd);
		}
	}
}
