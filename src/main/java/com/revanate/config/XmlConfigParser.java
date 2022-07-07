package com.revanate.config;

import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

//// WORK IN PROGRESS, testing xml reading //////
public class XmlConfigParser {
	private final String FILENAME;

	public XmlConfigParser(String fileName) {
		FILENAME = fileName;
	}
	
	private InputStream getResourceFileStream() {
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(FILENAME);
        if (inStream == null) {
            throw new IllegalArgumentException("Config file not found! " + inStream);
        } else {
            return inStream;
        }
	}
	
	public void readXmlFile() {
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
						if (name != null)
						{
							String attribute = name.getValue();
							if (attribute.equals("revanate.connection.url")) {
								xmlEvent = reader.nextEvent();
								System.out.println("DB Url: " + xmlEvent.asCharacters().getData());
							} else if (attribute.equals("revanate.connection.username")) {
								xmlEvent = reader.nextEvent();
								System.out.println("DB username: " + xmlEvent.asCharacters().getData());								
							} else if (attribute.equals("revanate.connection.password")) {
								xmlEvent = reader.nextEvent();
								System.out.println("DB password: " + xmlEvent.asCharacters().getData());
							}
						}
						break;
					case "mapping":
						Attribute clazz = startElement.getAttributeByName(new QName("class"));
						if (clazz != null)
						{
							String attribute = clazz.getValue();
							System.out.println(attribute);
//							try {
//								// get class from canonical name
//								Class entityClass = Class.forName(attribute);
//								System.out.println(entityClass);
//							} catch (ClassNotFoundException e) {
//								e.printStackTrace();
//							}
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
	}
}
