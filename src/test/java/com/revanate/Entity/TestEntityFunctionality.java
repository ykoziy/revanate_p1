package com.revanate.Entity;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.revanate.entity.EntityModel;
import com.revanate.entity.PrimaryKeyField;
import com.revanate.exception.EntityException;
import com.revanate.exception.PrimaryKeyMissing;

public class TestEntityFunctionality
{

	private TestEntityModel dummyEntity;

	@Before
	public void before()
	{
		dummyEntity = new TestEntityModel(0, 0, 0, 0, 0, 0);
	}
	
	@Test()
	public void TestPrimaryKeyStuff()
	{
		EntityModel em = new EntityModel(dummyEntity.getClass());
		PrimaryKeyField pkf = em.GetPrimaryKey();
		System.out.println(pkf);
		assertEquals(pkf.getName(), "primaryKey");
		System.out.println(em.GetForiegnKeys());
		System.out.println(em.GetColumns());
		
	}
}
