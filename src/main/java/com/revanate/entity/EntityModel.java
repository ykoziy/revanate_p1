package com.revanate.entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;

import com.revanate.annotations.Column;
import com.revanate.annotations.Entity;
import com.revanate.annotations.ForeignKey;
import com.revanate.annotations.Id;

public class EntityModel {

	
	private Class<?> annotetedClass;
	private Field[] fields;
	private int numColumns = 0, numForeignKeys = 0;
	private boolean primaryKey = false;
	
	
	public EntityModel (Class<?> annotetedClass)
	{
		this.annotetedClass = annotetedClass;
		isAnnotated();
		
	}
	
	private void isAnnotated()
	{
		if(annotetedClass.isAnnotationPresent(Entity.class))
		{
			fields = annotetedClass.getDeclaredFields();
			
			for(Field field : fields)
			{
				Annotation[] annotations = field.getAnnotations();
				for(Annotation ann : annotations)
				{
					if(ann.annotationType() == Column.class)
					{
						numColumns++;
						break;
					}
					
					else if(ann.annotationType() == ForeignKey.class)
					{
						numForeignKeys++;
						numColumns++;
						break;
					}
					else if (ann.annotationType() == Id.class)
					{
						if(!primaryKey)
						{
							primaryKey = true;
							numColumns++;
							break;
						}
						else 
						{
							throw new RuntimeException("The id annotation is used for primary keys, and you can not have multiple primary keys");
						}
					}
				}
			}
			
		}
		else 
		{
			throw new RuntimeException("The class was not properly annotated");
		}
	}
	
	
}
