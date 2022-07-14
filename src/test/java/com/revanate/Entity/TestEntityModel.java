package com.revanate.Entity;

import com.revanate.annotations.*;


@Entity
public class TestEntityModel
{
	@Id
	int primaryKey;
	
	@Column(columnName = "")
	@ForeignKey(columnName = "")
	int fKey1;
	@ForeignKey(columnName = "")
	int fKey2;
	
	@Column(columnName = "")
	int col1;
	@Column(columnName = "")
	int col2;
	@Column(columnName = "")
	double col3;
	
	
	public TestEntityModel(int primaryKey, int fKey1, int fKey2, int col1, int col2, double col3)
	{
		super();
		this.primaryKey = primaryKey;
		this.fKey1 = fKey1;
		this.fKey2 = fKey2;
		this.col1 = col1;
		this.col2 = col2;
		this.col3 = col3;
	}
	
}
