package com.revanate.entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.revanate.annotations.Column;
import com.revanate.annotations.Entity;
import com.revanate.annotations.ForeignKey;
import com.revanate.annotations.Id;
import com.revanate.exception.EntityException;
import com.revanate.exception.PrimaryKeyMissing;

public class EntityModel
{

	private Class<?> annotetedClass;
	private Field[] fields;
	@SuppressWarnings("unused")
	private int numColumns = 0, numForeignKeys = 0;
	private boolean primaryKey = false;

	public EntityModel(Class<?> annotetedClass)
	{
		this.annotetedClass = annotetedClass;
		isAnnotated();

	}

	private void isAnnotated()
	{
		if (annotetedClass.isAnnotationPresent(Entity.class))
		{
			fields = annotetedClass.getDeclaredFields();

			for (Field field : fields)
			{
				Annotation[] annotations = field.getAnnotations();
				for (Annotation ann : annotations)
				{
					if (ann.annotationType() == Column.class)
					{
						numColumns++;
						break;
					}

					else if (ann.annotationType() == ForeignKey.class)
					{
						numForeignKeys++;
						numColumns++;
						break;
					} else if (ann.annotationType() == Id.class)
					{
						if (!primaryKey)
						{
							primaryKey = true;
							// numColumns++;
							break;
						} else
						{
							throw new EntityException(
									"The id annotation is used for primary keys, and you can not have multiple primary keys");
						}
					}
				}
			}

		} else
		{
			throw new EntityException("The class was not properly annotated");
		}
	}

	public PrimaryKeyField GetPrimaryKey() throws IllegalArgumentException
	{

		if (!primaryKey)
		{
			throw new PrimaryKeyMissing("The object does not have a primary key field");
		}

		for (Field field : fields)
		{
			Annotation[] annotations = field.getAnnotations();
			for (Annotation ann : annotations)
			{
				if (ann.annotationType() == Id.class)
				{
					field.setAccessible(true);
					return new PrimaryKeyField(field);

				}
			}
		}

		throw new PrimaryKeyMissing("Primary key not found");
	}

	public List<ForeignKeyField> GetForiegnKeys() throws IllegalArgumentException
	{
		List<ForeignKeyField> temp = new ArrayList<>();

		for (Field field : fields)
		{
			Annotation[] annotations = field.getAnnotations();
			for (Annotation ann : annotations)
			{
				if (ann.annotationType() == ForeignKey.class)
				{
					field.setAccessible(true);
					temp.add(new ForeignKeyField(field));

				}
			}
		}

		return temp;
	}

	public List<ColumnField> GetColumns() throws IllegalArgumentException
	{
		List<ColumnField> temp = new ArrayList<>();

		for (Field field : fields)
		{
			Annotation[] annotations = field.getAnnotations();
			for (Annotation ann : annotations)
			{
				if (ann.annotationType() == Column.class)
				{
					field.setAccessible(true);
					temp.add(new ColumnField(field));
				}
			}
		}

		return temp;
	}

}
