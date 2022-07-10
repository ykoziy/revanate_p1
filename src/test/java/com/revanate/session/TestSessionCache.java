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
	public void shouldBeAbleToAddObjectClassDoesNotExist() {
		sessionCache.add(User.class, testUser1);
		Assert.assertEquals(1, sessionCache.get().size());
	}

	@Test
	public void shouldBeAbleToAddObjectClassExists() {
		sessionCache.add(User.class, testUser1);
		sessionCache.add(Employee.class, testEmp1);

		sessionCache.add(User.class, testUser3);
		Assert.assertEquals(2, sessionCache.getObjects(User.class).size());
	}
	// end testing add method //

	// testing get method //
	public void shouldGetSpecificObjectFromCache() {
		sessionCache.add(User.class, testUser1);
		sessionCache.add(User.class, testUser3);
		sessionCache.add(Employee.class, testEmp1);
		sessionCache.add(Employee.class, testEmp2);
		sessionCache.add(Employee.class, testEmp3);

		User getUser = (User) sessionCache.get(User.class, testUser3);
		Assert.assertEquals("Coding_Beast", getUser.getUsername());
	}

	public void shouldNotGetObjectIfClassNotInCache() {
		sessionCache.add(User.class, testUser1);
		sessionCache.add(User.class, testUser3);

		Employee getEmp = (Employee) sessionCache.get(Employee.class, testEmp1);
		Assert.assertNull(getEmp);
	}

	public void shouldNotGetObjectIfObjectNotInCache() {
		sessionCache.add(User.class, testUser1);
		sessionCache.add(User.class, testUser3);

		User user = new User("bob99");

		User getUser = (User) sessionCache.get(User.class, user);
		Assert.assertNull(getUser);
	}
	// end testing get method //

	// testing clear method //
	@Test
	public void shouldBeAbleToClearCache() {
		sessionCache.add(User.class, testUser1);
		sessionCache.add(Employee.class, testEmp1);
		sessionCache.add(Employee.class, testEmp2);
		sessionCache.add(Employee.class, testEmp3);
		sessionCache.clear();
		Assert.assertEquals(0, sessionCache.get().size());
	}

	// testing getObjects method //
	@Test
	public void shouldBeAbleToGetAllObjectsOfAClass() {
		sessionCache.add(User.class, testUser1);
		sessionCache.add(Employee.class, testEmp1);
		sessionCache.add(Employee.class, testEmp2);
		sessionCache.add(Employee.class, testEmp3);
		Assert.assertEquals(3, sessionCache.getObjects(Employee.class).size());
	}

}
