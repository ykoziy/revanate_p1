package com.revanate.config;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestXmlConfigParser 
{
	private Configuration cfg;
	private XmlConfigParser xmlp;
	
	private final String url = "jdbc:postgresql://localhost:5432/postgres?currentSchema=hibernate";
	private final String uname = "postgres";
	private final String pwd = "postgres";

	@Before
	public void beforeEach()
	{
		cfg = new Configuration();
		xmlp = new XmlConfigParser("revanate.cfg.xml");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void parsingInvalidFIleShouldThrowException()
	{
		xmlp = new XmlConfigParser("revanat.cfg.xml");
		xmlp.readXmlFile();
	}
	
	@Test 
	public void parsingShouldSetConnectionVars() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Configuration c = cfg.configure("revanate.cfg.xml");
	    Field urlField = c.getClass().getDeclaredField("dbUrl");
	    urlField.setAccessible(true);
	
	    Field unameField = c.getClass().getDeclaredField("dbUsername");
	    unameField.setAccessible(true);
	    
	    Field pwdField = c.getClass().getDeclaredField("dbPassword");
	    pwdField.setAccessible(true);

	    Assert.assertEquals(url, (String) urlField.get(c));
	    Assert.assertEquals(uname, (String) unameField.get(c));
	    Assert.assertEquals(pwd, (String) pwdField.get(c));
	}
	
	@After
	public void afterEach()
	{
		cfg = null;
		xmlp = null;
	}
}
