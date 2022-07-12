package com.revanate.entity;

import java.lang.reflect.Field;

import com.revanate.annotations.Column;


// extract the data type of the fields
public class ColumnField {
	private Field field;

	public ColumnField(Field field) {
		// lets check the field to ensure it has the column annotation that we're
		// expecting

		if (field.getAnnotation(Column.class) == null) {
			throw new IllegalStateException(
					"Cannot create ColumnField object! Provided field: " + getName() + " is not annotated with Column");
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
		return field.getAnnotation(Column.class).columnName();
	}
}
