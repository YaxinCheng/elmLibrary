package com.example.library.backend;

import java.util.List;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;

/**
 * this class contains all of the methods for dealing with different actions
 * regarding a user, it also contains methods for checking log in credentials
 */
public class UserService {

	private static UserService instance;
	public JPAContainer<User> Users = JPAContainerFactory.make(User.class,
			JPAContainerFactory.createEntityManagerForPersistenceUnit("library_db"));

	public static UserService createDemoService() {
		if (instance == null) {
			UserService UserService = new UserService();
			instance = UserService;
			UserService.populateUserService();
		}
		return instance;
	}

	public void populateUserService() {
		List<Book> borrowed = null;
		List<Book> waiting = null;
		User user1 = new User("Nick", "definitelynot@fake.ca", "902-666-1234", borrowed, waiting);
		user1.setAccount("nick");
		user1.setPassword("testlol!!");
		Users.addEntity(user1);

		User user2 = new User("Roc", "prince@god.org", "902-123-6969", borrowed, waiting);
		user2.setAccount("roc");
		user2.setPassword("poutine4@");
		Users.addEntity(user2);

		User user3 = new User("Prince", "real@email.com", "666-123-4567", borrowed, waiting);
		user3.setAccount("prince90");
		user3.setPassword("securepassword!");
		Users.addEntity(user3);

		User ycheng = new User("ycheng", "yaxin.Cheng@dal.ca", "902-877-9707", borrowed, waiting);
		ycheng.setAccount("ycheng");
		ycheng.setPassword("cyx123321X&");
		Users.addEntity(ycheng);
	}

	public synchronized int count() {
		return Users.size();
	}

	/** 'delete' will remove a user from the User JPAContainer */
	public synchronized boolean delete(User value) {
		try {
			Users.removeItem(value.getAccount());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 'replace' works by deleting a given user, and then saving them,
	 * ultimately ending up with a new copy of the user's instance
	 */
	public synchronized void replace(User user) {
		if (delete(user)) {
			save(user);
		}
	}

	/* adding a user to the User JPAContainer */
	public synchronized void save(User entry) {
		Users.addEntity(entry);
	}

	/**
	 * the checkLogIn method works by first making sure that the account passed
	 * to it is not null, then does a simple string comparison to find out if
	 * the given account's password matches the attempted password
	 */
	public synchronized boolean checklogIn(String account, String password) {
		final EntityItem<User> expected = Users.getItem(account);
		if (expected == null) {
			return false;
		}
		String passwordExpected = expected.getEntity().getPassword();
		return password.equals(passwordExpected);
	}

	/* returning a requested user object */
	public synchronized User getUser(String account, String password) {
		if (checklogIn(account, password)) {
			return Users.getItem(account).getEntity();
		}
		return null;
	}

	/**
	 * the formatChecking function will first check the format of a password
	 * associated with an account and works b receiving an account and password
	 * and making sure that: neither are empty, and the password contains a
	 * special character and is longer than 8 characters
	 * 
	 * secondly it will apply a filter that uses that User's name and checks to
	 * see if there is a match in the database, if there is the size returned
	 * will be greater than 0, and therefore not allow the user to user that
	 * name
	 */
	private boolean formatChecking(String account, String password) throws FormatCheckFailedException {
		if (account.isEmpty() || password.isEmpty()) {
			throw new FormatCheckFailedException("Account or Password Cannot Be Empty!");
		}
		if (password.length() < 8) {
			throw new FormatCheckFailedException("Password must be longer than 8 Characters!");
		}
		if (!password.matches(".*[\\-\\*\\&\\^\\%\\$\\#\\@\\!\\(\\)].*")) {
			throw new FormatCheckFailedException("Password Must Contain At Least 1 Special Character");
		}
		Filter accountCheck = new Compare.Equal("account", account);
		Users.addContainerFilter(accountCheck);
		// Users.refresh();
		if (Users.size() > 0) {
			Users.removeAllContainerFilters();
			throw new FormatCheckFailedException("The Account Is Already Existed in the Database!");
		}
		return true;
	}

	/**
	 * the register functions works by receiving an account and password, and
	 * then checking if the formatChecking function returns true, if so, then a
	 * user object is created and added to the JPAContainer
	 */
	public synchronized String register(String account, String password) {
		boolean fmtCheck = false;
		try {
			fmtCheck = formatChecking(account, password);
		} catch (FormatCheckFailedException e) {
			return e.getLocalizedMessage();
		}
		if (fmtCheck == false) {
			return "Error";
		}
		User newUser = new User(account, password);
		save(newUser);
		return "Register Success";
	}
}