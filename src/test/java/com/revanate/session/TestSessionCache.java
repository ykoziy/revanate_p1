package com.revanate.session;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestSessionCache {
	class User {
		private String username;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public User(String username) {
			this.username = username;
		}
	}

	class Employee {
		private String name;
		int age;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public Employee(String name, int age) {
			this.name = name;
			this.age = age;
		}
	}

	SessionCache sessionCache;

	User testUser1;
	User testUser2;
	User testUser3;

	Employee testEmp1;
	Employee testEmp2;
	Employee testEmp3;

	@Before
	public void setUp() {
		sessionCache = new SessionCache();
		testUser1 = new User("bob98");
		testUser2 = new User("xXXx_alice_xXXx");
		testUser3 = new User("Coding_Beast");
		testEmp1 = new Employee("Bob Mitnik", 24);
		testEmp2 = new Employee("Alice Hawkins", 19);
		testEmp3 = new Employee("Mike Henderson", 45);
	}

	@After
	public void tearDown() {
		sessionCache = null;
		testUser1 = null;
		testUser2 = null;
		testUser3 = null;
		testEmp1 = null;
		testEmp2 = null;
		testEmp3 = null;
	}

	// testing add method //
	@Test
	public void shouldBeAbleToAdd() {
		sessionCache.add(testUser1.getUsername(), testUser1);
		Assert.assertEquals(1, sessionCache.get().size());
	}

	// testing get method //
	public void shouldBeAbleToGet() {
		sessionCache.add(testEmp2.getName(), testEmp2);

		User getUser = (User) sessionCache.get(testEmp2.getName());
	}

	public void shouldNotGetObjectIfDoesNotExist() {
		sessionCache.add(testUser1.getUsername(), testUser1);
		sessionCache.add(testUser3.getUsername(), testUser3);

		Employee getEmp = (Employee) sessionCache.get(testUser2.getUsername());
		Assert.assertNull(getEmp);
	}


	// testing clear method //
	@Test
	public void shouldBeAbleToClearCache() {
		sessionCache.add(testUser1.getUsername(), testUser1);
		sessionCache.add(testUser3.getUsername(), testUser3);
		
		sessionCache.clear();
		Assert.assertEquals(0, sessionCache.get().size());
	}
}
