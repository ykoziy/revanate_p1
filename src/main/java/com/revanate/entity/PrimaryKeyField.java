package com.revanate.entity;

import java.lang.reflect.Field;

import com.revanate.annotations.Id;

public class PrimaryKeyField {
	private Field field;

	public PrimaryKeyField(Field field) {
		// lets check the field to ensure it has the column annotation that we're
		// expecting
		Id pk = field.getAnnotation(Id.class);
		if (pk == null) {
			// cant get a name of null!!!
			throw new IllegalStateException("Cannot create PrimaryKeyField object! Provided field: " + field.getName() + " is not annotated with @Id");
		}
		this.field = field;
	}

	public String getName() {
		return field.getName();
	}

	@Override
	public String toString()
	{
		return "PrimaryKeyField [field=" + field + "]";
	}

	// return the type of the field that annotated
	public Class<?> getType() {
		return field.getType();
	}

	public String getColumnName() {
		return field.getAnnotation(Id.class).columnName();
	}
	
	public String getGenerationType() {
		return field.getAnnotation(Id.class).autoIndex();
	}
}
