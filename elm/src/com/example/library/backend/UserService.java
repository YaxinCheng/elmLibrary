package com.example.library.backend;

import java.util.List;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;

/* this class will handle features for the different users
 * and will be fully implemented during the SECOND ITERATION */
public class UserService {

	private static UserService instance;
	public JPAContainer<User> Users = JPAContainerFactory.make(User.class,
			JPAContainerFactory.createEntityManagerForPersistenceUnit("library_db"));

	/**
	 */
	public static UserService createDemoService() {
		if (instance == null) {
			BookService BookService = new BookService();
			UserService UserService = new UserService();
			instance = UserService;
			UserService.populateUserService(BookService);
		}
		return instance;
	}

	@SuppressWarnings("null")
	public void populateUserService(BookService b){
		List<Book> borrowed = null;
		List<Book> waiting = null;
		
		borrowed.add((Book) b.shelf.getItem("B012OUZ3EQ"));
		borrowed.add((Book) b.shelf.getItem("11783986549"));
		User user1 = new User("Nick", "definitelynot@fake.ca", "902-666-1234", borrowed, waiting);
		user1.setAccount("nick");
		user1.setPassword("testlol!!");
		Users.addEntity(user1);
		borrowed.clear();
		waiting.clear();
		
		borrowed.add((Book) b.shelf.getItem("20293429872"));
		waiting.add((Book) b.shelf.getItem("11783986549"));
		waiting.add((Book) b.shelf.getItem("42039487621"));
		User user2 = new User("Roc", "prince@god.org", "902-123-6969", borrowed, waiting);
		user2.setAccount("roc");
		user2.setPassword("poutine4@");
		Users.addEntity(user2);
		borrowed.clear();
		waiting.clear();
		
		waiting.add((Book) b.shelf.getItem("9823742983"));
		User user3 = new User("Prince", "real@email.com", "666-123-4567", borrowed, waiting);
		user3.setAccount("prince90");
		user3.setPassword("securepassword!");
		Users.addEntity(user3);
		borrowed.clear();
		waiting.clear();
	}
	
	public synchronized int count() {
		return Users.size();
	}

	public synchronized boolean delete(User value) {
		try {
			Users.removeItem(value.getAccount());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public synchronized void replace(User user) {
		if (delete(user)) {
			save(user);
		}
	}

	public synchronized void save(User entry) {
		Users.addEntity(entry);
	}

	public synchronized boolean checklogIn(String account, String password) {
		final EntityItem<User> expected = Users.getItem(account);
		if (expected == null) {
			return false;
		}
		String passwordExpected = expected.getEntity().getPassword();
		return password.equals(passwordExpected);
	}

	public synchronized User getUser(String account, String password) {
		if (checklogIn(account, password)) {
			return Users.getItem(account).getEntity();
		}
		return null;
	}

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

}
