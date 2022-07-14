package com.revanate.entity;

import java.lang.reflect.Field;

import com.revanate.annotations.ForeignKey;

public class ForeignKeyField
{
	@Override
	public String toString()
	{
		return "ForeignKeyField [field=" + field + "]";
	}

	private Field field;

	public ForeignKeyField(Field field) {
		// lets check the field to ensure it has the column annotation that we're
		// expecting

		if (field.getAnnotation(ForeignKey.class) == null) {
			throw new IllegalStateException(
					"Cannot create ForeignKeyField object! Provided field: " + field.getName() + " is not annotated with JoinColumn");
		}
		this.field = field;
	}

	public String getName() {
		return field.getName();
	}

	// return the type of the field that annotated
	public Class<?> getType() {
		return field.getType();
	}

	public String getColumnName() {
		return field.getAnnotation(ForeignKey.class).columnName();
	}	
}
