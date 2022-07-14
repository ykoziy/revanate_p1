# Revanate

## Project Description
Revanate, a Java based ORM library simplifying connection to the PostgreSQL without the need for SQL or connection managment.

## Technologies Used

* PostgreSQL - version 42.3.3
* Java - version 8.0
* Apache commons - version 2.9.0
* JUnit - version 4.13.2

## Features

List of features goes here....

## Getting Started

Any Information one would need to utilize your repo

## Usage

### Annotations
In order for the Revanate ORM to know which Java classes need to be mapped with the database tables, you must properly annotate your Model layer classes.

  #### Annotations List
 - ```@Entity(tableName = "table_name")``` - defines a Java class as entity to be mapped.
 - ```@Column(columnName = "column_name")``` - defines a field inside a class as a column, will be mapped to the DB table.
 - ```@ForeignKey(columnName = "column_name")```
 - ```@Id(columnName = "column_name", autoIndex = "auto")```

### Setting up a session
In order to build a session you must configure ORM, build a SessionManager and open a session.

#### 1) Configuration
Configuration is store in the project resources folder as an XML file with extension *.cfg.xml.
Example configuration file, configuration is limited for time being. All options are in this example.
```<?xml version="1.0" encoding="UTF-8"?>
<revanate-configuration>
  <!-- These are JDBC Connection Credentials -->
  <property name="revanate.connection.url">jdbc:postgresql://localhost:5432/postgres?currentSchema=hibernate</property>
    <property name="revanate.connection.username">postgres</property>
    <property name="revanate.connection.password">postgres</property>

    <!-- List of mapping files -->
    <mapping class="com.revanate.testrun.model.Crime" />
    <mapping class="com.revanate.testrun.model.SuperVillain" />
    <mapping class="com.revanate.testrun.model.SuperPrison" />
</revanate-configuration>
```
#### 2) Building SessionManager instance
Once you have a valid configuration file, you can get a SessionManager.
```
SessionManager sm = new Configuration().configure("revanate.cfg.xml").buildSessionManager();
```

#### 3) Open Session
Using SessionManager you can how open a session. Opening a session establishes a connection to the PostgreSQL database. Almost all operations are performed on Session object.

### Begin transaction

## User API
