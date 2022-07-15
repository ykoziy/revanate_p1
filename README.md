# Revanate

## Project Description

Revanate, a Java based ORM library simplifying connection to the PostgreSQL without the need for SQL or connection managment.

## Technologies Used

- PostgreSQL - version 42.3.3
- Java - version 8.0
- Apache commons - version 2.9.0
- JUnit - version 4.13.2

## Features

List of features goes here....

## Getting Started

Any Information one would need to utilize your repo

## Usage

Using git:

- Start in a directory where to want to clone this repository
- Execute `git clone https://github.com/ykoziy/revanate_p1.git`
- Switch into newly created directory `revanate_p1`
- In console do `mvn install`
- Add this as dependency in another maven project (pom.xml)

```
<groupId>com.revanate</groupId>

<artifactId>revanate_p1</artifactId>

<version>0.0.1-SNAPSHOT</version>
```

### Annotations

In order for the Revanate ORM to know which Java classes need to be mapped with the database tables, you must properly annotate your Model layer classes.

#### Annotations List

- `@Entity(tableName = "table_name")` - defines a Java class as entity to be mapped.
- `@Column(columnName = "column_name")` - defines a field inside a class as a column, will be mapped to the DB table.
- `@ForeignKey(columnName = "column_name")` - defines foreign key column, sets foreign key column name
- `@Id(columnName = "column_name", autoIndex = "auto")` - defines primary key column, autoIndex set to `auto` will auto incerement primary key

### Setting up a session

In order to build a session you must configure ORM, build a SessionManager and open a session.

#### 1) Configuration

Configuration is store in the project resources folder as an XML file with extension \*.cfg.xml.
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

Using SessionManager you can now open a session. Opening a session establishes a connection to the PostgreSQL database. Almost all operations are performed on Session object.

### Begin transaction

To begin a transaction just use:

```
Transaction tx = ses.beginTransaction();
```

Subsequent transaction control done via Transaction class.

## User API

### Session Manager

`public Session openSession()` - use it to start the Session, returns Session

### Session

`List<EntityModel<Class<?>>> getEntityList()` - gets EntityModel list.

`void delete(Object object)` - delete object from the table row.

`Object save(Object object)` - store object in database table, returns primary key.

`void update(Object object)` - update object in database table.

`<T> Object get(Class<?> entityClass, T id)` - get object from the database, by using primary key id

`Query getAll(Class<?> resultType)` - get all objects from the database. Returns Query.

`Transaction beginTransaction()` - begins transaction, returns Transaction.

`void close()` - close the session. Releases connection and returns in to connection pool, clean up other resources.

### Transaction

Used for SQL transactions. Savepoints are stored as a HashMap.

`void commit()` - commit transaction.

`void releaseSavepoint(String name)` - removes the specified Savepoint.

`void rollback()` - undo all changes for the current transaction.

`void rollback(String name)` - undo all the changes after the given savepoint.

`void setAutoCommit(boolean isEnabled)` - changes auto commit mode for the current connection.

`void setSavepoint(String name)` - set savepoint with specified name.

### Configuration

`Configuration addEntittyModel(Class<?> annotetedClass)` - manually add entity model to the list of EntityModels

`SessionManager buildSessionManager()`- builds SessionManager

`Configuration configure(String resource)` - do configuration from XML

`String getDbPassword()` - get DB password

`String getDbUrl()` - get DB URL

`String getDbUsername()` - get DB username

`List<EntityModel<Class<?>>> getEntityModelList()` - get EntityModel list

`Configuration setConnection(String dbUrl, String dbUsername, String dbPassword)` - set connection to specified parameters

`void setDbPassword(String dbPassword)` - set password

`void setDbUrl(String dbUrl)` - set dbUrl

`void setDbUsername(String dbUsername)` - set DB username

`void setEntityModelList(List<EntityModel<Class<?>>> entityModelList)` - set EntityModel list to specified list
